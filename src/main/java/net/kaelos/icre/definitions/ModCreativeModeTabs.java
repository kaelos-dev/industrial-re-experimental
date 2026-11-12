package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ICRE.MOD_ID);

    // ==================== Resources Tab ====================
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RESOURCES_TAB = registerTab(
            "resources_tab",
            ModItems.BRONZE_INGOT,
            (parameters, output) -> {
                output.accept(ModItems.BRONZE_INGOT.get());
                output.accept(ModItems.LEAD_INGOT.get());
                output.accept(ModItems.SILVER_INGOT.get());
                output.accept(ModItems.STEEL_INGOT.get());
                output.accept(ModItems.TIN_INGOT.get());

                output.accept(ModItems.BRONZE_PLATE.get());
                output.accept(ModItems.COPPER_PLATE.get());
                output.accept(ModItems.GOLD_PLATE.get());
                output.accept(ModItems.IRON_PLATE.get());
                output.accept(ModItems.LAPIS_LAZULI_PLATE.get());
                output.accept(ModItems.LEAD_PLATE.get());
                output.accept(ModItems.OBSIDIAN_PLATE.get());
                output.accept(ModItems.STEEL_PLATE.get());
                output.accept(ModItems.TIN_PLATE.get());

                output.accept(ModItems.DENSE_BRONZE_PLATE.get());
                output.accept(ModItems.DENSE_COPPER_PLATE.get());
                output.accept(ModItems.DENSE_GOLD_PLATE.get());
                output.accept(ModItems.DENSE_IRON_PLATE.get());
                output.accept(ModItems.DENSE_LAPIS_LAZULI_PLATE.get());
                output.accept(ModItems.DENSE_LEAD_PLATE.get());
                output.accept(ModItems.DENSE_OBSIDIAN_PLATE.get());
                output.accept(ModItems.DENSE_STEEL_PLATE.get());
                output.accept(ModItems.DENSE_TIN_PLATE.get());

                output.accept(ModItems.BRONZE_CASING.get());
                output.accept(ModItems.COPPER_CASING.get());
                output.accept(ModItems.GOLD_CASING.get());
                output.accept(ModItems.IRON_CASING.get());
                output.accept(ModItems.LEAD_CASING.get());
                output.accept(ModItems.STEEL_CASING.get());
                output.accept(ModItems.TIN_CASING.get());

                output.accept(ModBlocks.LEAD_ORE.get());
                output.accept(ModBlocks.DEEPSLATE_LEAD_ORE.get());
                output.accept(ModBlocks.TIN_ORE.get());
                output.accept(ModBlocks.DEEPSLATE_TIN_ORE.get());
                output.accept(ModBlocks.DEEPSLATE_URANIUM_ORE.get());

                output.accept(ModItems.RAW_LEAD.get());
                output.accept(ModItems.RAW_TIN.get());
                output.accept(ModItems.RAW_URANIUM.get());

                output.accept(ModItems.LATEX.get());
                output.accept(ModItems.RUBBER.get());
            }
    );

    // ==================== Agriculture Tab ====================
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AGRICULTURE_TAB = registerTab(
            "agriculture_tab",
            ModBlocks.RUBBER_SAPLING,
            (parameters, output) -> {
                output.accept(ModBlocks.RUBBER_LOG.get());
                output.accept(ModBlocks.RUBBER_WOOD.get());
                output.accept(ModBlocks.STRIPPED_RUBBER_LOG.get());
                output.accept(ModBlocks.STRIPPED_RUBBER_WOOD.get());
                output.accept(ModBlocks.RUBBER_PLANKS.get());
                output.accept(ModBlocks.RUBBER_LEAVES.get());
                output.accept(ModBlocks.RUBBER_SAPLING.get());
            }
    );

    // ==================== Components Tab ====================
//    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COMPONENTS_TAB = registerTab(
//            "components_tab",
//            ModBlocks.BATTERY,
//            (parameters, output) -> {}
//    );

    // ==================== Machines Tab ====================
//    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MACHINES_TAB = registerTab(
//            "machines_tab",
//            ModBlocks.MACERATOR,
//            (parameters, output) -> {}
//    );

    // ==================== Tools Tab ====================
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TOOLS_TAB = registerTab(
            "tools_tab",
            ModItems.TREETAP,
            (parameters, output) -> {
                output.accept(ModItems.TREETAP.get());
            }
    );

    private static DeferredHolder<CreativeModeTab, CreativeModeTab> registerTab(String name, Supplier<? extends ItemLike> icon, CreativeModeTab.DisplayItemsGenerator displayItems) {
        return CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
                .title(Component.translatable("creativetab." + ICRE.MOD_ID + "." + name))
                .icon(() -> new ItemStack(icon.get()))
                .displayItems(displayItems)
                .build());
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
