package net.kaelos.icre.recipe.metalformer;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jspecify.annotations.NonNull;

public record MetalFormerInput(ItemStack item, MetalFormerMode mode) implements RecipeInput {
    @Override
    public @NonNull ItemStack getItem(int index) {
        if (index != 0) {
            throw new IllegalArgumentException("No item for index " + index);
        }

        return this.item;
    }

    @Override
    public int size() {
        return 1;
    }
}
