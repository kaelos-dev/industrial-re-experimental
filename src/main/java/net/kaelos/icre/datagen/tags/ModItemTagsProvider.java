package net.kaelos.icre.datagen.tags;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.block.ore.OreBlock;
import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.definitions.ModItems;
import net.kaelos.icre.definitions.ModItems.ItemGroup;
import net.kaelos.icre.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, ICRE.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        addMaterialTags();
        addToolTags();
        addBlockItemTags();
    }

    private void addMaterialTags() {
        addGroup(ItemGroup.INGOT, ModTags.Items.INGOT_ITEMS, Tags.Items.INGOTS);
        addGroup(ItemGroup.PLATE, ModTags.Items.PLATE_ITEMS);
        addGroup(ItemGroup.DENSE_PLATE, ModTags.Items.DENSE_PLATE_ITEMS);
        addGroup(ItemGroup.CASING, ModTags.Items.CASING_ITEMS);
        addGroup(ItemGroup.DUST, ModTags.Items.DUST_ITEMS, Tags.Items.DUSTS);
        addGroup(ItemGroup.RAW, ModTags.Items.RAW_MATERIAL_ITEMS, Tags.Items.RAW_MATERIALS);
    }

    private void addToolTags() {
        addGroup(ItemGroup.TOOL, ModTags.Items.TOOL_ITEMS, Tags.Items.TOOLS);
        addAll(Tags.Items.TOOLS_WRENCH, ModItems.WRENCH, ModItems.ELECTRIC_WRENCH);
        addAll(Tags.Items.TOOLS_SHEAR, ModItems.CHAINSAW);

        addAll(ItemTags.PICKAXES, ModItems.MINING_DRILL, ModItems.DIAMOND_DRILL, ModItems.IRIDIUM_DRILL);
        addAll(ItemTags.SHOVELS, ModItems.MINING_DRILL, ModItems.DIAMOND_DRILL, ModItems.IRIDIUM_DRILL);
        addAll(ItemTags.AXES, ModItems.CHAINSAW);
        addAll(ItemTags.HOES, ModItems.ELECTRIC_HOE);

        addAll(ItemTags.MINING_ENCHANTABLE,
                ModItems.MINING_DRILL, ModItems.DIAMOND_DRILL, ModItems.IRIDIUM_DRILL,
                ModItems.CHAINSAW, ModItems.ELECTRIC_HOE);
        addAll(ItemTags.MINING_LOOT_ENCHANTABLE,
                ModItems.MINING_DRILL, ModItems.DIAMOND_DRILL, ModItems.IRIDIUM_DRILL,
                ModItems.CHAINSAW, ModItems.ELECTRIC_HOE);
    }

    private void addBlockItemTags() {
        tag(ItemTags.PLANKS).add(ModBlocks.RUBBER_PLANKS.asItem());
        tag(ItemTags.SAPLINGS).add(ModBlocks.RUBBER_SAPLING.asItem());
        tag(ItemTags.LEAVES).add(ModBlocks.RUBBER_LEAVES.asItem());
        tag(ItemTags.LOGS_THAT_BURN).add(
                ModBlocks.RUBBER_LOG.asItem(),
                ModBlocks.STRIPPED_RUBBER_LOG.asItem(),
                ModBlocks.RUBBER_WOOD.asItem(),
                ModBlocks.STRIPPED_RUBBER_WOOD.asItem()
        );
        tag(Tags.Items.ORES).addAll(ModBlocks.getBlocks(OreBlock.class).stream().map(Block::asItem).toList());
    }

    @SafeVarargs
    private void addGroup(ItemGroup group, TagKey<Item>... tags) {
        var items = ModItems.getGroup(group);
        for (TagKey<Item> tagKey : tags) {
            tag(tagKey).addAll(items);
        }
    }

    @SafeVarargs
    private void addAll(TagKey<Item> tagKey, Supplier<? extends Item>... items) {
        var builder = tag(tagKey);
        for (Supplier<? extends Item> item : items) {
            builder.add(item.get());
        }
    }
}
