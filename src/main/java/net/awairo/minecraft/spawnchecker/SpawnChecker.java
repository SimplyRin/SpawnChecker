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
import net.minecraft.client.MinecraftClient;

import net.awairo.minecraft.spawnchecker.config.ConfigHolder;
import net.awairo.minecraft.spawnchecker.mode.SpawnCheckMode;

import lombok.extern.log4j.Log4j2;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Getter
@Log4j2
// @Mod(SpawnChecker.MOD_ID)
public class SpawnChecker {

    @Getter
    private static SpawnChecker instance;

    public static final String MOD_ID = "spawnchecker";

    private final WrappedProfiler profiler;
    private final ConfigHolder configHolder;
    private final SpawnCheckerState state;

    private File configFile;

    public SpawnChecker() {
        instance = this;

        File folder = new File("config");
        this.configFile = new File(folder, "config.yml");

        log.info("SpawnChecker initializing.");

        var minecraft = MinecraftClient.getInstance();
        this.profiler = new WrappedProfiler(minecraft.getProfiler());

        this.configHolder = new ConfigHolder(configFile);

        this.state = new SpawnCheckerState(minecraft, config);

        this.state.modeState()
            .add(new SpawnCheckMode(config.presetModeConfig()));
        // FIXME: not implemented X(
        //            .add(new SlimeCheckMode())
        //            .add(new SpawnerVisualizerMode());

        // register events

        ModLoadingContext.get()
            .registerConfig(Type.CLIENT, configSpec);

        // region Add event listeners
        // Mod lifecycle events
        modBus.addListener(this::onFMLClientSetup);

        // World load/unload
        forgeBus.addListener(this::onWorldLoad);
        forgeBus.addListener(this::onWorldUnload);

        // GUI connecting hook
        forgeBus.addListener(this::onGuiOpenEvent);

        // Tick events
        forgeBus.addListener(this::onClientTick);
        forgeBus.addListener(this::onRenderTick);
        forgeBus.addListener(this::onRenderWorldLast);
        // endregion

        log.info("SpawnChecker initialized.");
    }

    // region [FML] Mod lifecycle events

    private void onFMLClientSetup(FMLClientSetupEvent event) {
        log.info("[spawnchecker] onFMLClientSetup({})", event);
        this.state.keyBindingStates().bindings()
            .forEach(ClientRegistry::registerKeyBinding);
    }

    // endregion

    // region [FML] Mod config events

    // endregion

    // region [Forge] World events

    public void onGuiOpenEvent(String message, CallbackInfo info) {
        if (!message.startsWith("/spawnchecker")) {
            return;
        }

        if (state.commands().parse(message)) {
            info.cancel();
            log.debug("cancel '/spawnchecker' command chat.");
        }
    }

    private void onWorldLoad(WorldEvent.Load event) {
        state.loadWorld(event.getWorld());
    }

    private void onWorldUnload(WorldEvent.Unload event) {
        state.unloadWorld(event.getWorld());
    }

    // endregion

    // region [Forge] Tick/Render events

    private void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == Phase.START) {
            state.onTickStart();
        }
        if (event.phase == Phase.END && state.started()) {
            profiler.startClientTick();
            state.onTickEnd();
            profiler.endClientTick();
        }
    }

    private void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == Phase.END && state.started()) {
            profiler.startRenderHud();
            state.renderHud(event.renderTickTime);
            profiler.endRenderHud();
        }
    }

    private void onRenderWorldLast(RenderWorldLastEvent event) {
        if (state.started()) {
            profiler.startRenderMarker();
            state.modeState().renderMarkers(event.getContext(), event.getPartialTicks(), event.getMatrixStack());
            profiler.endRenderMarker();
        }
    }

    // endregion

}
