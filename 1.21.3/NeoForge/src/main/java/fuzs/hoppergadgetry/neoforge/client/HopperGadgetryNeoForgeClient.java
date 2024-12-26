package fuzs.hoppergadgetry.neoforge.client;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.client.HopperGadgetryClient;
import fuzs.hoppergadgetry.data.client.ModLanguageProvider;
import fuzs.hoppergadgetry.data.client.ModModelProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = HopperGadgetry.MOD_ID, dist = Dist.CLIENT)
public class HopperGadgetryNeoForgeClient {

    public HopperGadgetryNeoForgeClient() {
        ClientModConstructor.construct(HopperGadgetry.MOD_ID, HopperGadgetryClient::new);
        DataProviderHelper.registerDataProviders(HopperGadgetry.MOD_ID,
                ModLanguageProvider::new,
                ModModelProvider::new);
    }
}
