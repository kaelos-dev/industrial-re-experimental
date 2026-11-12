package net.kaelos.icre.item.tool;

import net.minecraft.world.item.Item;

public class CraftingTool extends Item {
    public CraftingTool(Properties properties, int maxUse) {
        super(properties
                .stacksTo(1)
                .durability(maxUse));
    }
}
