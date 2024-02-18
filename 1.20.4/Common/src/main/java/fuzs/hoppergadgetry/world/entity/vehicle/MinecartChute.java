package fuzs.hoppergadgetry.world.entity.vehicle;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.level.block.entity.ChuteBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MinecartChute extends MinecartHopper {

    public MinecartChute(Level level, double x, double y, double z) {
        this(ModRegistry.CHUTE_MINECART_ENTITY_TYPE.value(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    public MinecartChute(EntityType<? extends MinecartHopper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Type getMinecartType() {
        return ModRegistry.CHUTE_MINECART_TYPE;
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return ModRegistry.CHUTE_BLOCK.value().defaultBlockState();
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {
        // NO-OP
    }

    @Override
    public void addChestVehicleSaveData(CompoundTag tag) {
        // NO-OP
    }

    @Override
    public void readChestVehicleSaveData(CompoundTag tag) {
        // NO-OP
    }

    @Override
    public InteractionResult interactWithContainerVehicle(Player player) {
        return InteractionResult.PASS;
    }

    @Override
    public NonNullList<ItemStack> getItemStacks() {
        return NonNullList.create();
    }

    @Override
    public boolean suckInItems() {
        BlockPos blockPos = BlockPos.containing(this.getLevelX(), this.getLevelY(), this.getLevelZ());
        Container container = ChuteBlockEntity.getAttachedContainerWithSpace(this.level(),
                blockPos,
                Direction.DOWN
        );
        if (container == null) {
            return false;
        } else if (ChuteBlockEntity.suckInItems(this.level(), this, container)) {
            return true;
        } else {
            for (ItemEntity itemEntity : this.level().getEntitiesOfClass(ItemEntity.class,
                    this.getBoundingBox().inflate(0.25, 0.0, 0.25),
                    EntitySelector.ENTITY_STILL_ALIVE
            )) {
                if (HopperBlockEntity.addItem(container, itemEntity)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return null;
    }

    @Override
    protected Item getDropItem() {
        return ModRegistry.CHUTE_MINECART_ITEM.value();
    }

    @Override
    public ItemStack getPickResult() {
        return ModRegistry.CHUTE_MINECART_ITEM.value().getDefaultInstance();
    }
}
