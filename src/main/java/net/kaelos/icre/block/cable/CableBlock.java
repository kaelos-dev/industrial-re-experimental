package net.kaelos.icre.block.cable;

import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.entity.block.electric.CableBlockEntity;
import net.kaelos.icre.init.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class CableBlock extends Block implements EntityBlock {
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;

    private final VoltageTier tier;
    private final boolean insulated;
    private final int radius;

    private final VoxelShape shapeCenter;
    private final Map<Direction, VoxelShape> armShapes = new EnumMap<>(Direction.class);

    public CableBlock(Properties properties, VoltageTier tier, boolean insulated, int radius) {
        super(properties);
        this.tier = tier;
        this.insulated = insulated;
        this.radius = radius;

        BlockState defaultState = this.getStateDefinition().any();
        for (BooleanProperty property : PROPERTY_BY_DIRECTION.values()) {
            defaultState = defaultState.setValue(property, false);
        }
        this.registerDefaultState(defaultState);

        double min = 8.0 - radius;
        double max = 8.0 + radius;

        this.shapeCenter = Block.box(min, min, min, max, max, max);
        this.armShapes.put(Direction.UP, Block.box(min, max, min, max, 16.0, max));
        this.armShapes.put(Direction.DOWN, Block.box(min, 0.0, min, max, min, max));
        this.armShapes.put(Direction.NORTH, Block.box(min, min, 0.0, max, max, min));
        this.armShapes.put(Direction.SOUTH, Block.box(min, min, max, max, max, 16.0));
        this.armShapes.put(Direction.WEST, Block.box(0.0, min, min, min, max, max));
        this.armShapes.put(Direction.EAST, Block.box(max, min, min, 16.0, max, max));
    }

    public VoltageTier getTier() {
        return this.tier;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    protected void entityInside(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Entity entity, @NonNull InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (!level.isClientSide()
                && !this.insulated
                && entity instanceof LivingEntity livingEntity
                && level.getBlockEntity(pos) instanceof CableBlockEntity cableBE
                && cableBE.isElectrified()) {
            float damage = (float) this.tier.getMaxVoltage() / 32.0F;
            livingEntity.hurt(level.damageSources().lightningBolt(), damage);
        }

        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NonNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        PROPERTY_BY_DIRECTION.values().forEach(builder::add);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NonNull BlockPlaceContext context) {
        return updateConnections(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        VoxelShape shape = this.shapeCenter;
        for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
            if (state.getValue(entry.getValue())) {
                shape = Shapes.or(shape, this.armShapes.get(entry.getKey()));
            }
        }

        return shape;
    }

    @Override
    protected @NonNull BlockState updateShape(@NonNull BlockState state, @NonNull LevelReader level, @NonNull ScheduledTickAccess ticks, @NonNull BlockPos pos, @NonNull Direction direction, @NonNull BlockPos neighbourPos, @NonNull BlockState neighbourState, @NonNull RandomSource random) {
        if (level instanceof Level realLevel) {
            BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
            if (property != null) {
                return state.setValue(property, canConnect(realLevel, pos, direction));
            }
        }

        return super.updateShape(state, level, ticks, pos, direction, neighbourPos, neighbourState, random);
    }

    private BlockState updateConnections(BlockState state, Level level, BlockPos pos) {
        for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
            state = state.setValue(entry.getValue(), canConnect(level, pos, entry.getKey()));
        }

        return state;
    }

    private boolean canConnect(Level level, BlockPos pos, Direction direction) {
        BlockPos neighborPos = pos.relative(direction);
        BlockState neighborState = level.getBlockState(neighborPos);
        if (neighborState.getBlock() instanceof CableBlock) {
            return true;
        }

        return level.getCapability(ModCapabilities.EU_CAPABILITY, neighborPos, direction.getOpposite()) != null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new CableBlockEntity(pos, state);
    }
}
