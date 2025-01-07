package net.nishtown.immersionearlydays.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;

public class ModTags {

    public  static  class Blocks {

        public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");
        public static final TagKey<Block> WATERING_CAN_TICKABLE = tag("watering_can_tickable");


        private  static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(ImmersionEarlyDays.MOD_ID, name));
        }

    }

    public  static  class Items {

        public static  final TagKey<Item> COPPER_NUGGET = tag("copper_nugget");

        private  static  TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(ImmersionEarlyDays.MOD_ID, name));
        }
    }
}


