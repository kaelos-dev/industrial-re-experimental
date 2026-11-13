package net.kaelos.icre.datagen.loot;

import net.kaelos.icre.block.rubber.RubberLogBlock;
import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.definitions.ModItems;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class ModBlockLootSubProvider extends BlockLootSubProvider {
    private static final float[] RUBBER_LEAVES_SAPLING_CHANCES = new float[]{
            1.0F / 35.0F,
            1.0F / 30.0F,
            1.0F / 24.0F,
            1.0F / 18.0F
    };

    public ModBlockLootSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        add(ModBlocks.LEAD_ORE.get(), block -> createOreDrop(block, ModItems.RAW_LEAD.get()));
        add(ModBlocks.DEEPSLATE_LEAD_ORE.get(), block -> createOreDrop(block, ModItems.RAW_LEAD.get()));
        add(ModBlocks.TIN_ORE.get(), block -> createMultipleOreDrops(block, ModItems.RAW_TIN.get(), 2.0F, 5.0F));
        add(ModBlocks.DEEPSLATE_TIN_ORE.get(), block -> createMultipleOreDrops(block, ModItems.RAW_TIN.get(), 2.0F, 5.0F));
        add(ModBlocks.DEEPSLATE_URANIUM_ORE.get(), block -> createOreDrop(block, ModItems.RAW_URANIUM.get()));

        add(ModBlocks.RUBBER_LOG.get(), this::createRubberLogDrops);
        dropSelf(ModBlocks.STRIPPED_RUBBER_LOG.get());
        dropSelf(ModBlocks.RUBBER_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_RUBBER_WOOD.get());
        dropSelf(ModBlocks.RUBBER_PLANKS.get());
        dropSelf(ModBlocks.RUBBER_SAPLING.get());
        add(ModBlocks.RUBBER_LEAVES.get(), block ->
                this.createLeavesDrops(block, ModBlocks.RUBBER_SAPLING.get(), RUBBER_LEAVES_SAPLING_CHANCES));

        dropSelf(ModBlocks.TIN_CABLE.get());
        dropSelf(ModBlocks.COPPER_CABLE.get());
        dropSelf(ModBlocks.GOLD_CABLE.get());
        dropSelf(ModBlocks.IRON_CABLE.get());
        dropSelf(ModBlocks.GLASS_FIBRE_CABLE.get());

        dropSelf(ModBlocks.GENERATOR.get());
        dropSelf(ModBlocks.MACERATOR.get());
    }

    private LootTable.Builder createMultipleOreDrops(Block block, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    private LootTable.Builder createRubberLogDrops(Block logBlock) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        var hasWetResin = LootItemBlockStatePropertyCondition.hasBlockStateProperties(logBlock)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                        .hasProperty(RubberLogBlock.RESIN, true)
                        .hasProperty(RubberLogBlock.COLLECTABLE, true));
        var hasDryResin = LootItemBlockStatePropertyCondition.hasBlockStateProperties(logBlock)
                .setProperties(StatePropertiesPredicate.Builder.properties()
                        .hasProperty(RubberLogBlock.RESIN, true)
                        .hasProperty(RubberLogBlock.COLLECTABLE, false));

        return LootTable.lootTable()
                .withPool(this.applyExplosionCondition(logBlock, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(logBlock))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(this.doesNotHaveSilkTouch())
                        .when(hasWetResin)
                        .add(this.applyExplosionCondition(logBlock, LootItem.lootTableItem(ModItems.LATEX.get())
                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(
                                        enchantments.getOrThrow(Enchantments.FORTUNE),
                                        0.25F,
                                        0
                                )))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(this.doesNotHaveSilkTouch())
                        .when(hasDryResin)
                        .when(LootItemRandomChanceCondition.randomChance(0.2F))
                        .add(this.applyExplosionCondition(logBlock, LootItem.lootTableItem(ModItems.LATEX.get()))));
    }

    @Override
    protected @NonNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
