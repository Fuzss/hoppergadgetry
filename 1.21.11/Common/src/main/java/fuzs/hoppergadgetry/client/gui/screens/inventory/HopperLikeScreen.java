package fuzs.hoppergadgetry.client.gui.screens.inventory;

import fuzs.hoppergadgetry.HopperGadgetry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class HopperLikeScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public static final Identifier GRATED_HOPPER_LOCATION = HopperGadgetry.id(
            "textures/gui/container/grated_hopper.png");
    public static final Identifier DUCT_LOCATION = HopperGadgetry.id("textures/gui/container/duct.png");
    public static final int GRATED_HOPPER_IMAGE_HEIGHT = 164;
    public static final int DUCT_IMAGE_HEIGHT = 133;

    private final Identifier identifier;

    public HopperLikeScreen(Identifier identifier, int imageHeight, T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.identifier = identifier;
        this.imageHeight = imageHeight;
        this.inventoryLabelY = imageHeight - 94;
    }

    public static <T extends AbstractContainerMenu> MenuScreens.ScreenConstructor<T, HopperLikeScreen<T>> create(Identifier identifier, int imageHeight) {
        return (T abstractContainerMenu, Inventory inventory, Component component) -> {
            return new HopperLikeScreen<>(identifier, imageHeight, abstractContainerMenu, inventory, component);
        };
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                this.identifier,
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                256,
                256);
    }
}
