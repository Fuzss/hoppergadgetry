package fuzs.hoppergadgetry.data.client;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.level.block.entity.GratedHopperBlockEntity;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.addCreativeModeTab(HopperGadgetry.MOD_ID, HopperGadgetry.MOD_NAME);
        builder.add(ModRegistry.CHUTE_BLOCK.value(), "Chute");
        builder.add(ModRegistry.DUCT_BLOCK.value(), "Duct");
        builder.add(ModRegistry.GRATED_HOPPER_BLOCK.value(), "Grated Hopper");
        builder.add(GratedHopperBlockEntity.COMPONENT_GRATED_HOPPER, "Grated Hopper");
    }
}
