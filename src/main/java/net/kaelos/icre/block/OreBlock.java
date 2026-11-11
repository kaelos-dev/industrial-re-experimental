package net.kaelos.icre.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class OreBlock extends Block {
    public OreBlock(BlockBehaviour.Properties properties, boolean isDeepslate) {
        super(properties
                .mapColor(isDeepslate ? MapColor.DEEPSLATE : MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(isDeepslate ? 4.5F : 3.0F, 3.0F)
                .sound(isDeepslate ? SoundType.DEEPSLATE : SoundType.STONE));
    }
}
