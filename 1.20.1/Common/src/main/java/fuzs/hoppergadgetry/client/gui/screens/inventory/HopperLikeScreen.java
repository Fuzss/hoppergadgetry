package fuzs.hoppergadgetry.client.gui.screens.inventory;

import fuzs.hoppergadgetry.HopperGadgetry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class HopperLikeScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public static final ResourceLocation GRATED_HOPPER_LOCATION = HopperGadgetry.id(
            "textures/gui/container/grated_hopper.png");
    public static final ResourceLocation DUCT_LOCATION = HopperGadgetry.id("textures/gui/container/duct.png");
    public static final int GRATED_HOPPER_IMAGE_HEIGHT = 164;
    public static final int DUCT_IMAGE_HEIGHT = 133;

    private final ResourceLocation resourceLocation;

    public HopperLikeScreen(ResourceLocation resourceLocation, int imageHeight, T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.resourceLocation = resourceLocation;
        this.imageHeight = imageHeight;
        this.inventoryLabelY = imageHeight - 94;
    }

    public static <T extends AbstractContainerMenu> MenuScreens.ScreenConstructor<T, HopperLikeScreen<T>> create(ResourceLocation resourceLocation, int imageHeight) {
        return (T abstractContainerMenu, Inventory inventory, Component component) -> {
            return new HopperLikeScreen<>(resourceLocation, imageHeight, abstractContainerMenu, inventory, component);
        };
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(this.resourceLocation, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
