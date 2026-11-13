package net.kaelos.icre.datagen.models.electric;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.block.cable.CableBlock;
import net.kaelos.icre.datagen.models.ModelHelpers;
import net.kaelos.icre.definitions.ModBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.HashMap;
import java.util.Map;

public class CableModels {
    private static final Map<Direction, VariantMutator> ARM_ROTATIONS = Map.of(
            Direction.SOUTH, BlockModelGenerators.Y_ROT_180,
            Direction.EAST, BlockModelGenerators.Y_ROT_90,
            Direction.WEST, BlockModelGenerators.Y_ROT_270,
            Direction.UP, BlockModelGenerators.X_ROT_270,
            Direction.DOWN, BlockModelGenerators.X_ROT_90
    );

    private static final Map<Integer, CableTemplates> TEMPLATES_BY_RADIUS = new HashMap<>();

    private CableModels() {}

    public static void register(BlockModelGenerators blockModels) {
        for (Block block : ModBlocks.getBlocks(CableBlock.class)) {
            if (block instanceof CableBlock cable) {
                registerCable(blockModels, cable);
            }
        }
    }

    private static void registerCable(BlockModelGenerators blockModels, CableBlock cable) {
        String name = BuiltInRegistries.BLOCK.getKey(cable).getPath();

        Material blockMaterial = new Material(ICRE.makeId("block/cable/" + name));
        Material itemMaterial = new Material(ICRE.makeId("item/cable/" + name));

        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.ALL, blockMaterial)
                .put(TextureSlot.PARTICLE, blockMaterial);

        CableTemplates templates = TEMPLATES_BY_RADIUS.computeIfAbsent(cable.getRadius(), CableModels::createTemplates);

        MultiVariant centerVariant = BlockModelGenerators.plainVariant(
                templates.center().createWithSuffix(cable, "_center", mapping, blockModels.modelOutput)
        );

        MultiVariant armVariant = BlockModelGenerators.plainVariant(
                templates.arm().createWithSuffix(cable, "_arm", mapping, blockModels.modelOutput)
        );

        MultiPartGenerator multiPart = MultiPartGenerator.multiPart(cable).with(centerVariant);
        for (Map.Entry<Direction, BooleanProperty> entry : CableBlock.PROPERTY_BY_DIRECTION.entrySet()) {
            VariantMutator rotation = ARM_ROTATIONS.get(entry.getKey());
            MultiVariant variant = rotation != null ? armVariant.with(rotation) : armVariant;
            multiPart.with(new ConditionBuilder().term(entry.getValue(), true), variant);
        }

        blockModels.blockStateOutput.accept(multiPart);
        ModelHelpers.flatBlockItem(blockModels, cable, itemMaterial);
    }

    private static CableTemplates createTemplates(int radius) {
        int min = 8 - radius;
        int max = 8 + radius;

        ModelTemplate center = ExtendedModelTemplateBuilder.builder()
                .parent(Identifier.withDefaultNamespace("block/block"))
                .requiredTextureSlot(TextureSlot.ALL)
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .element(eb -> eb.from(min, min, min).to(max, max, max)
                        .allFaces((dir, f) -> f.texture(TextureSlot.ALL).uvs(min, min, max, max)))
                .build();

        ModelTemplate arm = ExtendedModelTemplateBuilder.builder()
                .parent(Identifier.withDefaultNamespace("block/block"))
                .requiredTextureSlot(TextureSlot.ALL)
                .requiredTextureSlot(TextureSlot.PARTICLE)
                .element(eb -> eb.from(min, min, 0).to(max, max, min)
                        .face(Direction.NORTH, f -> f.texture(TextureSlot.ALL).uvs(min, min, max, max))
                        .face(Direction.SOUTH, f -> f.texture(TextureSlot.ALL).uvs(min, min, max, max))
                        .face(Direction.UP, f -> f.texture(TextureSlot.ALL).uvs(min, 0, max, min))
                        .face(Direction.DOWN, f -> f.texture(TextureSlot.ALL).uvs(min, 0, max, min))
                        .face(Direction.EAST, f -> f.texture(TextureSlot.ALL).uvs(0, min, min, max))
                        .face(Direction.WEST, f -> f.texture(TextureSlot.ALL).uvs(max, min, 16, max)))
                .build();

        return new CableTemplates(center, arm);
    }

    private record CableTemplates(ModelTemplate center, ModelTemplate arm) {}
}
