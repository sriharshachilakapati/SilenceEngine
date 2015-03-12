/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
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

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;

/**
 * @author Sri Harsha Chilakapati
 */
public class TextureTest extends Game
{
    private Texture texture;

    public static void main(String[] args)
    {
        new TextureTest().start();
    }

    public void dispose()
    {
        texture.dispose();
    }

    public void init()
    {
        GL3Context.clearColor(Color.CORN_FLOWER_BLUE);

        texture = Texture.fromResource("resources/logo.png");
    }

    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        if (Keyboard.isPressed(Keyboard.KEY_F1))
            Display.setFullScreen(!Display.isFullScreen());
    }

    public void render(float delta, Batcher batcher)
    {
        texture.bind();

        batcher.begin();
        {
            if (Keyboard.isAnyKeyPressed())
            {
                batcher.vertex(-0.5f, 0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(0, 0);

                batcher.vertex(0.5f, 0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(1, 0);

                batcher.vertex(-0.5f, -0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(0, 1);

                batcher.vertex(0.5f, 0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(1, 0);

                batcher.vertex(-0.5f, -0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(0, 1);

                batcher.vertex(0.5f, -0.5f);
                batcher.color(Color.GOLDEN_ROD);
                batcher.texCoord(1, 1);
            }
            else
            {
                batcher.vertex(-0.5f, 0.5f);
                batcher.texCoord(0, 0);

                batcher.vertex(0.5f, 0.5f);
                batcher.texCoord(1, 0);

                batcher.vertex(-0.5f, -0.5f);
                batcher.texCoord(0, 1);

                batcher.vertex(0.5f, 0.5f);
                batcher.texCoord(1, 0);

                batcher.vertex(-0.5f, -0.5f);
                batcher.texCoord(0, 1);

                batcher.vertex(0.5f, -0.5f);
                batcher.texCoord(1, 1);
            }
        }
        batcher.end();
    }

    public void resize()
    {
        GL3Context.viewport(0, 0, Display.getWidth(), Display.getHeight());
    }
}
