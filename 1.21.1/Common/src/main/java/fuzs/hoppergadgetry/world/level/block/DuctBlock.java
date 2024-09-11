package fuzs.hoppergadgetry.world.level.block;

import com.mojang.serialization.MapCodec;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.level.block.entity.DuctBlockEntity;
import fuzs.puzzleslib.api.block.v1.entity.TickingEntityBlock;
import fuzs.puzzleslib.api.shape.v1.ShapesHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class DuctBlock extends BaseEntityBlock implements TickingEntityBlock<DuctBlockEntity> {
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
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
            BlockStateProperties.FACING_HOPPER,
            BlockStateProperties.FACING,
            BlockStateProperties.HORIZONTAL_FACING
    };

    public DuctBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN).setValue(ENABLED,
                Boolean.TRUE
        ).setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST,
                Boolean.FALSE
        ).setValue(UP, Boolean.FALSE).setValue(DOWN, Boolean.FALSE));
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntityType<? extends DuctBlockEntity> getBlockEntityType() {
        return ModRegistry.DUCT_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        BlockState blockState = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        return blockState.setValue(PROPERTY_BY_DIRECTION.get(direction),
                this.canConnect(neighborState, direction.getOpposite())
        );
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        this.checkPoweredState(level, pos, state);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(state.getBlock())) {
            this.checkPoweredState(level, pos, state);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (level.getBlockEntity(pos) instanceof HopperBlockEntity blockEntity) {
                player.openMenu(blockEntity);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape voxelShape = SHAPES[this.getAABBIndex(state)];
        return voxelShape != null ? voxelShape : super.getShape(state, level, pos, context);
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

    private void checkPoweredState(Level level, BlockPos pos, BlockState state) {
        boolean bl = !level.hasNeighborSignal(pos);
        if (bl != state.getValue(ENABLED)) {
            level.setBlock(pos, state.setValue(ENABLED, bl), 2);
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace().getOpposite();
        BlockState blockState = this.defaultBlockState().setValue(FACING, direction).setValue(ENABLED, Boolean.TRUE);
        for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
            BlockState neighborBlockState = context.getLevel().getBlockState(context.getClickedPos()
                    .relative(entry.getKey()));
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
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            if (level.getBlockEntity(pos) instanceof HopperBlockEntity blockEntity) {
                blockEntity.setCustomName(stack.getHoverName());
            }
        }
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

                            if ((i & 1 << inputDirection.get3DDataValue()) != 0) {

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
