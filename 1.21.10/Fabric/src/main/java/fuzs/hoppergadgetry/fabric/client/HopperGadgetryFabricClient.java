package fuzs.hoppergadgetry.fabric.client;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.client.HopperGadgetryClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class HopperGadgetryFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(HopperGadgetry.MOD_ID, HopperGadgetryClient::new);
    }
}
