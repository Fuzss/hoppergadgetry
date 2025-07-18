package fuzs.hoppergadgetry.data;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractLootProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.world.level.block.Block;

public class ModBlockLootProvider extends AbstractLootProvider.Blocks {

    public ModBlockLootProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addLootTables() {
        this.dropNameable(ModRegistry.GRATED_HOPPER_BLOCK.value());
        this.dropNameable(ModRegistry.CHUTE_BLOCK.value());
        this.dropNameable(ModRegistry.DUCT_BLOCK.value());
    }

    public void dropNameable(Block block) {
        this.add(block, this::createNameableBlockEntityTable);
    }
}
