package net.nishtown.immersionearlydays.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.worldgen.ModPlacedFeatures;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ImmersionEarlyDays.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SaplingGrowthHandler {

    private static final Map<Block, ResourceKey<PlacedFeature>> SAPLING_FEATURE_MAP = Map.of(
            Blocks.OAK_SAPLING, ModPlacedFeatures.GIANT_OAK_PLACED_KEY,
            Blocks.BIRCH_SAPLING, ModPlacedFeatures.GIANT_BIRCH_PLACED_KEY,
            Blocks.ACACIA_SAPLING, ModPlacedFeatures.GIANT_ACACIA_PLACED_KEY
    );


    public SaplingGrowthHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onSaplingGrow(SaplingGrowTreeEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return; // Ensure server-side execution
        }

        BlockPos triggeringSapling = event.getPos();
        BlockState state = level.getBlockState(triggeringSapling);

        if (SAPLING_FEATURE_MAP.containsKey(state.getBlock()))
        {
            Block sapling = state.getBlock();
            // Attempt to detect a 2x2 grid
            BlockPos bottomLeft = getBottomLeftSapling(level, triggeringSapling, sapling);

            if (bottomLeft != null) {
                // Suppress default growth
                event.setResult(Event.Result.DENY);
                growGiantTree(level, bottomLeft, sapling);

            }

        }

    }

    private static BlockPos getBottomLeftSapling(ServerLevel level, BlockPos triggeringSapling, Block saplingType) {

        if (level.getBlockState(triggeringSapling.north()).is(saplingType) && level.getBlockState(triggeringSapling.north().east()).is(saplingType) && level.getBlockState(triggeringSapling.east()).is(saplingType))
            return triggeringSapling;
        if (level.getBlockState(triggeringSapling.north()).is(saplingType) && level.getBlockState(triggeringSapling.north().west()).is(saplingType) && level.getBlockState(triggeringSapling.west()).is(saplingType))
            return triggeringSapling.west();
        if (level.getBlockState(triggeringSapling.south()).is(saplingType) && level.getBlockState(triggeringSapling.south().west()).is(saplingType) && level.getBlockState(triggeringSapling.west()).is(saplingType))
            return triggeringSapling.south().west();
        if (level.getBlockState(triggeringSapling.south()).is(saplingType) && level.getBlockState(triggeringSapling.south().east()).is(saplingType) && level.getBlockState(triggeringSapling.east()).is(saplingType))
            return triggeringSapling.south();


        return null;
    }



    private static void growGiantTree(ServerLevel level, BlockPos bottomLeft, Block sapling) {
        ResourceKey<PlacedFeature> featureKey = SAPLING_FEATURE_MAP.get(sapling);

        if (featureKey == null) return;

        PlacedFeature feature = level.registryAccess()
                .registryOrThrow(Registries.PLACED_FEATURE)
                .get(featureKey);


        if (feature != null) {
            // Clear saplings before placement
            clearSaplings(level, bottomLeft);

            // Attempt to place the feature
            boolean success = feature.place(level, level.getChunkSource().getGenerator(), level.getRandom(), bottomLeft.north());

        }
    }


    private static void clearSaplings(ServerLevel level, BlockPos bottomLeft) {
        BlockPos[] saplingPositions = {
                bottomLeft,                // Bottom-left
                bottomLeft.east(),         // Bottom-right
                bottomLeft.north(),        // Top-left
                bottomLeft.north().east()  // Top-right
        };

        for (BlockPos saplingPos : saplingPositions) {
            if (level.getBlockState(saplingPos).is(Blocks.OAK_SAPLING) || level.getBlockState(saplingPos).is(Blocks.BIRCH_SAPLING)) {
                level.setBlock(saplingPos, Blocks.AIR.defaultBlockState(), 2);
            }
        }
    }

}

