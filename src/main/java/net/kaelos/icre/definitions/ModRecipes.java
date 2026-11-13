package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.recipe.MaceratorRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ICRE.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ICRE.MOD_ID);

    // ==================== Macerator ====================
    public static final Supplier<RecipeType<MaceratorRecipe>> MACERATOR = TYPES.register("macerating", () ->
            new RecipeType<>() {
                @Override
                public String toString() {
                    return ICRE.MOD_ID + ":macerating";
                }
            });
    public static final Supplier<RecipeSerializer<MaceratorRecipe>> MACERATOR_SERIALIZER = SERIALIZERS.register("macerating", () ->
            new RecipeSerializer<>(MaceratorRecipe.CODEC, MaceratorRecipe.STREAM_CODEC));

    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }
}
