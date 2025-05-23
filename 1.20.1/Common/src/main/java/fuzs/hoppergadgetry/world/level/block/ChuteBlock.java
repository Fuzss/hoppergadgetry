package fuzs.hoppergadgetry.world.level.block;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.level.block.entity.ChuteBlockEntity;
import fuzs.puzzleslib.api.block.v1.TickingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ChuteBlock extends BaseEntityBlock implements TickingEntityBlock {
    //    public static final MapCodec<ChuteBlock> CODEC = simpleCodec(ChuteBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;
    public static final VoxelShape TOP = Block.box(0.0, 10.0, 0.0, 16.0, 16.0, 16.0);
    public static final VoxelShape FUNNEL = Block.box(4.0, 0.0, 4.0, 12.0, 10.0, 12.0);
    public static final VoxelShape SHAPE = Shapes.join(Shapes.or(FUNNEL, TOP), Hopper.INSIDE, BooleanOp.ONLY_FIRST);

    public ChuteBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN));
    }

//    @Override
//    public MapCodec<? extends BaseEntityBlock> codec() {
//        return CODEC;
//    }

    @Override
    public BlockEntityType<? extends ChuteBlockEntity> getBlockEntityType() {
        return ModRegistry.CHUTE_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Hopper.INSIDE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            if (level.getBlockEntity(pos) instanceof ChuteBlockEntity blockEntity) {
                blockEntity.setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.getBlockEntity(pos) instanceof ChuteBlockEntity blockEntity) {
            ChuteBlockEntity.entityInside(level, pos, state, entity, blockEntity);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.getBlockEntityType().create(pos, state);
    }
}
