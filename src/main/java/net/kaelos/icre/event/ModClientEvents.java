package net.kaelos.icre.event;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.client.item.EnergyFillProperty;
import net.kaelos.icre.client.screen.machine.generating.GeneratorScreen;
import net.kaelos.icre.client.screen.machine.producing.MetalFormerScreen;
import net.kaelos.icre.client.screen.machine.producing.ProcessingMachineScreen;
import net.kaelos.icre.definitions.ModMenuTypes;
import net.kaelos.icre.menu.machine.producing.ProcessingMachineMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = ICRE.MOD_ID, value = Dist.CLIENT)
public class ModClientEvents {
    private ModClientEvents() {}

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.GENERATOR_MENU.get(), GeneratorScreen::new);

        event.<ProcessingMachineMenu, ProcessingMachineScreen> register(
                ModMenuTypes.MACERATOR_MENU.get(),
                (menu, inv, title) -> new ProcessingMachineScreen(
                        menu, inv, title, ICRE.makeId("textures/gui/producing/macerator_gui.png"), 35, 26, 37, 14
                )
        );

        event.<ProcessingMachineMenu, ProcessingMachineScreen> register(
                ModMenuTypes.ELECTRIC_FURNACE_MENU.get(),
                (menu, inv, title) -> new ProcessingMachineScreen(
                        menu, inv, title, ICRE.makeId("textures/gui/producing/electric_furnace_gui.png"), 37, 26, 26, 10
                )
        );

        event.<ProcessingMachineMenu, ProcessingMachineScreen> register(
                ModMenuTypes.COMPRESSOR_MENU.get(),
                (menu, inv, title) -> new ProcessingMachineScreen(
                        menu, inv, title, ICRE.makeId("textures/gui/producing/compressor_gui.png"), 36, 26, 13, 12
                )
        );

        event.register(ModMenuTypes.METAL_FORMER_MENU.get(), MetalFormerScreen::new);
    }

    @SubscribeEvent
    public static void registerRangeSelectProperties(RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(ICRE.makeId("energy"), EnergyFillProperty.MAP_CODEC);
    }
}
