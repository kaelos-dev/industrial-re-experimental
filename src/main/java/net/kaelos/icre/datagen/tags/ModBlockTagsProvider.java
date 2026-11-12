package net.kaelos.icre.datagen.tags;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
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

        addRubberTags();
    }

    private void addRubberTags() {
        tag(ModTags.Blocks.RUBBER_LOGS_BLOCKS)
                .add(ModBlocks.RUBBER_LOG.get())
                .add(ModBlocks.STRIPPED_RUBBER_LOG.get())
                .add(ModBlocks.RUBBER_WOOD.get())
                .add(ModBlocks.STRIPPED_RUBBER_WOOD.get());

        tag(BlockTags.LOGS_THAT_BURN)
                .addTag(ModTags.Blocks.RUBBER_LOGS_BLOCKS);

        tag(BlockTags.OVERWORLD_NATURAL_LOGS)
                .add(ModBlocks.RUBBER_LOG.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(ModTags.Blocks.RUBBER_LOGS_BLOCKS)
                .add(ModBlocks.RUBBER_PLANKS.get());

        tag(BlockTags.LEAVES)
                .add(ModBlocks.RUBBER_LEAVES.get());

        tag(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocks.RUBBER_LEAVES.get());

        tag(BlockTags.SAPLINGS)
                .add(ModBlocks.RUBBER_SAPLING.get());

        tag(BlockTags.PLANKS)
                .add(ModBlocks.RUBBER_PLANKS.get());
    }
}
