package fuzs.hoppergadgetry.world.level.block.entity;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.inventory.DuctMenu;
import fuzs.hoppergadgetry.world.level.block.DuctBlock;
import fuzs.puzzleslib.api.block.v1.entity.TickingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class DuctBlockEntity extends NonHopperBlockEntity implements TickingBlockEntity {
    public static final Component COMPONENT_DUCT = Component.translatable("container.duct");

    public DuctBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
        this.type = ModRegistry.DUCT_BLOCK_ENTITY_TYPE.value();
        this.setItems(NonNullList.withSize(1, ItemStack.EMPTY));
    }

    @Override
    protected EnumProperty<Direction> getFacingProperty() {
        return DuctBlock.FACING;
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModRegistry.DUCT_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    protected Component getDefaultName() {
        return COMPONENT_DUCT;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new DuctMenu(containerId, inventory, this);
    }

    @Override
    public void serverTick() {
        pushItemsTick(this.getLevel(), this.getBlockPos(), this.getBlockState(), this);
    }

    public static void pushItemsTick(Level level, BlockPos pos, BlockState state, HopperBlockEntity blockEntity) {
        --blockEntity.cooldownTime;
        blockEntity.tickedGameTime = level.getGameTime();
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);
            tryMoveItems(level, pos, state, blockEntity);
        }
    }

    public static boolean tryMoveItems(Level level, BlockPos pos, BlockState state, HopperBlockEntity blockEntity) {
        if (!blockEntity.isOnCooldown() && state.getValue(HopperBlock.ENABLED)) {
            boolean bl = false;
            if (!blockEntity.isEmpty()) {
                bl = ejectItems(level, pos, state, blockEntity);
            }

            if (bl) {
                blockEntity.setCooldown(8);
                setChanged(level, pos, state);
                return true;
            }
        }

        return false;
    }

    private static boolean ejectItems(Level level, BlockPos pos, BlockState state, Container sourceContainer) {
        Container container = getAttachedContainer(level, pos, state);
        if (container == null) {
            return false;
        } else {
            Direction direction = state.getValue(DuctBlock.FACING).getOpposite();
            if (isFullContainer(container, direction)) {
                return false;
            } else {
                for (int i = 0; i < sourceContainer.getContainerSize(); ++i) {
                    if (!sourceContainer.getItem(i).isEmpty()) {
                        ItemStack itemStack = sourceContainer.getItem(i).copy();
                        ItemStack itemStack2 = addItem(sourceContainer,
                                container,
                                sourceContainer.removeItem(i, 1),
                                direction
                        );
                        if (itemStack2.isEmpty()) {
                            container.setChanged();
                            return true;
                        }

                        sourceContainer.setItem(i, itemStack);
                    }
                }

                return false;
            }
        }
    }

    @Nullable
    private static Container getAttachedContainer(Level level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(DuctBlock.FACING);
        return getContainerAt(level, blockPos.relative(direction));
    }
}
