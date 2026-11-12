package net.kaelos.icre.item.tool;

import net.kaelos.icre.block.rubber.RubberLogBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class TreeTapTool extends Item {
    public TreeTapTool(Properties properties) {
        super(properties
                .stacksTo(1)
                .durability(16));
    }

    @Override
    public @NonNull InteractionResult useOn(@NonNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (RubberLogBlock.extractResin(state, level, pos, context.getClickedFace(), context.getItemInHand())) {
            Player player = context.getPlayer();
            if (!level.isClientSide()) {
                level.playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (player != null) {
                    context.getItemInHand().hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
