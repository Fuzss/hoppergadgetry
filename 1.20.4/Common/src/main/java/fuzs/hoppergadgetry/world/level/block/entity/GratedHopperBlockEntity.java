package fuzs.hoppergadgetry.world.level.block.entity;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.inventory.GratedHopperMenu;
import fuzs.puzzleslib.api.block.v1.entity.TickingBlockEntity;
import fuzs.puzzleslib.api.container.v1.ContainerSerializationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
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

public class GratedHopperBlockEntity extends HopperBlockEntity implements TickingBlockEntity {
    public static final Component COMPONENT_GRATED_HOPPER = Component.translatable("container.grated_hopper");
    public static final String TAG_FILTER = HopperGadgetry.id("filter").toString();

    private final NonNullList<ItemStack> filterItems = NonNullList.withSize(1, ItemStack.EMPTY);

    public GratedHopperBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModRegistry.GRATED_HOPPER_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.filterItems.clear();
        ContainerSerializationHelper.loadAllItems(TAG_FILTER, tag, this.filterItems);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerSerializationHelper.saveAllItems(TAG_FILTER, tag, this.filterItems);
    }

    @Override
    protected Component getDefaultName() {
        return COMPONENT_GRATED_HOPPER;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new GratedHopperMenu(containerId, inventory, this, this.getFilterContainer());
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.filterItems.get(0).isEmpty() || ItemStack.isSameItemSameTags(this.filterItems.get(0), stack);
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
            HopperBlockEntity.tryMoveItems(level, pos, state, blockEntity, () -> {
                return suckInItems(level, blockEntity);
            });
        }
    }

    public static boolean suckInItems(Level level, Hopper hopper) {
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

    public Container getFilterContainer() {
        return createListBackedContainer(this.filterItems, this);
    }

    @Deprecated(forRemoval = true)
    public static SimpleContainer createListBackedContainer(NonNullList<ItemStack> items, @Nullable Container listener) {
        SimpleContainer simpleContainer = new SimpleContainer();
        simpleContainer.items = items;
        if (listener != null) {
            simpleContainer.addListener($ -> listener.setChanged());
        }
        return simpleContainer;
    }

    public static void entityInside(Level level, BlockPos pos, BlockState blockState, Entity entity, HopperBlockEntity blockEntity) {
        if (entity instanceof ItemEntity itemEntity) {
            if (!itemEntity.getItem().isEmpty() && Shapes.joinIsNotEmpty(Shapes.create(entity.getBoundingBox()
                    .move(-pos.getX(), -pos.getY(), -pos.getZ())), blockEntity.getSuckShape(), BooleanOp.AND)) {
                tryMoveItems(level, pos, blockState, blockEntity, () -> {
                    ItemStack itemStack = itemEntity.getItem();
                    return blockEntity.canPlaceItem(0, itemStack) && addItem(blockEntity, itemEntity);
                });
            }
        }
    }
}
