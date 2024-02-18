package fuzs.hoppergadgetry.world.level.block.entity;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.block.v1.entity.TickingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

public class ChuteBlockEntity extends HopperBlockEntity implements TickingBlockEntity {
    public static final Component COMPONENT_CHUTE = Component.translatable("container.chute");

    public ChuteBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
        super.setItems(NonNullList.create());
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModRegistry.CHUTE_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    protected Component getDefaultName() {
        return COMPONENT_CHUTE;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return NonNullList.create();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemStacks) {
        // NO-OP
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return null;
    }

    @Override
    public void serverTick() {
        --this.cooldownTime;
        this.tickedGameTime = this.getLevel().getGameTime();
        if (!this.isOnCooldown()) {
            if (suckInItems(this.getLevel(), this, this.getBlockPos())) {
                this.setCooldown(8);
                this.setChanged();
            }
        }
    }

    public static boolean suckInItems(Level level, Hopper hopper, BlockPos blockPos) {
        Container container = getAttachedContainerWithSpace(level, blockPos, Direction.DOWN);
        if (container != null) {
            for (ItemEntity itemEntity : getItemsAtAndAbove(level, hopper)) {
                if (addItem(container, itemEntity)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Container getAttachedContainerWithSpace(Level level, BlockPos blockPos, Direction direction) {
        Container container = getContainerAt(level, blockPos.relative(direction));
        return container != null && !isFullContainer(container, direction.getOpposite()) ? container : null;
    }

    @Override
    public boolean canOpen(Player player) {
        return false;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeItem(Container target, int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean tryLoadLootTable(CompoundTag tag) {
        return true;
    }

    @Override
    public boolean trySaveLootTable(CompoundTag tag) {
        return true;
    }

    @Override
    public void unpackLootTable(@Nullable Player player) {
        // NO-OP
    }

    public static void entityInside(Level level, BlockPos pos, BlockState state, Entity entity, HopperBlockEntity blockEntity) {
        if (entity instanceof ItemEntity itemEntity && !itemEntity.getItem().isEmpty()) {
            Container container = getAttachedContainerWithSpace(level, pos, Direction.DOWN);
            if (container != null && Shapes.joinIsNotEmpty(Shapes.create(entity.getBoundingBox()
                    .move(-pos.getX(), -pos.getY(), -pos.getZ())), blockEntity.getSuckShape(), BooleanOp.AND)) {
                addItem(container, itemEntity);
            }
        }
    }
}