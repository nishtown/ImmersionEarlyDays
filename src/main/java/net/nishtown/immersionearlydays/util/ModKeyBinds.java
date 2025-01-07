package net.nishtown.immersionearlydays.util;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class ModKeyBinds {
    public static final String CATEGORY = "key.categories.immersionearlydays";
    public static final String TOGGLE_BLOCK_POS_KEY = "key.immersionearlydays.toggle_block_pos";

    public static KeyMapping toggleBlockPosKey;

    public static void register(RegisterKeyMappingsEvent event) {
        toggleBlockPosKey = new KeyMapping(TOGGLE_BLOCK_POS_KEY, GLFW.GLFW_KEY_GRAVE_ACCENT, CATEGORY);
        event.register(toggleBlockPosKey);
    }
}
