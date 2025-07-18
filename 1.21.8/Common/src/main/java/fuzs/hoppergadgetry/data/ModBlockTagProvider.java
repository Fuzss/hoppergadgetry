package fuzs.hoppergadgetry.data;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModBlockTagProvider extends AbstractTagProvider<Block> {

    public ModBlockTagProvider(DataProviderContext context) {
        super(Registries.BLOCK, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModRegistry.GRATED_HOPPER_BLOCK.value());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModRegistry.CHUTE_BLOCK.value(), ModRegistry.DUCT_BLOCK.value());
        this.tag(ModRegistry.DUCT_INPUTS_BLOCK_TAG).add(Blocks.HOPPER, ModRegistry.DUCT_BLOCK.value(),
                ModRegistry.GRATED_HOPPER_BLOCK.value(), ModRegistry.CHUTE_BLOCK.value()
        );
    }
}
