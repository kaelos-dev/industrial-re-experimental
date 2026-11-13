package net.kaelos.icre.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kaelos.icre.definitions.ModRecipes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public record MaceratorRecipe(Ingredient input, ItemStackTemplate output) implements Recipe<SingleRecipeInput> {
    public static final MapCodec<MaceratorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(MaceratorRecipe::input),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(MaceratorRecipe::output)
    ).apply(inst, MaceratorRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MaceratorRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, MaceratorRecipe::input,
            ItemStackTemplate.STREAM_CODEC, MaceratorRecipe::output,
            MaceratorRecipe::new
    );

    @Override
    public boolean matches(@NonNull SingleRecipeInput recipeInput, @NonNull Level level) {
        return this.input.test(recipeInput.item());
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull SingleRecipeInput recipeInput) {
        return this.output.create();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public @NonNull String group() {
        return "";
    }

    @Override
    public @NonNull RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
        return ModRecipes.MACERATOR_SERIALIZER.get();
    }

    @Override
    public @NonNull RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return ModRecipes.MACERATOR.get();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return PlacementInfo.create(this.input);
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.FURNACE_MISC;
    }
}
