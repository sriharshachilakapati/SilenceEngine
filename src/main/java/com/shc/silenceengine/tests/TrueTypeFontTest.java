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
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class TrueTypeFontTest extends Game
{
    private TrueTypeFont font;
    private OrthoCam     cam;
    private Texture      logo;

    public static void main(String[] args)
    {
        new TrueTypeFontTest().start();
    }

    public void dispose()
    {
        font.dispose();
        logo.dispose();
    }

    public void init()
    {
        cam = new OrthoCam();

        logo = Texture.fromResource("resources/logo.png");
        font = new TrueTypeFont("Comic Sans MS", TrueTypeFont.STYLE_ITALIC | TrueTypeFont.STYLE_BOLD, 36);
    }

    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();
    }

    public void render(float delta, Batcher batcher)
    {
        // Draw some text: Warning, requires OrthoCam
        cam.apply();

        // Fonts require texture changes, so pass the batcher in inactive state
        font.drawString(batcher, "Hello World!!", 10, 10);
        font.drawString(batcher, "Colored Text!!", 10, 10 + font.getHeight(), Color.RED);
        font.drawString(batcher, "Multi line\nText!!", 10, 10 + 2 * font.getHeight(), Color.GOLD);

        String fpsString = "FPS: " + getFPS();
        font.drawString(batcher, fpsString, Display.getWidth() - font.getWidth(fpsString) - 10, 10, Color.CORN_FLOWER_BLUE);

        batcher.drawTexture2d(logo, new Vector2(Display.getWidth() - logo.getWidth(),
                                                       Display.getHeight() - logo.getHeight()));
    }

    public void resize()
    {
        cam.initProjection(Display.getWidth(), Display.getHeight());
    }
}
