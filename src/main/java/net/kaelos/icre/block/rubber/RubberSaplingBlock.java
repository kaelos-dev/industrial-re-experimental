package net.kaelos.icre.block.rubber;

import net.kaelos.icre.worldgen.ModWorldGen;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Optional;

public class RubberSaplingBlock extends SaplingBlock {
    public static final TreeGrower RUBBER_TREE_GROWER = new TreeGrower(
            "rubber",
            Optional.empty(),
            Optional.of(ModWorldGen.RUBBER_TREE_SAPLING_CF),
            Optional.empty()
    );

    public RubberSaplingBlock(Properties properties) {
        super(RUBBER_TREE_GROWER, properties
                .mapColor(MapColor.PLANT)
                .noCollision()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY));
    }
}
