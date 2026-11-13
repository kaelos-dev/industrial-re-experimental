package net.kaelos.icre.entity.block.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class InventoryBlockEntity extends BlockEntity implements MenuProvider, IItemHandlerProvider {
    private static final String KEY_INVENTORY = "inventory";

    public final ItemStacksResourceHandler inventory;

    public InventoryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state);
        this.inventory = new ItemStacksResourceHandler(inventorySize) {
            @Override
            protected void onContentsChanged(int index, @NonNull ItemStack previousContents) {
                setChanged();
                onInventoryChanged(index, previousContents);
            }

            @Override
            public boolean isValid(int index, @NonNull ItemResource resource) {
                return InventoryBlockEntity.this.isItemValid(index, resource);
            }
        };
    }

    @Override
    public @Nullable ResourceHandler<ItemResource> getItemHandler(@Nullable Direction side) {
        return this.inventory;
    }

    protected boolean isItemValid(int slot, ItemResource resource) {
        return true;
    }

    protected void onInventoryChanged(int slot, ItemStack previousContents) {}

    protected ItemStack getStack(int slot) {
        ItemResource resource = this.inventory.getResource(slot);
        int amount = this.inventory.getAmountAsInt(slot);
        return resource.isEmpty() || amount <= 0 ? ItemStack.EMPTY : resource.toStack(amount);
    }

    protected void setStack(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            this.inventory.set(slot, ItemResource.EMPTY, 0);
        } else {
            this.inventory.set(slot, ItemResource.of(stack), stack.getCount());
        }
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        this.inventory.serialize(output.child(KEY_INVENTORY));
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        input.child(KEY_INVENTORY).ifPresent(this.inventory::deserialize);
    }

    @Override
    public void preRemoveSideEffects(@NonNull BlockPos pos, @NonNull BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (this.level != null && !this.level.isClientSide()) {
            Containers.dropContents(this.level, pos, this.inventory.copyToList());
        }
    }

    @Override
    public @NonNull Component getDisplayName() {
        return this.getBlockState().getBlock().getName();
    }

    @Override
    public abstract @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory playerInv, @NonNull Player player);
}
