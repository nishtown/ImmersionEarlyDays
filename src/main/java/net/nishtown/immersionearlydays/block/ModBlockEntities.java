package net.nishtown.immersionearlydays.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.block.entity.*;

public class ModBlockEntities {
    public  static  final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ImmersionEarlyDays.MOD_ID);

    public static final RegistryObject<BlockEntityType<StoneWasherBlockEntity>> STONE_WASHER_BE =
            BLOCK_ENTITIES.register("stone_washer_be", () ->
                    BlockEntityType.Builder.of(StoneWasherBlockEntity::new,
                            ModBlocks.STONE_WASHER_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<BambooTapBlockEntity>> BAMBOO_TAP_BE =
            BLOCK_ENTITIES.register("bamboo_tap_be", () ->
                    BlockEntityType.Builder.of(BambooTapBlockEntity::new,
                            ModBlocks.BAMBOO_TAP_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<AnimalGateBlockEntity>> ANIMAL_GATE_BE =
            BLOCK_ENTITIES.register("animal_gate_be", () ->
                    BlockEntityType.Builder.of(AnimalGateBlockEntity::new,
                            ModBlocks.ANIMAL_GATE_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<WaterTankBlockEntity>> WATER_TANK_BE =
            BLOCK_ENTITIES.register("water_tank_be", () ->
                    BlockEntityType.Builder.of(WaterTankBlockEntity::new, ModBlocks.WATER_TANK_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<SprinklerBlockEntity>> SPRINKLER_BE =
            BLOCK_ENTITIES.register("sprinkler_be", () ->
                    BlockEntityType.Builder.of(SprinklerBlockEntity::new, ModBlocks.SPRINKLER_BLOCK.get()).build(null));


    public  static  void  register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
