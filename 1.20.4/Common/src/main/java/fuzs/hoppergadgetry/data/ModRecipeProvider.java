package fuzs.hoppergadgetry.data;

import fuzs.hoppergadgetry.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.GRATED_HOPPER_ITEM.value())
                .define('#', Items.IRON_BARS)
                .define('X', Items.HOPPER)
                .pattern("#")
                .pattern("X")
                .group(getItemName(ModRegistry.GRATED_HOPPER_ITEM.value()))
                .unlockedBy(getHasName(Items.HOPPER), has(Items.HOPPER))
                .save(recipeOutput, getItemName(ModRegistry.GRATED_HOPPER_ITEM.value()) + "_from_" + getItemName(Items.HOPPER));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.GRATED_HOPPER_ITEM.value())
                .define('#', Items.IRON_BARS)
                .define('I', Items.IRON_INGOT)
                .define('C', Items.CHEST)
                .pattern("I#I")
                .pattern("ICI")
                .pattern(" I ")
                .group(getItemName(ModRegistry.GRATED_HOPPER_ITEM.value()))
                .unlockedBy(getHasName(Items.CHEST), has(Items.CHEST))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.DUCT_ITEM.value())
                .define('#', ItemTags.PLANKS)
                .define('I', Items.IRON_INGOT)
                .pattern("#I#")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModRegistry.CHUTE_ITEM.value())
                .define('#', ItemTags.PLANKS)
                .define('X', ItemTags.LOGS)
                .pattern("# #")
                .pattern("#X#")
                .pattern(" # ")
                .unlockedBy(getHasName(Items.CHEST), has(Items.CHEST))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, ModRegistry.GRATED_HOPPER_MINECART_ITEM.value())
                .requires(ModRegistry.GRATED_HOPPER_ITEM.value())
                .requires(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, ModRegistry.CHUTE_MINECART_ITEM.value())
                .requires(ModRegistry.CHUTE_ITEM.value())
                .requires(Items.MINECART)
                .unlockedBy(getHasName(Items.MINECART), has(Items.MINECART))
                .save(recipeOutput);
    }
}
