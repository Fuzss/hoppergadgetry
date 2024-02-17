package fuzs.hoppergadgetry.forge.client;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.client.HopperGadgetryClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = HopperGadgetry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HopperGadgetryForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(HopperGadgetry.MOD_ID, HopperGadgetryClient::new);
    }
}
