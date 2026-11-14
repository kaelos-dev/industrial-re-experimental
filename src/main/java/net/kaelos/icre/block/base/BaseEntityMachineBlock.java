package net.kaelos.icre.block.base;

import net.kaelos.icre.entity.block.base.ITickableBlockEntity;
import net.kaelos.icre.item.tool.electric.BaseEnergyToolItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.BiFunction;

public class BaseEntityMachineBlock<TE extends BlockEntity> extends Block implements EntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    private final BiFunction<BlockPos, BlockState, TE> blockEntityFactory;

    protected BaseEntityMachineBlock(Properties properties, BiFunction<BlockPos, BlockState, TE> blockEntityFactory) {
        super(properties
                .mapColor(MapColor.METAL)
                .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                .strength(3.0F, 6.0F)
                .sound(SoundType.METAL));
        this.blockEntityFactory = blockEntityFactory;
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(getActiveProperty(), false));
    }

    protected BooleanProperty getActiveProperty() {
        return ACTIVE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NonNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, getActiveProperty());
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return this.blockEntityFactory.apply(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }

        return (lvl, pos, st, be) -> {
            if (lvl instanceof ServerLevel serverLevel && be instanceof ITickableBlockEntity tickable) {
                tickable.serverTick(serverLevel, pos, st);
            }
        };
    }

    @Override
    protected void affectNeighborsAfterRemoval(@NonNull BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    @Override
    protected @NonNull BlockState rotate(@NonNull BlockState state, @NonNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected @NonNull BlockState mirror(@NonNull BlockState state, @NonNull Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    protected @NonNull InteractionResult useItemOn(@NonNull ItemStack stack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hitResult) {
        if (stack.is(Tags.Items.TOOLS_WRENCH)) {
            return tryWrenchRotate(state, level, pos, player, hand, stack);
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    public InteractionResult tryWrenchRotate(BlockState state, Level level, BlockPos pos, @Nullable Player player, InteractionHand hand, ItemStack wrench) {
        if (wrench.getItem() instanceof BaseEnergyToolItem energyTool && !energyTool.canUse(wrench, player)) {
            return InteractionResult.FAIL;
        }

        Direction current = state.getValue(FACING);
        Direction next = player != null && player.isShiftKeyDown()
                ? current.getCounterClockWise()
                : current.getClockWise();

        if (!level.isClientSide()) {
            if (wrench.getItem() instanceof BaseEnergyToolItem energyTool) {
                energyTool.tryConsumeUseEnergy(wrench, player);
            } else if (player != null) {
                wrench.hurtAndBreak(1, player, hand.asEquipmentSlot());
            }

            level.setBlockAndUpdate(pos, state.setValue(FACING, next));
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0F, 1.2F);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof MenuProvider provider) {
            player.openMenu(provider, pos);
        }

        return InteractionResult.SUCCESS;
    }
}
