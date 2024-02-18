package fuzs.hoppergadgetry.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

public class HopperBehavior<T extends HopperBlockEntity> {

    public void pushItemsTick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        --blockEntity.cooldownTime;
        blockEntity.tickedGameTime = level.getGameTime();
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);
            HopperBlockEntity.tryMoveItems(level, pos, state, blockEntity, () -> {
                return this.suckInItems(level, blockEntity);
            });
        }
    }

    public boolean suckInItems(Level level, Hopper hopper) {
        Container container = HopperBlockEntity.getSourceContainer(level, hopper);
        if (container != null) {
            Direction direction = Direction.DOWN;
            return !HopperBlockEntity.isEmptyContainer(container, direction) && HopperBlockEntity.getSlots(container,
                    direction
            ).anyMatch((slot) -> {
                ItemStack itemStack = container.getItem(slot);
                return hopper.canPlaceItem(0, itemStack) && HopperBlockEntity.tryTakeInItemFromSlot(hopper,
                        container,
                        slot,
                        direction
                );
            });
        } else {
            for (ItemEntity itemEntity : HopperBlockEntity.getItemsAtAndAbove(level, hopper)) {
                ItemStack itemStack = itemEntity.getItem();
                if (hopper.canPlaceItem(0, itemStack) && HopperBlockEntity.addItem(hopper, itemEntity)) {
                    return true;
                }
            }

            return false;
        }
    }

    public void entityInside(Level level, BlockPos pos, BlockState state, Entity entity, T blockEntity) {
        if (entity instanceof ItemEntity itemEntity) {
            if (!itemEntity.getItem().isEmpty() && Shapes.joinIsNotEmpty(Shapes.create(entity.getBoundingBox()
                    .move(-pos.getX(), -pos.getY(), -pos.getZ())), blockEntity.getSuckShape(), BooleanOp.AND)) {
                HopperBlockEntity.tryMoveItems(level, pos, state, blockEntity, () -> {
                    ItemStack itemStack = itemEntity.getItem();
                    return blockEntity.canPlaceItem(0, itemStack) && HopperBlockEntity.addItem(blockEntity, itemEntity);
                });
            }
        }
    }
}
