package net.kaelos.icre.datagen.models;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.datagen.models.electric.CableModels;
import net.kaelos.icre.datagen.models.electric.MachineModels;
import net.kaelos.icre.datagen.models.rubber.RubberTreeModels;
import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.definitions.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ICRE.MOD_ID);
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        registerMaterials(itemModels);
        registerOres(blockModels);
        registerTools(itemModels);

        RubberTreeModels.register(blockModels);
        CableModels.register(blockModels);
        MachineModels.register(blockModels);
    }

    private static void registerMaterials(ItemModelGenerators itemModels) {
        ModelHelpers.flatItems(itemModels, "ingot",
                ModItems.BRONZE_INGOT, ModItems.LEAD_INGOT, ModItems.SILVER_INGOT,
                ModItems.STEEL_INGOT, ModItems.TIN_INGOT);

        ModelHelpers.flatItems(itemModels, "plate",
                ModItems.BRONZE_PLATE, ModItems.COPPER_PLATE, ModItems.GOLD_PLATE,
                ModItems.IRON_PLATE, ModItems.LAPIS_LAZULI_PLATE, ModItems.LEAD_PLATE,
                ModItems.OBSIDIAN_PLATE, ModItems.STEEL_PLATE, ModItems.TIN_PLATE);

        ModelHelpers.flatItems(itemModels, "denseplate",
                ModItems.DENSE_BRONZE_PLATE, ModItems.DENSE_COPPER_PLATE, ModItems.DENSE_GOLD_PLATE,
                ModItems.DENSE_IRON_PLATE, ModItems.DENSE_LAPIS_LAZULI_PLATE, ModItems.DENSE_LEAD_PLATE,
                ModItems.DENSE_OBSIDIAN_PLATE, ModItems.DENSE_STEEL_PLATE, ModItems.DENSE_TIN_PLATE);

        ModelHelpers.flatItems(itemModels, "casing",
                ModItems.BRONZE_CASING, ModItems.COPPER_CASING, ModItems.GOLD_CASING,
                ModItems.IRON_CASING, ModItems.LEAD_CASING, ModItems.STEEL_CASING,
                ModItems.TIN_CASING);

        ModelHelpers.flatItems(itemModels, "dust",
                ModItems.BRONZE_DUST, ModItems.COPPER_DUST, ModItems.GOLD_DUST,
                ModItems.IRON_DUST, ModItems.LAPIS_LAZULI_DUST, ModItems.LEAD_DUST,
                ModItems.SILVER_DUST, ModItems.TIN_DUST, ModItems.HYDRATED_TIN_DUST,
                ModItems.DIAMOND_DUST, ModItems.COAL_DUST, ModItems.HYDRATED_COAL_DUST,
                ModItems.OBSIDIAN_DUST, ModItems.ENERGIUM_DUST, ModItems.CLAY_DUST,
                ModItems.LITHIUM_DUST, ModItems.SILICON_DIOXIDE_DUST, ModItems.SULFUR_DUST,
                ModItems.NETHERRACK_DUST, ModItems.ENDER_PEARL_DUST, ModItems.ENDER_EYE_DUST,
                ModItems.EMERALD_DUST, ModItems.MILK_POWDER);

        ModelHelpers.flatItems(itemModels, "raw",
                ModItems.RAW_LEAD, ModItems.RAW_TIN, ModItems.RAW_URANIUM);

        ModelHelpers.flatItems(itemModels, "res",
                ModItems.LATEX, ModItems.RUBBER);
    }

    private static void registerOres(BlockModelGenerators blockModels) {
        ModelHelpers.cubeBlocks(blockModels, "ore",
                ModBlocks.LEAD_ORE, ModBlocks.DEEPSLATE_LEAD_ORE, ModBlocks.TIN_ORE,
                ModBlocks.DEEPSLATE_TIN_ORE, ModBlocks.DEEPSLATE_URANIUM_ORE);
    }

    private static void registerTools(ItemModelGenerators itemModels) {
        ModelHelpers.flatHandheldItems(itemModels, "tool",
                ModItems.TREETAP, ModItems.WRENCH, ModItems.FORGE_HAMMER, ModItems.CUTTER);
    }
}
