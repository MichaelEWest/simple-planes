package xyz.przemyk.simpleplanes.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import xyz.przemyk.simpleplanes.SimplePlanesMod;
import xyz.przemyk.simpleplanes.container.PlaneWorkbenchContainer;
import xyz.przemyk.simpleplanes.recipes.PlaneWorkbenchRecipe;
import xyz.przemyk.simpleplanes.setup.SimplePlanesBlocks;

import java.util.stream.StreamSupport;

public class PlaneWorkbenchRecipeCategory extends AbstractRecipeCategory<PlaneWorkbenchRecipe> {

    public static final RecipeType<PlaneWorkbenchRecipe> RECIPE_TYPE = RecipeType.create(SimplePlanesMod.MODID, "plane_workbench", PlaneWorkbenchRecipe.class);

    public PlaneWorkbenchRecipeCategory(IGuiHelper guiHelper) {
        super(
            RECIPE_TYPE,
            SimplePlanesBlocks.PLANE_WORKBENCH_BLOCK.get().getName(),
            guiHelper.createDrawableItemLike(SimplePlanesBlocks.PLANE_WORKBENCH_BLOCK.get()),
            125,
            18
        );
}
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PlaneWorkbenchRecipe recipe, IFocusGroup focuses) {
        var tag = BuiltInRegistries.BLOCK.getTagOrEmpty(PlaneWorkbenchContainer.PLANE_MATERIALS_TAG);
        var materialItems = StreamSupport.stream(tag.spliterator(), false).map(block -> (net.minecraft.world.level.ItemLike) block.value()).toList();
        var ingredientItems = recipe.ingredient().items().map(h -> (net.minecraft.world.level.ItemLike) h.value()).toList();
        builder.addInputSlot(1, 1).addIngredients(
            Ingredient.of(ingredientItems.stream())).setStandardSlotBackground();
        builder.addInputSlot(50, 1).addIngredients(Ingredient.of(materialItems.stream())).setStandardSlotBackground();
        builder.addOutputSlot(108, 1).addItemStack(recipe.result()).setStandardSlotBackground();
}
    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, PlaneWorkbenchRecipe recipe, IFocusGroup focuses) {
        builder.addRecipePlusSign().setPosition(27, 3);
        builder.addRecipeArrow().setPosition(76, 1);
}
}