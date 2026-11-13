package net.kaelos.icre.datagen.loot;

import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.block.cable.CableBlock;
import net.kaelos.icre.block.rubber.RubberLogBlock;
import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.definitions.ModItems;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

import java.util.Set;
import java.util.function.Supplier;

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
        generateOreDrops();
        generateRubberTreeDrops();
        generateCableDrops();
        generateMachineDrops();
    }

    private void generateOreDrops() {
        oreDrop(ModBlocks.LEAD_ORE, ModItems.RAW_LEAD);
        oreDrop(ModBlocks.DEEPSLATE_LEAD_ORE, ModItems.RAW_LEAD);
        multiOreDrop(ModBlocks.TIN_ORE, ModItems.RAW_TIN, 2.0F, 5.0F);
        multiOreDrop(ModBlocks.DEEPSLATE_TIN_ORE, ModItems.RAW_TIN, 2.0F, 5.0F);
        oreDrop(ModBlocks.DEEPSLATE_URANIUM_ORE, ModItems.RAW_URANIUM);
    }

    private void generateRubberTreeDrops() {
        add(ModBlocks.RUBBER_LOG.get(), this::createRubberLogDrops);
        dropSelfAll(
                ModBlocks.STRIPPED_RUBBER_LOG,
                ModBlocks.RUBBER_WOOD,
                ModBlocks.STRIPPED_RUBBER_WOOD,
                ModBlocks.RUBBER_PLANKS,
                ModBlocks.RUBBER_SAPLING
        );
        add(ModBlocks.RUBBER_LEAVES.get(), block ->
                this.createLeavesDrops(block, ModBlocks.RUBBER_SAPLING.get(), RUBBER_LEAVES_SAPLING_CHANCES));
    }

    private void generateCableDrops() {
        ModBlocks.getBlocks(CableBlock.class).forEach(this::dropSelf);
    }

    private void generateMachineDrops() {
        dropSelf(ModBlocks.BASIC_MACHINE_CASING.get());
        ModBlocks.getBlocks(BaseEntityMachineBlock.class).forEach(machine ->
                add(machine, this::createMachineDrops));
    }

    private void oreDrop(Supplier<? extends Block> block, Supplier<? extends Item> rawItem) {
        add(block.get(), b -> createOreDrop(b, rawItem.get()));
    }

    private void multiOreDrop(Supplier<? extends Block> block, Supplier<? extends Item> rawItem, float min, float max) {
        add(block.get(), b -> createMultipleOreDrops(b, rawItem.get(), min, max));
    }

    @SafeVarargs
    private void dropSelfAll(Supplier<? extends Block>... blocks) {
        for (Supplier<? extends Block> block : blocks) {
            dropSelf(block.get());
        }
    }

    private LootTable.Builder createMachineDrops(Block machine) {
        return createToolOrFallbackDrop(machine, hasWrench(), ModBlocks.BASIC_MACHINE_CASING.get());
    }

    private LootTable.Builder createToolOrFallbackDrop(Block block, LootItemCondition.Builder toolCondition, ItemLike fallback) {
        return LootTable.lootTable()
                .withPool(this.applyExplosionCondition(block, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(block)
                                .when(toolCondition)
                                .otherwise(LootItem.lootTableItem(fallback)))));
    }

    private LootItemCondition.Builder hasWrench() {
        HolderLookup.RegistryLookup<Item> items = this.registries.lookupOrThrow(Registries.ITEM);
        return MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, Tags.Items.TOOLS_WRENCH));
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
