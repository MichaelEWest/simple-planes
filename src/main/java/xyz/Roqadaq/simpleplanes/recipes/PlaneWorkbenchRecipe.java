package xyz.roqadaq.simpleplanes.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import xyz.roqadaq.simpleplanes.setup.SimplePlanesRecipes;

public record PlaneWorkbenchRecipe(Ingredient ingredient, int ingredientAmount,
                                   int materialAmount,
                                   Identifier resultId) implements Recipe<RecipeInput> {

    // ItemStack is created here (at use-time) not at decode-time, so components are bound.
    public ItemStack result() {
        return BuiltInRegistries.ITEM.getOptional(resultId)
            .map(item -> new ItemStack(item.builtInRegistryHolder()))
            .orElse(ItemStack.EMPTY);
    }

    public boolean canCraft(ItemStack ingredientStack, ItemStack materialStack) {
        return ingredientStack.getCount() >= ingredientAmount && materialStack.getCount() >= materialAmount && ingredient.test(ingredientStack);
    }

    @Override
    public RecipeSerializer<PlaneWorkbenchRecipe> getSerializer() {
        return SimplePlanesRecipes.PLANE_WORKBENCH_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return SimplePlanesRecipes.PLANE_WORKBENCH_RECIPE_TYPE.get();
    }

    @Override
    public boolean matches(RecipeInput p_77569_1_, Level p_77569_2_) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput container) {
        return ItemStack.EMPTY;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return new RecipeBookCategory();
    }

    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result();
    }
}
