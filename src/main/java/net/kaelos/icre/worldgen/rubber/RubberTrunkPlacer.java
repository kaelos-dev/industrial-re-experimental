package net.kaelos.icre.worldgen.rubber;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kaelos.icre.block.rubber.RubberLogBlock;
import net.kaelos.icre.definitions.ModPlacers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class RubberTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<RubberTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(inst ->
            trunkPlacerParts(inst)
                    .and(Codec.BOOL.fieldOf("force_grow").orElse(false).forGetter(placer -> placer.forceGrow))
                    .apply(inst, RubberTrunkPlacer::new));

    private final boolean forceGrow;

    public RubberTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        this(baseHeight, heightRandA, heightRandB, false);
    }

    public RubberTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, boolean forceGrow) {
        super(baseHeight, heightRandA, heightRandB);
        this.forceGrow = forceGrow;
    }

    @Override
    protected @NonNull TrunkPlacerType<?> type() {
        return ModPlacers.RUBBER_TRUNK_PLACER.get();
    }

    @Override
    public @NonNull List<FoliagePlacer.FoliageAttachment> placeTrunk(@NonNull WorldGenLevel level, @NonNull BiConsumer<BlockPos, BlockState> trunkSetter, @NonNull RandomSource random, int treeHeight, @NonNull BlockPos origin, @NonNull TreeConfiguration config) {
        if (!this.forceGrow) {
            Holder<Biome> biome = level.getBiome(origin);
            int max = 0;

            if (biome.is(BiomeTags.IS_FOREST) || biome.is(BiomeTags.IS_JUNGLE)) {
                max += 6;
            }

            if (biome.is(Tags.Biomes.IS_SWAMP)) {
                max += 15;
            }

            if (max <= 0 || random.nextInt(100) <= 100 - max * 2) {
                return List.of();
            }
        }

        placeBelowTrunkBlock(level, trunkSetter, random, origin.below(), config);

        int holeChance = 25;
        List<FoliagePlacer.FoliageAttachment> foliagePositions = new ArrayList<>();

        for (int i = 0; i < treeHeight; i++) {
            BlockPos logPos = origin.above(i);
            BlockState state = config.trunkProvider.getState(level, random, logPos);

            if (random.nextInt(100) <= holeChance) {
                holeChance -= 10;
                if (state.hasProperty(RubberLogBlock.RESIN)
                        && state.hasProperty(RubberLogBlock.COLLECTABLE)
                        && state.hasProperty(RubberLogBlock.RESIN_FACING)) {
                    state = state
                            .setValue(RubberLogBlock.RESIN, true)
                            .setValue(RubberLogBlock.COLLECTABLE, true)
                            .setValue(RubberLogBlock.RESIN_FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
                }
            }

            if (this.validTreePos(level, logPos)) {
                trunkSetter.accept(logPos, state);
            }

            if (treeHeight < 4 || (treeHeight < 7 && i > 1) || i > 2) {
                int x = origin.getX();
                int z = origin.getZ();
                int c = Math.max(1, i + 4 - treeHeight);

                for (int a = x - 2; a <= x + 2; a++) {
                    for (int b = z - 2; b <= z + 2; b++) {
                        if (a == x && b == z) {
                            continue;
                        }

                        boolean gen = (a > x - 2 && a < x + 2 && b > z - 2 && b < z + 2)
                                || (a > x - 2 && a < x + 2 && random.nextInt(c) == 0)
                                || (b > z - 2 && b < z + 2 && random.nextInt(c) == 0);

                        BlockPos leafPos = new BlockPos(a, origin.getY() + i, b);
                        if (gen && TreeFeature.isAirOrLeaves(level, leafPos)) {
                            foliagePositions.add(new FoliagePlacer.FoliageAttachment(leafPos, 0, true));
                        }
                    }
                }
            }
        }

        for (int i = 0; i <= 2; i++) {
            BlockPos leafPos = origin.above(treeHeight + i);
            if (TreeFeature.isAirOrLeaves(level, leafPos)) {
                foliagePositions.add(new FoliagePlacer.FoliageAttachment(leafPos, 0, true));
            }
        }

        return ImmutableList.copyOf(foliagePositions);
    }
}
