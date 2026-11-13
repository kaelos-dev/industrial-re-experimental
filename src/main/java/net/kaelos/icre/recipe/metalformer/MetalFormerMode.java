package net.kaelos.icre.recipe.metalformer;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.kaelos.icre.ICRE;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum MetalFormerMode implements StringRepresentable {
    ROLLING("rolling"),
    CUTTING("cutting"),
    EXTRUDING("extruding");

    public static final Codec<MetalFormerMode> CODEC =
            StringRepresentable.fromEnum(MetalFormerMode::values);
    public static final StreamCodec<ByteBuf, MetalFormerMode> STREAM_CODEC =
            ByteBufCodecs.VAR_INT.map(MetalFormerMode::byId, MetalFormerMode::ordinal);

    private final String name;

    MetalFormerMode(String name) {
        this.name = name;
    }

    @Override
    public @NonNull String getSerializedName() {
        return this.name;
    }

    public Component getDisplayName() {
        return Component.translatable("gui." + ICRE.MOD_ID + ".metal_former.mode." + this.name);
    }

    public MetalFormerMode next() {
        MetalFormerMode[] values = values();
        return values[(this.ordinal() + 1) % values.length];
    }

    public static MetalFormerMode byId(int id) {
        MetalFormerMode[] values = values();
        return id >= 0 && id < values.length ? values[id] : ROLLING;
    }
}
