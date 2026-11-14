package net.kaelos.icre.datagen.recipe;

import net.kaelos.icre.ICRE;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.level.ItemLike;
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
        registerMaceratorRecipes();
    }

    private void registerCraftingRecipes() {}

    private void registerMaceratorRecipes() {}

    // ==================== Crafting Shortcuts ====================
    protected void shaped(ItemLike result, ItemLike unlock, Consumer<ShapedRecipeBuilder> config) {
        shaped(RecipeCategory.MISC, result, 1, unlock, config);
    }

    protected void shaped(RecipeCategory category, ItemLike result, int count, ItemLike unlock, Consumer<ShapedRecipeBuilder> config) {
        ShapedRecipeBuilder builder = shaped(category, result, count);
        config.accept(builder);
        RecipeHelpers.saveCrafting(builder, result, unlock, this.output);
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

    // ==================== Shortcuts ====================
    protected void smeltAndBlast(ItemLike input, ItemLike result) {
        RecipeHelpers.furnace(input, result, this.output);
        RecipeHelpers.blasting(input, result, this.output);
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
