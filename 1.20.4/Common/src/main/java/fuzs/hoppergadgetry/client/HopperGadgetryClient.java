package fuzs.hoppergadgetry.client;

import fuzs.hoppergadgetry.client.gui.screens.inventory.GratedHopperScreen;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.MenuScreensContext;
import fuzs.puzzleslib.api.client.core.v1.context.RenderTypesContext;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class HopperGadgetryClient implements ClientModConstructor {

    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        context.registerMenuScreen(ModRegistry.GRATED_HOPPER_MENU_TYPE.value(), GratedHopperScreen::new);
    }

    @Override
    public void onRegisterBlockRenderTypes(RenderTypesContext<Block> context) {
        context.registerRenderType(RenderType.cutout(), ModRegistry.GRATED_HOPPER_BLOCK.value());
    }
}
