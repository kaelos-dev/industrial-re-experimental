package net.kaelos.icre.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kaelos.icre.definitions.ModRecipes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public record CompressorRecipe(Ingredient input, int inputCount, ItemStackTemplate output) implements Recipe<SingleRecipeInput> {
    public static final MapCodec<CompressorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(CompressorRecipe::input),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(CompressorRecipe::inputCount),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(CompressorRecipe::output)
    ).apply(inst, CompressorRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CompressorRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, CompressorRecipe::input,
            ByteBufCodecs.VAR_INT, CompressorRecipe::inputCount,
            ItemStackTemplate.STREAM_CODEC, CompressorRecipe::output,
            CompressorRecipe::new
    );

    @Override
    public boolean matches(@NonNull SingleRecipeInput recipeInput, @NonNull Level level) {
        ItemStack stack = recipeInput.item();
        return stack.getCount() >= this.inputCount && this.input.test(stack);
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
        return ModRecipes.COMPRESSOR_SERIALIZER.get();
    }

    @Override
    public @NonNull RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return ModRecipes.COMPRESSOR.get();
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
