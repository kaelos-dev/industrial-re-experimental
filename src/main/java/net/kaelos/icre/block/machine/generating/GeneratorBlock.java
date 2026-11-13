package net.kaelos.icre.block.machine.generating;

import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.entity.block.machine.generating.GeneratorBlockEntity;

public class GeneratorBlock extends BaseEntityMachineBlock<GeneratorBlockEntity> {
    public GeneratorBlock(Properties properties) {
        super(properties, GeneratorBlockEntity::new);
    }
}
