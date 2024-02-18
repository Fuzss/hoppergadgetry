package fuzs.hoppergadgetry.data.client;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModModelProvider extends AbstractModelProvider {
    public static final ModelTemplate HOPPER = ModelTemplates.create("hopper",
            TextureSlot.PARTICLE,
            TextureSlot.TOP,
            TextureSlot.SIDE,
            TextureSlot.INSIDE
    );

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators builder) {
        this.skipBlock(ModRegistry.DUCT_BLOCK.value());
        createChuteBlock(builder, ModRegistry.CHUTE_BLOCK.value());
        createGratedHopperBlock(builder);
    }

    private static void createChuteBlock(BlockModelGenerators builder, Block block) {
        TextureMapping textureMapping = chuteTextureMapping(Blocks.STRIPPED_OAK_LOG);
        builder.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block,
                HOPPER.create(block, textureMapping, builder.modelOutput)
        ));
    }

    private static void createGratedHopperBlock(BlockModelGenerators builder) {
        ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(Blocks.HOPPER);
        ResourceLocation resourceLocation2 = ModelLocationUtils.getModelLocation(Blocks.HOPPER, "_side");
        ResourceLocation resourceLocation3 = ModelLocationUtils.getModelLocation(ModRegistry.GRATED_HOPPER_BLOCK.value(),
                "_grate"
        );
        builder.blockStateOutput.accept(MultiPartGenerator.multiPart(ModRegistry.GRATED_HOPPER_BLOCK.value())
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.DOWN),
                        Variant.variant().with(VariantProperties.MODEL, resourceLocation)
                )
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.NORTH),
                        Variant.variant().with(VariantProperties.MODEL, resourceLocation2)
                )
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.EAST),
                        Variant.variant()
                                .with(VariantProperties.MODEL, resourceLocation2)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                )
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.SOUTH),
                        Variant.variant()
                                .with(VariantProperties.MODEL, resourceLocation2)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                )
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.WEST),
                        Variant.variant()
                                .with(VariantProperties.MODEL, resourceLocation2)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                )
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.DOWN),
                        Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
                )
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.NORTH),
                        Variant.variant().with(VariantProperties.MODEL, resourceLocation3)
                )
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.EAST),
                        Variant.variant()
                                .with(VariantProperties.MODEL, resourceLocation3)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                )
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.SOUTH),
                        Variant.variant()
                                .with(VariantProperties.MODEL, resourceLocation3)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                )
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.WEST),
                        Variant.variant()
                                .with(VariantProperties.MODEL, resourceLocation3)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                ));
    }

    private static TextureMapping chuteTextureMapping(Block strippedWoodBlock) {
        return new TextureMapping().put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(strippedWoodBlock)).put(
                TextureSlot.TOP,
                TextureMapping.getBlockTexture(strippedWoodBlock, "_top")
        ).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(strippedWoodBlock)).put(TextureSlot.INSIDE,
                TextureMapping.getBlockTexture(strippedWoodBlock, "_top")
        );
    }

    @Override
    public void addItemModels(ItemModelGenerators builder) {
        builder.generateFlatItem(ModRegistry.GRATED_HOPPER_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.CHUTE_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.DUCT_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.GRATED_HOPPER_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
    }
}
