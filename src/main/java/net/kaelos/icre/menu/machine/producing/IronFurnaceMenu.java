package net.kaelos.icre.menu.machine.producing;

import net.kaelos.icre.definitions.ModMenuTypes;
import net.kaelos.icre.entity.block.machine.producing.IronFurnaceBlockEntity;
import net.kaelos.icre.menu.base.BaseContainerMenu;
import net.kaelos.icre.menu.slot.FuelSlot;
import net.kaelos.icre.menu.slot.OutputSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

public class IronFurnaceMenu extends BaseContainerMenu<IronFurnaceBlockEntity> {
    public IronFurnaceMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, getBlockEntityFromBuf(playerInv, extraData), new SimpleContainerData(4));
    }

    public IronFurnaceMenu(int containerId, Inventory playerInv, IronFurnaceBlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.IRON_FURNACE_MENU.get(), containerId, playerInv, blockEntity, data);
    }

    @Override
    protected void addMachineSlots(Inventory playerInv) {
        var inv = this.blockEntity.inventory;
        this.addSlot(new ResourceHandlerSlot(inv, inv::set, IronFurnaceBlockEntity.SLOT_INPUT, 56, 17));
        this.addSlot(new FuelSlot(this.level, inv, IronFurnaceBlockEntity.SLOT_FUEL, 56, 53));
        this.addSlot(new OutputSlot(playerInv.player, inv, IronFurnaceBlockEntity.SLOT_OUTPUT, 116, 35));
    }

    @Override
    protected int getMachineSlotCount() {
        return 3;
    }

    @Override
    protected boolean moveToMachine(ItemStack stack, int teSlotCount) {
        if (this.level.recipeAccess().propertySet(RecipePropertySet.FURNACE_INPUT).test(stack)) {
            int inputSlot = machineSlot(IronFurnaceBlockEntity.SLOT_INPUT);
            if (this.moveItemStackTo(stack, inputSlot, inputSlot + 1, false)) {
                return true;
            }
        }

        if (stack.getBurnTime(RecipeType.SMELTING, this.level.fuelValues()) > 0) {
            int fuelSlot = machineSlot(IronFurnaceBlockEntity.SLOT_FUEL);
            return this.moveItemStackTo(stack, fuelSlot, fuelSlot + 1, false);
        }

        return false;
    }

    @Override
    protected boolean isOutputMachineSlot(int menuSlotIndex) {
        return menuSlotIndex == machineSlot(IronFurnaceBlockEntity.SLOT_OUTPUT);
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    public float getLitProgress() {
        int duration = this.data.get(1);
        if (duration <= 0) {
            duration = 200;
        }
        return (float) this.data.get(0) / duration;
    }

    public float getBurnProgress() {
        int progress = this.data.get(2);
        int totalTime = this.data.get(3);
        return progress > 0 && totalTime > 0 ? (float) progress / totalTime : 0.0F;
    }
}
