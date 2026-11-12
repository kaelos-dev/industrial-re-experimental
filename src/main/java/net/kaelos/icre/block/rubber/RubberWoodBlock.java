package net.kaelos.icre.block.rubber;

import net.kaelos.icre.definitions.ModBlocks;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class RubberWoodBlock extends RubberPillarBlock {
    private final boolean stripped;

    public RubberWoodBlock(boolean stripped, Properties properties) {
        super(properties);
        this.stripped = stripped;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(@NonNull BlockState state, @NonNull UseOnContext context, @NonNull ItemAbility itemAbility, boolean simulate) {
        if (!this.stripped && itemAbility == ItemAbilities.AXE_STRIP && context.getItemInHand().canPerformAction(itemAbility)) {
            return ModBlocks.STRIPPED_RUBBER_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }
}
