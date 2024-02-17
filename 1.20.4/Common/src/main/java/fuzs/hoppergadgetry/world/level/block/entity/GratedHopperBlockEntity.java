package fuzs.hoppergadgetry.world.level.block.entity;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.inventory.GratedHopperMenu;
import fuzs.puzzleslib.api.block.v1.entity.TickingBlockEntity;
import fuzs.puzzleslib.api.container.v1.ContainerSerializationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GratedHopperBlockEntity extends HopperBlockEntity implements TickingBlockEntity {
    public static final Component COMPONENT_GRATED_HOPPER = Component.translatable("container.grated_hopper");
    public static final String TAG_FILTER = HopperGadgetry.id("filter").toString();

    private final NonNullList<ItemStack> filterItems = NonNullList.withSize(1, ItemStack.EMPTY);
    private final HopperBehavior<GratedHopperBlockEntity> behavior = new HopperBehavior<>();

    public GratedHopperBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    public HopperBehavior<GratedHopperBlockEntity> getBehavior() {
        return this.behavior;
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
        this.behavior.pushItemsTick(this.getLevel(), this.getBlockPos(), this.getBlockState(), this);
    }

    public Container getFilterContainer() {
        SimpleContainer container = new SimpleContainer();
        container.items = this.filterItems;
        container.addListener($ -> this.setChanged());
        return container;
    }
}
