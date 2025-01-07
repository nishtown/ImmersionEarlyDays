package net.nishtown.immersionearlydays.block;

import com.google.common.collect.Lists;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.block.custom.*;
import net.nishtown.immersionearlydays.item.ModItems;
import net.nishtown.immersionearlydays.worldgen.tree.GiantBirchGrower;
import net.nishtown.immersionearlydays.worldgen.tree.GiantOakGrower;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ImmersionEarlyDays.MOD_ID);


    //START OF MY MOD ITEMS

    public  static  final  RegistryObject<Block> CRUSHED_STONE_BLOCK = registerBlock("crushed_stone_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE)));
    public  static  final  RegistryObject<Block> COARSE_GRAVEL_BLOCK = registerBlock("coarse_gravel_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE)
                    .sound(SoundType.GRAVEL)
                    .strength(0.6F)));
    public  static  final  RegistryObject<Block> DAMP_GRAVEL_BLOCK = registerBlock("damp_gravel_block",
            () -> new DampGravelBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL)
                    .randomTicks()));
    public  static  final  RegistryObject<Block> STONE_WASHER_BLOCK = registerBlock("stone_washer_block",
            () -> new StoneWasherBlock(BlockBehaviour.Properties.copy(Blocks.DIRT)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public  static  final  RegistryObject<Block> BAMBOO_TAP_BLOCK = registerBlock("bamboo_tap_block",
            () -> new BambooTapBlock(BlockBehaviour.Properties.copy(Blocks.DIRT)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

   public  static final RegistryObject<Block> ANIMAL_GATE_BLOCK = registerBlock( "animal_gate_block",
            () -> new AnimalGateBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));


   public static final RegistryObject<Block> GIANT_BIRCH_SAPLING = registerBlock("giant_birch_sapling",
           () -> new SaplingBlock(new GiantBirchGrower(), BlockBehaviour.Properties.copy(Blocks.BIRCH_SAPLING)));


    public static final RegistryObject<Block> GIANT_OAK_SAPLING = registerBlock("giant_oak_sapling",
            () -> new SaplingBlock(new GiantOakGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> WATER_TANK_BLOCK = registerBlock("water_tank",
            () -> new WaterTankBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)));

    public static final RegistryObject<Block> SPRINKLER_BLOCK = registerBlock("sprinkler",
            () -> new SprinklerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));



    public static final List<RegistryObject<Block>> ALL_BLOCKS = Lists.newArrayList(
            CRUSHED_STONE_BLOCK,
            COARSE_GRAVEL_BLOCK,
            STONE_WASHER_BLOCK,
            DAMP_GRAVEL_BLOCK,
            BAMBOO_TAP_BLOCK,
            ANIMAL_GATE_BLOCK,
            WATER_TANK_BLOCK,
            SPRINKLER_BLOCK
    );


    private  static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private  static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public  static void register(IEventBus eventbus)
    {
        BLOCKS.register(eventbus);
    }
}
