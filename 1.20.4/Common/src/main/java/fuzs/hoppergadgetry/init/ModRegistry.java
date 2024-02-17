package fuzs.hoppergadgetry.init;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.world.inventory.GratedHopperMenu;
import fuzs.hoppergadgetry.world.level.block.ChuteBlock;
import fuzs.hoppergadgetry.world.level.block.DuctBlock;
import fuzs.hoppergadgetry.world.level.block.GratedHopperBlock;
import fuzs.hoppergadgetry.world.level.block.entity.GratedHopperBlockEntity;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class ModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.from(HopperGadgetry.MOD_ID);
    public static final Holder.Reference<Block> CHUTE_BLOCK = REGISTRY.registerBlock("chute",
            () -> new ChuteBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
                    .noOcclusion())
    );
    public static final Holder.Reference<Item> CHUTE_ITEM = REGISTRY.registerBlockItem(CHUTE_BLOCK);
    public static final Holder.Reference<Block> DUCT_BLOCK = REGISTRY.registerBlock("duct",
            () -> new DuctBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F)
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
                    .noOcclusion())
    );
    public static final Holder.Reference<Item> DUCT_ITEM = REGISTRY.registerBlockItem(DUCT_BLOCK);
    public static final Holder.Reference<Block> GRATED_HOPPER_BLOCK = REGISTRY.registerBlock("grated_hopper",
            () -> new GratedHopperBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HOPPER))
    );
    public static final Holder.Reference<Item> GRATED_HOPPER_ITEM = REGISTRY.registerBlockItem(GRATED_HOPPER_BLOCK);
    public static final Holder.Reference<BlockEntityType<GratedHopperBlockEntity>> GRATED_HOPPER_BLOCK_ENTITY_TYPE = REGISTRY.registerBlockEntityType(
            "grated_hopper",
            () -> BlockEntityType.Builder.of(GratedHopperBlockEntity::new, GRATED_HOPPER_BLOCK.value())
    );
    public static final Holder.Reference<MenuType<GratedHopperMenu>> GRATED_HOPPER_MENU_TYPE = REGISTRY.registerMenuType("grated_hopper", () -> GratedHopperMenu::new);

    public static void touch() {

    }
}
