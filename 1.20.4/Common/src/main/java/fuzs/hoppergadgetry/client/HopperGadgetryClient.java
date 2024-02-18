package fuzs.hoppergadgetry.client;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.client.gui.screens.inventory.HopperLikeScreen;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.EntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.core.v1.context.MenuScreensContext;
import fuzs.puzzleslib.api.client.core.v1.context.RenderTypesContext;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class HopperGadgetryClient implements ClientModConstructor {
    static final ModelLayerFactory FACTORY = ModelLayerFactory.from(HopperGadgetry.MOD_ID);
    public static final ModelLayerLocation GRATED_HOPPER_MINECART = FACTORY.register("grated_hopper_minecart");
    public static final ResourceLocation GRATED_HOPPER_LOCATION = HopperGadgetry.id("textures/gui/container/grated_hopper.png");
    public static final ResourceLocation DUCT_LOCATION = HopperGadgetry.id("textures/gui/container/duct.png");

    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        context.registerMenuScreen(ModRegistry.GRATED_HOPPER_MENU_TYPE.value(), HopperLikeScreen.create(GRATED_HOPPER_LOCATION));
        context.registerMenuScreen(ModRegistry.DUCT_MENU_TYPE.value(), HopperLikeScreen.create(DUCT_LOCATION));
    }

    @Override
    public void onRegisterBlockRenderTypes(RenderTypesContext<Block> context) {
        context.registerRenderType(RenderType.cutout(), ModRegistry.GRATED_HOPPER_BLOCK.value());
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(GRATED_HOPPER_MINECART, MinecartModel::createBodyLayer);
    }

    @Override
    public void onRegisterEntityRenderers(EntityRenderersContext context) {
        context.registerEntityRenderer(ModRegistry.GRATED_HOPPER_MINECART_ENTITY_TYPE.value(), context1 -> new MinecartRenderer<>(context1, GRATED_HOPPER_MINECART));
    }
}
