package net.kaelos.icre.block.machine.producing;

import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.entity.block.machine.producing.MaceratorBlockEntity;

public class MaceratorBlock extends BaseEntityMachineBlock<MaceratorBlockEntity> {
    public MaceratorBlock(Properties properties) {
        super(properties, MaceratorBlockEntity::new);
    }
}
