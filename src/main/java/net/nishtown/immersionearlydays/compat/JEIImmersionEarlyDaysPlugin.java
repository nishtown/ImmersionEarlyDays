package net.nishtown.immersionearlydays.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.recipe.StoneWashingRecipe;
import net.nishtown.immersionearlydays.screen.StoneWasherScreen;

import java.util.List;

@JeiPlugin
public class JEIImmersionEarlyDaysPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ImmersionEarlyDays.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new StoneWashingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<StoneWashingRecipe> washingRecipes = recipeManager.getAllRecipesFor(StoneWashingRecipe.Type.INSTANCE);
        registration.addRecipes(StoneWashingCategory.STONE_WASHING_RECIPE_TYPE, washingRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(StoneWasherScreen.class, 77, 30,20,30,
                StoneWashingCategory.STONE_WASHING_RECIPE_TYPE);
    }
}
