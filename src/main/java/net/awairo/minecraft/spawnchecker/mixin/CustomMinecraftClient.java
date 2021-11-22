package net.awairo.minecraft.spawnchecker.mixin;

import net.awairo.minecraft.spawnchecker.SpawnChecker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class CustomMinecraftClient {

    @Inject(method = "joinWorld", at = @At("RETURN"), cancellable = true)
    public void onRender(ClientWorld world, CallbackInfo info) {
        SpawnChecker.instance().onWorldLoad(world);
    }

}
