package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.menu.base.BaseContainerMenu;
import net.kaelos.icre.menu.machine.generating.GeneratorMenu;
import net.kaelos.icre.menu.machine.producing.ProcessingMachineMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ICRE.MOD_ID);

    // ==================== Generators ====================
    public static final DeferredHolder<MenuType<?>, MenuType<GeneratorMenu>> GENERATOR_MENU =
            registerMenu("generator_menu", GeneratorMenu::new);

    // ==================== Machines ====================
    public static DeferredHolder<MenuType<?>, MenuType<ProcessingMachineMenu>> MACERATOR_MENU =
            registerMenu("macerator_menu", ModMenuTypes::createMaceratorMenu);

    private static ProcessingMachineMenu createMaceratorMenu(int windowId, Inventory inv, FriendlyByteBuf data) {
        return new ProcessingMachineMenu(MACERATOR_MENU.get(), windowId, inv, data);
    }

    private static <T extends BaseContainerMenu<?>> DeferredHolder<MenuType<?>, MenuType<T>> registerMenu(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
