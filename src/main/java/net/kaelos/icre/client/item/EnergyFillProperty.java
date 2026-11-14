package net.kaelos.icre.client.item;

import com.mojang.serialization.MapCodec;
import net.kaelos.icre.item.energy.BaseEnergyItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record EnergyFillProperty() implements RangeSelectItemModelProperty {
    public static final EnergyFillProperty INSTANCE = new EnergyFillProperty();
    public static final MapCodec<EnergyFillProperty> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public float get(@NonNull ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        if (stack.getItem() instanceof BaseEnergyItem energyItem && energyItem.getCapacity() > 0L) {
            return Mth.clamp((float) energyItem.getEnergy(stack) / energyItem.getCapacity(), 0.0F, 1.0F);
        }
        return 0.0F;
    }

    @Override
    public @NonNull MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }
}
