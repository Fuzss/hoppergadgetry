package fuzs.hoppergadgetry.world.level.block;

import com.mojang.serialization.MapCodec;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.level.block.entity.GratedHopperBlockEntity;
import fuzs.puzzleslib.api.block.v1.TickingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GratedHopperBlock extends HopperBlock implements TickingEntityBlock {
//    public static final MapCodec<HopperBlock> CODEC = simpleCodec(GratedHopperBlock::new);

    public GratedHopperBlock(Properties properties) {
        super(properties);
    }

//    @Override
//    public MapCodec<HopperBlock> codec() {
//        return CODEC;
//    }

    @Override
    public BlockEntityType<? extends GratedHopperBlockEntity> getBlockEntityType() {
        return ModRegistry.GRATED_HOPPER_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.getBlockEntityType().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return TickingEntityBlock.super.getTicker(level, state, blockEntityType);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof GratedHopperBlockEntity blockEntity) {
                Containers.dropContents(level, pos, blockEntity.getFilterContainer());
            }

        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.getBlockEntity(pos) instanceof GratedHopperBlockEntity blockEntity) {
            GratedHopperBlockEntity.entityInside(level, pos, state, entity, blockEntity);
        }
    }
}
