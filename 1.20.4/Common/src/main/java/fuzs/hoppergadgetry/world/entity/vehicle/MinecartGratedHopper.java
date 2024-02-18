package fuzs.hoppergadgetry.world.entity.vehicle;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.inventory.GratedHopperMenu;
import fuzs.hoppergadgetry.world.level.block.entity.GratedHopperBlockEntity;
import fuzs.puzzleslib.api.container.v1.ContainerSerializationHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MinecartGratedHopper extends MinecartHopper {
    private final NonNullList<ItemStack> filterItems = NonNullList.withSize(1, ItemStack.EMPTY);

    public MinecartGratedHopper(Level level, double x, double y, double z) {
        this(ModRegistry.GRATED_HOPPER_MINECART_ENTITY_TYPE.value(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    public MinecartGratedHopper(EntityType<? extends MinecartHopper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Type getMinecartType() {
        return ModRegistry.GRATED_HOPPER_MINECART_TYPE;
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
            for (ItemEntity itemEntity : this.level().getEntitiesOfClass(ItemEntity.class,
                    this.getBoundingBox().inflate(0.25, 0.0, 0.25),
                    EntitySelector.ENTITY_STILL_ALIVE
            )) {
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
        return this.filterItems.get(0).isEmpty() || ItemStack.isSameItemSameTags(this.filterItems.get(0), stack);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        ContainerSerializationHelper.saveAllItems(GratedHopperBlockEntity.TAG_FILTER, compound, this.filterItems);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.filterItems.clear();
        ContainerSerializationHelper.loadAllItems(GratedHopperBlockEntity.TAG_FILTER, compound, this.filterItems);
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new GratedHopperMenu(containerId,
                playerInventory,
                this,
                GratedHopperBlockEntity.createListBackedContainer(this.filterItems, this)
        );
    }

    @Override
    public ItemStack getPickResult() {
        return ModRegistry.GRATED_HOPPER_MINECART_ITEM.value().getDefaultInstance();
    }
}
