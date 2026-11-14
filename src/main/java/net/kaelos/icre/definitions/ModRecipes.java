package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.recipe.CompressorRecipe;
import net.kaelos.icre.recipe.MaceratorRecipe;
import net.kaelos.icre.recipe.crafting.ToolShapelessRecipe;
import net.kaelos.icre.recipe.metalformer.MetalFormerRecipe;
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

    // ==================== Crafting ====================
    public static final Supplier<RecipeSerializer<ToolShapelessRecipe>> TOOL_SHAPELESS_SERIALIZER = SERIALIZERS.register("tool_crafting_shapeless", () ->
            new RecipeSerializer<>(ToolShapelessRecipe.CODEC, ToolShapelessRecipe.STREAM_CODEC));

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

    // ==================== Compressor ====================
    public static final Supplier<RecipeType<CompressorRecipe>> COMPRESSOR = TYPES.register("compressing", () ->
            new RecipeType<>() {
                @Override
                public String toString() {
                    return ICRE.MOD_ID + ":compressing";
                }
            });
    public static final Supplier<RecipeSerializer<CompressorRecipe>> COMPRESSOR_SERIALIZER = SERIALIZERS.register("compressing", () ->
            new RecipeSerializer<>(CompressorRecipe.CODEC, CompressorRecipe.STREAM_CODEC));

    // ==================== Metal Former ====================
    public static final Supplier<RecipeType<MetalFormerRecipe>> METAL_FORMER = TYPES.register("metal_forming", () ->
            new RecipeType<>() {
                @Override
                public String toString() {
                    return ICRE.MOD_ID + ":metal_forming";
                }
            });
    public static final Supplier<RecipeSerializer<MetalFormerRecipe>> METAL_FORMER_SERIALIZER = SERIALIZERS.register("metal_forming", () ->
            new RecipeSerializer<>(MetalFormerRecipe.CODEC, MetalFormerRecipe.STREAM_CODEC));

    public static void register(IEventBus eventBus) {
        TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }
}
