package net.kaelos.icre.item.tool.electric;

import net.kaelos.icre.api.energy.VoltageTier;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Set;

public class ElectricHoeTool extends BaseEnergyToolItem {
    private static final float MINING_SPEED = 12.0F;

    public ElectricHoeTool(Properties properties) {
        super(properties.component(DataComponents.TOOL, createToolProperties()),
                10_000L, 100L, VoltageTier.LV, 50L,
                Set.of(ItemAbilities.HOE_TILL));
    }

    private static Tool createToolProperties() {
        HolderGetter<Block> blocks = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return new Tool(
                List.of(Tool.Rule.minesAndDrops(blocks.getOrThrow(BlockTags.MINEABLE_WITH_HOE), MINING_SPEED)),
                1.0F,
                0,
                true
        );
    }

    @Override
    public @NonNull InteractionResult useOn(@NonNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        BlockState tilled = state.getToolModifiedState(context, ItemAbilities.HOE_TILL, false);

        if (tilled == null) {
            return InteractionResult.PASS;
        }

        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (!canUse(stack, player)) {
            return InteractionResult.FAIL;
        }

        level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
        }

        if (!level.isClientSide()) {
            level.setBlock(pos, tilled, Block.UPDATE_ALL_IMMEDIATE);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, tilled));
            if (state.is(Blocks.ROOTED_DIRT)) {
                Block.popResourceFromFace(level, pos, context.getClickedFace(), new ItemStack(Items.HANGING_ROOTS));
            }

            tryConsumeUseEnergy(stack, player);
        }

        return InteractionResult.SUCCESS;
    }
}
