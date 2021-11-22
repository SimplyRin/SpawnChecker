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

package net.awairo.minecraft.spawnchecker.api;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public interface Renderer {
    void bindTexture(Identifier texture);

    default Tessellator tessellator() {
        return Tessellator.getInstance();
    }

    default BufferBuilder buffer() {
        return tessellator().getBuffer();
    }

    default void beginQuads(VertexFormat format) {
        begin(VertexFormat.DrawMode.QUADS, format);
    }

    default void begin(VertexFormat.DrawMode drawMode, VertexFormat format) {
        buffer().begin(drawMode, format);
    }

    void addVertex(double x, double y, double z);

    void addVertex(double x, double y, double z, float u, float v);

    void addVertex(double x, double y, double z, Color color);

    void addVertex(double x, double y, double z, float u, float v, Color color);

    default void draw() {
        tessellator().draw();
    }

    float partialTicks();
}
