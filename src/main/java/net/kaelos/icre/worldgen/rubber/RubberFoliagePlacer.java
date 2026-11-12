package net.kaelos.icre.worldgen.rubber;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kaelos.icre.definitions.ModPlacers;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.jspecify.annotations.NonNull;

public class RubberFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<RubberFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(inst ->
            foliagePlacerParts(inst).apply(inst, RubberFoliagePlacer::new));

    public RubberFoliagePlacer() {
        this(ConstantInt.of(0), ConstantInt.of(0));
    }

    public RubberFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected @NonNull FoliagePlacerType<?> type() {
        return ModPlacers.RUBBER_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(@NonNull WorldGenLevel level, @NonNull FoliageSetter foliageSetter, @NonNull RandomSource random, @NonNull TreeConfiguration config, int treeHeight, @NonNull FoliageAttachment attachment, int foliageHeight, int leafRadius, int offset) {
        tryPlaceLeaf(level, foliageSetter, random, config, attachment.pos());
    }

    @Override
    public int foliageHeight(@NonNull RandomSource random, int treeHeight, @NonNull TreeConfiguration config) {
        int h = 8;
        int height = h / 2;
        h -= height;
        height += random.nextInt(h + 1);
        return height;
    }

    @Override
    protected boolean shouldSkipLocation(@NonNull RandomSource random, int dx, int y, int dz, int currentRadius, boolean doubleTrunk) {
        return false;
    }
}
