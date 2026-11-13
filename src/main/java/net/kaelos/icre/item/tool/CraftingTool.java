package net.kaelos.icre.item.tool;

public class CraftingTool extends BaseToolItem {
    public CraftingTool(Properties properties, int maxUse) {
        super(properties
                .stacksTo(1)
                .durability(maxUse));
    }
}
