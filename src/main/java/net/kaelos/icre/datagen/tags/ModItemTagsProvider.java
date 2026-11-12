package net.kaelos.icre.datagen.tags;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.definitions.ModItems;
import net.kaelos.icre.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
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
