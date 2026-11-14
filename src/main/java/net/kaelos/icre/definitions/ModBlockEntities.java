package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.block.cable.CableBlock;
import net.kaelos.icre.entity.block.electric.CableBlockEntity;
import net.kaelos.icre.entity.block.machine.generating.GeneratorBlockEntity;
import net.kaelos.icre.entity.block.machine.producing.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ICRE.MOD_ID);

    // ==================== Cables ====================
    public static final Supplier<BlockEntityType<CableBlockEntity>> CABLE_BE = BLOCK_ENTITIES.register("cable", () ->
            new BlockEntityType<>(CableBlockEntity::new,
                    ModBlocks.getBlocks(CableBlock.class).toArray(Block[]::new)));

    // ==================== Generators ====================
    public static final Supplier<BlockEntityType<GeneratorBlockEntity>> GENERATOR_BE = BLOCK_ENTITIES.register("generator", () ->
            new BlockEntityType<>(GeneratorBlockEntity::new, ModBlocks.GENERATOR.get()));

    // ==================== Machines ====================
    public static final Supplier<BlockEntityType<IronFurnaceBlockEntity>> IRON_FURNACE_BE = BLOCK_ENTITIES.register("iron_furnace", () ->
            new BlockEntityType<>(IronFurnaceBlockEntity::new, ModBlocks.IRON_FURNACE.get()));
    public static final Supplier<BlockEntityType<MaceratorBlockEntity>> MACERATOR_BE = BLOCK_ENTITIES.register("macerator", () ->
            new BlockEntityType<>(MaceratorBlockEntity::new, ModBlocks.MACERATOR.get()));
    public static final Supplier<BlockEntityType<MetalFormerBlockEntity>> METAL_FORMER_BE = BLOCK_ENTITIES.register("metal_former", () ->
            new BlockEntityType<>(MetalFormerBlockEntity::new, ModBlocks.METAL_FORMER.get()));
    public static final Supplier<BlockEntityType<ElectricFurnaceBlockEntity>> ELECTRIC_FURNACE_BE = BLOCK_ENTITIES.register("electric_furnace", () ->
            new BlockEntityType<>(ElectricFurnaceBlockEntity::new, ModBlocks.ELECTRIC_FURNACE.get()));
    public static final Supplier<BlockEntityType<CompressorBlockEntity>> COMPRESSOR_BE = BLOCK_ENTITIES.register("compressor", () ->
            new BlockEntityType<>(CompressorBlockEntity::new, ModBlocks.COMPRESSOR.get()));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
