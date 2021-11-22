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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.awairo.minecraft.spawnchecker.rin.Config;
import net.md_5.bungee.config.Configuration;

import java.io.File;

public final class ConfigHolder {

    private final File file;

    @Getter
    private Configuration config;

    public ConfigHolder(File file) {
        this.file = file;

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (Exception e) {
            }
        }
    }

    public Configuration loadConfig() {
        if (this.config == null) {
            this.config = Config.getConfig(this.file);
        }
        return this.config;
    }

    public void saveConfig() {
        Config.saveConfig(this.config, this.file);
    }

}
