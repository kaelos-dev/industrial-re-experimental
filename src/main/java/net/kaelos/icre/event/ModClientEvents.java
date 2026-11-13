package net.kaelos.icre.event;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.client.screen.machine.generating.GeneratorScreen;
import net.kaelos.icre.client.screen.machine.producing.ProcessingMachineScreen;
import net.kaelos.icre.definitions.ModMenuTypes;
import net.kaelos.icre.menu.machine.producing.ProcessingMachineMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

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
    }
}
