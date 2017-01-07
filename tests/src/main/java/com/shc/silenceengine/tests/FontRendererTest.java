/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.fonts.BitmapFont;
import com.shc.silenceengine.graphics.fonts.BitmapFontRenderer;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;

/**
 * @author Sri Harsha Chilakapati
 */
public class FontRendererTest extends SilenceTest
{
    private BitmapFont bitmapFont;
    private OrthoCam   camera;

    @Override
    public void init()
    {
        SilenceEngine.display.setTitle("FontRendererTest");

        BitmapFont.load(FilePath.getResourceFile("/engine_resources/fonts/roboto32px.fnt"), font ->
                bitmapFont = font);

        camera = new OrthoCam(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
        camera.apply();
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();
    }

    @Override
    public void render(float delta)
    {
        if (bitmapFont == null)
            return;

        BitmapFontRenderer fontRenderer = IGraphicsDevice.Renderers.bitmapFont;

        fontRenderer.begin();
        {
            fontRenderer.render(bitmapFont, "Hello World", 10, 10, Color.RED);
            fontRenderer.render(bitmapFont, "\nFPS: " + SilenceEngine.gameLoop.getFPS(), 10, 10);
            fontRenderer.render(bitmapFont, "\n\nUPS: " + SilenceEngine.gameLoop.getUPS(), 10, 10);
            fontRenderer.render(bitmapFont, "\n\n\nRC: " + IGraphicsDevice.Data.renderCallsThisFrame, 10, 10);
        }
        fontRenderer.end();
    }

    @Override
    public void resized()
    {
        camera.initProjection(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
        camera.apply();

        GLContext.viewport(0, 0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    @Override
    public void dispose()
    {
        if (bitmapFont != null)
            bitmapFont.dispose();
    }
}
