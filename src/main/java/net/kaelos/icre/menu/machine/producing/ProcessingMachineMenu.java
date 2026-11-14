package net.kaelos.icre.menu.machine.producing;

import net.kaelos.icre.api.energy.EnergyUtils;
import net.kaelos.icre.entity.block.machine.producing.AbstractProcessingMachineBlockEntity;
import net.kaelos.icre.menu.base.BaseContainerMenu;
import net.kaelos.icre.menu.slot.BatterySlot;
import net.kaelos.icre.menu.slot.OutputSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class ProcessingMachineMenu extends BaseContainerMenu<AbstractProcessingMachineBlockEntity<?, ?>> {
    public ProcessingMachineMenu(MenuType<?> menuType, int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(menuType, containerId, playerInv, getBlockEntityFromBuf(playerInv, extraData), new SimpleContainerData(4));
    }

    public ProcessingMachineMenu(MenuType<?> menuType, int containerId, Inventory playerInv, AbstractProcessingMachineBlockEntity<?, ?> blockEntity, ContainerData data) {
        super(menuType, containerId, playerInv, blockEntity, data);
    }

    @Override
    protected void addMachineSlots(Inventory playerInv) {
        var inv = this.blockEntity.inventory;
        this.addSlot(new ResourceHandlerSlot(inv, inv::set, AbstractProcessingMachineBlockEntity.SLOT_INPUT, 47, 17));
        this.addSlot(new OutputSlot(playerInv.player, inv, AbstractProcessingMachineBlockEntity.SLOT_OUTPUT, 109, 35));
        this.addSlot(new BatterySlot(inv, AbstractProcessingMachineBlockEntity.SLOT_BATTERY, 47, 53, BatterySlot.Mode.DISCHARGE));
    }

    @Override
    protected int getMachineSlotCount() {
        return 3;
    }

    @Override
    protected boolean moveToMachine(ItemStack stack, int teSlotCount) {
        if (EnergyUtils.canDischarge(stack)) {
            int batterySlot = machineSlot(AbstractProcessingMachineBlockEntity.SLOT_BATTERY);
            if (this.moveItemStackTo(stack, batterySlot, batterySlot + 1, false)) {
                return true;
            }
        }

        int inputSlot = machineSlot(AbstractProcessingMachineBlockEntity.SLOT_INPUT);
        return this.moveItemStackTo(stack, inputSlot, inputSlot + 1, false);
    }

    @Override
    protected boolean isOutputMachineSlot(int menuSlotIndex) {
        return menuSlotIndex == machineSlot(AbstractProcessingMachineBlockEntity.SLOT_OUTPUT);
    }

    public int getEnergy() {
        return this.data.get(0);
    }

    public int getMaxEnergy() {
        return this.data.get(1);
    }

    public int getProgress() {
        return this.data.get(2);
    }

    public int getMaxProgress() {
        return this.data.get(3);
    }

    public int getScaledEnergy() {
        return scale(getEnergy(), getMaxEnergy(), 52);
    }

    public int getScaledProgress() {
        return scale(getProgress(), getMaxProgress(), 20);
    }
}
