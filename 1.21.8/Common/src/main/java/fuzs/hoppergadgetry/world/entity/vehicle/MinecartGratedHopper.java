package fuzs.hoppergadgetry.world.entity.vehicle;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.util.ContainerSerializationHelper;
import fuzs.hoppergadgetry.world.inventory.GratedHopperMenu;
import fuzs.hoppergadgetry.world.level.block.entity.GratedHopperBlockEntity;
import fuzs.puzzleslib.api.container.v1.ContainerMenuHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class MinecartGratedHopper extends MinecartHopper {
    private final NonNullList<ItemStack> filterItems = NonNullList.withSize(GratedHopperMenu.FILTER_CONTAINER_SIZE,
            ItemStack.EMPTY);

    public MinecartGratedHopper(EntityType<? extends MinecartHopper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return ModRegistry.GRATED_HOPPER_BLOCK.value().defaultBlockState();
    }

    @Override
    public boolean suckInItems() {
        if (GratedHopperBlockEntity.suckInItems(this.level(), this)) {
            return true;
        } else {
            for (ItemEntity itemEntity : this.level()
                    .getEntitiesOfClass(ItemEntity.class,
                            this.getBoundingBox().inflate(0.25, 0.0, 0.25),
                            EntitySelector.ENTITY_STILL_ALIVE)) {
                ItemStack itemStack = itemEntity.getItem();
                if (this.canPlaceItem(0, itemStack) && HopperBlockEntity.addItem(this, itemEntity)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.filterItems.getFirst().isEmpty() || ItemStack.isSameItemSameComponents(this.filterItems.getFirst(),
                stack);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        ContainerSerializationHelper.storeAsSlots(this.filterItems,
                valueOutput.list(GratedHopperBlockEntity.TAG_FILTER, ItemStackWithSlot.CODEC));
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        ContainerSerializationHelper.fromSlots(this.filterItems,
                valueInput.listOrEmpty(GratedHopperBlockEntity.TAG_FILTER, ItemStackWithSlot.CODEC));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new GratedHopperMenu(containerId,
                playerInventory,
                this,
                ContainerMenuHelper.createListBackedContainer(this.filterItems, this));
    }

    @Override
    protected Item getDropItem() {
        return ModRegistry.GRATED_HOPPER_MINECART_ITEM.value();
    }

    @Override
    public ItemStack getPickResult() {
        return ModRegistry.GRATED_HOPPER_MINECART_ITEM.value().getDefaultInstance();
    }
}
