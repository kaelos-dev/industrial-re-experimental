package net.kaelos.icre.datagen.tags;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.block.cable.CableBlock;
import net.kaelos.icre.block.ore.OreBlock;
import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ICRE.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        addOreTags();
        addRubberTags();
        addMachineTags();
        addCableTags();
    }

    private void addOreTags() {
        List<Block> ores = ModBlocks.getBlocks(OreBlock.class);
        addAll(ModTags.Blocks.ORE_BLOCKS, ores);
        addAll(Tags.Blocks.ORES, ores);
        addAll(BlockTags.MINEABLE_WITH_PICKAXE, ores);

        addAll(Tags.Blocks.ORES_IN_GROUND_STONE, ModBlocks.LEAD_ORE, ModBlocks.TIN_ORE);
        addAll(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE,
                ModBlocks.DEEPSLATE_LEAD_ORE, ModBlocks.DEEPSLATE_TIN_ORE, ModBlocks.DEEPSLATE_URANIUM_ORE);

        addAll(BlockTags.NEEDS_STONE_TOOL, ModBlocks.TIN_ORE, ModBlocks.DEEPSLATE_TIN_ORE);
        addAll(BlockTags.NEEDS_IRON_TOOL, ModBlocks.LEAD_ORE, ModBlocks.DEEPSLATE_LEAD_ORE);
        addAll(BlockTags.NEEDS_DIAMOND_TOOL, ModBlocks.DEEPSLATE_URANIUM_ORE);
    }

    private void addRubberTags() {
        addAll(ModTags.Blocks.RUBBER_LOGS_BLOCKS,
                ModBlocks.RUBBER_LOG, ModBlocks.STRIPPED_RUBBER_LOG,
                ModBlocks.RUBBER_WOOD, ModBlocks.STRIPPED_RUBBER_WOOD);

        tag(BlockTags.LOGS_THAT_BURN).addTag(ModTags.Blocks.RUBBER_LOGS_BLOCKS);
        addAll(BlockTags.OVERWORLD_NATURAL_LOGS, ModBlocks.RUBBER_LOG);
        addAll(Tags.Blocks.STRIPPED_LOGS, ModBlocks.STRIPPED_RUBBER_LOG);
        addAll(Tags.Blocks.STRIPPED_WOODS, ModBlocks.STRIPPED_RUBBER_WOOD);

        tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(ModTags.Blocks.RUBBER_LOGS_BLOCKS)
                .add(ModBlocks.RUBBER_PLANKS.get());

        addAll(BlockTags.LEAVES, ModBlocks.RUBBER_LEAVES);
        addAll(BlockTags.MINEABLE_WITH_HOE, ModBlocks.RUBBER_LEAVES);
        addAll(BlockTags.SAPLINGS, ModBlocks.RUBBER_SAPLING);
        addAll(BlockTags.PLANKS, ModBlocks.RUBBER_PLANKS);
    }

    private void addMachineTags() {
        List<Block> machines = ModBlocks.getBlocks(BaseEntityMachineBlock.class);
        addAll(ModTags.Blocks.MACHINES, machines);

        tag(ModTags.Blocks.MINEABLE_WITH_WRENCH)
                .addAll(machines)
                .add(ModBlocks.BASIC_MACHINE_CASING.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addAll(machines)
                .add(ModBlocks.BASIC_MACHINE_CASING.get());
    }

    private void addCableTags() {
        List<Block> cables = ModBlocks.getBlocks(CableBlock.class);
        addAll(ModTags.Blocks.CABLES, cables);
        addAll(ModTags.Blocks.MINEABLE_WITH_WRENCH, cables);
        addAll(BlockTags.MINEABLE_WITH_PICKAXE, cables);
    }

    private void addAll(TagKey<Block> tagKey, Collection<Block> blocks) {
        tag(tagKey).addAll(blocks);
    }

    @SafeVarargs
    private void addAll(TagKey<Block> tagKey, Supplier<? extends Block>... blocks) {
        var builder = tag(tagKey);
        for (Supplier<? extends Block> block : blocks) {
            builder.add(block.get());
        }
    }
}
