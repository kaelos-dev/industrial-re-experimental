package net.kaelos.icre.datagen.recipe;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.definitions.ModItems;
import net.kaelos.icre.recipe.metalformer.MetalFormerMode;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    private final RecipeOutput toolOutput;

    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
        this.toolOutput = RecipeHelpers.asToolOutput(output);
    }

    @Override
    protected void buildRecipes() {
        registerCraftingRecipes();
        registerFurnaceAndBlastingRecipes();
        registerMaceratorRecipes();
        registerCompressorRecipes();
        registerMetalFormerRecipes();
    }

    private void registerCraftingRecipes() {
        // Rubber tree & planks
        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUBBER_PLANKS, 4, ModBlocks.RUBBER_LOG);
        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUBBER_PLANKS, 4, ModBlocks.STRIPPED_RUBBER_LOG);
        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUBBER_PLANKS, 4, ModBlocks.RUBBER_WOOD);
        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUBBER_PLANKS, 4, ModBlocks.STRIPPED_RUBBER_WOOD);

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUBBER_WOOD, 3, ModBlocks.RUBBER_LOG, builder -> builder
                .pattern("LL")
                .pattern("LL")
                .define('L', ModBlocks.RUBBER_LOG));
        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_RUBBER_WOOD, 3, ModBlocks.STRIPPED_RUBBER_LOG, builder -> builder
                .pattern("LL")
                .pattern("LL")
                .define('L', ModBlocks.STRIPPED_RUBBER_LOG));

        // Storage blocks <-> ingots
        storageBlock(ModItems.BRONZE_INGOT, ModBlocks.BRONZE_BLOCK);
        storageBlock(ModItems.LEAD_INGOT, ModBlocks.LEAD_BLOCK);
        storageBlock(ModItems.SILVER_INGOT, ModBlocks.SILVER_BLOCK);
        storageBlock(ModItems.STEEL_INGOT, ModBlocks.STEEL_BLOCK);
        storageBlock(ModItems.TIN_INGOT, ModBlocks.TIN_BLOCK);

        // Dusts & mixtures
        shapeless(ModItems.BRONZE_DUST, 4, ModItems.COPPER_DUST, ModItems.COPPER_DUST, ModItems.COPPER_DUST, ModItems.TIN_DUST);
        shaped(RecipeCategory.MISC, ModItems.ENERGIUM_DUST, 9, ModItems.DIAMOND_DUST, builder -> builder
                .pattern("RDR")
                .pattern("DRD")
                .pattern("RDR")
                .define('R', Items.REDSTONE)
                .define('D', ModItems.DIAMOND_DUST));
        shaped(RecipeCategory.MISC, ModItems.HYDRATED_COAL_DUST, 8, ModItems.COAL_DUST, builder -> builder
                .pattern("CCC")
                .pattern("CWC")
                .pattern("CCC")
                .define('C', ModItems.COAL_DUST)
                .define('W', Items.WATER_BUCKET));
        shaped(RecipeCategory.MISC, ModItems.HYDRATED_TIN_DUST, 8, ModItems.TIN_DUST, builder -> builder
                .pattern("TTT")
                .pattern("TWT")
                .pattern("TTT")
                .define('T', ModItems.TIN_DUST)
                .define('W', Items.WATER_BUCKET));

        // Tools
        shaped(RecipeCategory.TOOLS, ModItems.FORGE_HAMMER, 1, Items.IRON_INGOT, builder -> builder
                .pattern("II ")
                .pattern("ISS")
                .pattern("II ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK));
        shaped(RecipeCategory.TOOLS, ModItems.WRENCH, 1, ModItems.BRONZE_INGOT, builder -> builder
                .pattern("B B")
                .pattern("BBB")
                .pattern(" B ")
                .define('B', ModItems.BRONZE_INGOT));
        shaped(RecipeCategory.TOOLS, ModItems.CUTTER, 1, ModItems.IRON_PLATE, builder -> builder
                .pattern("P P")
                .pattern(" P ")
                .pattern("I I")
                .define('P', ModItems.IRON_PLATE)
                .define('I', Items.IRON_INGOT));
        ShapedRecipeBuilder treetap = shaped(RecipeCategory.TOOLS, ModItems.TREETAP)
                .pattern(" P ")
                .pattern("PPP")
                .pattern("P  ")
                .define('P', ItemTags.PLANKS);
        treetap.unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(this.output, ResourceKey.create(Registries.RECIPE, ICRE.makeId("crafting/treetap_from_planks")));

        // Hammer -> plates
        hammerPlate(ModItems.BRONZE_INGOT, ModItems.BRONZE_PLATE);
        hammerPlate(Items.COPPER_INGOT, ModItems.COPPER_PLATE);
        hammerPlate(Items.GOLD_INGOT, ModItems.GOLD_PLATE);
        hammerPlate(Items.IRON_INGOT, ModItems.IRON_PLATE);
        hammerPlate(ModItems.LEAD_INGOT, ModItems.LEAD_PLATE);
        hammerPlate(ModItems.STEEL_INGOT, ModItems.STEEL_PLATE);
        hammerPlate(ModItems.TIN_INGOT, ModItems.TIN_PLATE);

        // Hammer -> casings
        hammerCasing(ModItems.BRONZE_PLATE, ModItems.BRONZE_CASING);
        hammerCasing(ModItems.COPPER_PLATE, ModItems.COPPER_CASING);
        hammerCasing(ModItems.GOLD_PLATE, ModItems.GOLD_CASING);
        hammerCasing(ModItems.IRON_PLATE, ModItems.IRON_CASING);
        hammerCasing(ModItems.LEAD_PLATE, ModItems.LEAD_CASING);
        hammerCasing(ModItems.STEEL_PLATE, ModItems.STEEL_CASING);
        hammerCasing(ModItems.TIN_PLATE, ModItems.TIN_CASING);

        // Cutter -> uninsulated cables
        toolShapeless(ModBlocks.COPPER_CABLE, 2, ModItems.CUTTER, ModItems.COPPER_PLATE);
        toolShapeless(ModBlocks.TIN_CABLE, 3, ModItems.CUTTER, ModItems.TIN_PLATE);
        toolShapeless(ModBlocks.GOLD_CABLE, 4, ModItems.CUTTER, ModItems.GOLD_PLATE);
        toolShapeless(ModBlocks.IRON_CABLE, 4, ModItems.CUTTER, ModItems.IRON_PLATE);

        // Cable insulation
        shapeless(ModBlocks.TIN_CABLE_1X, 1, ModBlocks.TIN_CABLE, ModItems.RUBBER);
        shapeless(ModBlocks.COPPER_CABLE_1X, 1, ModBlocks.COPPER_CABLE, ModItems.RUBBER);
        shapeless(ModBlocks.GOLD_CABLE_1X, 1, ModBlocks.GOLD_CABLE, ModItems.RUBBER);
        shapeless(ModBlocks.GOLD_CABLE_2X, 1, ModBlocks.GOLD_CABLE_1X, ModItems.RUBBER);
        shapeless(ModBlocks.GOLD_CABLE_2X, 1, ModBlocks.GOLD_CABLE, ModItems.RUBBER, ModItems.RUBBER);
        shapeless(ModBlocks.IRON_CABLE_1X, 1, ModBlocks.IRON_CABLE, ModItems.RUBBER);
        shapeless(ModBlocks.IRON_CABLE_2X, 1, ModBlocks.IRON_CABLE_1X, ModItems.RUBBER);
        shapeless(ModBlocks.IRON_CABLE_2X, 1, ModBlocks.IRON_CABLE, ModItems.RUBBER, ModItems.RUBBER);
        shapeless(ModBlocks.IRON_CABLE_4X, 1, ModBlocks.IRON_CABLE_2X, ModItems.RUBBER, ModItems.RUBBER);
        shapeless(ModBlocks.IRON_CABLE_4X, 1, ModBlocks.IRON_CABLE_1X, ModItems.RUBBER, ModItems.RUBBER, ModItems.RUBBER);
        shapeless(ModBlocks.IRON_CABLE_4X, 1, ModBlocks.IRON_CABLE, ModItems.RUBBER, ModItems.RUBBER, ModItems.RUBBER, ModItems.RUBBER);
        shaped(RecipeCategory.MISC, ModBlocks.GLASS_FIBRE_CABLE, 6, ModItems.ENERGIUM_DUST, builder -> builder
                .pattern("GGG")
                .pattern("ESE")
                .pattern("GGG")
                .define('G', Blocks.GLASS)
                .define('E', ModItems.ENERGIUM_DUST)
                .define('S', ModItems.SILVER_DUST));

        // Components & machine casing
        shaped(ModBlocks.BASIC_MACHINE_CASING, ModItems.IRON_PLATE, builder -> builder
                .pattern("PPP")
                .pattern("P P")
                .pattern("PPP")
                .define('P', ModItems.IRON_PLATE));
        shaped(ModItems.COIL, ModBlocks.COPPER_CABLE, builder -> builder
                .pattern("CCC")
                .pattern("CIC")
                .pattern("CCC")
                .define('C', ModBlocks.COPPER_CABLE)
                .define('I', Items.IRON_INGOT));
        shaped("electronic_circuit_horizontal", ModItems.ELECTRONIC_CIRCUIT, 1, ModItems.IRON_PLATE, builder -> builder
                .pattern("CCC")
                .pattern("RPR")
                .pattern("CCC")
                .define('C', ModBlocks.COPPER_CABLE_1X)
                .define('R', Items.REDSTONE)
                .define('P', ModItems.IRON_PLATE));
        shaped("electronic_circuit_vertical", ModItems.ELECTRONIC_CIRCUIT, 1, ModItems.IRON_PLATE, builder -> builder
                .pattern("CRC")
                .pattern("CPC")
                .pattern("CRC")
                .define('C', ModBlocks.COPPER_CABLE_1X)
                .define('R', Items.REDSTONE)
                .define('P', ModItems.IRON_PLATE));
        shaped(ModItems.RE_BATTERY, ModItems.TIN_CASING, builder -> builder
                .pattern(" T ")
                .pattern("CRC")
                .pattern("CRC")
                .define('T', ModBlocks.TIN_CABLE_1X)
                .define('C', ModItems.TIN_CASING)
                .define('R', Items.REDSTONE));

        // Machines & generator
        shaped(ModBlocks.IRON_FURNACE, Blocks.FURNACE, builder -> builder
                .pattern(" P ")
                .pattern("P P")
                .pattern("PFP")
                .define('P', ModItems.IRON_PLATE)
                .define('F', Blocks.FURNACE));
        shaped(ModBlocks.GENERATOR, ModBlocks.IRON_FURNACE, builder -> builder
                .pattern(" B ")
                .pattern("PPP")
                .pattern(" F ")
                .define('B', ModItems.RE_BATTERY)
                .define('P', ModItems.IRON_PLATE)
                .define('F', ModBlocks.IRON_FURNACE));
        shaped(ModBlocks.GENERATOR, ModBlocks.BASIC_MACHINE_CASING, builder -> builder
                .pattern(" B ")
                .pattern(" M ")
                .pattern(" F ")
                .define('B', ModItems.RE_BATTERY)
                .define('M', ModBlocks.BASIC_MACHINE_CASING)
                .define('F', Blocks.FURNACE));
        shaped(ModBlocks.ELECTRIC_FURNACE, ModBlocks.IRON_FURNACE, builder -> builder
                .pattern(" C ")
                .pattern("RFR")
                .define('C', ModItems.ELECTRONIC_CIRCUIT)
                .define('R', Items.REDSTONE)
                .define('F', ModBlocks.IRON_FURNACE));
        shaped(ModBlocks.MACERATOR, ModBlocks.BASIC_MACHINE_CASING, builder -> builder
                .pattern("FFF")
                .pattern("SMS")
                .pattern(" C ")
                .define('F', Items.FLINT)
                .define('S', Blocks.COBBLESTONE)
                .define('M', ModBlocks.BASIC_MACHINE_CASING)
                .define('C', ModItems.ELECTRONIC_CIRCUIT));
        shaped(ModBlocks.COMPRESSOR, ModBlocks.BASIC_MACHINE_CASING, builder -> builder
                .pattern("S S")
                .pattern("SMS")
                .pattern("SCS")
                .define('S', Blocks.STONE)
                .define('M', ModBlocks.BASIC_MACHINE_CASING)
                .define('C', ModItems.ELECTRONIC_CIRCUIT));

        // Edit next
        shaped(ModBlocks.METAL_FORMER, ModItems.COIL, builder -> builder
                .pattern(" C ")
                .pattern("TMT")
                .pattern("KKK")
                .define('C', ModItems.ELECTRONIC_CIRCUIT)
                .define('T', ModItems.TIN_CASING)
                .define('M', ModBlocks.BASIC_MACHINE_CASING)
                .define('K', ModItems.COIL));
    }

    private void registerFurnaceAndBlastingRecipes() {
        // Ores & raw materials
        smeltAndBlast(ModBlocks.LEAD_ORE, ModItems.LEAD_INGOT);
        smeltAndBlast(ModBlocks.DEEPSLATE_LEAD_ORE, ModItems.LEAD_INGOT);
        smeltAndBlast(ModItems.RAW_LEAD, ModItems.LEAD_INGOT);
        smeltAndBlast(ModBlocks.TIN_ORE, ModItems.TIN_INGOT);
        smeltAndBlast(ModBlocks.DEEPSLATE_TIN_ORE, ModItems.TIN_INGOT);
        smeltAndBlast(ModItems.RAW_TIN, ModItems.TIN_INGOT);

        // Dusts -> ingots
        smeltAndBlast(ModItems.BRONZE_DUST, ModItems.BRONZE_INGOT);
        smeltAndBlast(ModItems.COPPER_DUST, Items.COPPER_INGOT);
        smeltAndBlast(ModItems.GOLD_DUST, Items.GOLD_INGOT);
        smeltAndBlast(ModItems.IRON_DUST, Items.IRON_INGOT);
        smeltAndBlast(ModItems.LEAD_DUST, ModItems.LEAD_INGOT);
        smeltAndBlast(ModItems.SILVER_DUST, ModItems.SILVER_INGOT);
        smeltAndBlast(ModItems.TIN_DUST, ModItems.TIN_INGOT);
    }

    private void registerMaceratorRecipes() {
        // Ores -> 2 dust
        macerator(ModBlocks.LEAD_ORE, ModItems.LEAD_DUST, 2);
        macerator(ModBlocks.DEEPSLATE_LEAD_ORE, ModItems.LEAD_DUST, 2);
        macerator(ModBlocks.TIN_ORE, ModItems.TIN_DUST, 2);
        macerator(ModBlocks.DEEPSLATE_TIN_ORE, ModItems.TIN_DUST, 2);
        macerator(Blocks.IRON_ORE, ModItems.IRON_DUST, 2);
        macerator(Blocks.DEEPSLATE_IRON_ORE, ModItems.IRON_DUST, 2);
        macerator(Blocks.GOLD_ORE, ModItems.GOLD_DUST, 2);
        macerator(Blocks.DEEPSLATE_GOLD_ORE, ModItems.GOLD_DUST, 2);
        macerator(Blocks.NETHER_GOLD_ORE, ModItems.GOLD_DUST, 2);
        macerator(Blocks.COPPER_ORE, ModItems.COPPER_DUST, 2);
        macerator(Blocks.DEEPSLATE_COPPER_ORE, ModItems.COPPER_DUST, 2);

        // Raw materials -> 1 dust (избегаем эксплойта с Удачей III)
        macerator(ModItems.RAW_LEAD, ModItems.LEAD_DUST);
        macerator(ModItems.RAW_TIN, ModItems.TIN_DUST);
        macerator(Items.RAW_IRON, ModItems.IRON_DUST);
        macerator(Items.RAW_GOLD, ModItems.GOLD_DUST);
        macerator(Items.RAW_COPPER, ModItems.COPPER_DUST);

        // Ingots -> 1 dust
        macerator(ModItems.BRONZE_INGOT, ModItems.BRONZE_DUST);
        macerator(Items.COPPER_INGOT, ModItems.COPPER_DUST);
        macerator(Items.GOLD_INGOT, ModItems.GOLD_DUST);
        macerator(Items.IRON_INGOT, ModItems.IRON_DUST);
        macerator(ModItems.LEAD_INGOT, ModItems.LEAD_DUST);
        macerator(ModItems.SILVER_INGOT, ModItems.SILVER_DUST);
        macerator(ModItems.TIN_INGOT, ModItems.TIN_DUST);

        // Minerals & special dusts
        macerator(Items.COAL, ModItems.COAL_DUST);
        macerator(Items.CHARCOAL, ModItems.COAL_DUST);
        macerator(Items.DIAMOND, ModItems.DIAMOND_DUST);
        macerator(Items.EMERALD, ModItems.EMERALD_DUST);
        macerator(Items.LAPIS_LAZULI, ModItems.LAPIS_LAZULI_DUST);
        macerator(Blocks.OBSIDIAN, ModItems.OBSIDIAN_DUST);
        macerator(Items.CLAY_BALL, ModItems.CLAY_DUST);
        macerator(Blocks.CLAY, Items.CLAY_BALL, 4);
        macerator(Blocks.SAND, ModItems.SILICON_DIOXIDE_DUST);
        macerator(Blocks.NETHERRACK, ModItems.NETHERRACK_DUST);
        macerator(Items.ENDER_PEARL, ModItems.ENDER_PEARL_DUST);
        macerator(Items.ENDER_EYE, ModItems.ENDER_EYE_DUST);

        // Vanilla utility maceration
        macerator(Blocks.COBBLESTONE, Blocks.SAND);
        macerator(Blocks.GRAVEL, Items.FLINT);
        macerator(Blocks.GLOWSTONE, Items.GLOWSTONE_DUST, 4);
        macerator(Items.BONE, Items.BONE_MEAL, 5);
        macerator(Items.BLAZE_ROD, Items.BLAZE_POWDER, 5);
        macerator(Blocks.MELON, Items.MELON_SEEDS, 8);
        macerator(Blocks.PUMPKIN, Items.PUMPKIN_SEEDS, 4);
    }

    private void registerCompressorRecipes() {
        // 9 plates -> dense plate
        compressor(ModItems.BRONZE_PLATE, 8, ModItems.DENSE_BRONZE_PLATE);
        compressor(ModItems.COPPER_PLATE, 8, ModItems.DENSE_COPPER_PLATE);
        compressor(ModItems.GOLD_PLATE, 8, ModItems.DENSE_GOLD_PLATE);
        compressor(ModItems.IRON_PLATE, 8, ModItems.DENSE_IRON_PLATE);
        compressor(ModItems.LAPIS_LAZULI_PLATE, 8, ModItems.DENSE_LAPIS_LAZULI_PLATE);
        compressor(ModItems.LEAD_PLATE, 8, ModItems.DENSE_LEAD_PLATE);
        compressor(ModItems.OBSIDIAN_PLATE, 8, ModItems.DENSE_OBSIDIAN_PLATE);
        compressor(ModItems.STEEL_PLATE, 8, ModItems.DENSE_STEEL_PLATE);
        compressor(ModItems.TIN_PLATE, 8, ModItems.DENSE_TIN_PLATE);

        // Dusts -> plates
        compressor(ModItems.LAPIS_LAZULI_DUST, ModItems.LAPIS_LAZULI_PLATE);
        compressor(ModItems.OBSIDIAN_DUST, ModItems.OBSIDIAN_PLATE);

        // Vanilla utility compression
        compressor(Blocks.SNOW_BLOCK, Blocks.ICE);
        compressor(Blocks.SAND, 4, Blocks.SANDSTONE);
        compressor(Items.CLAY_BALL, 4, Blocks.CLAY);
        compressor(Items.BLAZE_POWDER, 4, Items.BLAZE_ROD);
        compressor(Items.NETHER_BRICK, 4, Blocks.NETHER_BRICKS);
    }

    private void registerMetalFormerRecipes() {
        // Rolling: ingots -> plates
        metalFormer(MetalFormerMode.ROLLING, ModItems.BRONZE_INGOT, ModItems.BRONZE_PLATE);
        metalFormer(MetalFormerMode.ROLLING, Items.COPPER_INGOT, ModItems.COPPER_PLATE);
        metalFormer(MetalFormerMode.ROLLING, Items.GOLD_INGOT, ModItems.GOLD_PLATE);
        metalFormer(MetalFormerMode.ROLLING, Items.IRON_INGOT, ModItems.IRON_PLATE);
        metalFormer(MetalFormerMode.ROLLING, ModItems.LEAD_INGOT, ModItems.LEAD_PLATE);
        metalFormer(MetalFormerMode.ROLLING, ModItems.STEEL_INGOT, ModItems.STEEL_PLATE);
        metalFormer(MetalFormerMode.ROLLING, ModItems.TIN_INGOT, ModItems.TIN_PLATE);

        // Rolling: plates -> 2 casings
        metalFormer(MetalFormerMode.ROLLING, ModItems.BRONZE_PLATE, ModItems.BRONZE_CASING, 2);
        metalFormer(MetalFormerMode.ROLLING, ModItems.COPPER_PLATE, ModItems.COPPER_CASING, 2);
        metalFormer(MetalFormerMode.ROLLING, ModItems.GOLD_PLATE, ModItems.GOLD_CASING, 2);
        metalFormer(MetalFormerMode.ROLLING, ModItems.IRON_PLATE, ModItems.IRON_CASING, 2);
        metalFormer(MetalFormerMode.ROLLING, ModItems.LEAD_PLATE, ModItems.LEAD_CASING, 2);
        metalFormer(MetalFormerMode.ROLLING, ModItems.STEEL_PLATE, ModItems.STEEL_CASING, 2);
        metalFormer(MetalFormerMode.ROLLING, ModItems.TIN_PLATE, ModItems.TIN_CASING, 2);

        // Cutting: plates -> cables
        metalFormer(MetalFormerMode.CUTTING, ModItems.COPPER_PLATE, ModBlocks.COPPER_CABLE, 3);
        metalFormer(MetalFormerMode.CUTTING, ModItems.TIN_PLATE, ModBlocks.TIN_CABLE, 4);
        metalFormer(MetalFormerMode.CUTTING, ModItems.GOLD_PLATE, ModBlocks.GOLD_CABLE, 6);
        metalFormer(MetalFormerMode.CUTTING, ModItems.IRON_PLATE, ModBlocks.IRON_CABLE, 6);

        // Extruding: ingots -> cables
        metalFormer(MetalFormerMode.EXTRUDING, Items.COPPER_INGOT, ModBlocks.COPPER_CABLE, 3);
        metalFormer(MetalFormerMode.EXTRUDING, ModItems.TIN_INGOT, ModBlocks.TIN_CABLE, 4);
        metalFormer(MetalFormerMode.EXTRUDING, Items.GOLD_INGOT, ModBlocks.GOLD_CABLE, 6);
        metalFormer(MetalFormerMode.EXTRUDING, Items.IRON_INGOT, ModBlocks.IRON_CABLE, 6);
    }

    // ==================== Domain Helpers ====================
    private void storageBlock(ItemLike ingot, ItemLike block) {
        shaped(RecipeCategory.BUILDING_BLOCKS, block, 1, ingot, builder -> builder
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ingot));
        shapeless(RecipeCategory.MISC, ingot, 9, block);
    }

    private void hammerPlate(ItemLike ingot, ItemLike plate) {
        toolShapeless(plate, ModItems.FORGE_HAMMER, ingot);
    }

    private void hammerCasing(ItemLike plate, ItemLike casing) {
        toolShapeless(casing, 2, ModItems.FORGE_HAMMER, plate);
    }

    // ==================== Crafting Shortcuts ====================
    protected void shaped(ItemLike result, ItemLike unlock, Consumer<ShapedRecipeBuilder> config) {
        shaped(RecipeCategory.MISC, result, 1, unlock, config);
    }

    protected void shaped(RecipeCategory category, ItemLike result, int count, ItemLike unlock, Consumer<ShapedRecipeBuilder> config) {
        ShapedRecipeBuilder builder = shaped(category, result, count);
        config.accept(builder);
        RecipeHelpers.saveCrafting(builder, result, unlock, this.output);
    }

    protected void shaped(String name, ItemLike result, int count, ItemLike unlock, Consumer<ShapedRecipeBuilder> config) {
        shaped(name, RecipeCategory.MISC, result, count, unlock, config);
    }

    protected void shaped(String name, RecipeCategory category, ItemLike result, int count, ItemLike unlock, Consumer<ShapedRecipeBuilder> config) {
        ShapedRecipeBuilder builder = shaped(category, result, count);
        config.accept(builder);
        RecipeHelpers.saveCrafting(name, builder, unlock, this.output);
    }

    protected void shapeless(ItemLike result, int count, ItemLike... inputs) {
        shapeless(RecipeCategory.MISC, result, count, inputs);
    }

    protected void shapeless(RecipeCategory category, ItemLike result, int count, ItemLike... inputs) {
        ShapelessRecipeBuilder builder = shapeless(category, result, count);
        for (ItemLike input : inputs) {
            builder.requires(input);
        }
        RecipeHelpers.saveCrafting(builder, result, this.output, inputs);
    }

    protected void shapeless(ItemLike result, int count, ItemLike unlock, Consumer<ShapelessRecipeBuilder> config) {
        shapeless(RecipeCategory.MISC, result, count, unlock, config);
    }

    protected void shapeless(RecipeCategory category, ItemLike result, int count, ItemLike unlock, Consumer<ShapelessRecipeBuilder> config) {
        ShapelessRecipeBuilder builder = shapeless(category, result, count);
        config.accept(builder);
        RecipeHelpers.saveCrafting(builder, result, unlock, this.output);
    }

    protected void toolShapeless(ItemLike result, ItemLike tool, ItemLike... inputs) {
        toolShapeless(result, 1, tool, inputs);
    }

    protected void toolShapeless(ItemLike result, int count, ItemLike tool, ItemLike... inputs) {
        toolShapeless(RecipeCategory.MISC, result, count, tool, inputs);
    }

    protected void toolShapeless(RecipeCategory category, ItemLike result, int count, ItemLike tool, ItemLike... inputs) {
        ShapelessRecipeBuilder builder = shapeless(category, result, count).requires(tool);
        ItemLike[] allInputs = new ItemLike[inputs.length + 1];
        allInputs[0] = tool;
        for (int i = 0; i < inputs.length; i++) {
            builder.requires(inputs[i]);
            allInputs[i + 1] = inputs[i];
        }
        RecipeHelpers.saveCrafting(builder, result, this.toolOutput, allInputs);
    }

    protected void toolShapeless(ItemLike result, int count, ItemLike unlock, Consumer<ShapelessRecipeBuilder> config) {
        toolShapeless(RecipeCategory.MISC, result, count, unlock, config);
    }

    protected void toolShapeless(RecipeCategory category, ItemLike result, int count, ItemLike unlock, Consumer<ShapelessRecipeBuilder> config) {
        ShapelessRecipeBuilder builder = shapeless(category, result, count);
        config.accept(builder);
        RecipeHelpers.saveCrafting(builder, result, unlock, this.toolOutput);
    }

    // ==================== Processing Shortcuts ====================
    protected void furnace(ItemLike input, ItemLike result) {
        RecipeHelpers.furnace(input, result, this.output);
    }

    protected void smeltAndBlast(ItemLike input, ItemLike result) {
        RecipeHelpers.furnace(input, result, this.output);
        RecipeHelpers.blasting(input, result, this.output);
    }

    protected void macerator(ItemLike input, ItemLike result) {
        RecipeHelpers.macerator(input, result, this.output);
    }

    protected void macerator(ItemLike input, ItemLike result, int count) {
        RecipeHelpers.macerator(input, result, count, this.output);
    }

    protected void compressor(ItemLike input, ItemLike result) {
        RecipeHelpers.compressor(input, result, this.output);
    }

    protected void compressor(ItemLike input, int inputCount, ItemLike result) {
        RecipeHelpers.compressor(input, inputCount, result, this.output);
    }

    protected void compressor(ItemLike input, int inputCount, ItemLike result, int resultCount) {
        RecipeHelpers.compressor(input, inputCount, result, resultCount, this.output);
    }

    protected void metalFormer(MetalFormerMode mode, ItemLike input, ItemLike result) {
        RecipeHelpers.metalFormer(mode, input, result, this.output);
    }

    protected void metalFormer(MetalFormerMode mode, ItemLike input, ItemLike result, int count) {
        RecipeHelpers.metalFormer(mode, input, result, count, this.output);
    }

    // ==================== Runner ====================
    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registries, @NonNull RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public @NonNull String getName() {
            return ICRE.MOD_ID + " Recipes";
        }
    }
}
