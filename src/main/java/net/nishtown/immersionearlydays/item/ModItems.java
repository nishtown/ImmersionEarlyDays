package net.nishtown.immersionearlydays.item;

import com.google.common.collect.Lists;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.item.custom.MetalDetectorItem;
import net.nishtown.immersionearlydays.item.custom.WateringCanItem;

import java.util.*;

public class ModItems {
    public  static  final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ImmersionEarlyDays.MOD_ID);

    public  static  final RegistryObject<Item> METAL_DETECTOR = ITEMS.register("metal_detector",
            () -> new MetalDetectorItem(new Item.Properties().durability(100)));

    public  static  final RegistryObject<Item> WOODEN_WATERING_CAN = ITEMS.register("wooden_watering_can",
            () -> new WateringCanItem(500,0,0.35,new Item.Properties().stacksTo(1)));
    public  static  final RegistryObject<Item> IRON_WATERING_CAN = ITEMS.register("iron_watering_can",
            () -> new WateringCanItem(1000,0,0.55,new Item.Properties().stacksTo(1)));
    public  static  final RegistryObject<Item> GOLD_WATERING_CAN = ITEMS.register("gold_watering_can",
            () -> new WateringCanItem(8000, 1,0.75,new Item.Properties().stacksTo(1)));
    public  static  final RegistryObject<Item> DIAMOND_WATERING_CAN = ITEMS.register("diamond_watering_can",
            () -> new WateringCanItem(20000, 2,0.90,new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> COPPER_NODULE = ITEMS.register("copper_nodule",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_NODULE = ITEMS.register("iron_nodule",
            () -> new Item(new Item.Properties()));


    //RECIPES GO IN MODRECIPEPROVDER

    public static final List<RegistryObject<Item>> ALL_ITEMS = Lists.newArrayList(
            METAL_DETECTOR,
            IRON_NODULE,
            COPPER_NODULE,
            COPPER_NUGGET,
            WOODEN_WATERING_CAN,
            IRON_WATERING_CAN,
            GOLD_WATERING_CAN,
            DIAMOND_WATERING_CAN
    );

    public static final List<RegistryObject<Item>> WATERING_CANS = Lists.newArrayList(
            WOODEN_WATERING_CAN,
            IRON_WATERING_CAN,
            GOLD_WATERING_CAN,
            DIAMOND_WATERING_CAN
    );

    public  static void regiser(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
