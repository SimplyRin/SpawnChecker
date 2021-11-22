package net.awairo.minecraft.spawnchecker.mixin;

import net.awairo.minecraft.spawnchecker.SpawnChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class CustomGameRenderer {

    // renderWorld(float tickDelta, long limitTime, MatrixStack matrix)
    @Inject(method = "renderWorld", at = @At("RETURN"), cancellable = true)
    public void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo info) {
        MinecraftClient mc = MinecraftClient.getInstance();
        SpawnChecker.instance().onRenderWorldLast(mc.worldRenderer, tickDelta, matrix);
    }

}
