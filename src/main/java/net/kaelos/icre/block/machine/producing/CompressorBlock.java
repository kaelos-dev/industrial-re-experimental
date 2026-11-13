package net.kaelos.icre.block.machine.producing;

import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.entity.block.machine.producing.CompressorBlockEntity;

public class CompressorBlock extends BaseEntityMachineBlock<CompressorBlockEntity> {
    public CompressorBlock(Properties properties) {
        super(properties, CompressorBlockEntity::new);
    }
}
