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

package com.shc.silenceengine.tests;

import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.programs.DynamicProgram;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Touch;
import com.shc.silenceengine.io.FilePath;

/**
 * @author Sri Harsha Chilakapati
 */
public class ResourceLoaderTest extends SilenceTest
{
    private ResourceLoader loader;
    private OrthoCam       camera;

    private Texture texture;
    private Sound   sound;

    private DynamicRenderer renderer;
    private DynamicProgram  program;

    private long texID, sndID;

    private boolean loaded = false;

    @Override
    public void init()
    {
        SilenceEngine.display.setTitle("ResourceLoaderTest");
        camera = new OrthoCam();

        renderer = new DynamicRenderer();
        loader = new ResourceLoader();

        texID = loader.define(Texture.class, FilePath.getResourceFile("test_resources/test_texture.png"));
        sndID = loader.define(Sound.class, FilePath.getResourceFile("test_resources/shoot.wav"));

        DynamicProgram.create(program -> {
            this.program = program;
            program.use();

            program.applyToRenderer(renderer);

            loader.start();
        });

        GLContext.clearColor(Color.DARK_RED);
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        if (!loaded)
        {
            if (loader.isDone())
            {
                texture = loader.get(texID);
                sound = loader.get(sndID);

                GLContext.clearColor(Color.BLACK);
                loaded = true;
            }
        }
        else
        {
            if (Keyboard.isKeyTapped(Keyboard.KEY_SPACE) || Touch.isFingerTapped(Touch.FINGER_0))
                sound.play();
        }
    }

    @Override
    public void render(float delta)
    {
        camera.apply();

        if (loaded)
        {
            texture.bind();
            program.use();

            renderer.begin(Primitive.TRIANGLE_FAN);
            {
                renderer.vertex(100, 100);
                renderer.texCoord(texture.getMinU(), texture.getMinV());

                renderer.vertex(300, 100);
                renderer.texCoord(texture.getMaxU(), texture.getMinV());

                renderer.vertex(300, 300);
                renderer.texCoord(texture.getMaxU(), texture.getMaxV());

                renderer.vertex(100, 300);
                renderer.texCoord(texture.getMinU(), texture.getMaxV());
            }
            renderer.end();
        }
    }

    @Override
    public void dispose()
    {
        loader.disposeAll();
        program.dispose();
        renderer.dispose();
    }
}
