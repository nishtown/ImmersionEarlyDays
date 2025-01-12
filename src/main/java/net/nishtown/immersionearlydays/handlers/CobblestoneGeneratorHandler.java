package net.nishtown.immersionearlydays.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;

public class CobblestoneGeneratorHandler {
    private static boolean cobblestoneGeneratorExists = false; // Global flag
    public static final String COBBLE_GEN_KEY = "CobbleGeneratorDetected";

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new CobblestoneGeneratorHandler());
    }

    @SubscribeEvent
    public void onBlockUpdate(BlockEvent.NeighborNotifyEvent event) {
        if (event.getLevel().isClientSide()) return;

        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);

        // Check if the block is cobblestone
        if (state.getBlock() == Blocks.COBBLESTONE) {
            // Check if a generator is formed
            if (isCobblestoneGenerator((Level) event.getLevel(), pos)) {
                cobblestoneGeneratorExists = true; // Set the global flag
                var player = event.getLevel().getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 15, false);
                if (player != null) {
                    CompoundTag persistentData = player.getPersistentData();
                    CompoundTag modData = persistentData.getCompound(ImmersionEarlyDays.MOD_ID);
                    modData.putBoolean(COBBLE_GEN_KEY, true);
                    // Save the updated modData back to persistentData
                    persistentData.put(ImmersionEarlyDays.MOD_ID, modData);


                }
            }
        }
    }

    private boolean isCobblestoneGenerator(Level world, BlockPos pos) {
        boolean hasWater = false;
        boolean hasLava = false;

        for (Direction direction : Direction.values()) {
            BlockState neighbor = world.getBlockState(pos.relative(direction));
            if (neighbor.getBlock() == Blocks.WATER || neighbor.getFluidState().isSource()) {
                hasWater = true;
            } else if (neighbor.getBlock() == Blocks.LAVA || neighbor.getFluidState().isSource()) {
                hasLava = true;
            }
        }

        return hasWater && hasLava;
    }

    public static boolean hasCobblestoneGenerator() {
        return cobblestoneGeneratorExists;
    }
}