package fuzs.hoppergadgetry.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * A helper class for bypasses vanilla expecting the block state passed in the constructor to have the hopper block
 * state facing property.
 */
public abstract class NonHopperBlockEntity extends HopperBlockEntity {

    public NonHopperBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, Blocks.HOPPER.defaultBlockState());
        this.setBlockState(blockState);
    }

    @Override
    public boolean isValidBlockState(BlockState blockState) {
        return super.isValidBlockState(blockState) || blockState == Blocks.HOPPER.defaultBlockState();
    }

    @Override
    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
        this.facing = blockState.getValue(this.getFacingProperty());
    }

    protected abstract EnumProperty<Direction> getFacingProperty();
}
