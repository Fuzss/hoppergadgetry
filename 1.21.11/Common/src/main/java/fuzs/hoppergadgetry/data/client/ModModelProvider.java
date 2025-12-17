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
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
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
        Identifier identifier = TextureMapping.getBlockTexture(block, suffix);
        return new TextureMapping().put(TextureSlot.TEXTURE, identifier)
                .put(TextureSlot.PARTICLE, identifier);
    }

    @Override
    public void addBlockModels(BlockModelGenerators blockModelGenerators) {
        this.createDuct(ModRegistry.DUCT_BLOCK.value(), blockModelGenerators);
        this.createChute(ModRegistry.CHUTE_BLOCK.value(), blockModelGenerators);
        this.createGratedHopper(ModRegistry.GRATED_HOPPER_BLOCK.value(), blockModelGenerators);
    }

    public final void createDuct(Block block, BlockModelGenerators blockModelGenerators) {
        MultiVariant blockModel = BlockModelGenerators.plainVariant(DUCT_TEMPLATE.create(block,
                createParticleTextureMapping(block),
                blockModelGenerators.modelOutput));
        MultiVariant poweredModel = BlockModelGenerators.plainVariant(DUCT_TEMPLATE.createWithSuffix(block,
                "_powered",
                createParticleTextureMapping(block, "_powered"),
                blockModelGenerators.modelOutput));
        MultiVariant inputModel = BlockModelGenerators.plainVariant(DUCT_INPUT_TEMPLATE.createWithSuffix(block,
                "_input",
                createParticleTextureMapping(block, "_input"),
                blockModelGenerators.modelOutput));
        blockModelGenerators.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.NORTH)
                        .term(DuctBlock.ENABLED, Boolean.FALSE), poweredModel)
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.EAST)
                        .term(DuctBlock.ENABLED, Boolean.FALSE), poweredModel.with(BlockModelGenerators.Y_ROT_90))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.SOUTH)
                        .term(DuctBlock.ENABLED, Boolean.FALSE), poweredModel.with(BlockModelGenerators.Y_ROT_180))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.WEST)
                        .term(DuctBlock.ENABLED, Boolean.FALSE), poweredModel.with(BlockModelGenerators.Y_ROT_270))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.DOWN)
                        .term(DuctBlock.ENABLED, Boolean.FALSE), poweredModel.with(BlockModelGenerators.X_ROT_90))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.UP)
                        .term(DuctBlock.ENABLED, Boolean.FALSE), poweredModel.with(BlockModelGenerators.X_ROT_270))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.NORTH)
                        .term(DuctBlock.ENABLED, Boolean.TRUE), blockModel)
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.EAST)
                        .term(DuctBlock.ENABLED, Boolean.TRUE), blockModel.with(BlockModelGenerators.Y_ROT_90))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.SOUTH)
                        .term(DuctBlock.ENABLED, Boolean.TRUE), blockModel.with(BlockModelGenerators.Y_ROT_180))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.WEST)
                        .term(DuctBlock.ENABLED, Boolean.TRUE), blockModel.with(BlockModelGenerators.Y_ROT_270))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.DOWN)
                        .term(DuctBlock.ENABLED, Boolean.TRUE), blockModel.with(BlockModelGenerators.X_ROT_90))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.FACING, Direction.UP)
                        .term(DuctBlock.ENABLED, Boolean.TRUE), blockModel.with(BlockModelGenerators.X_ROT_270))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.NORTH, Boolean.TRUE)
                        .negatedTerm(DuctBlock.FACING, Direction.NORTH), inputModel)
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.EAST, Boolean.TRUE)
                        .negatedTerm(DuctBlock.FACING, Direction.EAST), inputModel.with(BlockModelGenerators.Y_ROT_90))
                .with(BlockModelGenerators.condition()
                                .term(DuctBlock.SOUTH, Boolean.TRUE)
                                .negatedTerm(DuctBlock.FACING, Direction.SOUTH),
                        inputModel.with(BlockModelGenerators.Y_ROT_180))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.WEST, Boolean.TRUE)
                        .negatedTerm(DuctBlock.FACING, Direction.WEST), inputModel.with(BlockModelGenerators.Y_ROT_270))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.DOWN, Boolean.TRUE)
                        .negatedTerm(DuctBlock.FACING, Direction.DOWN), inputModel.with(BlockModelGenerators.X_ROT_90))
                .with(BlockModelGenerators.condition()
                        .term(DuctBlock.UP, Boolean.TRUE)
                        .negatedTerm(DuctBlock.FACING, Direction.UP), inputModel.with(BlockModelGenerators.X_ROT_270)));

    }

    public final void createChute(Block block, BlockModelGenerators blockModelGenerators) {
        Identifier identifier = CHUTE_TEMPLATE.create(block,
                TextureMapping.logColumn(Blocks.STRIPPED_OAK_LOG),
                blockModelGenerators.modelOutput);
        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block,
                BlockModelGenerators.plainVariant(identifier)));
    }

    public final void createGratedHopper(Block block, BlockModelGenerators blockModelGenerators) {
        MultiVariant blockModel = BlockModelGenerators.plainVariant(ModelLocationHelper.getBlockModel(Blocks.HOPPER));
        MultiVariant sideModel = BlockModelGenerators.plainVariant(ModelLocationHelper.getBlockModel(Blocks.HOPPER,
                "_side"));
        MultiVariant grateModel = BlockModelGenerators.plainVariant(GRATE_TEMPLATE.createWithSuffix(block,
                "_grate",
                createParticleTextureMapping(Blocks.IRON_BARS),
                blockModelGenerators.modelOutput));
        blockModelGenerators.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
                .with(BlockModelGenerators.condition().term(BlockStateProperties.FACING_HOPPER, Direction.DOWN),
                        blockModel)
                .with(BlockModelGenerators.condition().term(BlockStateProperties.FACING_HOPPER, Direction.NORTH),
                        sideModel)
                .with(BlockModelGenerators.condition().term(BlockStateProperties.FACING_HOPPER, Direction.EAST),
                        sideModel.with(BlockModelGenerators.Y_ROT_90))
                .with(BlockModelGenerators.condition().term(BlockStateProperties.FACING_HOPPER, Direction.SOUTH),
                        sideModel.with(BlockModelGenerators.Y_ROT_180))
                .with(BlockModelGenerators.condition().term(BlockStateProperties.FACING_HOPPER, Direction.WEST),
                        sideModel.with(BlockModelGenerators.Y_ROT_270))
                .with(BlockModelGenerators.condition().term(BlockStateProperties.FACING_HOPPER, Direction.DOWN),
                        grateModel)
                .with(BlockModelGenerators.condition().term(BlockStateProperties.FACING_HOPPER, Direction.NORTH),
                        grateModel)
                .with(BlockModelGenerators.condition().term(BlockStateProperties.FACING_HOPPER, Direction.EAST),
                        grateModel.with(BlockModelGenerators.Y_ROT_90))
                .with(BlockModelGenerators.condition().term(BlockStateProperties.FACING_HOPPER, Direction.SOUTH),
                        grateModel.with(BlockModelGenerators.Y_ROT_180))
                .with(BlockModelGenerators.condition().term(BlockStateProperties.FACING_HOPPER, Direction.WEST),
                        grateModel.with(BlockModelGenerators.Y_ROT_270)));
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
