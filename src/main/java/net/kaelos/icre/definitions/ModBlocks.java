package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.block.OreBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ICRE.MOD_ID);

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
