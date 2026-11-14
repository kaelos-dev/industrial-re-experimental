package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.item.LatexItem;
import net.kaelos.icre.item.energy.BatteryItem;
import net.kaelos.icre.item.tool.CraftingTool;
import net.kaelos.icre.item.tool.TreeTapTool;
import net.kaelos.icre.item.tool.WrenchTool;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ModItems {
    public enum ItemGroup {
        INGOT,
        PLATE,
        DENSE_PLATE,
        CASING,
        DUST,
        RAW,
        TOOL,
        BATTERY
    }

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ICRE.MOD_ID);

    private static final Map<ItemGroup, List<DeferredItem<? extends Item>>> GROUPS = new EnumMap<>(ItemGroup.class);

    // ==================== Ingots ====================
    public static final DeferredItem<Item> BRONZE_INGOT = registerItem("bronze_ingot", Item::new, ItemGroup.INGOT);
    public static final DeferredItem<Item> LEAD_INGOT = registerItem("lead_ingot", Item::new, ItemGroup.INGOT);
    public static final DeferredItem<Item> SILVER_INGOT = registerItem("silver_ingot", Item::new, ItemGroup.INGOT);
    public static final DeferredItem<Item> STEEL_INGOT = registerItem("steel_ingot", Item::new, ItemGroup.INGOT);
    public static final DeferredItem<Item> TIN_INGOT = registerItem("tin_ingot", Item::new, ItemGroup.INGOT);

    // ==================== Plates ====================
    public static final DeferredItem<Item> BRONZE_PLATE = registerItem("bronze_plate", Item::new, ItemGroup.PLATE);
    public static final DeferredItem<Item> COPPER_PLATE = registerItem("copper_plate", Item::new, ItemGroup.PLATE);
    public static final DeferredItem<Item> GOLD_PLATE = registerItem("gold_plate", Item::new, ItemGroup.PLATE);
    public static final DeferredItem<Item> IRON_PLATE = registerItem("iron_plate", Item::new, ItemGroup.PLATE);
    public static final DeferredItem<Item> LAPIS_LAZULI_PLATE = registerItem("lapis_lazuli_plate", Item::new, ItemGroup.PLATE);
    public static final DeferredItem<Item> LEAD_PLATE = registerItem("lead_plate", Item::new, ItemGroup.PLATE);
    public static final DeferredItem<Item> OBSIDIAN_PLATE = registerItem("obsidian_plate", Item::new, ItemGroup.PLATE);
    public static final DeferredItem<Item> STEEL_PLATE = registerItem("steel_plate", Item::new, ItemGroup.PLATE);
    public static final DeferredItem<Item> TIN_PLATE = registerItem("tin_plate", Item::new, ItemGroup.PLATE);

    // ==================== Dense Plates ====================
    public static final DeferredItem<Item> DENSE_BRONZE_PLATE = registerItem("dense_bronze_plate", Item::new, ItemGroup.DENSE_PLATE);
    public static final DeferredItem<Item> DENSE_COPPER_PLATE = registerItem("dense_copper_plate", Item::new, ItemGroup.DENSE_PLATE);
    public static final DeferredItem<Item> DENSE_GOLD_PLATE = registerItem("dense_gold_plate", Item::new, ItemGroup.DENSE_PLATE);
    public static final DeferredItem<Item> DENSE_IRON_PLATE = registerItem("dense_iron_plate", Item::new, ItemGroup.DENSE_PLATE);
    public static final DeferredItem<Item> DENSE_LAPIS_LAZULI_PLATE = registerItem("dense_lapis_lazuli_plate", Item::new, ItemGroup.DENSE_PLATE);
    public static final DeferredItem<Item> DENSE_LEAD_PLATE = registerItem("dense_lead_plate", Item::new, ItemGroup.DENSE_PLATE);
    public static final DeferredItem<Item> DENSE_OBSIDIAN_PLATE = registerItem("dense_obsidian_plate", Item::new, ItemGroup.DENSE_PLATE);
    public static final DeferredItem<Item> DENSE_STEEL_PLATE = registerItem("dense_steel_plate", Item::new, ItemGroup.DENSE_PLATE);
    public static final DeferredItem<Item> DENSE_TIN_PLATE = registerItem("dense_tin_plate", Item::new, ItemGroup.DENSE_PLATE);

    // ==================== Casings ====================
    public static final DeferredItem<Item> BRONZE_CASING = registerItem("bronze_casing", Item::new, ItemGroup.CASING);
    public static final DeferredItem<Item> COPPER_CASING = registerItem("copper_casing", Item::new, ItemGroup.CASING);
    public static final DeferredItem<Item> GOLD_CASING = registerItem("gold_casing", Item::new, ItemGroup.CASING);
    public static final DeferredItem<Item> IRON_CASING = registerItem("iron_casing", Item::new, ItemGroup.CASING);
    public static final DeferredItem<Item> LEAD_CASING = registerItem("lead_casing", Item::new, ItemGroup.CASING);
    public static final DeferredItem<Item> STEEL_CASING = registerItem("steel_casing", Item::new, ItemGroup.CASING);
    public static final DeferredItem<Item> TIN_CASING = registerItem("tin_casing", Item::new, ItemGroup.CASING);

    // ==================== Dusts ====================
    public static final DeferredItem<Item> BRONZE_DUST = registerItem("bronze_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> COPPER_DUST = registerItem("copper_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> GOLD_DUST = registerItem("gold_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> IRON_DUST = registerItem("iron_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> LAPIS_LAZULI_DUST = registerItem("lapis_lazuli_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> LEAD_DUST = registerItem("lead_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> SILVER_DUST = registerItem("silver_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> TIN_DUST = registerItem("tin_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> HYDRATED_TIN_DUST = registerItem("hydrated_tin_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> DIAMOND_DUST = registerItem("diamond_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> COAL_DUST = registerItem("coal_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> HYDRATED_COAL_DUST = registerItem("hydrated_coal_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> OBSIDIAN_DUST = registerItem("obsidian_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> ENERGIUM_DUST = registerItem("energium_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> CLAY_DUST = registerItem("clay_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> LITHIUM_DUST = registerItem("lithium_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> SILICON_DIOXIDE_DUST = registerItem("silicon_dioxide_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> SULFUR_DUST = registerItem("sulfur_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> NETHERRACK_DUST = registerItem("netherrack_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> ENDER_PEARL_DUST = registerItem("ender_pearl_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> ENDER_EYE_DUST = registerItem("ender_eye_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> EMERALD_DUST = registerItem("emerald_dust", Item::new, ItemGroup.DUST);
    public static final DeferredItem<Item> MILK_POWDER = registerItem("milk_powder", Item::new, ItemGroup.DUST);

    // ==================== Raws ====================
    public static final DeferredItem<Item> RAW_LEAD = registerItem("raw_lead", Item::new, ItemGroup.RAW);
    public static final DeferredItem<Item> RAW_TIN = registerItem("raw_tin", Item::new, ItemGroup.RAW);
    public static final DeferredItem<Item> RAW_URANIUM = registerItem("raw_uranium", Item::new, ItemGroup.RAW);

    // ==================== Components ====================
    public static final DeferredItem<Item> LATEX = registerItem("latex", LatexItem::new);
    public static final DeferredItem<Item> RUBBER = registerItem("rubber", Item::new);
    public static final DeferredItem<Item> COIL = registerItem("coil", Item::new);
    public static final DeferredItem<Item> ELECTRONIC_CIRCUIT = registerItem("electronic_circuit", Item::new);
    public static final DeferredItem<BatteryItem> RE_BATTERY = registerItem("re_battery", prop ->
            new BatteryItem(prop, 10_000L, 100L, VoltageTier.LV), ItemGroup.BATTERY);

    // ==================== Tools ====================
    public static final DeferredItem<TreeTapTool> TREETAP = registerItem("treetap", TreeTapTool::new, ItemGroup.TOOL);
    public static final DeferredItem<WrenchTool> WRENCH = registerItem("wrench", WrenchTool::new, ItemGroup.TOOL);
    public static final DeferredItem<Item> FORGE_HAMMER = registerItem("forge_hammer", prop -> new CraftingTool(prop, 80), ItemGroup.TOOL);
    public static final DeferredItem<Item> CUTTER = registerItem("cutter", prop -> new CraftingTool(prop, 60), ItemGroup.TOOL);

    public static List<Item> getGroup(ItemGroup group) {
        return GROUPS.getOrDefault(group, List.of()).stream()
                .<Item>map(DeferredItem::get)
                .toList();
    }

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> factory) {
        return ITEMS.registerItem(name, factory);
    }

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> factory, ItemGroup group) {
        DeferredItem<T> item = ITEMS.registerItem(name, factory);
        GROUPS.computeIfAbsent(group, k -> new ArrayList<>()).add(item);
        return item;
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
