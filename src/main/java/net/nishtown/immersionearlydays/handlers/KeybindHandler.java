package net.nishtown.immersionearlydays.handlers;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeybindHandler {
    // Define the keybinding
    public static final KeyMapping CUSTOM_KEY = new KeyMapping(
            "key.immersion_early_days.block_coords", // Translation key
            GLFW.GLFW_KEY_GRAVE_ACCENT,                         // Default key
            "key.categories.immersion_early_days"    // Category
    );

    // Register the keybinding
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(KeybindHandler::onRegisterKeyMappings);
    }

    private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(CUSTOM_KEY);
    }
}
