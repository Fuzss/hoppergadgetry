package fuzs.hoppergadgetry.fabric;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class HopperGadgetryFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(HopperGadgetry.MOD_ID, HopperGadgetry::new);
    }
}
