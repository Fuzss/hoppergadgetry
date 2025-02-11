package fuzs.hoppergadgetry.data.client;

import fuzs.hoppergadgetry.HopperGadgetry;
import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.hoppergadgetry.world.level.block.DuctBlock;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.client.data.v2.models.ModelLocationHelper;
import fuzs.puzzleslib.api.client.data.v2.models.ModelTemplateHelper;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.Condition;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModModelProvider extends AbstractModelProvider {
    public static final ModelTemplate CHUTE_TEMPLATE = ModelTemplateHelper.createBlockModelTemplate(HopperGadgetry.id(
            "template_chute"), TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.PARTICLE);
    public static final ModelTemplate DUCT_TEMPLATE = ModelTemplateHelper.createBlockModelTemplate(HopperGadgetry.id(
            "template_duct"), TextureSlot.TEXTURE, TextureSlot.PARTICLE);
    public static final ModelTemplate DUCT_INPUT_TEMPLATE = ModelTemplateHelper.createBlockModelTemplate(HopperGadgetry.id(
            "template_duct_input"), TextureSlot.TEXTURE, TextureSlot.PARTICLE);
    public static final ModelTemplate GRATE_TEMPLATE = ModelTemplateHelper.createBlockModelTemplate(HopperGadgetry.id(
            "template_grate"), TextureSlot.TEXTURE, TextureSlot.PARTICLE);

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    public static TextureMapping createParticleTextureMapping(Block block) {
        return createParticleTextureMapping(block, "");
    }

    public static TextureMapping createParticleTextureMapping(Block block, String suffix) {
        ResourceLocation resourceLocation = TextureMapping.getBlockTexture(block, suffix);
        return new TextureMapping().put(TextureSlot.TEXTURE, resourceLocation)
                .put(TextureSlot.PARTICLE, resourceLocation);
    }

    @Override
    public void addBlockModels(BlockModelGenerators blockModelGenerators) {
        this.createDuct(ModRegistry.DUCT_BLOCK.value(), blockModelGenerators);
        this.createChute(ModRegistry.CHUTE_BLOCK.value(), blockModelGenerators);
        this.createGratedHopper(ModRegistry.GRATED_HOPPER_BLOCK.value(), blockModelGenerators);
    }

    public final void createDuct(Block block, BlockModelGenerators blockModelGenerators) {
        ResourceLocation blockModel = DUCT_TEMPLATE.create(block,
                createParticleTextureMapping(block),
                blockModelGenerators.modelOutput);
        ResourceLocation poweredModel = DUCT_TEMPLATE.createWithSuffix(block,
                "_powered",
                createParticleTextureMapping(block, "_powered"),
                blockModelGenerators.modelOutput);
        ResourceLocation inputModel = DUCT_INPUT_TEMPLATE.createWithSuffix(block,
                "_input",
                createParticleTextureMapping(block, "_input"),
                blockModelGenerators.modelOutput);
        blockModelGenerators.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(Condition.condition()
                                .term(DuctBlock.FACING, Direction.NORTH)
                                .term(DuctBlock.ENABLED, Boolean.FALSE),
                        Variant.variant().with(VariantProperties.MODEL, poweredModel))
                .with(Condition.condition()
                                .term(DuctBlock.FACING, Direction.EAST)
                                .term(DuctBlock.ENABLED, Boolean.FALSE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, poweredModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .with(Condition.condition()
                                .term(DuctBlock.FACING, Direction.SOUTH)
                                .term(DuctBlock.ENABLED, Boolean.FALSE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, poweredModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .with(Condition.condition()
                                .term(DuctBlock.FACING, Direction.WEST)
                                .term(DuctBlock.ENABLED, Boolean.FALSE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, poweredModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .with(Condition.condition()
                                .term(DuctBlock.FACING, Direction.DOWN)
                                .term(DuctBlock.ENABLED, Boolean.FALSE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, poweredModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .with(Condition.condition().term(DuctBlock.FACING, Direction.UP).term(DuctBlock.ENABLED, Boolean.FALSE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, poweredModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
                .with(Condition.condition()
                                .term(DuctBlock.FACING, Direction.NORTH)
                                .term(DuctBlock.ENABLED, Boolean.TRUE),
                        Variant.variant().with(VariantProperties.MODEL, blockModel))
                .with(Condition.condition()
                                .term(DuctBlock.FACING, Direction.EAST)
                                .term(DuctBlock.ENABLED, Boolean.TRUE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, blockModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .with(Condition.condition()
                                .term(DuctBlock.FACING, Direction.SOUTH)
                                .term(DuctBlock.ENABLED, Boolean.TRUE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, blockModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .with(Condition.condition()
                                .term(DuctBlock.FACING, Direction.WEST)
                                .term(DuctBlock.ENABLED, Boolean.TRUE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, blockModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .with(Condition.condition()
                                .term(DuctBlock.FACING, Direction.DOWN)
                                .term(DuctBlock.ENABLED, Boolean.TRUE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, blockModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .with(Condition.condition().term(DuctBlock.FACING, Direction.UP).term(DuctBlock.ENABLED, Boolean.TRUE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, blockModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
                .with(Condition.condition().term(DuctBlock.NORTH, Boolean.TRUE),
                        Variant.variant().with(VariantProperties.MODEL, inputModel))
                .with(Condition.condition().term(DuctBlock.EAST, Boolean.TRUE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, inputModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .with(Condition.condition().term(DuctBlock.SOUTH, Boolean.TRUE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, inputModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .with(Condition.condition().term(DuctBlock.WEST, Boolean.TRUE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, inputModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .with(Condition.condition().term(DuctBlock.DOWN, Boolean.TRUE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, inputModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .with(Condition.condition().term(DuctBlock.UP, Boolean.TRUE),
                        Variant.variant()
                                .with(VariantProperties.MODEL, inputModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R270)));
    }

    public final void createChute(Block block, BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block,
                CHUTE_TEMPLATE.create(block,
                        TextureMapping.logColumn(Blocks.STRIPPED_OAK_LOG),
                        blockModelGenerators.modelOutput)));
    }

    public final void createGratedHopper(Block block, BlockModelGenerators blockModelGenerators) {
        ResourceLocation blockModel = ModelLocationHelper.getBlockModel(Blocks.HOPPER);
        ResourceLocation sideModel = ModelLocationHelper.getBlockModel(Blocks.HOPPER, "_side");
        ResourceLocation grateModel = GRATE_TEMPLATE.createWithSuffix(block,
                "_grate",
                createParticleTextureMapping(Blocks.IRON_BARS),
                blockModelGenerators.modelOutput);
        blockModelGenerators.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.DOWN),
                        Variant.variant().with(VariantProperties.MODEL, blockModel))
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.NORTH),
                        Variant.variant().with(VariantProperties.MODEL, sideModel))
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.EAST),
                        Variant.variant()
                                .with(VariantProperties.MODEL, sideModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.SOUTH),
                        Variant.variant()
                                .with(VariantProperties.MODEL, sideModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.WEST),
                        Variant.variant()
                                .with(VariantProperties.MODEL, sideModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.DOWN),
                        Variant.variant().with(VariantProperties.MODEL, grateModel))
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.NORTH),
                        Variant.variant().with(VariantProperties.MODEL, grateModel))
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.EAST),
                        Variant.variant()
                                .with(VariantProperties.MODEL, grateModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.SOUTH),
                        Variant.variant()
                                .with(VariantProperties.MODEL, grateModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .with(Condition.condition().term(BlockStateProperties.FACING_HOPPER, Direction.WEST),
                        Variant.variant()
                                .with(VariantProperties.MODEL, grateModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
    }

    @Override
    public void addItemModels(ItemModelGenerators builder) {
        builder.generateFlatItem(ModRegistry.GRATED_HOPPER_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.CHUTE_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.DUCT_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.GRATED_HOPPER_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
        builder.generateFlatItem(ModRegistry.CHUTE_MINECART_ITEM.value(), ModelTemplates.FLAT_ITEM);
    }
}
