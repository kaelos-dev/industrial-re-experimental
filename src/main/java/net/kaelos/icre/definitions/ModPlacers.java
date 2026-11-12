package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.worldgen.rubber.RubberFoliagePlacer;
import net.kaelos.icre.worldgen.rubber.RubberTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPlacers {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS =
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, ICRE.MOD_ID);
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS =
            DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, ICRE.MOD_ID);

    // ==================== Rubber Tree ====================
    public static final DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<RubberTrunkPlacer>> RUBBER_TRUNK_PLACER =
            TRUNK_PLACERS.register("rubber_trunk", () -> new TrunkPlacerType<>(RubberTrunkPlacer.CODEC));
    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<RubberFoliagePlacer>> RUBBER_FOLIAGE_PLACER =
            FOLIAGE_PLACERS.register("rubber_foliage", () -> new FoliagePlacerType<>(RubberFoliagePlacer.CODEC));

    public static void register(IEventBus eventBus) {
        TRUNK_PLACERS.register(eventBus);
        FOLIAGE_PLACERS.register(eventBus);
    }
}
