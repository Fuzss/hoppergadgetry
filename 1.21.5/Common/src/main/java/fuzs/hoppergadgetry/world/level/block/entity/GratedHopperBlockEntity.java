package fuzs.hoppergadgetry.world.level.block.entity;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.inventory.GratedHopperMenu;
import fuzs.puzzleslib.api.block.v1.entity.TickingBlockEntity;
import fuzs.puzzleslib.api.container.v1.ContainerMenuHelper;
import fuzs.puzzleslib.api.container.v1.ContainerSerializationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
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

public class GratedHopperBlockEntity extends HopperBlockEntity implements TickingBlockEntity {
    public static final Component COMPONENT_GRATED_HOPPER = Component.translatable("container.grated_hopper");
    public static final String TAG_FILTER = HopperGadgetry.id("filter").toString();

    private final NonNullList<ItemStack> filterItems = NonNullList.withSize(GratedHopperMenu.FILTER_CONTAINER_SIZE,
            ItemStack.EMPTY);

    public GratedHopperBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModRegistry.GRATED_HOPPER_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.filterItems.clear();
        ContainerSerializationHelper.loadAllItems(TAG_FILTER, tag, this.filterItems, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerSerializationHelper.saveAllItems(TAG_FILTER, tag, this.filterItems, registries);
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
        boolean isEmpty = true;
        for (ItemStack filter : this.filterItems) {
            if (!filter.isEmpty()) {
                if (ItemStack.isSameItemSameComponents(filter, stack)) {
                    return true;
                } else {
                    isEmpty = false;
                }
            }
        }

        return isEmpty;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (this.hasLevel()) {
            Containers.dropContents(this.getLevel(), pos, this.filterItems);
        }
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
        BlockPos blockPos = BlockPos.containing(hopper.getLevelX(), hopper.getLevelY() + 1.0, hopper.getLevelZ());
        BlockState blockState = level.getBlockState(blockPos);
        Container container = HopperBlockEntity.getSourceContainer(level, hopper, blockPos, blockState);
        if (container != null) {
            Direction direction = Direction.DOWN;

            for (int slot : getSlots(container, direction)) {
                ItemStack itemStack = container.getItem(slot);
                if (hopper.canPlaceItem(0, itemStack) && tryTakeInItemFromSlot(hopper, container, slot, direction)) {
                    return true;
                }
            }

            return false;
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
        return ContainerMenuHelper.createListBackedContainer(this.filterItems, this);
    }

    public static void entityInside(Level level, BlockPos blockPos, BlockState blockState, Entity entity, HopperBlockEntity blockEntity) {
        if (entity instanceof ItemEntity itemEntity) {
            if (!itemEntity.getItem().isEmpty() && entity.getBoundingBox()
                    .move(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ())
                    .intersects(blockEntity.getSuckAabb())) {
                tryMoveItems(level, blockPos, blockState, blockEntity, () -> {
                    ItemStack itemStack = itemEntity.getItem();
                    return blockEntity.canPlaceItem(0, itemStack) && addItem(blockEntity, itemEntity);
                });
            }
        }
    }
}
