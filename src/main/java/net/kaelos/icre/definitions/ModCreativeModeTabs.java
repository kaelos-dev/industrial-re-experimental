package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ICRE.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RESOURCES_TAB = registerTab(
            "resources_tab",
            ModItems.BRONZE_INGOT,
            (parameters, output) -> {
                output.accept(ModItems.BRONZE_INGOT.get());
                output.accept(ModItems.LEAD_INGOT.get());
                output.accept(ModItems.SILVER_INGOT.get());
                output.accept(ModItems.STEEL_INGOT.get());
                output.accept(ModItems.TIN_INGOT.get());

                output.accept(ModItems.BRONZE_PLATE.get());
                output.accept(ModItems.COPPER_PLATE.get());
                output.accept(ModItems.GOLD_PLATE.get());
                output.accept(ModItems.IRON_PLATE.get());
                output.accept(ModItems.LAPIS_LAZULI_PLATE.get());
                output.accept(ModItems.LEAD_PLATE.get());
                output.accept(ModItems.OBSIDIAN_PLATE.get());
                output.accept(ModItems.STEEL_PLATE.get());
                output.accept(ModItems.TIN_PLATE.get());

                output.accept(ModItems.DENSE_BRONZE_PLATE.get());
                output.accept(ModItems.DENSE_COPPER_PLATE.get());
                output.accept(ModItems.DENSE_GOLD_PLATE.get());
                output.accept(ModItems.DENSE_IRON_PLATE.get());
                output.accept(ModItems.DENSE_LAPIS_LAZULI_PLATE.get());
                output.accept(ModItems.DENSE_LEAD_PLATE.get());
                output.accept(ModItems.DENSE_OBSIDIAN_PLATE.get());
                output.accept(ModItems.DENSE_STEEL_PLATE.get());
                output.accept(ModItems.DENSE_TIN_PLATE.get());

                output.accept(ModItems.BRONZE_CASING.get());
                output.accept(ModItems.COPPER_CASING.get());
                output.accept(ModItems.GOLD_CASING.get());
                output.accept(ModItems.IRON_CASING.get());
                output.accept(ModItems.LEAD_CASING.get());
                output.accept(ModItems.STEEL_CASING.get());
                output.accept(ModItems.TIN_CASING.get());
            }
    );

    private static DeferredHolder<CreativeModeTab, CreativeModeTab> registerTab(String name, Supplier<? extends ItemLike> icon, CreativeModeTab.DisplayItemsGenerator displayItems) {
        return CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
                .title(Component.translatable("creativetab." + ICRE.MOD_ID + "." + name))
                .icon(() -> new ItemStack(icon.get()))
                .displayItems(displayItems)
                .build());
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
