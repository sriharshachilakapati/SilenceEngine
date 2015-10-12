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
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;

/**
 * @author Sri Harsha Chilakapati
 */
public class ResourceLoaderTest extends Game
{
    private Texture texture1;
    private Texture texture2;

    private int texture2ID;

    private TrueTypeFont font1;
    private TrueTypeFont font2;

    private OrthoCam cam;

    private ResourceLoader loader1;
    private ResourceLoader loader2;

    public static void main(String[] args)
    {
        new ResourceLoaderTest().start();
    }

    public void init()
    {
        SilenceEngine.graphics.setClearColor(Color.NAVY);

        loader1 = new ResourceLoader();
        loader1.setProgressRenderCallback(this::customProgressRenderCallback);

        int textureID = loader1.loadResource(Texture.class, "resources/texture.png");
        int fontID1 = loader1.loadResource(TrueTypeFont.class, "Times New Roman");
        int fontID2 = loader1.loadResource(TrueTypeFont.class, "resources/FREEBSC_.ttf");

        loader1.startLoading();

        texture1 = loader1.getResource(textureID);
        font1 = loader1.getResource(fontID1);
        font2 = loader1.getResource(fontID2);
        font1.setSize(26);
        font2.setSizeAndStyle(48, TrueTypeFont.STYLE_BOLD | TrueTypeFont.STYLE_ITALIC);

        cam = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());
    }

    public void resize()
    {
        cam.initProjection(Display.getWidth(), Display.getHeight());
    }

    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        if (Keyboard.isClicked(Keyboard.KEY_SPACE) && loader2 == null)
        {
            loader2 = new ResourceLoader();
            loader2.setProgressRenderCallback(this::customProgressRenderCallback);

            texture2ID = loader2.loadResource(Texture.class, FilePath.getResourceFile("resources/tree2-final.png"));

            // Start loading asynchronously, not blocking the main thread
            loader2.startLoading(true);
        }
    }

    public void render(float delta, Batcher batcher)
    {
        cam.apply();

        if (loader2 != null)
        {
            if (!loader2.isDone())
            {
                loader2.renderLoadingScreen();
                return;
            }
            else
                texture2 = loader2.getResource(texture2ID);
        }

        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();

        g2d.drawTexture(texture1, 150, 150);

        if (texture2 != null)
            g2d.drawTexture(texture2, 300, 200);

        g2d.setFont(font1);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Times New Roman!!", 10, 10);

        g2d.setFont(font2);
        g2d.setColor(Color.GREEN);
        g2d.drawString("Free Booter Script", 10, 40);

        if (loader2 == null)
        {
            g2d.setFont(font1);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Press SPACE to load another texture", 10, 110);
        }
    }

    public void dispose()
    {
        loader1.dispose();

        if (loader2 != null)
            loader2.dispose();
    }

    private void customProgressRenderCallback(String info, float percentage)
    {
        System.out.println(percentage + "% => " + info);
        loader1.defaultRenderProgressCallback(info, percentage);
    }
}
