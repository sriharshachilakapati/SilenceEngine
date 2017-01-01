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
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.programs.DynamicProgram;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.scene.tiled.TmxMap;
import com.shc.silenceengine.scene.tiled.renderers.TmxMapRenderer;

/**
 * @author Sri Harsha Chilakapati
 */
public class TmxMapTest extends SilenceTest
{
    private OrthoCam camera;
    private TmxMap   map;

    private TmxMapRenderer  mapRenderer;
    private DynamicRenderer dynamicRenderer;

    private DynamicProgram dynamicProgram;

    private String path;

    public TmxMapTest(String file)
    {
        this.path = file;
    }

    @Override
    public void init()
    {
        TmxMap.load(FilePath.getResourceFile(path), map ->
        {
            this.map = map;
            camera = new OrthoCam(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());

            dynamicRenderer = new DynamicRenderer(500);

            DynamicProgram.create(dynamicProgram ->
            {
                this.dynamicProgram = dynamicProgram;
                TmxMapRenderer.create(map, renderer -> this.mapRenderer = renderer);
            });

            GLContext.clearColor(Color.GRAY);
        });
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        SilenceEngine.display.setTitle("SilenceEngine TmxMapTest | FPS: " + SilenceEngine.gameLoop.getFPS() +
                                       " | UPS: " + SilenceEngine.gameLoop.getUPS() +
                                       " | RC: " + IGraphicsDevice.Data.renderCallsThisFrame);

        if (mapRenderer != null)
            mapRenderer.update(delta);
    }

    @Override
    public void render(float delta)
    {
        if (mapRenderer == null)
            return;

        camera.apply();

        dynamicProgram.use();
        dynamicProgram.applyToRenderer(dynamicRenderer);
        mapRenderer.render(dynamicRenderer);
    }

    @Override
    public void resized()
    {
        if (mapRenderer == null)
            return;

        camera.initProjection(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
        camera.center(map.getWidth() * map.getTileWidth() / 2, map.getHeight() * map.getTileHeight() / 2);
        GLContext.viewport(0, 0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    @Override
    public void dispose()
    {
        if (mapRenderer == null)
            return;

        mapRenderer.dispose();
        dynamicProgram.dispose();
        dynamicRenderer.dispose();
    }
}
