package net.nishtown.immersionearlydays.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;

@Mod.EventBusSubscriber(modid = "immersionearlydays", value = Dist.CLIENT)
public class CoordinateDisplayHandler {

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent.Pre event) {
        if (ImmersionEarlyDays.ClientState.showBlockPos) {
            Minecraft mc = Minecraft.getInstance();

            if (mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK) {
                // Get the block position
                BlockHitResult blockHit = (BlockHitResult) mc.hitResult;
                BlockPos blockPos = blockHit.getBlockPos();

                // Format the display text
                String text = "Looking at: " + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ();

                // Use GuiGraphics for rendering
                event.getGuiGraphics().drawString(
                        mc.font,
                        Component.literal(text),
                        10, 10, // Screen position
                        0xFFFFFF, // Text color
                        false // Shadow
                );
            }
        }
    }
}