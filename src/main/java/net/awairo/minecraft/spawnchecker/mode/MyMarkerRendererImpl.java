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

package net.awairo.minecraft.spawnchecker.mode;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import net.awairo.minecraft.spawnchecker.api.Color;
import net.awairo.minecraft.spawnchecker.api.MarkerRenderer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import net.minecraft.util.math.Quaternion;

@Value
final class MyMarkerRendererImpl implements MarkerRenderer {
    private final WorldRenderer worldRenderer;
    private final float partialTicks;
    @Getter(AccessLevel.PRIVATE)
    private final MatrixStack matrixStack;
    private final TextureManager textureManager;
    private final EntityRenderDispatcher renderManager;

    @Override
    public void bindTexture(Identifier texture) {
        textureManager.bindTexture(texture);
    }

    @Override
    public void addVertex(double x, double y, double z) {
        buffer()
            .vertex(matrixStack.peek().getModel(), (float) x, (float) y, (float) z)
            .next();
    }

    @Override
    public void addVertex(double x, double y, double z, float u, float v) {
        buffer()
            .vertex(matrixStack.peek().getModel(), (float) x, (float) y, (float) z)
            .texture(u, v)
            .next();
    }

    @Override
    public void addVertex(double x, double y, double z, Color color) {
        buffer()
            .vertex(matrixStack.peek().getModel(), (float) x, (float) y, (float) z)
            .color(color.red(), color.green(), color.blue(), color.alpha())
            .next();
    }

    @Override
    public void addVertex(double x, double y, double z, float u, float v, Color color) {
        buffer()
            .vertex(matrixStack.peek().getModel(), (float) x, (float) y, (float) z)
            .texture(u, v)
            .color(color.red(), color.green(), color.blue(), color.alpha())
            .next();
    }

    @Override
    public void push() {
        matrixStack.push();
    }

    @Override
    public void pop() {
        matrixStack.pop();
    }

    @Override
    public void translate(double x, double y, double z) {
        matrixStack.translate(x, y, z);
    }

    @Override
    public void scale(float m00, float m11, float m22) {
        matrixStack.scale(m00, m11, m22);
    }

    @Override
    public void rotate(Quaternion quaternion) {
        matrixStack.multiply(quaternion);
    }

    @Override
    public void clear() {
        matrixStack.isEmpty();
    }
}
