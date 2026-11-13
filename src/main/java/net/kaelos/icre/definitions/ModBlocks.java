package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.block.cable.CableBlock;
import net.kaelos.icre.block.machine.generating.GeneratorBlock;
import net.kaelos.icre.block.machine.producing.MaceratorBlock;
import net.kaelos.icre.block.ore.OreBlock;
import net.kaelos.icre.block.rubber.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ICRE.MOD_ID);

    // ==================== Ores ====================
    public static final DeferredBlock<OreBlock> LEAD_ORE = registerBlock("lead_ore", prop ->
            new OreBlock(prop, false));
    public static final DeferredBlock<OreBlock> DEEPSLATE_LEAD_ORE = registerBlock("deepslate_lead_ore", prop ->
            new OreBlock(prop, true));
    public static final DeferredBlock<OreBlock> TIN_ORE = registerBlock("tin_ore", prop ->
            new OreBlock(prop, false));
    public static final DeferredBlock<OreBlock> DEEPSLATE_TIN_ORE = registerBlock("deepslate_tin_ore", prop ->
            new OreBlock(prop, true));
    public static final DeferredBlock<OreBlock> DEEPSLATE_URANIUM_ORE = registerBlock("deepslate_uranium_ore", prop ->
            new OreBlock(prop, true));

    // ==================== Rubber Tree ====================
    public static final DeferredBlock<RubberLogBlock> RUBBER_LOG = registerBlock("rubber_log", RubberLogBlock::new);
    public static final DeferredBlock<RubberPillarBlock> STRIPPED_RUBBER_LOG = registerBlock("stripped_rubber_log", RubberPillarBlock::new);
    public static final DeferredBlock<RubberWoodBlock> RUBBER_WOOD = registerBlock("rubber_wood", prop -> new RubberWoodBlock(false, prop));
    public static final DeferredBlock<RubberWoodBlock> STRIPPED_RUBBER_WOOD = registerBlock("stripped_rubber_wood", prop -> new RubberWoodBlock(true, prop));
    public static final DeferredBlock<RubberLeavesBlock> RUBBER_LEAVES = registerBlock("rubber_leaves", RubberLeavesBlock::new);
    public static final DeferredBlock<RubberSaplingBlock> RUBBER_SAPLING = registerBlock("rubber_sapling", RubberSaplingBlock::new);
    public static final DeferredBlock<RubberPlanksBlock> RUBBER_PLANKS = registerBlock("rubber_planks", RubberPlanksBlock::new);

    // ==================== Cables ====================
    public static final DeferredBlock<CableBlock> TIN_CABLE = registerBlock("tin_cable", prop ->
            new CableBlock(prop, VoltageTier.LV, false, 2));
    public static final DeferredBlock<CableBlock> COPPER_CABLE = registerBlock("copper_cable", prop ->
            new CableBlock(prop, VoltageTier.MV, false, 2));
    public static final DeferredBlock<CableBlock> GOLD_CABLE = registerBlock("gold_cable", prop ->
            new CableBlock(prop, VoltageTier.HV, false, 2));
    public static final DeferredBlock<CableBlock> IRON_CABLE = registerBlock("iron_cable", prop ->
            new CableBlock(prop, VoltageTier.EV, false, 2));
    public static final DeferredBlock<CableBlock> GLASS_FIBRE_CABLE = registerBlock("glass_fibre_cable", prop ->
            new CableBlock(prop, VoltageTier.IV, true, 2));

    // ==================== Generators ====================
    public static final DeferredBlock<GeneratorBlock> GENERATOR = registerBlock("generator", GeneratorBlock::new);

    // ==================== Machines ====================
    public static final DeferredBlock<MaceratorBlock> MACERATOR = registerBlock("macerator", MaceratorBlock::new);

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> factory) {
        DeferredBlock<T> object = BLOCKS.registerBlock(name, factory);
        registerBlockItem(name, object);
        return object;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerSimpleBlockItem(name, block);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
