package net.awairo.minecraft.spawnchecker.mixin;

import net.awairo.minecraft.spawnchecker.SpawnChecker;
import net.awairo.minecraft.spawnchecker.hud.HudRendererImpl;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class CustomInGameHud {

    public static MatrixStack matrixStack;

    @Inject(method = "render", at = @At("RETURN"), cancellable = true)
    public void onRender(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        matrixStack = matrices;
        HudRendererImpl.matrix = matrices;
        SpawnChecker.instance().onRenderTick(tickDelta);
    }

}
