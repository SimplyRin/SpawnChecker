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

package net.awairo.minecraft.spawnchecker.hud;

import java.util.Objects;
import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.val;
import net.awairo.minecraft.spawnchecker.mixin.CustomInGameHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.realms.gui.screen.RealmsUploadScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import net.awairo.minecraft.spawnchecker.api.Color;
import net.awairo.minecraft.spawnchecker.api.HudData;
import net.awairo.minecraft.spawnchecker.api.HudData.Visibility;
import net.awairo.minecraft.spawnchecker.api.HudRenderer;
import net.awairo.minecraft.spawnchecker.config.SpawnCheckerConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

// TODO: 位置と表示内容の実装
@Log4j2
@RequiredArgsConstructor
public final class HudRendererImpl implements HudRenderer {
    private final MinecraftClient minecraft;
    private final SpawnCheckerConfig config;

    public static MatrixStack matrix = new MatrixStack();

    private static final long UNDEFINED = -1;
    @Nullable
    private HudData hudData = null;
    private long showStartTime = UNDEFINED;
    private int tickCount;
    private float partialTicks;

    @Override
    public int tickCount() {
        return tickCount;
    }

    @Override
    public float partialTicks() {
        return partialTicks;
    }

    @Override
    public TextRenderer fontRenderer() {
        return minecraft.textRenderer;
    }

    @Override
    public void bindTexture(Identifier texture) {
        minecraft.getTextureManager().bindTexture(texture);
    }

    @Override
    public void addVertex(double x, double y, double z) {
        buffer()
            .vertex(x, y, z)
            .next();
    }

    @Override
    public void addVertex(double x, double y, double z, float u, float v) {
        buffer()
            .vertex(x, y, z)
            .texture(u, v)
            .next();
    }

    @Override
    public void addVertex(double x, double y, double z, Color color) {
        buffer()
            .vertex(x, y, z)
            .color(color.red(), color.green(), color.blue(), color.alpha())
            .next();
    }

    @Override
    public void addVertex(double x, double y, double z, float u, float v, Color color) {
        buffer()
            .vertex(x, y, z)
            .color(color.red(), color.green(), color.blue(), color.alpha())
            .texture(u, v)
            .next();
    }

    public void setData(HudData hudData) {
        removeData();
        this.hudData = Objects.requireNonNull(hudData, "hudData");
    }

    private void removeData() {
        this.hudData = null;
        showStartTime = UNDEFINED;
    }

    public void render(int tickCount, float partialTicks) {
        if (hudData == null || minecraft.isPaused()) {
            return;
        }

        this.tickCount = tickCount;
        this.partialTicks = partialTicks;
        long now = Util.nanoTimeSupplier.getAsLong() / 1000000L;
        if (showStartTime == UNDEFINED) {
            showStartTime = now;
        }
        var h = minecraft.getWindow().getScaledHeight();
        var w = minecraft.getWindow().getScaledWidth();

        matrix.push();
        matrix.translate(
            w / 20 + config.hudConfig().xOffset().value(),
            h / 3 + config.hudConfig().yOffset().value(),
            0d
        );
        matrix.scale(1.0f, 1.0f, 1f);
        var hudVisibility = hudData.draw(this, now - showStartTime);
        matrix.pop();

        if (hudVisibility == Visibility.HIDE) {
            removeData();
        }
    }

    public void renderHUD(@SuppressWarnings("unused") float partialTicks) {


        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.enableTexture();

        var x = 0f;
        var y = 50f;
        var r = 0;
        var g = 255;
        var b = 0;
        var a = 127;
        var color = (r << 16) | (g << 8) | b | (a << 24);
        var text = "test";
        var iconResource = HudIconResource.SPAWN_CHECKER;

        drawIcon(minecraft, iconResource, x, y, 255, g, b, a);
        drawTextWithShadow(minecraft, text, x + 30, y, color);
    }

    private void drawTextWithShadow(MinecraftClient game, String text, float x, float y, int color) {
        game.textRenderer.drawWithShadow(CustomInGameHud.matrixStack, text, x, y, color);
    }

    private void drawIcon(MinecraftClient game, HudIconResource resource, float x, float y, int r, int g, int b, int a) {
        final double ltx, rtx, lbx, rbx, lty, rty, lby, rby;

        var iconWidth = 16d;
        var iconHeight = 16d;
        var z = 1d;
        var uMin = 0d;
        var uMax = 1d;
        var vMin = 0d;
        var vMax = 1d;

        ltx = lbx = x + 0d;
        rtx = rbx = x + iconWidth;
        lty = rty = y + iconHeight;
        lby = rby = y + 0d;

        game.getTextureManager().bindTexture(resource.location());

        var tessellator = Tessellator.getInstance();
        var buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        addVertexWithUV(buffer, lbx, lby, z, uMin, vMin, r, g, b, a);
        addVertexWithUV(buffer, ltx, lty, z, uMin, vMax, r, g, b, a);
        addVertexWithUV(buffer, rtx, rty, z, uMax, vMax, r, g, b, a);
        addVertexWithUV(buffer, rbx, rby, z, uMax, vMin, r, g, b, a);
        tessellator.draw();
    }

    private void addVertexWithUV(BufferBuilder buffer, double x, double y, double z, double u, double v, int r, int g, int b, int a) {
        buffer
            .vertex(x, y, z)
            .texture((float) u, (float) v)
            .color(r, g, b, a)
            .next();
    }
}
