package fuzs.hoppergadgetry.world.inventory;

import fuzs.hoppergadgetry.init.ModRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GratedHopperMenu extends AbstractContainerMenu {
    public static final int CONTAINER_SIZE = 5;
    public static final int FILTER_CONTAINER_SIZE = 5;

    private final Container hopper;

    public GratedHopperMenu(int containerId, Inventory playerInventory) {
        this(containerId,
                playerInventory,
                new SimpleContainer(CONTAINER_SIZE),
                new SimpleContainer(FILTER_CONTAINER_SIZE));
    }

    public GratedHopperMenu(int containerId, Inventory inventory, Container container, Container filterContainer) {
        super(ModRegistry.GRATED_HOPPER_MENU_TYPE.value(), containerId);
        this.hopper = container;
        checkContainerSize(container, CONTAINER_SIZE);
        checkContainerSize(filterContainer, FILTER_CONTAINER_SIZE);
        container.startOpen(inventory.player);
        this.addContainerSlots(container, filterContainer);
        this.addStandardInventorySlots(inventory, 8, 51 + 31);
    }

    private void addContainerSlots(Container container, Container filterContainer) {
        for (int i = 0; i < container.getContainerSize(); ++i) {
            this.addSlot(new Slot(container, i, 89 - container.getContainerSize() * 18 / 2 + i * 18, 20));
        }
        for (int i = 0; i < filterContainer.getContainerSize(); i++) {
            this.addSlot(new Slot(filterContainer, i, 89 - container.getContainerSize() * 18 / 2 + i * 18, 20 + 31) {
                @Override
                public int getMaxStackSize() {
                    return 1;
                }
            });
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < CONTAINER_SIZE) {
                if (!this.moveItemStackTo(itemStack2,
                        CONTAINER_SIZE + FILTER_CONTAINER_SIZE,
                        this.slots.size(),
                        true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < CONTAINER_SIZE + FILTER_CONTAINER_SIZE) {
                if (!this.moveItemStackTo(itemStack2,
                        CONTAINER_SIZE + FILTER_CONTAINER_SIZE,
                        this.slots.size(),
                        true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 0, CONTAINER_SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.hopper.stopOpen(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.hopper.stillValid(player);
    }
}
