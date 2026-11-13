package net.kaelos.icre.datagen;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.recipe.MaceratorRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        registerMaceratorRecipes();
    }

    private void registerMaceratorRecipes() {
        macerator("apple_from_rod", Items.BLAZE_ROD, Items.APPLE, 4);
    }

    private void macerator(String name, ItemLike input, ItemLike result, int count) {
        macerator(name, Ingredient.of(input), result, count);
    }

    private void macerator(String name, Ingredient input, ItemLike result, int count) {
        ResourceKey<Recipe<?>> key = ResourceKey.create(
                Registries.RECIPE,
                ICRE.makeId("macerating/" + name)
        );

        MaceratorRecipe recipe = new MaceratorRecipe(input, new ItemStackTemplate(result.asItem(), count));
        this.output.accept(key, recipe, null);
    }

    public static class Runner extends RecipeProvider.Runner {
        protected Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
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
