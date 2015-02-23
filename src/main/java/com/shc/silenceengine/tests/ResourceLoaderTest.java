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
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class ResourceLoaderTest extends Game
{
    private Texture texture;

    private TrueTypeFont font1;
    private TrueTypeFont font2;

    private OrthoCam cam;

    public static void main(String[] args)
    {
        new ResourceLoaderTest().start();
    }

    public void dispose()
    {
        ResourceLoader.getInstance().dispose();
    }

    public void init()
    {
        ResourceLoader loader = ResourceLoader.getInstance();

        int fontID1 = loader.defineFont("Times New Roman", TrueTypeFont.STYLE_NORMAL, 24);
        int fontID2 = loader.defineFont("resources/FREEBSC_.ttf", TrueTypeFont.STYLE_ITALIC | TrueTypeFont.STYLE_BOLD, 48);
        int textureID = loader.defineTexture("resources/texture2.png");

        loader.startLoading();

        texture = loader.getTexture(textureID);
        font1 = loader.getFont(fontID1);
        font2 = loader.getFont(fontID2);

        cam = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());
    }

    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();
    }

    public void render(float delta, Batcher batcher)
    {
        cam.apply();

        batcher.drawTexture2d(texture, new Vector2(100, 100));

        font1.drawString(batcher, "Times New Roman!!", 10, 10);
        font2.drawString(batcher, "Free Booter Script", 10, 40, Color.GREEN);
    }

    public void resize()
    {
        cam.initProjection(Display.getWidth(), Display.getHeight());
    }
}
