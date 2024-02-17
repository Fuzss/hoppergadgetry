package fuzs.hoppergadgetry.data;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractLootProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModBlockLootProvider extends AbstractLootProvider.Blocks {

    public ModBlockLootProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addLootTables() {
        this.dropSelf(ModRegistry.GRATED_HOPPER_BLOCK.value());
        this.dropSelf(ModRegistry.CHUTE_BLOCK.value());
        this.dropSelf(ModRegistry.DUCT_BLOCK.value());
    }
}
