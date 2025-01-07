package net.nishtown.immersionearlydays.handlers;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;

@Mod.EventBusSubscriber(modid = "immersion_early_days", value = Dist.CLIENT)
public class KeyInputHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        // Check if the player is in a world
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.level != null) {
            // Handle the key press
            if (KeybindHandler.CUSTOM_KEY.isDown()) {
                // Toggle the ClientState.showBlockPos flag
                ImmersionEarlyDays.ClientState.showBlockPos = !ImmersionEarlyDays.ClientState.showBlockPos;

            }
        }
    }
}