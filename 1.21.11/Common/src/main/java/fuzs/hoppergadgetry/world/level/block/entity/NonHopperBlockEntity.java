package fuzs.hoppergadgetry.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * A helper class for bypasses vanilla expecting the block state passed in the constructor to have the hopper block
 * state facing property.
 */
public abstract class NonHopperBlockEntity extends HopperBlockEntity {

    public NonHopperBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockPos, Blocks.HOPPER.defaultBlockState());
        this.type = blockEntityType;
        this.setBlockState(blockState);
    }

    @Override
    public final BlockEntityType<?> getType() {
        // change this via the field, as it is called in the constructor and must match with the vanilla hopper
        // especially for compatibility with the Lithium mod,
        // which otherwise stops implementations extending this class from successfully pushing items into other inventories
        return super.getType();
    }

    @Override
    public final boolean isValidBlockState(BlockState blockState) {
        return super.isValidBlockState(blockState) || blockState == Blocks.HOPPER.defaultBlockState();
    }

    @Override
    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
        this.facing = blockState.getValue(this.getFacingProperty());
    }

    protected abstract EnumProperty<Direction> getFacingProperty();
}
