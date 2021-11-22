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

package net.awairo.minecraft.spawnchecker.config;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import net.awairo.minecraft.spawnchecker.SpawnChecker;
import net.awairo.minecraft.spawnchecker.api.UsingHands;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class SpawnCheckerConfig {

    @Getter
    private final HudConfig hudConfig;
    @Getter
    private final KeyConfig keyConfig;
    @Getter
    private final ModeConfig modeConfig;

    @Getter
    private final PresetModeConfig presetModeConfig;

    private final ConfigHolder holder;

    public SpawnCheckerConfig(ConfigHolder holder) {
        this.holder = holder;

        holder.config().set(SpawnChecker.MOD_ID, "\n SpawnChecker configurations.\n\n   https://github.com/alalwww/SpawnChecker");

        enabledValue = holder.config().getBoolean("enabled", true);
        usingHandsValue = holder.config().getString("usingHands");

        hudConfig = new HudConfig(holder);
        keyConfig = new KeyConfig(holder);
        modeConfig = new ModeConfig(holder);
        presetModeConfig = new PresetModeConfig(holder);
    }

    // region [config] Enabled

    private boolean enabledValue;

    public boolean enabled() {
        return enabledValue;
    }

    public UpdateResult enable() {
        holder.config().set("enabled", true);
        this.enabledValue = true;
        return UpdateResult.CHANGED;
    }

    public UpdateResult disable() {
        holder.config().set("enabled", false);
        this.enabledValue = false;
        return UpdateResult.CHANGED;
    }

    // endregion

    // region [config] usingHands

    private String usingHandsValue;

    public UsingHands usingHand() {
        return UsingHands.vOf(this.usingHandsValue);
    }

    public UpdateResult usingHand(UsingHands value) {
        this.usingHandsValue = value.name();
        holder.config().set("usingHands", value.name());
        return UpdateResult.CHANGED;
    }

    // endregion

    // region utility methods

    private static String configGuiKey(String key) {
        return String.join(".", SpawnChecker.MOD_ID, "config", key);
    }

    static String configGuiKey(String path, String key) {
        return String.join(".", SpawnChecker.MOD_ID, "config", path, key);
    }

    static String defaultMinMax(Object defaultValue, Object min, Object max) {
        return String.format("  default: %s, min: %s, max: %s", defaultValue, min, max);
    }

    static <E extends Enum<E>> String defaultValue(E defaultValue) {
        return "  default: " + defaultValue.name();
    }

    static <E extends Enum<E>> String allowValues(E[] values) {
        return Arrays.stream(values)
            .map(E::name)
            .collect(Collectors.joining(", ", "  values: [", "]"));
    }

    // endregion

}
