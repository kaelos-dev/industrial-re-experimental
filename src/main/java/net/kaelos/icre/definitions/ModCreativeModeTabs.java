package net.kaelos.icre.definitions;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.block.cable.CableBlock;
import net.kaelos.icre.block.storage.StorageBlock;
import net.kaelos.icre.block.ore.OreBlock;
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

    // ==================== Resources Tab ====================
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RESOURCES_TAB = registerTab(
            "resources_tab",
            ModItems.BRONZE_INGOT,
            (parameters, output) -> {
                ModItems.getGroup(ModItems.ItemGroup.INGOT).forEach(output::accept);
                ModItems.getGroup(ModItems.ItemGroup.PLATE).forEach(output::accept);
                ModItems.getGroup(ModItems.ItemGroup.DENSE_PLATE).forEach(output::accept);
                ModItems.getGroup(ModItems.ItemGroup.CASING).forEach(output::accept);
                ModItems.getGroup(ModItems.ItemGroup.DUST).forEach(output::accept);
                ModBlocks.getBlocks(OreBlock.class).forEach(output::accept);
                ModBlocks.getBlocks(StorageBlock.class).forEach(output::accept);
                ModItems.getGroup(ModItems.ItemGroup.RAW).forEach(output::accept);

                output.accept(ModItems.LATEX.get());
                output.accept(ModItems.RUBBER.get());
            }
    );

    // ==================== Agriculture Tab ====================
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AGRICULTURE_TAB = registerTab(
            "agriculture_tab",
            ModBlocks.RUBBER_SAPLING,
            (parameters, output) -> {
                output.accept(ModBlocks.RUBBER_LOG.get());
                output.accept(ModBlocks.RUBBER_WOOD.get());
                output.accept(ModBlocks.STRIPPED_RUBBER_LOG.get());
                output.accept(ModBlocks.STRIPPED_RUBBER_WOOD.get());
                output.accept(ModBlocks.RUBBER_PLANKS.get());
                output.accept(ModBlocks.RUBBER_LEAVES.get());
                output.accept(ModBlocks.RUBBER_SAPLING.get());
            }
    );

    // ==================== Components Tab ====================
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COMPONENTS_TAB = registerTab(
            "components_tab",
            ModBlocks.TIN_CABLE,
            (parameters, output) -> {
                ModBlocks.getBlocks(CableBlock.class).forEach(output::accept);
                output.accept(ModBlocks.BASIC_MACHINE_CASING.get());

                output.accept(ModItems.COIL.get());
                output.accept(ModItems.ELECTRONIC_CIRCUIT.get());

                ModItems.getGroup(ModItems.ItemGroup.BATTERY).forEach(output::accept);
                output.accept(ModItems.RE_BATTERY.get().createChargedStack());
            }
    );

    // ==================== Machines Tab ====================
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MACHINES_TAB = registerTab(
            "machines_tab",
            ModBlocks.MACERATOR,
            (parameters, output) -> ModBlocks.getBlocks(BaseEntityMachineBlock.class).forEach(output::accept)
    );

    // ==================== Tools Tab ====================
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TOOLS_TAB = registerTab(
            "tools_tab",
            ModItems.TREETAP,
            (parameters, output) -> ModItems.getGroup(ModItems.ItemGroup.TOOL).forEach(output::accept)
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
