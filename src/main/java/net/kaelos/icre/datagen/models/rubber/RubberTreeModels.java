package net.kaelos.icre.datagen.models.rubber;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.block.rubber.RubberLogBlock;
import net.kaelos.icre.datagen.models.ModelHelpers;
import net.kaelos.icre.definitions.ModBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public class RubberTreeModels {
    private RubberTreeModels() {}

    public static void register(BlockModelGenerators blockModels) {
        registerRubberLog(blockModels);

        ModelHelpers.pillarWithHorizontal(blockModels, ModBlocks.STRIPPED_RUBBER_LOG.get(),
                tex("stripped_rubber_log"), tex("stripped_rubber_log_top"));
        ModelHelpers.woodBlock(blockModels, ModBlocks.RUBBER_WOOD.get(), tex("rubber_log"));
        ModelHelpers.woodBlock(blockModels, ModBlocks.STRIPPED_RUBBER_WOOD.get(), tex("stripped_rubber_log"));
        ModelHelpers.cubeBlock(blockModels, ModBlocks.RUBBER_LEAVES.get(), tex("rubber_leaves"));
        ModelHelpers.cubeBlock(blockModels, ModBlocks.RUBBER_PLANKS.get(), tex("rubber_planks"));
        ModelHelpers.saplingBlock(blockModels, ModBlocks.RUBBER_SAPLING.get(), tex("rubber_sapling"));
    }

    private static void registerRubberLog(BlockModelGenerators blockModels) {
        Block log = ModBlocks.RUBBER_LOG.get();
        Material logSide = tex("rubber_log");
        Material logTop = tex("rubber_log_top");

        TextureMapping columnMapping = TextureMapping.column(logSide, logTop);
        Identifier logVerticalId = ModelTemplates.CUBE_COLUMN.create(log, columnMapping, blockModels.modelOutput);

        MultiVariant logVertical = BlockModelGenerators.plainVariant(logVerticalId);
        MultiVariant logHorizontal = BlockModelGenerators.plainVariant(
                ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(log, columnMapping, blockModels.modelOutput));

        MultiVariant logDry = resinVariant(log, "_dry", logSide, logTop, tex("rubber_log_dry"), blockModels);
        MultiVariant logWet = resinVariant(log, "_wet", logSide, logTop, tex("rubber_log_wet"), blockModels);

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(log)
                        .with(PropertyDispatch.initial(
                                RubberLogBlock.AXIS,
                                RubberLogBlock.RESIN,
                                RubberLogBlock.COLLECTABLE,
                                RubberLogBlock.RESIN_FACING
                        )
                                .generate((axis, resin, collectable, facing) -> {
                                    if (!resin || axis != Direction.Axis.Y) {
                                        return switch (axis) {
                                            case Y -> logVertical;
                                            case Z -> logHorizontal.with(BlockModelGenerators.X_ROT_90);
                                            case X -> logHorizontal.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90);
                                        };
                                    }

                                    MultiVariant base = collectable ? logWet : logDry;
                                    return switch (facing) {
                                        case EAST -> base.with(BlockModelGenerators.Y_ROT_90);
                                        case SOUTH -> base.with(BlockModelGenerators.Y_ROT_180);
                                        case WEST -> base.with(BlockModelGenerators.Y_ROT_270);
                                        default -> base;
                                    };
                                }))
        );

        blockModels.registerSimpleItemModel(log, logVerticalId);
    }

    private static MultiVariant resinVariant(Block log, String suffix, Material side, Material end, Material front, BlockModelGenerators blockModels) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.SIDE, side)
                .put(TextureSlot.TOP, end)
                .put(TextureSlot.BOTTOM, end)
                .put(TextureSlot.FRONT, front);
        return BlockModelGenerators.plainVariant(
                ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.createWithSuffix(log, suffix, mapping, blockModels.modelOutput));
    }

    private static Material tex(String name) {
        return new Material(ICRE.makeId("block/rubber/" + name));
    }
}
