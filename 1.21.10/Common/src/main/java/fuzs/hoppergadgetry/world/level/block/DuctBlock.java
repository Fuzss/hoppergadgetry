package fuzs.hoppergadgetry.world.level.block;

import com.mojang.serialization.MapCodec;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.level.block.entity.DuctBlockEntity;
import fuzs.puzzleslib.api.block.v1.entity.TickingEntityBlock;
import fuzs.puzzleslib.api.util.v1.ShapesHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DuctBlock extends BaseEntityBlock implements TickingEntityBlock<DuctBlockEntity> {
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final MapCodec<DuctBlock> CODEC = simpleCodec(DuctBlock::new);
    private static final VoxelShape SHAPE = Block.box(4.0, 4.0, 4.0, 12.0, 12.0, 12.0);
    private static final VoxelShape OUTPUT_SHAPE = Block.box(6.0, 12.0, 6.0, 10.0, 16.0, 10.0);
    private static final VoxelShape INPUT_SHAPE = Block.box(5.0, 12.0, 5.0, 11.0, 16.0, 11.0);
    private static final Map<Direction, VoxelShape> DIRECTIONAL_OUTPUT_SHAPES = ShapesHelper.rotate(OUTPUT_SHAPE);
    private static final Map<Direction, VoxelShape> DIRECTIONAL_INPUT_SHAPES = ShapesHelper.rotate(INPUT_SHAPE);
    private static final VoxelShape[] SHAPES = makeVoxelShapes();
    private static final Property<?>[] FACING_PROPERTIES = new Property[]{
            // order matters, check hopper facing first
            BlockStateProperties.FACING_HOPPER, BlockStateProperties.FACING, BlockStateProperties.HORIZONTAL_FACING
    };

    public DuctBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.DOWN)
                .setValue(ENABLED, Boolean.TRUE)
                .setValue(NORTH, Boolean.FALSE)
                .setValue(EAST, Boolean.FALSE)
                .setValue(SOUTH, Boolean.FALSE)
                .setValue(WEST, Boolean.FALSE)
                .setValue(UP, Boolean.FALSE)
                .setValue(DOWN, Boolean.FALSE));
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntityType<? extends DuctBlockEntity> getBlockEntityType() {
        return ModRegistry.DUCT_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    public boolean isPathfindable(BlockState blockState, PathComputationType type) {
        return false;
    }

    @Override
    protected BlockState updateShape(BlockState blockState, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        blockState = super.updateShape(blockState,
                level,
                scheduledTickAccess,
                blockPos,
                direction,
                neighborPos,
                neighborState,
                random);
        return blockState.setValue(PROPERTY_BY_DIRECTION.get(direction),
                this.canConnect(neighborState, direction.getOpposite()));
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        this.checkPoweredState(level, blockPos, blockState);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(blockState.getBlock())) {
            this.checkPoweredState(level, blockPos, blockState);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(blockState, serverLevel, blockPos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            if (level.getBlockEntity(blockPos) instanceof HopperBlockEntity blockEntity) {
                player.openMenu(blockEntity);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        VoxelShape voxelShape = SHAPES[this.getAABBIndex(blockState)];
        return voxelShape != null ? voxelShape : super.getShape(blockState, level, blockPos, context);
    }

    protected int getAABBIndex(BlockState blockState) {
        int index = 0;

        for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
            if (blockState.getValue(entry.getValue())) {
                index |= 1 << entry.getKey().get3DDataValue();
            }
        }

        index |= 1 << (blockState.getValue(FACING).get3DDataValue() + PROPERTY_BY_DIRECTION.size());

        return index;
    }

    private void checkPoweredState(Level level, BlockPos blockPos, BlockState blockState) {
        boolean bl = !level.hasNeighborSignal(blockPos);
        if (bl != blockState.getValue(ENABLED)) {
            level.setBlock(blockPos, blockState.setValue(ENABLED, bl), 2);
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace().getOpposite();
        BlockState blockState = this.defaultBlockState().setValue(FACING, direction).setValue(ENABLED, Boolean.TRUE);
        for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
            BlockState neighborBlockState = context.getLevel()
                    .getBlockState(context.getClickedPos().relative(entry.getKey()));
            if (this.canConnect(neighborBlockState, entry.getKey().getOpposite())) {
                blockState = blockState.setValue(entry.getValue(), Boolean.TRUE);
            }
        }
        return blockState;
    }

    private boolean canConnect(BlockState neighborBlockState, Direction direction) {
        if (neighborBlockState.is(ModRegistry.DUCT_INPUTS_BLOCK_TAG)) {
            for (Property<?> property : FACING_PROPERTIES) {
                if (neighborBlockState.hasProperty(property)) {
                    return neighborBlockState.getValue(property) == direction;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED, NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    private static VoxelShape[] makeVoxelShapes() {
        VoxelShape[] voxelShapes = new VoxelShape[4096];
        voxelShapes[0] = SHAPE;

        for (int i = 0; i < voxelShapes.length; i++) {

            int x = i >> PROPERTY_BY_DIRECTION.size();
            // don't generate shapes that will never be used
            // https://stackoverflow.com/questions/600293/how-to-check-if-a-number-is-a-power-of-2
            if (x != 0 && (x & (x - 1)) == 0) {
                for (Direction outputDirection : PROPERTY_BY_DIRECTION.keySet()) {

                    if (x == 1 << outputDirection.get3DDataValue()) {

                        VoxelShape voxelShape = Shapes.or(SHAPE, DIRECTIONAL_OUTPUT_SHAPES.get(outputDirection));
                        for (Direction inputDirection : PROPERTY_BY_DIRECTION.keySet()) {

                            if (outputDirection != inputDirection && (i & 1 << inputDirection.get3DDataValue()) != 0) {

                                voxelShape = Shapes.or(voxelShape, DIRECTIONAL_INPUT_SHAPES.get(inputDirection));
                            }
                        }

                        voxelShapes[i] = voxelShape;
                        break;
                    }
                }
            }
        }

        return voxelShapes;
    }
}
