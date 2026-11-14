package net.kaelos.icre.item.tool.electric;

import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Set;

public class ElectricWrenchTool extends BaseEnergyToolItem {
    private static final float MINING_SPEED = 20.0F;

    public ElectricWrenchTool(Properties properties) {
        super(properties.component(DataComponents.TOOL, createToolProperties()),
                10_000L, 100L, VoltageTier.LV, 50L, Set.of());
    }

    private static Tool createToolProperties() {
        HolderGetter<Block> blocks = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return new Tool(
                List.of(Tool.Rule.minesAndDrops(blocks.getOrThrow(ModTags.Blocks.MINEABLE_WITH_WRENCH), MINING_SPEED)),
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

        if (state.getBlock() instanceof BaseEntityMachineBlock<?> machine) {
            return machine.tryWrenchRotate(state, level, pos, context.getPlayer(), context.getHand(), context.getItemInHand());
        }

        return InteractionResult.PASS;
    }
}
