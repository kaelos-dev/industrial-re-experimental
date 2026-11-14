package net.kaelos.icre.recipe.crafting;

import net.kaelos.icre.item.tool.BaseToolItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;

public abstract class ToolCraftingRecipe<R extends CraftingRecipe> implements CraftingRecipe {
    protected final R delegate;

    protected ToolCraftingRecipe(R delegate) {
        this.delegate = delegate;
    }

    public R delegate() {
        return this.delegate;
    }

    @Override
    public boolean matches(@NonNull CraftingInput input, @NonNull Level level) {
        return this.delegate.matches(input, level);
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull CraftingInput input) {
        return this.delegate.assemble(input);
    }

    @Override
    public @NonNull NonNullList<ItemStack> getRemainingItems(@NonNull CraftingInput input) {
        NonNullList<ItemStack> remaining = CraftingRecipe.defaultCraftingReminder(input);
        for (int i = 0; i < input.size(); i++) {
            if (!remaining.get(i).isEmpty()) {
                continue;
            }

            ItemStack stack = input.getItem(i);
            if (isCraftingTool(stack)) {
                remaining.set(i, damageTool(stack));
            }
        }

        return remaining;
    }

    protected boolean isCraftingTool(ItemStack stack) {
        return !stack.isEmpty()
                && stack.isDamageableItem()
                && stack.getItem() instanceof BaseToolItem;
    }

    private static ItemStack damageTool(ItemStack stack) {
        ItemStack copy = stack.copyWithCount(1);
        int nextDamage = copy.getDamageValue() + 1;
        if (nextDamage >= copy.getMaxDamage()) {
            return ItemStack.EMPTY;
        }

        copy.setDamageValue(nextDamage);
        return copy;
    }

    @Override
    public boolean isSpecial() {
        return this.delegate.isSpecial();
    }

    @Override
    public boolean showNotification() {
        return this.delegate.showNotification();
    }

    @Override
    public @NonNull String group() {
        return this.delegate.group();
    }

    @Override
    public @NonNull CraftingBookCategory category() {
        return this.delegate.category();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return this.delegate.placementInfo();
    }

    @Override
    public @NonNull List<RecipeDisplay> display() {
        return this.delegate.display();
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return this.delegate.recipeBookCategory();
    }

    @Override
    public abstract @NonNull RecipeSerializer<? extends CraftingRecipe> getSerializer();
}
