package net.kaelos.icre.item.tool.electric;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.definitions.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class MiningDrillTool extends BaseEnergyToolItem {
    private final boolean supportsSilkTouchMode;

    public MiningDrillTool(Properties properties, long capacity, long maxReceive, VoltageTier tier,
                           long energyPerUse, float miningSpeed, TagKey<Block> incorrectBlocksForDrops, boolean supportsSilkTouchMode
    ) {
        super(properties.component(DataComponents.TOOL, createToolProperties(miningSpeed, incorrectBlocksForDrops)),
                capacity, maxReceive, tier, energyPerUse, Set.of());
        this.supportsSilkTouchMode = supportsSilkTouchMode;
    }

    private static Tool createToolProperties(float miningSpeed, TagKey<Block> incorrectBlocksForDrops) {
        HolderGetter<Block> blocks = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return new Tool(
                List.of(
                        Tool.Rule.deniesDrops(blocks.getOrThrow(incorrectBlocksForDrops)),
                        Tool.Rule.minesAndDrops(blocks.getOrThrow(BlockTags.MINEABLE_WITH_PICKAXE), miningSpeed),
                        Tool.Rule.minesAndDrops(blocks.getOrThrow(BlockTags.MINEABLE_WITH_SHOVEL), miningSpeed)
                ),
                1.0F,
                0,
                true
        );
    }

    public boolean isSilkTouchEnabled(ItemStack stack) {
        return this.supportsSilkTouchMode && stack.getOrDefault(ModDataComponents.SILK_TOUCH.get(), false);
    }

    private InteractionResult toggleSilkTouch(Level level, Player player, ItemStack stack) {
        boolean nextMode = !isSilkTouchEnabled(stack);
        if (!level.isClientSide()) {
            if (nextMode) {
                stack.set(ModDataComponents.SILK_TOUCH.get(), true);
            } else {
                stack.remove(ModDataComponents.SILK_TOUCH.get());
            }

            level.registryAccess().lookup(Registries.ENCHANTMENT)
                    .flatMap(reg -> reg.get(Enchantments.SILK_TOUCH))
                    .ifPresent(silkTouch -> EnchantmentHelper.updateEnchantments(stack, mutable ->
                            mutable.set(silkTouch, nextMode ? 1 : 0)));

            level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.PLAYERS, 0.5F, nextMode ? 1.2F : 0.8F);

            player.sendOverlayMessage(Component.translatable(
                    "message." + ICRE.MOD_ID + ".iridium_drill." + (nextMode ? "silk_touch_on" : "silk_touch_off")
            ));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (this.supportsSilkTouchMode && player.isShiftKeyDown()) {
            return toggleSilkTouch(level, player, player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    @Override
    public @NonNull InteractionResult useOn(@NonNull UseOnContext context) {
        Player player = context.getPlayer();
        if (this.supportsSilkTouchMode && player != null && player.isShiftKeyDown()) {
            return toggleSilkTouch(context.getLevel(), player, context.getItemInHand());
        }

        return super.useOn(context);
    }

    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay display, @NonNull Consumer<Component> builder, @NonNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, builder, tooltipFlag);
        if (this.supportsSilkTouchMode) {
            boolean enabled = isSilkTouchEnabled(stack);
            builder.accept(Component.translatable("tooltip." + ICRE.MOD_ID + (enabled ? ".silk_touch_on" : ".silk_touch_off"))
                    .withStyle(enabled ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY));
        }
    }
}
