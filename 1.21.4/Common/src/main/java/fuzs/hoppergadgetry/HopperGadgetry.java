package fuzs.hoppergadgetry;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.CreativeModeTabContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.server.RegisterFuelValuesCallback;
import fuzs.puzzleslib.api.item.v2.CreativeModeTabConfigurator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.FuelValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HopperGadgetry implements ModConstructor {
    public static final String MOD_ID = "hoppergadgetry";
    public static final String MOD_NAME = "Hopper Gadgetry";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        RegisterFuelValuesCallback.EVENT.register((FuelValues.Builder builder, int fuelBaseValue) -> {
            builder.add(ModRegistry.DUCT_BLOCK.value(), fuelBaseValue * 3 / 2);
            builder.add(ModRegistry.CHUTE_BLOCK.value(), fuelBaseValue * 3 / 2);
        });
    }

    @Override
    public void onRegisterCreativeModeTabs(CreativeModeTabContext context) {
        context.registerCreativeModeTab(CreativeModeTabConfigurator.from(MOD_ID, ModRegistry.DUCT_ITEM)
                .displayItems((itemDisplayParameters, output) -> {
                    output.accept(ModRegistry.DUCT_ITEM.value());
                    output.accept(ModRegistry.GRATED_HOPPER_ITEM.value());
                    output.accept(ModRegistry.CHUTE_ITEM.value());
                    output.accept(ModRegistry.GRATED_HOPPER_MINECART_ITEM.value());
                    output.accept(ModRegistry.CHUTE_MINECART_ITEM.value());
                }));
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
