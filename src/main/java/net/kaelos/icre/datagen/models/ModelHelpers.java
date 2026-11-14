package net.kaelos.icre.datagen.models;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.client.item.EnergyFillProperty;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModelHelpers {
    private ModelHelpers() {}

    public static void flatItem(ItemModelGenerators itemModels, Item item, String path, ModelTemplate template) {
        Material material = new Material(ICRE.makeId("item/" + path));
        TextureMapping mapping = TextureMapping.layer0(material);
        Identifier modelId = template.create(item, mapping, itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelId));
    }

    public static void energyItem(ItemModelGenerators itemModels, Item item, String folder, float... stageThresholds) {
        String name = BuiltInRegistries.ITEM.getKey(item).getPath();
        String basePath = folder.isEmpty() ? name : folder + "/" + name;

        Identifier emptyModelId = createFlatStageModel(itemModels, name + "_0", basePath + "_0");
        List<RangeSelectItemModel.Entry> entries = new ArrayList<>(stageThresholds.length);
        for (int i = 0; i < stageThresholds.length; i++) {
            int stage = i + 1;
            Identifier stageModelId = createFlatStageModel(itemModels, name + "_" + stage, basePath + "_" + stage);
            entries.add(ItemModelUtils.override(ItemModelUtils.plainModel(stageModelId), stageThresholds[i]));
        }

        itemModels.itemModelOutput.accept(
                item,
                ItemModelUtils.rangeSelect(EnergyFillProperty.INSTANCE, ItemModelUtils.plainModel(emptyModelId), entries)
        );
    }

    private static Identifier createFlatStageModel(ItemModelGenerators itemModels, String modelName, String texturePath) {
        Material material = new Material(ICRE.makeId("item/" + texturePath));
        return ModelTemplates.FLAT_ITEM.create(
                ICRE.makeId("item/" + modelName),
                TextureMapping.layer0(material),
                itemModels.modelOutput
        );
    }

    @SafeVarargs
    public static void flatItems(ItemModelGenerators itemModels, String folder, Supplier<? extends Item>... items) {
        for (Supplier<? extends Item> holder : items) {
            Item item = holder.get();
            String name = BuiltInRegistries.ITEM.getKey(item).getPath();
            String path = folder.isEmpty() ? name : folder + "/" + name;
            ModelHelpers.flatItem(itemModels, item, path, ModelTemplates.FLAT_ITEM);
        }
    }

    @SafeVarargs
    public static void flatHandheldItems(ItemModelGenerators itemModels, String folder, Supplier<? extends Item>... items) {
        for (Supplier<? extends Item> holder : items) {
            Item item = holder.get();
            String name = BuiltInRegistries.ITEM.getKey(item).getPath();
            String path = folder.isEmpty() ? name : folder + "/" + name;
            ModelHelpers.flatItem(itemModels, item, path, ModelTemplates.FLAT_HANDHELD_ITEM);
        }
    }

    public static void cubeBlock(BlockModelGenerators blockModels, Block block, String path) {
        Material material = new Material(ICRE.makeId("block/" + path));
        TextureMapping mapping = TextureMapping.cube(material);
        Identifier modelId = ModelTemplates.CUBE_ALL.create(block, mapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelId)));
        blockModels.registerSimpleItemModel(block, modelId);
    }

    public static void cubeBlock(BlockModelGenerators blockModels, Block block, Material texture) {
        Identifier modelId = ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(texture), blockModels.modelOutput);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelId)));
        blockModels.registerSimpleItemModel(block, modelId);
    }

    @SafeVarargs
    public static void cubeBlocks(BlockModelGenerators blockModels, String folder, Supplier<? extends Block>... blocks) {
        for (Supplier<? extends Block> holder : blocks) {
            Block block = holder.get();
            String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
            String path = folder.isEmpty() ? name : folder + "/" + name;
            ModelHelpers.cubeBlock(blockModels, block, path);
        }
    }

    // --- Cables ---
    public static void flatBlockItem(BlockModelGenerators blockModels, Block block, Material texture) {
        Identifier itemModelId = ModelTemplates.FLAT_ITEM.create(
                block.asItem(), TextureMapping.layer0(texture), blockModels.modelOutput
        );
        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(itemModelId));
    }

    // --- Rubber Tree ---
    public static void pillarWithHorizontal(BlockModelGenerators blockModels, Block block, Material side, Material end) {
        TextureMapping mapping = TextureMapping.column(side, end);
        Identifier verticalId = ModelTemplates.CUBE_COLUMN.create(block, mapping, blockModels.modelOutput);

        MultiVariant horizontal = BlockModelGenerators.plainVariant(
                ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(block, mapping, blockModels.modelOutput)
        );

        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createRotatedPillarWithHorizontalVariant(
                        block, BlockModelGenerators.plainVariant(verticalId), horizontal
                )
        );

        blockModels.registerSimpleItemModel(block, verticalId);
    }

    public static void woodBlock(BlockModelGenerators blockModels, Block block, Material barkTexture) {
        TextureMapping mapping = TextureMapping.column(barkTexture, barkTexture);
        Identifier modelId = ModelTemplates.CUBE_COLUMN.create(block, mapping, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createAxisAlignedPillarBlock(
                        block, BlockModelGenerators.plainVariant(modelId)
                )
        );

        blockModels.registerSimpleItemModel(block, modelId);
    }

    public static void saplingBlock(BlockModelGenerators blockModels, Block sapling, Material texture) {
        Identifier blockModelId = ModelTemplates.CROSS.create(sapling, TextureMapping.cross(texture), blockModels.modelOutput);

        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(
                        sapling, BlockModelGenerators.plainVariant(blockModelId)
                )
        );

        flatBlockItem(blockModels, sapling, texture);
    }
}
