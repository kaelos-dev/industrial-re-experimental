package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.item.LatexItem;
import net.kaelos.icre.item.tool.TreeTapItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ICRE.MOD_ID);

    // ==================== Ingots ====================
    public static final DeferredItem<Item> BRONZE_INGOT = registerItem("bronze_ingot", Item::new);
    public static final DeferredItem<Item> LEAD_INGOT = registerItem("lead_ingot", Item::new);
    public static final DeferredItem<Item> SILVER_INGOT = registerItem("silver_ingot", Item::new);
    public static final DeferredItem<Item> STEEL_INGOT = registerItem("steel_ingot", Item::new);
    public static final DeferredItem<Item> TIN_INGOT = registerItem("tin_ingot", Item::new);

    // ==================== Plates ====================
    public static final DeferredItem<Item> BRONZE_PLATE = registerItem("bronze_plate", Item::new);
    public static final DeferredItem<Item> COPPER_PLATE = registerItem("copper_plate", Item::new);
    public static final DeferredItem<Item> GOLD_PLATE = registerItem("gold_plate", Item::new);
    public static final DeferredItem<Item> IRON_PLATE = registerItem("iron_plate", Item::new);
    public static final DeferredItem<Item> LAPIS_LAZULI_PLATE = registerItem("lapis_lazuli_plate", Item::new);
    public static final DeferredItem<Item> LEAD_PLATE = registerItem("lead_plate", Item::new);
    public static final DeferredItem<Item> OBSIDIAN_PLATE = registerItem("obsidian_plate", Item::new);
    public static final DeferredItem<Item> STEEL_PLATE = registerItem("steel_plate", Item::new);
    public static final DeferredItem<Item> TIN_PLATE = registerItem("tin_plate", Item::new);

    // ==================== Dense Plates ====================
    public static final DeferredItem<Item> DENSE_BRONZE_PLATE = registerItem("dense_bronze_plate", Item::new);
    public static final DeferredItem<Item> DENSE_COPPER_PLATE = registerItem("dense_copper_plate", Item::new);
    public static final DeferredItem<Item> DENSE_GOLD_PLATE = registerItem("dense_gold_plate", Item::new);
    public static final DeferredItem<Item> DENSE_IRON_PLATE = registerItem("dense_iron_plate", Item::new);
    public static final DeferredItem<Item> DENSE_LAPIS_LAZULI_PLATE = registerItem("dense_lapis_lazuli_plate", Item::new);
    public static final DeferredItem<Item> DENSE_LEAD_PLATE = registerItem("dense_lead_plate", Item::new);
    public static final DeferredItem<Item> DENSE_OBSIDIAN_PLATE = registerItem("dense_obsidian_plate", Item::new);
    public static final DeferredItem<Item> DENSE_STEEL_PLATE = registerItem("dense_steel_plate", Item::new);
    public static final DeferredItem<Item> DENSE_TIN_PLATE = registerItem("dense_tin_plate", Item::new);

    // ==================== Casings ====================
    public static final DeferredItem<Item> BRONZE_CASING = registerItem("bronze_casing", Item::new);
    public static final DeferredItem<Item> COPPER_CASING = registerItem("copper_casing", Item::new);
    public static final DeferredItem<Item> GOLD_CASING = registerItem("gold_casing", Item::new);
    public static final DeferredItem<Item> IRON_CASING = registerItem("iron_casing", Item::new);
    public static final DeferredItem<Item> LEAD_CASING = registerItem("lead_casing", Item::new);
    public static final DeferredItem<Item> STEEL_CASING = registerItem("steel_casing", Item::new);
    public static final DeferredItem<Item> TIN_CASING = registerItem("tin_casing", Item::new);

    // ==================== Raws ====================
    public static final DeferredItem<Item> RAW_LEAD = registerItem("raw_lead", Item::new);
    public static final DeferredItem<Item> RAW_TIN = registerItem("raw_tin", Item::new);
    public static final DeferredItem<Item> RAW_URANIUM = registerItem("raw_uranium", Item::new);

    // ==================== Components ====================
    public static final DeferredItem<Item> LATEX = registerItem("latex", LatexItem::new);
    public static final DeferredItem<Item> RUBBER = registerItem("rubber", Item::new);

    // ==================== Tools ====================
    public static final DeferredItem<TreeTapItem> TREETAP = registerItem("treetap", TreeTapItem::new);

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> factory) {
        return ITEMS.registerItem(name, factory);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
