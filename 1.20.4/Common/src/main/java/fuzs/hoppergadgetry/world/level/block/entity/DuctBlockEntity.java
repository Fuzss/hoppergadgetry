package fuzs.hoppergadgetry.world.level.block.entity;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.block.v1.entity.TickingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DuctBlockEntity extends HopperBlockEntity implements TickingBlockEntity {

    public DuctBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModRegistry.DUCT_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    public void serverTick() {
        TickingBlockEntity.super.serverTick();
    }
}
