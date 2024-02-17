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
    private final Container hopper;

    public GratedHopperMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(5), new SimpleContainer(1));
    }

    public GratedHopperMenu(int containerId, Inventory inventory, Container container, Container filterContainer) {
        super(ModRegistry.GRATED_HOPPER_MENU_TYPE.value(), containerId);
        this.hopper = container;
        checkContainerSize(container, 5);
        container.startOpen(inventory.player);
        this.addContainerSlots(container, filterContainer);
        this.addInventorySlots(inventory, 51);
    }

    private void addContainerSlots(Container container, Container filterContainer) {
        for (int i = 0; i < 5; ++i) {
            this.addSlot(new Slot(container, i, 44 + 18 + i * 18, 20));
        }
        this.addSlot(new Slot(filterContainer, 0, 44 - 18, 20));
    }

    private void addInventorySlots(Inventory inventory, int offsetY) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, i * 18 + offsetY));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 3 * 18 + offsetY + 4));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < this.hopper.getContainerSize()) {
                if (!this.moveItemStackTo(itemStack2, this.hopper.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 0, this.hopper.getContainerSize(), false)) {
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
