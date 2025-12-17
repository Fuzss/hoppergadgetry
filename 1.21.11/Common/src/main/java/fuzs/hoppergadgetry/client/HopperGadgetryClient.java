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
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.cart.MinecartModel;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.Block;

public class HopperGadgetryClient implements ClientModConstructor {
    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(HopperGadgetry.MOD_ID);
    public static final ModelLayerLocation GRATED_HOPPER_MINECART = MODEL_LAYERS.registerModelLayer(
            "grated_hopper_minecart");
    public static final ModelLayerLocation CHUTE_MINECART = MODEL_LAYERS.registerModelLayer("chute_minecart");

    @Override
    public void onRegisterEntityRenderers(EntityRenderersContext context) {
        context.registerEntityRenderer(ModRegistry.GRATED_HOPPER_MINECART_ENTITY_TYPE.value(),
                getMinecartRendererProvider(GRATED_HOPPER_MINECART));
        context.registerEntityRenderer(ModRegistry.CHUTE_MINECART_ENTITY_TYPE.value(),
                getMinecartRendererProvider(CHUTE_MINECART));
    }

    @SuppressWarnings("unchecked")
    private static <T extends AbstractMinecart> EntityRendererProvider<T> getMinecartRendererProvider(ModelLayerLocation modelLayerLocation) {
        return (EntityRendererProvider.Context context) -> (EntityRenderer<T, ?>) new MinecartRenderer(context,
                modelLayerLocation);
    }

    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        context.registerMenuScreen(ModRegistry.GRATED_HOPPER_MENU_TYPE.value(),
                HopperLikeScreen.create(HopperLikeScreen.GRATED_HOPPER_LOCATION,
                        HopperLikeScreen.GRATED_HOPPER_IMAGE_HEIGHT));
        context.registerMenuScreen(ModRegistry.DUCT_MENU_TYPE.value(),
                HopperLikeScreen.create(HopperLikeScreen.DUCT_LOCATION, HopperLikeScreen.DUCT_IMAGE_HEIGHT));
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(GRATED_HOPPER_MINECART, MinecartModel::createBodyLayer);
        context.registerLayerDefinition(CHUTE_MINECART, MinecartModel::createBodyLayer);
    }

    @Override
    public void onRegisterBlockRenderTypes(RenderTypesContext<Block> context) {
        context.registerChunkRenderType(ModRegistry.GRATED_HOPPER_BLOCK.value(), ChunkSectionLayer.CUTOUT);
    }
}
