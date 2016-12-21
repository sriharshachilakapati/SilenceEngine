/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shc.silenceengine.utils;

import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.fonts.BitmapFont;
import com.shc.silenceengine.graphics.fonts.BitmapFontRenderer;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.programs.DynamicProgram;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.functional.SimpleCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public class ResourceLoadingState extends GameState
{
    private DynamicProgram dynamicProgram;
    private OrthoCam camera;

    private ResourceLoader loader;
    private SimpleCallback doneCallback;

    private BitmapFontRenderer fontRenderer;
    private DynamicRenderer    dynamicRenderer;

    private BitmapFont font;

    public ResourceLoadingState(ResourceLoader loader, SimpleCallback doneCallback)
    {
        this.camera = new OrthoCam(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
        this.loader = loader;
        this.doneCallback = doneCallback;
        this.dynamicRenderer = new DynamicRenderer();

        BitmapFont.load(FilePath.getResourceFile("engine_resources/fonts/roboto32px.fnt"), font -> this.font = font);
        BitmapFontRenderer.create(fontRenderer -> this.fontRenderer = fontRenderer);
        DynamicProgram.create(dynamicProgram ->
        {
            this.dynamicProgram = dynamicProgram;
            dynamicProgram.applyToRenderer(dynamicRenderer);
        });
    }

    @Override
    public void update(float delta)
    {
        if (!loader.isActive() && fontRenderer != null && font != null && dynamicProgram != null)
            loader.start();

        if (loader.isDone())
            doneCallback.invoke();
    }

    @Override
    public void render(float delta)
    {
        if (dynamicProgram == null)
            return;

        camera.apply();
        Texture.EMPTY.bind(0);

        dynamicProgram.use();
        float percentage = MathUtils.convertRange(loader.getPercentage(), 0, 100, 100, SilenceEngine.display.getWidth() - 100);

        dynamicRenderer.begin(Primitive.TRIANGLE_FAN);
        {
            dynamicRenderer.vertex(100, SilenceEngine.display.getHeight() - 110);
            dynamicRenderer.color(Color.DARK_GREEN);

            dynamicRenderer.vertex(percentage, SilenceEngine.display.getHeight() - 110);
            dynamicRenderer.color(Color.GREEN);

            dynamicRenderer.vertex(percentage, SilenceEngine.display.getHeight() - 70);
            dynamicRenderer.color(Color.GREEN);

            dynamicRenderer.vertex(100, SilenceEngine.display.getHeight() - 70);
            dynamicRenderer.color(Color.DARK_GREEN);
        }
        dynamicRenderer.end();

        if (fontRenderer == null || font == null)
            return;

        fontRenderer.begin();
        fontRenderer.render(font, "Loading " + loader.getPercentage(), 110, SilenceEngine.display.getHeight() - 100, Color.WHITE);
        fontRenderer.end();
    }

    @Override
    public void onLeave()
    {
        if (fontRenderer != null)
            fontRenderer.dispose();

        dynamicRenderer.dispose();

        if (dynamicProgram != null)
            dynamicProgram.dispose();
    }

    @Override
    public void resized()
    {
        camera.initProjection(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
        GLContext.viewport(0, 0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }
}
