package fuzs.hoppergadgetry.data.client;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.level.block.entity.ChuteBlockEntity;
import fuzs.hoppergadgetry.world.level.block.entity.DuctBlockEntity;
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
        builder.add(ModRegistry.GRATED_HOPPER_MINECART_ITEM.value(), "Grated Hopper Minecart");
        builder.add(ModRegistry.GRATED_HOPPER_MINECART_ENTITY_TYPE.value(), "Minecart with Grated Hopper");
        builder.add(GratedHopperBlockEntity.COMPONENT_GRATED_HOPPER, "Grated Hopper");
        builder.add(ChuteBlockEntity.COMPONENT_CHUTE, "Item Chute");
        builder.add(DuctBlockEntity.COMPONENT_DUCT, "Item Duct");
    }
}
