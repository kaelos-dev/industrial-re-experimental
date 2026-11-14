package net.kaelos.icre.block.storage;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class StorageBlock extends Block {
    public StorageBlock(Properties properties) {
        super(properties
                .mapColor(MapColor.METAL)
                .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 3.0F)
                .sound(SoundType.IRON));
    }
}
