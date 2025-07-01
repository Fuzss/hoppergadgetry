package fuzs.hoppergadgetry;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.GameplayContentContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.math.Fraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HopperGadgetry implements ModConstructor {
    public static final String MOD_ID = "hoppergadgetry";
    public static final String MOD_NAME = "Hopper Gadgetry";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
    }

    @Override
    public void onRegisterGameplayContent(GameplayContentContext context) {
        context.registerFuel(ModRegistry.DUCT_BLOCK, Fraction.getFraction(3, 2));
        context.registerFuel(ModRegistry.CHUTE_BLOCK, Fraction.getFraction(3, 2));
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
