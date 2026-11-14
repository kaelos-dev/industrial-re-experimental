package net.kaelos.icre.item.energy;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.api.energy.IEuStorage;
import net.kaelos.icre.api.energy.ItemEuStorage;
import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.definitions.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public abstract class BaseEnergyItem extends Item {
    private static final int ENERGY_BAR_COLOR = 0x38D6FF;

    protected final long capacity;
    protected final long maxReceive;
    protected final long maxExtract;
    protected final VoltageTier tier;

    public BaseEnergyItem(Properties properties, long capacity, long maxReceive, long maxExtract, VoltageTier tier) {
        super(properties.stacksTo(1));
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.tier = tier;
    }

    public IEuStorage createEuStorage(ItemAccess access) {
        return new ItemEuStorage(access, this, this.capacity, this.maxReceive, this.maxExtract, this.tier);
    }

    public long getCapacity() {
        return this.capacity;
    }

    public VoltageTier getTier() {
        return this.tier;
    }

    public long getEnergy(ItemStack stack) {
        long stored = stack.getOrDefault(ModDataComponents.ENERGY.get(), 0L);
        return Math.clamp(stored, 0L, this.capacity);
    }

    public void setEnergy(ItemStack stack, long energy) {
        long clamped = Math.clamp(energy, 0L, this.capacity);
        if (clamped == 0L) {
            stack.remove(ModDataComponents.ENERGY.get());
        } else {
            stack.set(ModDataComponents.ENERGY.get(), clamped);
        }
    }

    public ItemStack createChargedStack() {
        ItemStack stack = new ItemStack(this);
        setEnergy(stack, this.capacity);
        return stack;
    }

    @Override
    public boolean isBarVisible(@NonNull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@NonNull ItemStack stack) {
        if (this.capacity <= 0L) {
            return 0;
        }
        return Mth.clamp(Math.round(13.0F * getEnergy(stack) / this.capacity), 0, 13);
    }

    @Override
    public int getBarColor(@NonNull ItemStack stack) {
        return ENERGY_BAR_COLOR;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay display, @NonNull Consumer<Component> builder, @NonNull TooltipFlag tooltipFlag) {
        long stored = getEnergy(stack);
        builder.accept(Component.translatable("tooltip." + ICRE.MOD_ID + ".energy")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.literal(stored + " / " + this.capacity + " EU")
                        .withColor(ENERGY_BAR_COLOR)));
    }
}
