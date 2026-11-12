package net.kaelos.icre.block.rubber;

import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.definitions.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class RubberLogBlock extends RubberPillarBlock {
    public static final EnumProperty<Direction> RESIN_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty RESIN = BooleanProperty.create("resin");
    public static final BooleanProperty COLLECTABLE = BooleanProperty.create("collectable");

    private static final int RESIN_REGEN_RANGE = 200;
    private static final int RESIN_RETRY_TICKS = 100;

    public RubberLogBlock(Properties properties) {
        super(properties
                .mapColor(state -> state.getValue(AXIS) == Direction.Axis.Y ? MapColor.PODZOL : MapColor.WOOD)
                .randomTicks());
        this.registerDefaultState(this.defaultBlockState()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(RESIN, false)
                .setValue(COLLECTABLE, false)
                .setValue(RESIN_FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NonNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(RESIN_FACING, RESIN, COLLECTABLE);
    }

    @Override
    public @Nullable BlockState getToolModifiedState(@NonNull BlockState state, @NonNull UseOnContext context, @NonNull ItemAbility itemAbility, boolean simulate) {
        if (itemAbility == ItemAbilities.AXE_STRIP && context.getItemInHand().canPerformAction(itemAbility)) {
            return ModBlocks.STRIPPED_RUBBER_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }

    @Override
    public void destroy(@NonNull LevelAccessor level, @NonNull BlockPos pos, @NonNull BlockState state) {
        super.destroy(level, pos, state);
        if (areChunksLoaded(level, pos.offset(-5, -5, -5), pos.offset(5, 5, 5))) {
            for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-4, -4, -4), pos.offset(4, 4, 4))) {
                if (level.getBlockState(nearbyPos).is(BlockTags.LEAVES)) {
                    Blocks.AIR.defaultBlockState().updateNeighbourShapes(level, nearbyPos, Block.UPDATE_CLIENTS);
                }
            }
        }
    }

    private boolean areChunksLoaded(@NonNull LevelAccessor level, BlockPos minPos, BlockPos maxPos) {
        int minChunkX = SectionPos.blockToSectionCoord(minPos.getX());
        int maxChunkX = SectionPos.blockToSectionCoord(maxPos.getX());
        int minChunkZ = SectionPos.blockToSectionCoord(minPos.getZ());
        int maxChunkZ = SectionPos.blockToSectionCoord(maxPos.getZ());

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                if (!level.hasChunk(cx, cz)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected boolean isRandomlyTicking(@NonNull BlockState state) {
        return state.getValue(RESIN) && !state.getValue(COLLECTABLE);
    }

    @Override
    protected void randomTick(@NonNull BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        this.regenerateResin(state, level, pos, random);
    }

    @Override
    protected void tick(@NonNull BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        this.regenerateResin(state, level, pos, random);
    }

    private void regenerateResin(@NonNull BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        if (state.getValue(RESIN) && !state.getValue(COLLECTABLE)) {
            int range = RESIN_REGEN_RANGE;
            if (random.nextInt(range) == 0) {
                level.setBlockAndUpdate(pos, state.setValue(COLLECTABLE, true));
            } else {
                level.scheduleTick(pos, this, range - RESIN_RETRY_TICKS);
            }
        }
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(@NonNull BlockState state) {
        return state.getValue(RESIN) ? PushReaction.DESTROY : super.getPistonPushReaction(state);
    }

    @Override
    protected @NonNull BlockState rotate(@NonNull BlockState state, @NonNull Rotation rotation) {
        return super.rotate(state, rotation).setValue(RESIN_FACING, rotation.rotate(state.getValue(RESIN_FACING)));
    }

    @Override
    protected @NonNull BlockState mirror(@NonNull BlockState state, @NonNull Mirror mirror) {
        return state.setValue(RESIN_FACING, mirror.mirror(state.getValue(RESIN_FACING)));
    }

    public static boolean hasResinSpotOnFace(BlockState state, Direction side) {
        return state.getBlock() instanceof RubberLogBlock
                && state.getValue(AXIS) == Direction.Axis.Y
                && state.getValue(RESIN)
                && state.getValue(RESIN_FACING) == side;
    }

    public static boolean extractResin(BlockState state, Level level, BlockPos pos, Direction side, ItemStack tool) {
        if (!hasResinSpotOnFace(state, side)) {
            return false;
        }

        if (level.isClientSide()) {
            return true;
        }

        RandomSource random = level.getRandom();
        if (state.getValue(COLLECTABLE)) {
            level.setBlockAndUpdate(pos, state.setValue(COLLECTABLE, false));
            popResin(level, pos, side, tool, random);
        } else {
            if (random.nextInt(5) == 0) {
                level.setBlockAndUpdate(pos, state.setValue(RESIN, false));
            }

            if (random.nextInt(5) == 0) {
                popResin(level, pos, side, tool, random);
            }
        }

        return true;
    }

    private static void popResin(Level level, BlockPos pos, Direction side, ItemStack tool, RandomSource random) {
        int fortune = 0;
        var enchantment = level.registryAccess().lookup(Registries.ENCHANTMENT);

        if (enchantment.isPresent()) {
            var fortuneHolder = enchantment.get().get(Enchantments.FORTUNE);
            if (fortuneHolder.isPresent()) {
                fortune = tool.getEnchantmentLevel(fortuneHolder.get());
            }
        }

        int amount = random.nextInt(3 + fortune) + 1;
        Block.popResourceFromFace(level, pos, side, new ItemStack(ModItems.LATEX.get(), amount));
    }
}
