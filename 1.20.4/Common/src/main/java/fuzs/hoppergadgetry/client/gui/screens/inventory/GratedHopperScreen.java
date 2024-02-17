package fuzs.hoppergadgetry.client.gui.screens.inventory;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.world.inventory.GratedHopperMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GratedHopperScreen extends AbstractContainerScreen<GratedHopperMenu> {
    private static final ResourceLocation HOPPER_LOCATION = HopperGadgetry.id("textures/gui/container/grated_hopper.png");

    public GratedHopperScreen(GratedHopperMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 133;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(HOPPER_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
