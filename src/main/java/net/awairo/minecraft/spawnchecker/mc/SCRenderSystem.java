package net.awairo.minecraft.spawnchecker.mc;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class SCRenderSystem {

    private static boolean initialized = false;
    private static void initialize() {
        if (initialized) {
            return;
        }

        GL.createCapabilities();
        initialized = true;
    }


    public static void pushMatrix() {
        initialize();

        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);

        // GL11.glPushMatrix();
    }

    public static void translated(double x, double y, double z) {
        initialize();

        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        GL11.glTranslated(x, y, z);
    }

    public static void scalef(float x, float y, float z) {
        initialize();

        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        GL11.glScalef(x, y, z);
    }

    public static void popMatrix() {
        initialize();

        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);

        // GL11.glPopMatrix();
    }

    public static float RED = 0F, GREEN = 0F, BLUE = 0F, ALPHA = 0F;
    public static void color4f(float red, float green, float blue, float alpha) {
        initialize();

        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);

        if (red != RED || green != GREEN || blue != BLUE || alpha != ALPHA) {
            RED = red;
            GREEN = green;
            BLUE = blue;
            ALPHA = alpha;

            // GL11.glColor4f(red, green, blue, alpha);
        }
    }

}
