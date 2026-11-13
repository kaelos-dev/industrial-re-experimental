package net.kaelos.icre.menu.base;

import net.kaelos.icre.ICRE;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public abstract class BaseContainerMenu<T extends BlockEntity> extends AbstractContainerMenu {
    protected static final int HOTBAR_SLOT_COUNT = 9;
    protected static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    protected static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    protected static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    protected static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    protected static final int VANILLA_FIRST_SLOT_INDEX = 0;
    protected static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    protected final Level level;
    public final T blockEntity;
    protected final ContainerData data;

    protected BaseContainerMenu(MenuType<?> menuType, int containerId, Inventory playerInv, T blockEntity, ContainerData data) {
        super(menuType, containerId);
        this.level = playerInv.player.level();
        this.blockEntity = blockEntity;
        this.data = data;

        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
        addMachineSlots(playerInv);
        addDataSlots(data);
    }

    @SuppressWarnings("unchecked")
    protected static <T extends BlockEntity> T getBlockEntityFromBuf(Inventory playerInv, FriendlyByteBuf extraData) {
        BlockEntity be = playerInv.player.level().getBlockEntity(extraData.readBlockPos());
        return (T) Objects.requireNonNull(be, "BlockEntity not found at menu position");
    }

    protected abstract void addMachineSlots(Inventory playerInv);

    protected abstract int getMachineSlotCount();

    protected boolean moveToMachine(ItemStack stack, int teSlotCount) {
        return this.moveItemStackTo(stack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + teSlotCount, false);
    }

    protected int machineSlot(int localIndex) {
        return TE_INVENTORY_FIRST_SLOT_INDEX + localIndex;
    }

    protected boolean isOutputMachineSlot(int menuSlotIndex) {
        return false;
    }

    private void addPlayerInventory(Inventory playerInv) {
        for (int row = 0; row < PLAYER_INVENTORY_ROW_COUNT; ++row) {
            for (int col = 0; col < PLAYER_INVENTORY_COLUMN_COUNT; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInv) {
        for (int col = 0; col < HOTBAR_SLOT_COUNT; ++col) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    public static int scale(int value, int max, int pixels) {
        if (max <= 0 || value <= 0) {
            return 0;
        }

        return (int) (((float) value / max) * pixels);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int index) {
        Slot sourceSlot = this.slots.get(index);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        int teSlotCount = getMachineSlotCount();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveToMachine(sourceStack, teSlotCount)) {
                if (index < VANILLA_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT) {
                    if (!this.moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + teSlotCount) {
            boolean reverse = isOutputMachineSlot(index);
            if (!this.moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, reverse)) {
                return ItemStack.EMPTY;
            }

            if (reverse) {
                sourceSlot.onQuickCraft(sourceStack, copyOfSourceStack);
            }
        } else {
            ICRE.LOGGER.error("Invalid slotIndex: {}", index);
            return ItemStack.EMPTY;
        }

        if (sourceStack.isEmpty()) {
            sourceSlot.setByPlayer(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        if (sourceStack.getCount() == copyOfSourceStack.getCount()) {
            return ItemStack.EMPTY;
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return stillValid(
                ContainerLevelAccess.create(this.level, this.blockEntity.getBlockPos()),
                player,
                this.blockEntity.getBlockState().getBlock()
        );
    }
}
