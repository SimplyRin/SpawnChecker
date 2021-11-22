/*
 * SpawnChecker
 * Copyright (C) 2019 alalwww
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.awairo.minecraft.spawnchecker;

import lombok.Getter;
import net.awairo.minecraft.spawnchecker.api.Mode;
import net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;

import net.awairo.minecraft.spawnchecker.config.ConfigHolder;
import net.awairo.minecraft.spawnchecker.mode.SpawnCheckMode;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Getter
// @Mod(SpawnChecker.MOD_ID)
public class SpawnChecker implements ModInitializer {

    @Getter
    private static SpawnChecker instance;

    public static final String MOD_ID = "spawnchecker";

    private WrappedProfiler profiler;
    private ConfigHolder configHolder;
    private SpawnCheckerConfig config;
    private SpawnCheckerState state;

    private File configFile;

    @Override
    public void onInitialize() {
        System.out.println("SpawnChecker initializing...");

        instance = this;

        var folder = new File("config");
        folder.mkdirs();
        this.configFile = new File(folder, "config.yml");

        var minecraft = MinecraftClient.getInstance();
        this.profiler = new WrappedProfiler(minecraft.getProfiler());

        this.configHolder = new ConfigHolder(configFile);
        this.configHolder.loadConfig();

        this.config = new SpawnCheckerConfig(this.configHolder);

        this.state = new SpawnCheckerState(minecraft, this.config);
        this.state.modeState().add(new SpawnCheckMode(config.presetModeConfig()));
        this.state.initialize();
        // FIXME: not implemented X(
        //            .add(new SlimeCheckMode())
        //            .add(new SpawnerVisualizerMode());

        // register events

        // ModLoadingContext.get().registerConfig(Type.CLIENT, configSpec);

        // region Add event listeners
        // Mod lifecycle events
        for (KeyBinding keyBinding : this.state.keyBindingStates().bindings()) {
            KeyBindingHelper.registerKeyBinding(keyBinding);
        }

        // World load/unload
        // forgeBus.addListener(this::onWorldLoad);
        // forgeBus.addListener(this::onWorldUnload);

        // GUI connecting hook

        // Tick events
        ClientTickEvents.START_CLIENT_TICK.register(this::onStartClientTick);
        ClientTickEvents.END_CLIENT_TICK.register(this::onEndClientTick);

        // ClientTickEvents.START_WORLD_TICK.register(this::onRenderTick);
        // ClientTickEvents.END_WORLD_TICK.register(this::onRenderWorldLast);
        // forgeBus.addListener(this::onRenderTick);
        // forgeBus.addListener(this::onRenderWorldLast);
        // endregion

        System.out.println("SpawnChecker initialized.");
    }

    // region [FML] Mod lifecycle events

    // endregion

    // region [FML] Mod config events

    // endregion

    // region [Forge] World events

    public void onClientChat(String message, CallbackInfo info) {
        if (!message.startsWith("/spawnchecker")) {
            return;
        }

        if (this.state.commands().parse(message)) {
            info.cancel();
            System.out.println("cancel '/spawnchecker' command chat.");
        }
    }

    public void onWorldLoad(ClientWorld world) {
        this.state.loadWorld(world);
    }

    public void onWorldUnload(ClientWorld world) {
        this.state.unloadWorld(world);
    }

    // endregion

    // region [Forge] Tick/Render events

    public void onStartClientTick(MinecraftClient client) {
        state.onTickStart();
    }

    public void onEndClientTick(MinecraftClient client) {
        profiler.startClientTick();
        state.onTickEnd();
        profiler.endClientTick();
    }

    // CustomInGameHud
    public void onRenderTick(float renderTickTime) {
        profiler.startRenderHud();
        state.renderHud(renderTickTime);
        profiler.endRenderHud();
    }

    // CustomGameRenderer
    public void onRenderWorldLast(WorldRenderer worldRenderer, float partialTicks, MatrixStack matrixStack) {
        if (state.started()) {
            profiler.startRenderMarker();
            state.modeState().renderMarkers(worldRenderer, partialTicks, matrixStack);
            profiler.endRenderMarker();
        }
    }

    // endregion

}
