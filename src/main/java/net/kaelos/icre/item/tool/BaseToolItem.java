package net.kaelos.icre.item.tool;

import net.kaelos.icre.ICRE;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public abstract class BaseToolItem extends Item {
    protected BaseToolItem(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(@NonNull ItemStack stack, @NonNull TooltipContext context, @NonNull TooltipDisplay display, @NonNull Consumer<Component> builder, @NonNull TooltipFlag tooltipFlag) {
        if (!stack.isDamageableItem() || !display.shows(DataComponents.DAMAGE)) {
            return;
        }

        int max = stack.getMaxDamage();
        int remaining = max - stack.getDamageValue();

        builder.accept(Component.translatable("tooltip." + ICRE.MOD_ID + ".durability")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.literal(remaining + " / " + max)
                        .withColor(stack.getBarColor())));
    }
}
