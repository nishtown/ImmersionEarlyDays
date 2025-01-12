package net.nishtown.immersionearlydays.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.item.ModItems;

import java.util.Random;

public class PebbleDropHandler {
    private static final Random RANDOM = new Random();
    private static final double DROP_CHANCE = 0.1; // 10% drop chance

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new PebbleDropHandler());
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getLevel() instanceof ServerLevel plevel)) {
            return; // Ensure server-side execution
        }
        var player = event.getEntity();
        // Ensure the player's hand is empty
        if (!player.getMainHandItem().isEmpty()) {
            return; // Exit if the hand is not empty
        }

        // Access the player's persistent data
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag modData = persistentData.getCompound(ImmersionEarlyDays.MOD_ID);
        var res = modData.getBoolean(CobblestoneGeneratorHandler.COBBLE_GEN_KEY);

        if (!res) {
            var level = event.getLevel();
            // Check if the block is dirt or grass block
            BlockPos pos = event.getPos();
            var block = level.getBlockState(pos).getBlock();
            if (block == Blocks.DIRT || block == Blocks.GRASS_BLOCK) {
                // Force hand swing animation
                if (!level.isClientSide()) {
                    // Ensure the swing happens on both server and client
                    player.swing(InteractionHand.MAIN_HAND, true);
                }
                // Check if the random chance allows a drop
                if (RANDOM.nextDouble() < DROP_CHANCE) {
                    // Drop a Stone Pebble
                    var pebble = new ItemStack(ModItems.STONE_PEBBLE.get(), 1);

                    if (!player.isCreative()) {
                        level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, pebble));
                    }
                }
            }
        }
    }
}