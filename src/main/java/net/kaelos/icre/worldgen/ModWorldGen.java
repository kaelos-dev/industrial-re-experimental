package net.kaelos.icre.worldgen;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.util.ModTags;
import net.kaelos.icre.worldgen.rubber.RubberFoliagePlacer;
import net.kaelos.icre.worldgen.rubber.RubberTrunkPlacer;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class ModWorldGen {
    public static final ResourceKey<ConfiguredFeature<?, ?>> LEAD_ORE_CF = configuredFeature("lead_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TIN_ORE_CF = configuredFeature("tin_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> URANIUM_ORE_CF = configuredFeature("uranium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RUBBER_TREE_CF = configuredFeature("rubber_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RUBBER_TREE_SAPLING_CF = configuredFeature("rubber_tree_sapling");

    public static final ResourceKey<PlacedFeature> LEAD_ORE_PF = placedFeature("lead_ore");
    public static final ResourceKey<PlacedFeature> TIN_ORE_PF = placedFeature("tin_ore");
    public static final ResourceKey<PlacedFeature> URANIUM_ORE_PF = placedFeature("uranium_ore");
    public static final ResourceKey<PlacedFeature> RUBBER_TREE_PF = placedFeature("rubber_tree");

    public static final ResourceKey<BiomeModifier> ADD_LEAD_ORE = biomeModifier("add_lead_ore");
    public static final ResourceKey<BiomeModifier> ADD_TIN_ORE = biomeModifier("add_tin_ore");
    public static final ResourceKey<BiomeModifier> ADD_URANIUM_ORE = biomeModifier("add_uranium_ore");
    public static final ResourceKey<BiomeModifier> ADD_RUBBER_TREE = biomeModifier("add_rubber_tree");

    private static ResourceKey<ConfiguredFeature<?, ?>> configuredFeature(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ICRE.makeId(name));
    }

    private static ResourceKey<PlacedFeature> placedFeature(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ICRE.makeId(name));
    }

    private static ResourceKey<BiomeModifier> biomeModifier(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ICRE.makeId(name));
    }

    public static void configured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        var stone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        var deepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        FeatureUtils.register(context, LEAD_ORE_CF, Feature.ORE, new OreConfiguration(List.of(
                OreConfiguration.target(stone, ModBlocks.LEAD_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslate, ModBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState())), 8));
        FeatureUtils.register(context, TIN_ORE_CF, Feature.ORE, new OreConfiguration(List.of(
                OreConfiguration.target(stone, ModBlocks.TIN_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslate, ModBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState())), 9));
        FeatureUtils.register(context, URANIUM_ORE_CF, Feature.ORE, new OreConfiguration(List.of(
                OreConfiguration.target(deepslate, ModBlocks.DEEPSLATE_URANIUM_ORE.get().defaultBlockState())), 4, 0.5F));

        FeatureUtils.register(context, RUBBER_TREE_CF, Feature.TREE, createTreeConfig(false));
        FeatureUtils.register(context, RUBBER_TREE_SAPLING_CF, Feature.TREE, createTreeConfig(true));
    }

    public static void placed(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(context, LEAD_ORE_PF, configuredFeatures.getOrThrow(LEAD_ORE_CF),
                ore(8, HeightRangePlacement.triangle(VerticalAnchor.absolute(-48), VerticalAnchor.absolute(56))));
        PlacementUtils.register(context, TIN_ORE_PF, configuredFeatures.getOrThrow(TIN_ORE_CF),
                ore(12, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(96))));
        PlacementUtils.register(context, URANIUM_ORE_PF, configuredFeatures.getOrThrow(URANIUM_ORE_CF),
                ore(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-24))));

        PlacementUtils.register(context, RUBBER_TREE_PF, configuredFeatures.getOrThrow(RUBBER_TREE_CF),
                VegetationPlacements.treePlacement(CountPlacement.of(10), ModBlocks.RUBBER_SAPLING.get()));
    }

    public static void biomeModifiers(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        add(context, biomes, placed, ADD_LEAD_ORE, LEAD_ORE_PF);
        add(context, biomes, placed, ADD_TIN_ORE, TIN_ORE_PF);
        add(context, biomes, placed, ADD_URANIUM_ORE, URANIUM_ORE_PF);

        context.register(ADD_RUBBER_TREE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_RUBBER_TREE),
                HolderSet.direct(placed.getOrThrow(RUBBER_TREE_PF)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
    }

    private static void add(BootstrapContext<BiomeModifier> context, HolderGetter<Biome> biomes, HolderGetter<PlacedFeature> placed,
                            ResourceKey<BiomeModifier> key, ResourceKey<PlacedFeature> feature) {
        context.register(key, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placed.getOrThrow(feature)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
    }

    private static List<PlacementModifier> ore(int veinsPerChunk, PlacementModifier height) {
        return List.of(CountPlacement.of(veinsPerChunk), InSquarePlacement.spread(), height, BiomeFilter.biome());
    }

    private static TreeConfiguration createTreeConfig(boolean forceGrow) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.RUBBER_LOG.get()),
                new RubberTrunkPlacer(5, 2, 0, forceGrow),
                BlockStateProvider.simple(ModBlocks.RUBBER_LEAVES.get()),
                new RubberFoliagePlacer(),
                new TwoLayersFeatureSize(1, 0, 1)
        ).ignoreVines().build();
    }
}
