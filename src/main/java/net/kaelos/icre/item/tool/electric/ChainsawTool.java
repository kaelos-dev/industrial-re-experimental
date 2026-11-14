package net.kaelos.icre.item.tool.electric;

import net.kaelos.icre.api.energy.VoltageTier;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Set;

public class ChainsawTool extends BaseEnergyToolItem {
    private static final float MINING_SPEED = 12.0F;
    private static final float BONUS_ATTACK_DAMAGE = 8.0F;
    private static final float ATTACK_SPEED = -2.4F;

    public ChainsawTool(Properties properties) {
        super(properties
                        .component(DataComponents.TOOL, createToolProperties())
                        .component(DataComponents.WEAPON, new Weapon(0, Weapon.AXE_DISABLES_BLOCKING_FOR_SECONDS))
                        .attributes(createAttributes()), 30_000L, 100L, VoltageTier.LV, 100L,
                Set.of(ItemAbilities.AXE_STRIP, ItemAbilities.SHEARS_DIG, ItemAbilities.SHEARS_HARVEST, ItemAbilities.SHEARS_TRIM));
    }

    @SuppressWarnings("deprecation")
    private static Tool createToolProperties() {
        HolderGetter<Block> blocks = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return new Tool(
                List.of(
                        Tool.Rule.deniesDrops(blocks.getOrThrow(BlockTags.INCORRECT_FOR_IRON_TOOL)),
                        Tool.Rule.minesAndDrops(blocks.getOrThrow(BlockTags.MINEABLE_WITH_AXE), MINING_SPEED),
                        Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 15.0F),
                        Tool.Rule.overrideSpeed(blocks.getOrThrow(BlockTags.LEAVES), 15.0F),
                        Tool.Rule.overrideSpeed(blocks.getOrThrow(BlockTags.WOOL), 8.0F),
                        Tool.Rule.overrideSpeed(HolderSet.direct(
                                Blocks.VINE.builtInRegistryHolder(),
                                Blocks.GLOW_LICHEN.builtInRegistryHolder()
                        ), 4.0F)
                ),
                1.0F,
                0,
                true
        );
    }

    private static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, BONUS_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public float getAttackDamageBonus(@NonNull Entity target, float damage, @NonNull DamageSource damageSource) {
        ItemStack weapon = damageSource.getWeaponItem();
        if (weapon != null && !hasUseEnergy(weapon)) {
            return -BONUS_ATTACK_DAMAGE;
        }

        return 0.0F;
    }

    @Override
    public void hurtEnemy(@NonNull ItemStack stack, @NonNull LivingEntity target, @NonNull LivingEntity attacker) {
        if (!attacker.level().isClientSide()) {
            tryConsumeUseEnergy(stack, attacker);
        }
    }

    @Override
    public @NonNull InteractionResult useOn(@NonNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        BlockState stripped = state.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false);
        if (stripped != null) {
            if (!canUse(stack, player)) {
                return InteractionResult.FAIL;
            }

            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return applyBlockModification(level, pos, stripped, player, stack);
        }

        BlockState trimmed = state.getToolModifiedState(context, ItemAbilities.SHEARS_TRIM, false);
        if (trimmed != null) {
            if (!canUse(stack, player)) {
                return InteractionResult.FAIL;
            }

            level.playSound(player, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return applyBlockModification(level, pos, trimmed, player, stack);
        }

        return InteractionResult.PASS;
    }

    private InteractionResult applyBlockModification(Level level, BlockPos pos, BlockState modifiedState, Player player, ItemStack stack) {
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
        }

        if (!level.isClientSide()) {
            level.setBlock(pos, modifiedState, Block.UPDATE_ALL_IMMEDIATE);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, modifiedState));
            tryConsumeUseEnergy(stack, player);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull InteractionResult interactLivingEntity(@NonNull ItemStack stack, @NonNull Player player, @NonNull LivingEntity entity, @NonNull InteractionHand hand) {
        if (entity instanceof IShearable shearable) {
            BlockPos pos = entity.blockPosition();
            Level level = entity.level();
            if (shearable.isShearable(player, stack, level, pos)) {
                if (!canUse(stack, player)) {
                    return InteractionResult.FAIL;
                }

                List<ItemStack> drops = shearable.onSheared(player, stack, level, pos);
                if (level instanceof ServerLevel serverLevel) {
                    for (ItemStack drop : drops) {
                        shearable.spawnShearedDrop(serverLevel, pos, drop);
                    }

                    tryConsumeUseEnergy(stack, player);
                }

                entity.gameEvent(GameEvent.SHEAR, player);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
