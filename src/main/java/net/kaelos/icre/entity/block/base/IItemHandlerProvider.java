package net.kaelos.icre.entity.block.base;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import javax.annotation.Nullable;

public interface IItemHandlerProvider {
    @Nullable
    ResourceHandler<ItemResource> getItemHandler(@Nullable Direction side);
}
