package net.nishtown.immersionearlydays.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import org.jetbrains.annotations.Nullable;

public class StoneWashingRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final NonNullList<RecipeOutput> output;
    private final ResourceLocation id;

    public StoneWashingRecipe(NonNullList<Ingredient> inputItems, NonNullList<RecipeOutput> output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level plevel) {
        if (plevel.isClientSide()) {
            return false;
        }
        return inputItems.get(0).test(pContainer.getItem(0));
    }


    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.get(0).getStack();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.get(0).getStack();
    }

    public NonNullList<RecipeOutput> getResultItems(RegistryAccess pRegistryAccess) {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public  static class Type implements RecipeType<StoneWashingRecipe> {
        public  static  final Type INSTANCE = new Type();
        public  static  final  String ID = "stone_washing";
    }

    public static class Serializer implements RecipeSerializer<StoneWashingRecipe> {
        public  static  final Serializer INSTANCE = new Serializer();
        public  static  final ResourceLocation ID = new ResourceLocation(ImmersionEarlyDays.MOD_ID, "stone_washing");


        @Override
        public StoneWashingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            JsonArray outputs = GsonHelper.getAsJsonArray(pSerializedRecipe, "results");
            NonNullList<RecipeOutput> results = NonNullList.create();

            for(int i = 0; i< outputs.size(); i++)
            {
                results.add(RecipeOutput.fromJson(outputs.get((i))));
            }

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i< inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new StoneWashingRecipe(inputs, results, pRecipeId);
        }

        @Override
        public @Nullable StoneWashingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();
            ItemStack secondaryOutput = pBuffer.readItem();
            NonNullList<RecipeOutput> results = NonNullList.create();

            results.add(new RecipeOutput(output,1));
            results.add(new RecipeOutput(secondaryOutput, 1));
            return new StoneWashingRecipe(inputs, results, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, StoneWashingRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}
