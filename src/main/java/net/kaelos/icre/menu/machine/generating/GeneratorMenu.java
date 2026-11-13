package net.kaelos.icre.menu.machine.generating;

import net.kaelos.icre.definitions.ModMenuTypes;
import net.kaelos.icre.entity.block.machine.generating.GeneratorBlockEntity;
import net.kaelos.icre.menu.base.BaseContainerMenu;
import net.kaelos.icre.menu.slot.FuelSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class GeneratorMenu extends BaseContainerMenu<GeneratorBlockEntity> {
    public GeneratorMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, getBlockEntityFromBuf(playerInv, extraData), new SimpleContainerData(4));
    }

    public GeneratorMenu(int containerId, Inventory playerInv, GeneratorBlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.GENERATOR_MENU.get(), containerId, playerInv, blockEntity, data);
    }

    @Override
    protected void addMachineSlots(Inventory playerInv) {
        this.addSlot(new FuelSlot(this.level, this.blockEntity.inventory, GeneratorBlockEntity.SLOT_FUEL, 53, 53));
    }

    @Override
    protected int getMachineSlotCount() {
        return 1;
    }

    public int getEnergy() {
        return this.data.get(0);
    }

    public int getMaxEnergy() {
        return this.data.get(1);
    }

    public int getBurnTime() {
        return this.data.get(2);
    }

    public int getMaxBurnTime() {
        return this.data.get(3);
    }

    public int getScaledEnergy() {
        return scale(getEnergy(), getMaxEnergy(), 52);
    }

    public int getScaledBurnTime() {
        return scale(getBurnTime(), getMaxBurnTime(), 12);
    }

    public int getScaledProgress() {
        int burnTime = getBurnTime();
        int maxBurnTime = getMaxBurnTime();
        if (burnTime <= 0 || maxBurnTime <= 0) {
            return 0;
        }

        return scale(maxBurnTime - burnTime, maxBurnTime, 13);
    }
}
