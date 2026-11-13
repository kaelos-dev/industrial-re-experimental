package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.block.cable.CableBlock;
import net.kaelos.icre.block.machine.generating.GeneratorBlock;
import net.kaelos.icre.block.machine.producing.CompressorBlock;
import net.kaelos.icre.block.machine.producing.ElectricFurnaceBlock;
import net.kaelos.icre.block.machine.producing.MaceratorBlock;
import net.kaelos.icre.block.machine.producing.MetalFormerBlock;
import net.kaelos.icre.block.ore.OreBlock;
import net.kaelos.icre.block.rubber.*;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
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
    public static final DeferredBlock<CableBlock> TIN_CABLE = registerCable("tin_cable", VoltageTier.LV, false, 2);
    public static final DeferredBlock<CableBlock> TIN_CABLE_1X = registerCable("tin_cable_1x", VoltageTier.LV, true, 3);

    public static final DeferredBlock<CableBlock> COPPER_CABLE = registerCable("copper_cable", VoltageTier.MV, false, 2);
    public static final DeferredBlock<CableBlock> COPPER_CABLE_1X = registerCable("copper_cable_1x", VoltageTier.MV, true, 3);

    public static final DeferredBlock<CableBlock> GOLD_CABLE = registerCable("gold_cable", VoltageTier.HV, false, 2);
    public static final DeferredBlock<CableBlock> GOLD_CABLE_1X = registerCable("gold_cable_1x", VoltageTier.HV, true, 3);
    public static final DeferredBlock<CableBlock> GOLD_CABLE_2X = registerCable("gold_cable_2x", VoltageTier.HV, true, 4);

    public static final DeferredBlock<CableBlock> IRON_CABLE = registerCable("iron_cable", VoltageTier.EV, false, 2);
    public static final DeferredBlock<CableBlock> IRON_CABLE_1X = registerCable("iron_cable_1x", VoltageTier.EV, true, 3);
    public static final DeferredBlock<CableBlock> IRON_CABLE_2X = registerCable("iron_cable_2x", VoltageTier.EV, true, 4);
    public static final DeferredBlock<CableBlock> IRON_CABLE_4X = registerCable("iron_cable_4x", VoltageTier.EV, true, 6);

    public static final DeferredBlock<CableBlock> GLASS_FIBRE_CABLE = registerCable("glass_fibre_cable", VoltageTier.IV, true, 2);

    // ==================== Machine Casings ====================
    public static final DeferredBlock<Block> BASIC_MACHINE_CASING = registerBlock("basic_machine_casing", prop ->
            new Block(prop
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .strength(2.5F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));

    // ==================== Generators ====================
    public static final DeferredBlock<GeneratorBlock> GENERATOR = registerBlock("generator", GeneratorBlock::new);

    // ==================== Machines ====================
    public static final DeferredBlock<MaceratorBlock> MACERATOR = registerBlock("macerator", MaceratorBlock::new);
    public static final DeferredBlock<MetalFormerBlock> METAL_FORMER = registerBlock("metal_former", MetalFormerBlock::new);
    public static final DeferredBlock<ElectricFurnaceBlock> ELECTRIC_FURNACE = registerBlock("electric_furnace", ElectricFurnaceBlock::new);
    public static final DeferredBlock<CompressorBlock> COMPRESSOR = registerBlock("compressor", CompressorBlock::new);

    public static List<Block> getBlocks(Class<? extends Block> type) {
        return BLOCKS.getEntries().stream()
                .map(Holder::value)
                .filter(type::isInstance)
                .toList();
    }

    private static DeferredBlock<CableBlock> registerCable(String name, VoltageTier tier, boolean insulated, int radius) {
        return registerBlock(name, prop -> new CableBlock(prop, tier, insulated, radius));
    }

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
