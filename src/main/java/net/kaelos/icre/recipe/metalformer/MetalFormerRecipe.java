package net.kaelos.icre.recipe.metalformer;

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

public record MetalFormerRecipe(MetalFormerMode mode, Ingredient input, ItemStackTemplate output) implements Recipe<MetalFormerInput> {
    public static final MapCodec<MetalFormerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            MetalFormerMode.CODEC.fieldOf("mode").forGetter(MetalFormerRecipe::mode),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(MetalFormerRecipe::input),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(MetalFormerRecipe::output)
    ).apply(inst, MetalFormerRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MetalFormerRecipe> STREAM_CODEC = StreamCodec.composite(
            MetalFormerMode.STREAM_CODEC, MetalFormerRecipe::mode,
            Ingredient.CONTENTS_STREAM_CODEC, MetalFormerRecipe::input,
            ItemStackTemplate.STREAM_CODEC, MetalFormerRecipe::output,
            MetalFormerRecipe::new
    );

    @Override
    public boolean matches(@NonNull MetalFormerInput recipeInput, @NonNull Level level) {
        return recipeInput.mode() == this.mode && this.input.test(recipeInput.item());
    }
    @Override
    public @NonNull ItemStack assemble(@NonNull MetalFormerInput recipeInput) {
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
    public @NonNull RecipeSerializer<? extends Recipe<MetalFormerInput>> getSerializer() {
        return ModRecipes.METAL_FORMER_SERIALIZER.get();
    }
    @Override
    public @NonNull RecipeType<? extends Recipe<MetalFormerInput>> getType() {
        return ModRecipes.METAL_FORMER.get();
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
