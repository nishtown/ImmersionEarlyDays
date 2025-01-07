package net.nishtown.immersionearlydays.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.item.ModItems;
import oshi.jna.platform.mac.SystemB;

public class ModItemModelProvider extends ItemModelProvider      {
    public ModItemModelProvider(PackOutput output,ExistingFileHelper existingFileHelper) {
        super(output, ImmersionEarlyDays.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModItems.ALL_ITEMS.stream()
                .filter(item -> !ModItems.WATERING_CANS.contains(item)) // Exclude watering cans
                .forEach(this::simpleItem); // Register all other items
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ImmersionEarlyDays.MOD_ID, "item/" + item.getId().getPath()));

    }
}
