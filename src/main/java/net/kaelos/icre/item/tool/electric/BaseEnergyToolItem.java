package net.kaelos.icre.item.tool.electric;

import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.definitions.ModDataComponents;
import net.kaelos.icre.item.energy.BaseEnergyItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class BaseEnergyToolItem extends BaseEnergyItem {
    protected final long energyPerUse;
    private final Set<ItemAbility> abilities;

    public BaseEnergyToolItem(Properties properties, long capacity, long maxReceive, VoltageTier tier,
                              long energyPerUse, Set<ItemAbility> abilities) {
        super(properties, capacity, maxReceive, 0L, tier);
        this.energyPerUse = energyPerUse;
        this.abilities = abilities;
    }

    public long getEnergyPerUse() {
        return this.energyPerUse;
    }

    public boolean hasUseEnergy(ItemInstance stack) {
        if (this.energyPerUse <= 0L) {
            return true;
        }

        long stored = Math.clamp(stack.getOrDefault(ModDataComponents.ENERGY.get(), 0L), 0L, this.capacity);
        return stored >= this.energyPerUse;
    }

    public boolean canUse(ItemStack stack, Player player) {
        return (player != null && player.isCreative()) || hasUseEnergy(stack);
    }

    public void tryConsumeUseEnergy(ItemStack stack, LivingEntity user) {
        if (user instanceof Player player && player.isCreative()) {
            return;
        }

        long stored = getEnergy(stack);
        if (stored < this.energyPerUse) {
            return;
        }

        if (user == null || !user.level().isClientSide()) {
            setEnergy(stack, stored - this.energyPerUse);
        }

    }

    @Override
    public float getDestroySpeed(@NonNull ItemStack stack, @NonNull BlockState state) {
        if (!hasUseEnergy(stack)) {
            return 1.0F;
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean isCorrectToolForDrops(@NonNull ItemStack stack, @NonNull BlockState state) {
        if (!hasUseEnergy(stack)) {
            return false;
        }
        return super.isCorrectToolForDrops(stack, state);
    }

    @Override
    public boolean mineBlock(@NonNull ItemStack stack, @NonNull Level level, @NonNull BlockState state, @NonNull BlockPos pos, @NonNull LivingEntity owner) {
        if (!level.isClientSide() && state.getDestroySpeed(level, pos) != 0.0F) {
            tryConsumeUseEnergy(stack, owner);
        }
        return true;
    }

    @Override
    public boolean canPerformAction(@NonNull ItemInstance stack, @NonNull ItemAbility itemAbility) {
        return hasUseEnergy(stack) && this.abilities.contains(itemAbility);
    }
}
