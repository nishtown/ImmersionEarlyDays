package net.nishtown.immersionearlydays.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.block.ModBlocks;
import net.nishtown.immersionearlydays.item.ModItems;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        //oreSmelting(pWriter, WASHABLES, RecipeCategory.MISC, ModItems.SAPPHIRE.get(), 0.25f, 200, "sapphire");
        //oreBlasting(pWriter, WASHABLES, RecipeCategory.MISC, ModItems.SAPPHIRE.get(), 0.25f, 100, "sapphire");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ANIMAL_GATE_BLOCK.get())
                .pattern("   ")
                .pattern("GFG")
                .pattern("GGG")
                .define('G', ItemTags.FENCE_GATES)
                .define('F', Items.FEATHER)
                .unlockedBy(getHasName(ModBlocks.ANIMAL_GATE_BLOCK.get()), has(ModBlocks.ANIMAL_GATE_BLOCK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.COBBLESTONE)
                .pattern("PPP")
                .pattern("PPP")
                .pattern("PPP")
                .define('P', ModItems.STONE_PEBBLE.get())
                .unlockedBy(getHasName(Blocks.DIRT), has(Blocks.DIRT))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.STONE_WASHER_BLOCK.get())
                .pattern("P P")
                .pattern("PBP")
                .pattern("L L")
                .define('P', ItemTags.PLANKS)
                .define('B', Items.BOWL)
                .define('L', ItemTags.LOGS)
                .unlockedBy(getHasName( Items.BOWL), has( Items.BOWL))
                .unlockedBy(getHasName( Items.OAK_PLANKS), has(Items.OAK_PLANKS))
                .unlockedBy(getHasName( Items.OAK_LOG), has(Items.OAK_LOG))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.IRON_NUGGET)
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ModItems.IRON_NODULE.get())
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                .save(pWriter, new ResourceLocation(ImmersionEarlyDays.MOD_ID, "iron_nuggets_to_iron_nodules"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.IRON_NUGGET, 9)
                .requires(Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter, new ResourceLocation("minecraft", "iron_nuggets_from_ingots"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COPPER_NUGGET.get())
                .pattern("CCC")
                .pattern("CCC")
                .pattern("CCC")
                .define('C', ModItems.COPPER_NODULE.get())
                .unlockedBy(getHasName(ModItems.COPPER_NODULE.get()), has(ModItems.COPPER_NODULE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COPPER_INGOT)
                .pattern("CCC")
                .pattern("CCC")
                .pattern("CCC")
                .define('C', ModItems.COPPER_NUGGET.get())
                .unlockedBy(getHasName(ModItems.COPPER_NUGGET.get()), has(ModItems.COPPER_NUGGET.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WOODEN_WATERING_CAN.get())
                .pattern("P  ")
                .pattern("PBP")
                .pattern(" P ")
                .define('P', ItemTags.PLANKS)
                .define('B', Items.BOWL)
                .unlockedBy(getHasName(Items.BOWL), has(Items.BOWL))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IRON_WATERING_CAN.get())
                .pattern("P  ")
                .pattern("PBP")
                .pattern(" P ")
                .define('P', Items.IRON_INGOT)
                .define('B', ModItems.WOODEN_WATERING_CAN.get())
                .unlockedBy(getHasName(ModItems.WOODEN_WATERING_CAN.get()), has(ModItems.WOODEN_WATERING_CAN.get()))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GOLD_WATERING_CAN.get())
                .pattern("P  ")
                .pattern("PBP")
                .pattern(" P ")
                .define('P', Items.GOLD_INGOT)
                .define('B', ModItems.IRON_WATERING_CAN.get())
                .unlockedBy(getHasName(ModItems.IRON_WATERING_CAN.get()), has(ModItems.IRON_WATERING_CAN.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIAMOND_WATERING_CAN.get())
                .pattern("P  ")
                .pattern("PBP")
                .pattern(" P ")
                .define('P', Items.DIAMOND)
                .define('B', ModItems.GOLD_WATERING_CAN.get())
                .unlockedBy(getHasName(ModItems.GOLD_WATERING_CAN.get()), has(ModItems.GOLD_WATERING_CAN.get()))
                .save(pWriter);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BAMBOO_TAP_BLOCK.get())
                .pattern("BB ")
                .pattern("BC ")
                .pattern("BC ")
                .define('B', Blocks.BAMBOO)
                .define('C', Items.BOWL)
                .unlockedBy(getHasName(Blocks.BAMBOO), has(Blocks.BAMBOO))
                .unlockedBy(getHasName(Items.BOWL), has(Items.BOWL))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SPRINKLER_BLOCK.get())
                .pattern("IWI")
                .pattern( " B ")
                .pattern(" B ")
                .define('B', Blocks.BAMBOO)
                .define('I', Items.IRON_NUGGET)
                .define('W', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .unlockedBy(getHasName(Blocks.BAMBOO), has(Blocks.BAMBOO))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.WATER_TANK_BLOCK.get(), 1)
                .requires(Items.BARREL)
                .requires(Items.BOWL)
                .unlockedBy("has_barrel", has(Items.BARREL)) // Recipe unlock condition
                .unlockedBy("has_bucket", has(Items.BOWL)) // Another unlock condition
                .save(pWriter); // Save with the appropriate namespace

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Blocks.GRASS_BLOCK, 1)
                .requires(Items.GRASS)
                .requires(Items.GRASS)
                .requires(Items.GRASS)
                .requires(Items.GRASS)
                .unlockedBy("has grass", has(Items.GRASS))
                .save(pWriter);

//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SAPPHIRE.get(), 9)
//                .requires(ModBlocks.SAPPHIRE_BLOCK.get())
//                .unlockedBy(getHasName(ModBlocks.SAPPHIRE_BLOCK.get()), has(ModBlocks.SAPPHIRE_BLOCK.get()))
//                .save(pWriter);
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                            pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer,  ImmersionEarlyDays.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

}
