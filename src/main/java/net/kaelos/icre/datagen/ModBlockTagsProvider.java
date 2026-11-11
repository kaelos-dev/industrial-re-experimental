package net.kaelos.icre.datagen;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.definitions.ModItems;
import net.kaelos.icre.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ICRE.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.LEAD_ORE.get())
                .add(ModBlocks.DEEPSLATE_LEAD_ORE.get())
                .add(ModBlocks.TIN_ORE.get())
                .add(ModBlocks.DEEPSLATE_TIN_ORE.get())
                .add(ModBlocks.DEEPSLATE_URANIUM_ORE.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.TIN_ORE.get())
                .add(ModBlocks.DEEPSLATE_TIN_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.LEAD_ORE.get())
                .add(ModBlocks.DEEPSLATE_LEAD_ORE.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.DEEPSLATE_URANIUM_ORE.get());

        tag(ModTags.Blocks.ORE_BLOCKS)
                .add(ModBlocks.LEAD_ORE.get())
                .add(ModBlocks.DEEPSLATE_LEAD_ORE.get())
                .add(ModBlocks.TIN_ORE.get())
                .add(ModBlocks.DEEPSLATE_TIN_ORE.get())
                .add(ModBlocks.DEEPSLATE_URANIUM_ORE.get());
    }

    public static class ModItemTagsProvider extends ItemTagsProvider {
        public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
            super(output, lookupProvider, ICRE.MOD_ID);
        }

        @Override
        protected void addTags(HolderLookup.@NonNull Provider provider) {
            tag(ModTags.Items.INGOT_ITEMS)
                    .add(ModItems.BRONZE_INGOT.get())
                    .add(ModItems.LEAD_INGOT.get())
                    .add(ModItems.SILVER_INGOT.get())
                    .add(ModItems.STEEL_INGOT.get())
                    .add(ModItems.TIN_INGOT.get());

            tag(ModTags.Items.PLATE_ITEMS)
                    .add(ModItems.BRONZE_PLATE.get())
                    .add(ModItems.COPPER_PLATE.get())
                    .add(ModItems.GOLD_PLATE.get())
                    .add(ModItems.IRON_PLATE.get())
                    .add(ModItems.LAPIS_LAZULI_PLATE.get())
                    .add(ModItems.LEAD_PLATE.get())
                    .add(ModItems.OBSIDIAN_PLATE.get())
                    .add(ModItems.STEEL_PLATE.get())
                    .add(ModItems.TIN_PLATE.get());

            tag(ModTags.Items.DENSE_PLATE_ITEMS)
                    .add(ModItems.DENSE_BRONZE_PLATE.get())
                    .add(ModItems.DENSE_COPPER_PLATE.get())
                    .add(ModItems.DENSE_GOLD_PLATE.get())
                    .add(ModItems.DENSE_IRON_PLATE.get())
                    .add(ModItems.DENSE_LAPIS_LAZULI_PLATE.get())
                    .add(ModItems.DENSE_LEAD_PLATE.get())
                    .add(ModItems.DENSE_OBSIDIAN_PLATE.get())
                    .add(ModItems.DENSE_STEEL_PLATE.get())
                    .add(ModItems.DENSE_TIN_PLATE.get());

            tag(ModTags.Items.CASING_ITEMS)
                    .add(ModItems.BRONZE_CASING.get())
                    .add(ModItems.COPPER_CASING.get())
                    .add(ModItems.GOLD_CASING.get())
                    .add(ModItems.IRON_CASING.get())
                    .add(ModItems.LEAD_CASING.get())
                    .add(ModItems.STEEL_CASING.get())
                    .add(ModItems.TIN_CASING.get());
        }
    }
}
