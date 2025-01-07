package net.nishtown.immersionearlydays.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class RecipeOutput {

    public static final RecipeOutput EMPTY = new RecipeOutput(ItemStack.EMPTY, 1);


    private static final Random r = new Random();
    private final ItemStack stack;
    private final float chance;


    public RecipeOutput(ItemStack pStack, float pChance) {
        this.stack = pStack;
        this.chance = pChance;
    }

    public RecipeOutput() {
        stack = ItemStack.EMPTY;
        chance = 1f;
    }

    public ItemStack getStack() {
        return  stack;
    }

    public  float getChance() {
        return chance;
    }

    public ItemStack rollOutput() {
        int outputAmount = stack.getCount();
        for (int roll = 0; roll < stack.getCount(); roll++)
            if (r.nextFloat() > chance)
                outputAmount--;
        if (outputAmount == 0)
            return ItemStack.EMPTY;
        ItemStack out = stack.copy();
        out.setCount(outputAmount);
        return out;
    }


    public static RecipeOutput fromJson(JsonElement je) {

        JsonObject json = je.getAsJsonObject();
        String itemId = GsonHelper.getAsString(json, "item");
        int count = GsonHelper.getAsInt(json, "count", 1);
        float chance = GsonHelper.isValidNode(json, "chance") ? GsonHelper.getAsFloat(json, "chance") : 1;
        ItemStack itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId)), count);



        return new RecipeOutput(itemstack, chance);
    }



}
