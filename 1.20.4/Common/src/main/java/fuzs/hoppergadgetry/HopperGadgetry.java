package fuzs.hoppergadgetry;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.CreativeModeTabContext;
import fuzs.puzzleslib.api.item.v2.CreativeModeTabConfigurator;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HopperGadgetry implements ModConstructor {
    public static final String MOD_ID = "hoppergadgetry";
    public static final String MOD_NAME = "Hopper Gadgetry";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
    }

    @Override
    public void onRegisterCreativeModeTabs(CreativeModeTabContext context) {
        context.registerCreativeModeTab(CreativeModeTabConfigurator.from(MOD_ID)
                .icon(() -> ModRegistry.DUCT_ITEM.value().getDefaultInstance())
                .displayItems((itemDisplayParameters, output) -> {
                    output.accept(ModRegistry.DUCT_ITEM.value());
                    output.accept(ModRegistry.GRATED_HOPPER_ITEM.value());
                    output.accept(ModRegistry.CHUTE_ITEM.value());
                }));
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
