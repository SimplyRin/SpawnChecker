package net.awairo.minecraft.spawnchecker.mixin;

import net.awairo.minecraft.spawnchecker.SpawnChecker;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class CustomClientPlayerEntity {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void onSendChatMessage(String message, CallbackInfo info) {
        if (message != null && message.startsWith("/")) {
            SpawnChecker.instance().onClientChat(message, info);
        }
    }

}
