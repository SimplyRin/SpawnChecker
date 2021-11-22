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

import net.awairo.minecraft.spawnchecker.keybinding.RepeatDelay;
import net.awairo.minecraft.spawnchecker.keybinding.RepeatRate;

import lombok.NonNull;

public final class KeyConfig {
    private static final String PATH = "key";

    private final ConfigHolder holder;

    KeyConfig(@NonNull ConfigHolder holder) {
        this.holder = holder;

        repeatDelayValue = holder.config().getInt(PATH + ".repeatDelay");
        repeatRateValue = holder.config().getInt(PATH + ".repeatRate");
    }

    // region [key binding] RepeatDelay

    private final int repeatDelayValue;
    private RepeatDelay repeatDelayCache;

    public RepeatDelay repeatDelay() {
        var cache = repeatDelayCache;
        if (cache == null || cache.milliSeconds() != repeatDelayValue)
            repeatDelayCache = cache = RepeatDelay.ofMilliSeconds(repeatDelayValue);
        return cache;
    }

    // endregion

    // region [key binding] RepeatRate

    private final int repeatRateValue;
    private RepeatRate repeatRateCache;

    public RepeatRate repeatRate() {
        var cache = repeatRateCache;
        if (cache == null || cache.milliSeconds() != repeatRateValue)
            repeatRateCache = cache = RepeatRate.ofMilliSeconds(repeatRateValue);
        return cache;
    }

    // endregion

}
