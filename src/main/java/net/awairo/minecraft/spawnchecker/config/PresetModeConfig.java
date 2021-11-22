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

import lombok.NonNull;

public final class PresetModeConfig {
    private static final String PATH = "preset_mode";

    private final ConfigHolder holder;

    PresetModeConfig(@NonNull ConfigHolder holder) {
        this.holder = holder;

        drawGuideline = holder.config().getBoolean(PATH + ".drawGuideline");
    }

    // region [preset_mode] guideline

    private boolean drawGuideline;

    public boolean drawGuideline() {
        return drawGuideline;
    }

    public UpdateResult guidelineOn() {
        this.drawGuideline = true;
        holder.config().set(PATH + ".drawGuideline", true);
        return UpdateResult.CHANGED;
    }

    public UpdateResult guidelineOff() {
        this.drawGuideline = false;
        holder.config().set(PATH + ".drawGuideline", false);
        return UpdateResult.CHANGED;
    }

    // endregion
}
