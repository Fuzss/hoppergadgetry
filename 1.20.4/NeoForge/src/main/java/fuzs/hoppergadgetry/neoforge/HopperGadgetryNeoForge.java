package fuzs.hoppergadgetry.neoforge;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.data.ModBlockLootProvider;
import fuzs.hoppergadgetry.data.ModRecipeProvider;
import fuzs.hoppergadgetry.data.client.ModLanguageProvider;
import fuzs.hoppergadgetry.data.client.ModModelProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(HopperGadgetry.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class HopperGadgetryNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(HopperGadgetry.MOD_ID, HopperGadgetry::new);
        DataProviderHelper.registerDataProviders(HopperGadgetry.MOD_ID,
                ModBlockLootProvider::new,
                ModRecipeProvider::new,
                ModLanguageProvider::new,
                ModModelProvider::new
        );
    }
}
