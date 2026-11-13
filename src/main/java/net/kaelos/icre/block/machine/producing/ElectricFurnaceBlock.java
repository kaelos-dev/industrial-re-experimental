package net.kaelos.icre.block.machine.producing;

import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.entity.block.machine.producing.ElectricFurnaceBlockEntity;

public class ElectricFurnaceBlock extends BaseEntityMachineBlock<ElectricFurnaceBlockEntity> {
    public ElectricFurnaceBlock(Properties properties) {
        super(properties, ElectricFurnaceBlockEntity::new);
    }
}
