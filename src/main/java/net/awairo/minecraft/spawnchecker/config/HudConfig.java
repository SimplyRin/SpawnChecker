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

import net.awairo.minecraft.spawnchecker.api.HudData.ShowDuration;
import net.awairo.minecraft.spawnchecker.hud.HudOffset;

import lombok.NonNull;

import static net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig.*;

public final class HudConfig {
    private static final String PATH = "hud";

    private final ConfigHolder holder;

    HudConfig(@NonNull ConfigHolder holder) {
        this.holder = holder;

        showDurationValue = holder.config().getLong(PATH + ".showDuration", 5000);
        xOffsetValue = holder.config().getInt(PATH + ".xOffset", 0);
        yOffsetValue = holder.config().getInt(PATH + ".topOffset", 0);
    }

    // region [hud] HudShowDuration

    private final long showDurationValue;
    private ShowDuration showDurationCache = null;

    public ShowDuration showDuration() {
        var cache = showDurationCache;
        if (cache == null || cache.milliSeconds() != showDurationValue)
            cache = showDurationCache = new ShowDuration(showDurationValue);
        return cache;
    }

    // endregion

    private final int xOffsetValue;
    private HudOffset.X xOffsetCache;

    public HudOffset.X xOffset() {
        var cache = xOffsetCache;
        if (cache == null || cache.value() != xOffsetValue)
            cache = xOffsetCache = HudOffset.xOf(xOffsetValue);
        return cache;
    }

    private final int yOffsetValue;
    private HudOffset.Y yOffsetCache;

    public HudOffset.Y yOffset() {
        var cache = yOffsetCache;
        if (cache == null || cache.value() != yOffsetValue)
            cache = yOffsetCache = HudOffset.yOf(yOffsetValue);
        return cache;
    }
}
