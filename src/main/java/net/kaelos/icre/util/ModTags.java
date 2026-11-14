package net.kaelos.icre.util;

import net.kaelos.icre.ICRE;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> ORE_BLOCKS = createTag("ore_blocks");
        public static final TagKey<Block> STORAGE_BLOCKS = createTag("storage_blocks");
        public static final TagKey<Block> RUBBER_LOGS_BLOCKS = createTag("rubber_logs_blocks");
        public static final TagKey<Block> MACHINES = createTag("machines");
        public static final TagKey<Block> CABLES = createTag("cables");
        public static final TagKey<Block> MINEABLE_WITH_WRENCH = createTag("mineable/wrench");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ICRE.makeId(name));
        }
    }

    public static class Items {
        public static final TagKey<Item> INGOT_ITEMS = createTag("ingot_items");
        public static final TagKey<Item> PLATE_ITEMS = createTag("plate_items");
        public static final TagKey<Item> DENSE_PLATE_ITEMS = createTag("dense_plate_items");
        public static final TagKey<Item> CASING_ITEMS = createTag("casing_items");
        public static final TagKey<Item> DUST_ITEMS = createTag("dust_items");
        public static final TagKey<Item> RAW_MATERIAL_ITEMS = createTag("raw_material_items");
        public static final TagKey<Item> TOOL_ITEMS = createTag("tool_items");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ICRE.makeId(name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> HAS_RUBBER_TREE = TagKey.create(Registries.BIOME, ICRE.makeId("has_rubber_tree"));
    }
}
