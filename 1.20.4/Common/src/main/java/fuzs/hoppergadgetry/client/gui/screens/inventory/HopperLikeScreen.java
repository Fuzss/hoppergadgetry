package fuzs.hoppergadgetry.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class HopperLikeScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private final ResourceLocation resourceLocation;

    public HopperLikeScreen(ResourceLocation resourceLocation, T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.resourceLocation = resourceLocation;
        this.imageHeight = 133;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    public static <T extends AbstractContainerMenu> MenuScreens.ScreenConstructor<T, HopperLikeScreen<T>> create(ResourceLocation resourceLocation) {
        return (T abstractContainerMenu, Inventory inventory, Component component) -> {
            return new HopperLikeScreen<>(resourceLocation, abstractContainerMenu, inventory, component);
        };
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(this.resourceLocation, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
