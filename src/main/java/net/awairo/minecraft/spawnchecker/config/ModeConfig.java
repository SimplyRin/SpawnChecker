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

import net.awairo.minecraft.spawnchecker.api.Brightness;
import net.awairo.minecraft.spawnchecker.api.Mode;
import net.awairo.minecraft.spawnchecker.api.ScanRange.Horizontal;
import net.awairo.minecraft.spawnchecker.api.ScanRange.Vertical;
import net.awairo.minecraft.spawnchecker.mode.UpdateTimer.Interval;

import lombok.NonNull;

import static net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig.*;

public final class ModeConfig {
    private static final String PATH = "mode";

    private final ConfigHolder holder;

    ModeConfig(@NonNull ConfigHolder holder) {
        this.holder = holder;

        selectedModeNameValue = holder.config().getString(PATH + ".selectedMode", "spawnchecker.mode.spawnchecker");
        checkIntervalValue = holder.config().getInt(PATH + ".scan.interval", 500);
        horizontalRangeValue = holder.config().getInt(PATH + ".scan.horizontalRange", 3);
        verticalRangeValue = holder.config().getInt(PATH + ".scan.verticalRange", 5);
        brightnessValue = holder.config().getInt(PATH + ".marker.brightness", 0);
    }

    // region Selected mode name

    private final String selectedModeNameValue;

    public Mode.Name selectedModeName() {
        return new Mode.Name(selectedModeNameValue);
    }

    public UpdateResult selectedModeName(Mode.Name name) {
        holder.config().set(PATH + ".selectedMode", name);
        return UpdateResult.CHANGED;
    }

    // endregion

    // region Check interval

    private final int checkIntervalValue;
    private Interval checkIntervalValueCache;

    public Interval checkInterval() {
        var cache = checkIntervalValueCache;
        if (cache == null || cache.milliSeconds() != checkIntervalValue) {
            checkIntervalValueCache = cache = Interval.ofMilliSeconds(checkIntervalValue);
        }
        return cache;
    }
    // endregion

    // region Horizontal scan ranges
    private int horizontalRangeValue;

    public Horizontal horizontalRange() {
        return Horizontal.of(horizontalRangeValue);
    }

    public UpdateResult horizontalRange(Horizontal newValue) {
        holder.config().set(PATH + ".scan.horizontalRange", newValue.value());
        this.horizontalRangeValue = newValue.value();
        return UpdateResult.CHANGED;
    }

    // endregion

    // region Vertical scan ranges
    private int verticalRangeValue;

    public Vertical verticalRange() {
        return Vertical.of(verticalRangeValue);
    }

    public UpdateResult verticalRange(Vertical newValue) {
        holder.config().set(PATH + ".scan.verticalRange", newValue.value());
        this.verticalRangeValue = newValue.value();
        return UpdateResult.CHANGED;
    }
    // endregion

    // region Brightness
    private int brightnessValue;

    public Brightness brightness() {
        return Brightness.valueOf(this.brightnessValue);
    }

    public UpdateResult brightness(Brightness newValue) {
        holder.config().set(PATH + ".marker.brightness", newValue.value());
        this.brightnessValue = newValue.value();
        return UpdateResult.CHANGED;
    }
    // endregion

}
