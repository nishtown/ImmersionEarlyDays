package net.nishtown.immersionearlydays.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.block.ModBlocks;
import net.nishtown.immersionearlydays.recipe.StoneWashingRecipe;
import org.jetbrains.annotations.Nullable;

public class StoneWashingCategory implements IRecipeCategory<StoneWashingRecipe> {
    public static  final ResourceLocation UID = new ResourceLocation(ImmersionEarlyDays.MOD_ID, "stone_washing");
    public static  final ResourceLocation TEXTURE = new ResourceLocation(ImmersionEarlyDays.MOD_ID,
            "textures/gui/stone_washer_gui2.png");

    public static final RecipeType<StoneWashingRecipe> STONE_WASHING_RECIPE_TYPE =
            new RecipeType<>(UID, StoneWashingRecipe.class);


    private final IDrawable background;
    private final IDrawable icon;

    public StoneWashingCategory(IGuiHelper helper){
        this.background = helper.createDrawable(TEXTURE, 0,0,176,80);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack((ModBlocks.STONE_WASHER_BLOCK.get())));
    }

    @Override
    public RecipeType<StoneWashingRecipe> getRecipeType() {
        return STONE_WASHING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.immersionearlydays.stone_washer_block");
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, StoneWashingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 80,11).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 67, 59).addItemStack((recipe.getResultItem(null)));

        // Secondary output slot with rich tooltip
        if (recipe.getResultItems(null).size() > 1) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 94, 59)
                    .addItemStack(recipe.getResultItems(null).get(1).getStack())
                    .addRichTooltipCallback((slotView, tooltip) -> {
                        // Get the chance for the secondary output
                        float chance = recipe.getResultItems(null).get(1).getChance() * 100;

                        // Add a line to the tooltip showing the chance
                        tooltip.add(Component.translatable("jei.tooltip.chance", String.format("%.1f%%", chance)));
                    });
        }
    }
}
