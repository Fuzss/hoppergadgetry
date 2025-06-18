package fuzs.hoppergadgetry.world.level.block.entity;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.level.block.ChuteBlock;
import fuzs.puzzleslib.api.block.v1.entity.TickingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
        // do some wacky stuff so this cannot hold any items
        // main purpose is to still extend hopper to be compatible with all the cooldown shenanigans some mods might be doing
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
        pushItemsTick(this.getLevel(), this.getBlockPos(), this.getBlockState(), this);
    }

    public static void pushItemsTick(Level level, BlockPos blockPos, BlockState blockState, HopperBlockEntity blockEntity) {
        --blockEntity.cooldownTime;
        blockEntity.tickedGameTime = level.getGameTime();
        if (!blockEntity.isOnCooldown()) {
            Container container = getAttachedContainerWithSpace(level,
                    blockPos,
                    blockState.getValue(ChuteBlock.FACING));
            if (suckInItems(level, blockEntity, container)) {
                blockEntity.setCooldown(8);
                blockEntity.setChanged();
            }
        }
    }

    public static boolean suckInItems(Level level, Hopper hopper, @Nullable Container container) {
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
    public boolean tryLoadLootTable(ValueInput valueInput) {
        return true;
    }

    @Override
    public boolean trySaveLootTable(ValueOutput valueOutput) {
        return true;
    }

    @Override
    public void unpackLootTable(@Nullable Player player) {
        // NO-OP
    }

    public static void entityInside(Level level, BlockPos blockPos, BlockState blockState, Entity entity, HopperBlockEntity blockEntity) {
        if (entity instanceof ItemEntity itemEntity && !itemEntity.getItem().isEmpty()) {
            Container container = getAttachedContainerWithSpace(level,
                    blockPos,
                    blockState.getValue(ChuteBlock.FACING));
            if (container != null && entity.getBoundingBox()
                    .move(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ())
                    .intersects(blockEntity.getSuckAabb())) {
                addItem(container, itemEntity);
            }
        }
    }
}
