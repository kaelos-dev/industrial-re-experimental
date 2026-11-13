package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.entity.block.electric.CableBlockEntity;
import net.kaelos.icre.entity.block.machine.generating.GeneratorBlockEntity;
import net.kaelos.icre.entity.block.machine.producing.MaceratorBlockEntity;
import net.minecraft.core.registries.Registries;
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
                    ModBlocks.TIN_CABLE.get(), ModBlocks.COPPER_CABLE.get(), ModBlocks.GOLD_CABLE.get(),
                    ModBlocks.IRON_CABLE.get(), ModBlocks.GLASS_FIBRE_CABLE.get()));

    // ==================== Generators ====================
    public static final Supplier<BlockEntityType<GeneratorBlockEntity>> GENERATOR_BE = BLOCK_ENTITIES.register("generator", () ->
            new BlockEntityType<>(GeneratorBlockEntity::new, ModBlocks.GENERATOR.get()));

    // ==================== Machines ====================
    public static final Supplier<BlockEntityType<MaceratorBlockEntity>> MACERATOR_BE = BLOCK_ENTITIES.register("macerator", () ->
            new BlockEntityType<>(MaceratorBlockEntity::new, ModBlocks.MACERATOR.get()));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
