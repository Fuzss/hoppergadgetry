package fuzs.hoppergadgetry.init;

import fuzs.extensibleenums.api.v2.CommonAbstractions;
import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.world.entity.vehicle.MinecartChute;
import fuzs.hoppergadgetry.world.entity.vehicle.MinecartGratedHopper;
import fuzs.hoppergadgetry.world.inventory.DuctMenu;
import fuzs.hoppergadgetry.world.inventory.GratedHopperMenu;
import fuzs.hoppergadgetry.world.level.block.ChuteBlock;
import fuzs.hoppergadgetry.world.level.block.DuctBlock;
import fuzs.hoppergadgetry.world.level.block.GratedHopperBlock;
import fuzs.hoppergadgetry.world.level.block.entity.ChuteBlockEntity;
import fuzs.hoppergadgetry.world.level.block.entity.DuctBlockEntity;
import fuzs.hoppergadgetry.world.level.block.entity.GratedHopperBlockEntity;
import fuzs.puzzleslib.api.init.v3.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.BoundTagFactory;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.Locale;
import java.util.Objects;

public class ModRegistry {
    public static final AbstractMinecart.Type GRATED_HOPPER_MINECART_TYPE = CommonAbstractions.createMinecartType(HopperGadgetry.id("grated_hopper"));
    public static final AbstractMinecart.Type CHUTE_MINECART_TYPE = CommonAbstractions.createMinecartType(HopperGadgetry.id("chute"));

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
            () -> new GratedHopperBlock(BlockBehaviour.Properties.copy(Blocks.HOPPER))
    );
    public static final Holder.Reference<Item> GRATED_HOPPER_ITEM = REGISTRY.registerBlockItem(GRATED_HOPPER_BLOCK);
    public static final Holder.Reference<Item> GRATED_HOPPER_MINECART_ITEM = REGISTRY.registerItem("grated_hopper_minecart", () -> new MinecartItem(GRATED_HOPPER_MINECART_TYPE, new Item.Properties().stacksTo(1)));
    public static final Holder.Reference<Item> CHUTE_MINECART_ITEM = REGISTRY.registerItem("chute_minecart", () -> new MinecartItem(CHUTE_MINECART_TYPE, new Item.Properties().stacksTo(1)));
    public static final Holder.Reference<BlockEntityType<GratedHopperBlockEntity>> GRATED_HOPPER_BLOCK_ENTITY_TYPE = REGISTRY.registerBlockEntityType(
            "grated_hopper",
            () -> BlockEntityType.Builder.of(GratedHopperBlockEntity::new, GRATED_HOPPER_BLOCK.value())
    );
    public static final Holder.Reference<BlockEntityType<ChuteBlockEntity>> CHUTE_BLOCK_ENTITY_TYPE = REGISTRY.registerBlockEntityType(
            "chute",
            () -> BlockEntityType.Builder.of(ChuteBlockEntity::new, CHUTE_BLOCK.value())
    );
    public static final Holder.Reference<BlockEntityType<DuctBlockEntity>> DUCT_BLOCK_ENTITY_TYPE = REGISTRY.registerBlockEntityType(
            "duct",
            () -> BlockEntityType.Builder.of(DuctBlockEntity::new, DUCT_BLOCK.value())
    );
    public static final Holder.Reference<EntityType<MinecartGratedHopper>> GRATED_HOPPER_MINECART_ENTITY_TYPE = REGISTRY.registerEntityType(
            "grated_hopper_minecart",
            () -> EntityType.Builder.<MinecartGratedHopper>of(MinecartGratedHopper::new, MobCategory.MISC)
                    .sized(0.98F, 0.7F)
                    .clientTrackingRange(8)
    );
    public static final Holder.Reference<EntityType<MinecartChute>> CHUTE_MINECART_ENTITY_TYPE = REGISTRY.registerEntityType(
            "chute_minecart",
            () -> EntityType.Builder.<MinecartChute>of(MinecartChute::new, MobCategory.MISC)
                    .sized(0.98F, 0.7F)
                    .clientTrackingRange(8)
    );
    public static final Holder.Reference<MenuType<GratedHopperMenu>> GRATED_HOPPER_MENU_TYPE = REGISTRY.registerMenuType(
            "grated_hopper",
            () -> GratedHopperMenu::new
    );
    public static final Holder.Reference<MenuType<DuctMenu>> DUCT_MENU_TYPE = REGISTRY.registerMenuType(
            "duct",
            () -> DuctMenu::new
    );

    static final BoundTagFactory TAGS = BoundTagFactory.make(HopperGadgetry.MOD_ID);
    public static final TagKey<Block> DUCT_INPUTS_BLOCK_TAG = TAGS.registerBlockTag("duct_inputs");

    public static void touch() {
        MinecartTypeRegistry.INSTANCE.register(GRATED_HOPPER_MINECART_TYPE, MinecartGratedHopper::new);
        MinecartTypeRegistry.INSTANCE.register(CHUTE_MINECART_TYPE, MinecartChute::new);
    }
}
