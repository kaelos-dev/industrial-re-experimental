package net.kaelos.icre.recipe.crafting;

import com.mojang.serialization.MapCodec;
import net.kaelos.icre.definitions.ModRecipes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.jspecify.annotations.NonNull;

public class ToolShapelessRecipe extends ToolCraftingRecipe<ShapelessRecipe> {
    public static final MapCodec<ToolShapelessRecipe> CODEC =
            ShapelessRecipe.MAP_CODEC.xmap(ToolShapelessRecipe::new, ToolCraftingRecipe::delegate);

    public static final StreamCodec<RegistryFriendlyByteBuf, ToolShapelessRecipe> STREAM_CODEC =
            ShapelessRecipe.STREAM_CODEC.map(ToolShapelessRecipe::new, ToolCraftingRecipe::delegate);

    public ToolShapelessRecipe(ShapelessRecipe delegate) {
        super(delegate);
    }

    @Override
    public @NonNull RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return ModRecipes.TOOL_SHAPELESS_SERIALIZER.get();
    }
}
