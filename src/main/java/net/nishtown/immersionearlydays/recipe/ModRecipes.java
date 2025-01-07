package net.nishtown.immersionearlydays.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ImmersionEarlyDays.MOD_ID);

    public static final RegistryObject<RecipeSerializer<StoneWashingRecipe>> STONE_WASHING_SERIALIZER =
            SERIALIZERS.register("stone_washing", () -> StoneWashingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
