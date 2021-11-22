package net.awairo.minecraft.spawnchecker.mc;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

public class SCRenderSystem {

    public static void pushMatrix() {
        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        GL11.glPushMatrix();
    }

    public static void translated(double x, double y, double z) {
        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        GL11.glTranslated(x, y, z);
    }

    public static void scalef(float x, float y, float z) {
        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        GL11.glScalef(x, y, z);
    }

    public static void popMatrix() {
        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        GL11.glPopMatrix();
    }

    public static void color4f(float red, float green, float blue, float alpha) {
        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);

        GlStateManager.
        if (red != COLOR.red || green != COLOR.green || blue != COLOR.blue || alpha != COLOR.alpha) {
            COLOR.red = red;
            COLOR.green = green;
            COLOR.blue = blue;
            COLOR.alpha = alpha;
            GL11.glColor4f(red, green, blue, alpha);
        }
    }

}
