package net.kaelos.icre.datagen.recipe;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.recipe.CompressorRecipe;
import net.kaelos.icre.recipe.MaceratorRecipe;
import net.kaelos.icre.recipe.crafting.ToolShapelessRecipe;
import net.kaelos.icre.recipe.metalformer.MetalFormerMode;
import net.kaelos.icre.recipe.metalformer.MetalFormerRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RecipeHelpers {
    private static final float DEFAULT_SMELT_XP = 0.7F;
    private static final int DEFAULT_FURNACE_TIME = 200;
    private static final int DEFAULT_BLASTING_TIME = 100;

    // ==================== Crafting (Workbench) ====================
    public static void saveCrafting(RecipeBuilder builder, ItemLike result, ItemLike unlock, RecipeOutput output) {
        saveCrafting(autoName(unlock, result), builder, unlock, output);
    }

    public static void saveCrafting(String name, RecipeBuilder builder, ItemLike unlock, RecipeOutput output) {
        builder.unlockedBy("has_" + itemName(unlock), InventoryChangeTrigger.TriggerInstance.hasItems(unlock))
                .save(output, recipeKey("crafting/" + name));
    }

    public static void saveCrafting(RecipeBuilder builder, ItemLike result, RecipeOutput output, ItemLike... inputs) {
        if (inputs.length == 0) {
            throw new IllegalArgumentException("Shapeless recipe must have at least one input");
        }

        saveCrafting(autoName(result, inputs), builder, inputs[0], output);
    }

    public static RecipeOutput asToolOutput(RecipeOutput delegate) {
        return new RecipeOutput() {
            @Override
            public Advancement.@NonNull Builder advancement() {
                return delegate.advancement();
            }

            @Override
            public void includeRootAdvancement() {
                delegate.includeRootAdvancement();
            }

            @Override
            public void accept(@NonNull ResourceKey<Recipe<?>> key, @NonNull Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition @NonNull ... conditions) {
                Recipe<?> wrapped = switch (recipe) {
                    case ShapelessRecipe shapeless -> new ToolShapelessRecipe(shapeless);
                    default -> throw new IllegalArgumentException("Expected ShapedRecipe or ShapelessRecipe, got: " + recipe.getClass().getName());
                };

                delegate.accept(key, wrapped, advancement);
            }
        };
    }

    // ==================== Furnace ====================
    public static void furnace(ItemLike input, ItemLike result, RecipeOutput output) {
        furnace(input, result, DEFAULT_SMELT_XP, DEFAULT_FURNACE_TIME, output);
    }

    private static void furnace(ItemLike input, ItemLike result, float experience, int cookingTime, RecipeOutput output) {
        String id = itemName(result) + "_from_smelting_" + itemName(input);
        SimpleCookingRecipeBuilder.smelting(
                Ingredient.of(input),
                RecipeCategory.MISC,
                CookingBookCategory.MISC,
                result,
                experience,
                cookingTime
        )
                .unlockedBy("has_" + itemName(input), InventoryChangeTrigger.TriggerInstance.hasItems(input))
                .save(output, recipeKey(id));
    }

    // ==================== Blasting ====================
    public static void blasting(ItemLike input, ItemLike result, RecipeOutput output) {
        blasting(input, result, DEFAULT_SMELT_XP, DEFAULT_BLASTING_TIME, output);
    }

    private static void blasting(ItemLike input, ItemLike result, float experience, int cookingTime, RecipeOutput output) {
        String id = itemName(result) + "_from_blasting_" + itemName(input);
        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(input),
                RecipeCategory.MISC,
                CookingBookCategory.MISC,
                result,
                experience,
                cookingTime
        )
                .unlockedBy("has_" + itemName(input), InventoryChangeTrigger.TriggerInstance.hasItems(input))
                .save(output, recipeKey(id));
    }

    // ==================== Macerator ====================
    public static void macerator(ItemLike input, ItemLike result, RecipeOutput output) {
        macerator(autoName(input, result), input, result, 1, output);
    }

    public static void macerator(ItemLike input, ItemLike result, int count, RecipeOutput output) {
        macerator(autoName(input, result), input, result, count, output);
    }

    private static void macerator(String name, ItemLike input, ItemLike result, int count, RecipeOutput output) {
        MaceratorRecipe recipe = new MaceratorRecipe(Ingredient.of(input), new ItemStackTemplate(result.asItem(), count));
        output.accept(recipeKey("macerating/" + name), recipe, null);
    }

    // ==================== Metal Former ====================
    public static void metalFormer(MetalFormerMode mode, ItemLike input, ItemLike result, RecipeOutput output) {
        metalFormer(autoName(input, result), mode, input, result, 1, output);
    }

    public static void metalFormer(MetalFormerMode mode, ItemLike input, ItemLike result, int count, RecipeOutput output) {
        metalFormer(autoName(input, result), mode, input, result, count, output);
    }

    private static void metalFormer(String name, MetalFormerMode mode, ItemLike input, ItemLike result, int count, RecipeOutput output) {
        MetalFormerRecipe recipe = new MetalFormerRecipe(mode, Ingredient.of(input), new ItemStackTemplate(result.asItem(), count));
        output.accept(recipeKey("metal_forming/" + mode.getSerializedName() + "/" + name), recipe, null);
    }

    // ==================== Compressor ====================
    public static void compressor(ItemLike input, ItemLike result, RecipeOutput output) {
        compressor(autoName(input, result), input, 1, result, 1, output);
    }

    public static void compressor(ItemLike input, ItemLike result, int resultCount, RecipeOutput output) {
        compressor(autoName(input, result), input, 1, result, resultCount, output);
    }

    public static void compressor(ItemLike input, int inputCount, ItemLike result, RecipeOutput output) {
        compressor(autoName(input, result), input, inputCount, result, 1, output);
    }

    public static void compressor(ItemLike input, int inputCount, ItemLike result, int resultCount, RecipeOutput output) {
        compressor(autoName(input, result), input, inputCount, result, resultCount, output);
    }

    private static void compressor(String name, ItemLike input, int inputCount, ItemLike result, int resultCount, RecipeOutput output) {
        CompressorRecipe recipe = new CompressorRecipe(Ingredient.of(input), inputCount, new ItemStackTemplate(result.asItem(), resultCount));
        output.accept(recipeKey("compressing/" + name), recipe, null);
    }

    // ==================== Helpers ====================
    private static String autoName(ItemLike input, ItemLike result) {
        return itemName(result) + "_from_" + itemName(input);
    }

    private static String autoName(ItemLike result, ItemLike... inputs) {
        String joinedInputs = Arrays.stream(inputs)
                .map(RecipeHelpers::itemName)
                .distinct()
                .collect(Collectors.joining("_and_"));
        return itemName(result) + "_from_" + joinedInputs;
    }

    private static String itemName(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    private static ResourceKey<Recipe<?>> recipeKey(String path) {
        return ResourceKey.create(Registries.RECIPE, ICRE.makeId(path));
    }
}
