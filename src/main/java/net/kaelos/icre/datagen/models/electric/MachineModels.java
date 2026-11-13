package net.kaelos.icre.datagen.models.electric;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.definitions.ModBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class MachineModels {
    private MachineModels() {}

    public static void register(BlockModelGenerators blockModels) {
        registerMachine(blockModels, ModBlocks.GENERATOR.get(), faces -> faces
                .front(tex("generator/generator_front"), tex("generator/generator_front_on"))
                .side(tex("generator/generator_side"), tex("generator/generator_side_on"))
                .top(tex("generator/generator_top"), tex("generator/generator_top_on"))
                .bottom(tex("generator/generator_bottom"))
        );

        registerMachine(blockModels, ModBlocks.MACERATOR.get(), faces -> faces
                .front(tex("producing/macerator/macerator_front"), tex("producing/macerator/macerator_front_on"))
                .side(tex("producing/macerator/macerator_side"))
                .top(tex("producing/macerator/macerator_top"), tex("producing/macerator/macerator_top_on"))
                .bottom(tex("producing/macerator/macerator_bottom"))
        );
    }

    private static void registerMachine(
            BlockModelGenerators blockModels, Block machine, Consumer<MachineTextures> config
    ) {
        registerMachine(blockModels, machine, BaseEntityMachineBlock.ACTIVE, config);
    }

    private static void registerMachine(
            BlockModelGenerators blockModels, Block machine, BooleanProperty activeProp, Consumer<MachineTextures> config
    ) {
        MachineTextures textures = new MachineTextures();
        config.accept(textures);
        textures.validate();

        Identifier offModelId = ModelTemplates.CUBE.create(
                machine, textures.toMapping(false), blockModels.modelOutput
        );

        Identifier onModelId = textures.hasActiveTextures()
                ? ModelTemplates.CUBE.createWithSuffix(machine, "_active", textures.toMapping(true), blockModels.modelOutput)
                : offModelId;

        MultiVariant offVariant = BlockModelGenerators.plainVariant(offModelId);
        MultiVariant onVariant = BlockModelGenerators.plainVariant(onModelId);

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(machine)
                        .with(PropertyDispatch.initial(BaseEntityMachineBlock.FACING, activeProp)
                                .generate((facing, active) -> {
                                    MultiVariant base = active ? onVariant : offVariant;
                                    return switch (facing) {
                                        case EAST -> base.with(BlockModelGenerators.Y_ROT_90);
                                        case SOUTH -> base.with(BlockModelGenerators.Y_ROT_180);
                                        case WEST -> base.with(BlockModelGenerators.Y_ROT_270);
                                        default -> base;
                                    };
                                }))
        );

        blockModels.registerSimpleItemModel(machine, offModelId);
    }

    private static final class MachineTextures {
        private final Map<TextureSlot, Material> off = new LinkedHashMap<>();
        private final Map<TextureSlot, Material> on = new LinkedHashMap<>();

        private MachineTextures front(Material off) { return front(off, off); }
        private MachineTextures front(Material off, Material on) { return face(TextureSlot.NORTH, off, on); }
        private MachineTextures back(Material off) { return back(off, off); }
        private MachineTextures back(Material off, Material on) { return face(TextureSlot.SOUTH, off, on); }
        private MachineTextures left(Material off) { return left(off, off); }
        private MachineTextures left(Material off, Material on) { return face(TextureSlot.WEST, off, on); }
        private MachineTextures right(Material off) { return right(off, off); }
        private MachineTextures right(Material off, Material on) { return face(TextureSlot.EAST, off, on); }
        private MachineTextures side(Material off) { return side(off, off); }
        private MachineTextures side(Material off, Material on) {
            return back(off, on).left(off, on).right(off, on);
        }
        private MachineTextures top(Material off) { return top(off, off); }
        private MachineTextures top(Material off, Material on) { return face(TextureSlot.UP, off, on); }
        private MachineTextures bottom(Material off) { return bottom(off, off); }
        private MachineTextures bottom(Material off, Material on) { return face(TextureSlot.DOWN, off, on); }

        private MachineTextures particle(Material texture) {
            this.off.put(TextureSlot.PARTICLE, texture);
            this.on.put(TextureSlot.PARTICLE, texture);
            return this;
        }

        private MachineTextures face(TextureSlot slot, Material off, Material on) {
            this.off.put(slot, off);
            this.on.put(slot, on);
            return this;
        }

        private boolean hasActiveTextures() {
            return !this.off.equals(this.on);
        }

        private TextureMapping toMapping(boolean active) {
            Map<TextureSlot, Material> source = active ? this.on : this.off;
            TextureMapping mapping = new TextureMapping();
            source.forEach(mapping::put);
            if (!source.containsKey(TextureSlot.PARTICLE)) {
                mapping.put(TextureSlot.PARTICLE, Objects.requireNonNull(this.off.get(TextureSlot.NORTH)));
            }
            return mapping;
        }

        private void validate() {
            for (TextureSlot slot : List.of(
                    TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.WEST,
                    TextureSlot.EAST, TextureSlot.UP, TextureSlot.DOWN
            )) {
                Objects.requireNonNull(this.off.get(slot), "Missing texture for slot: " + slot.getId());
            }
        }
    }

    private static Material tex(String path) {
        return new Material(ICRE.makeId("block/machine/" + path));
    }
}
