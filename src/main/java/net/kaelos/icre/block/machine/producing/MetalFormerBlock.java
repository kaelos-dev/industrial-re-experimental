package net.kaelos.icre.block.machine.producing;

import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.entity.block.machine.producing.MetalFormerBlockEntity;

public class MetalFormerBlock extends BaseEntityMachineBlock<MetalFormerBlockEntity> {
    public MetalFormerBlock(Properties properties) {
        super(properties, MetalFormerBlockEntity::new);
    }
}
