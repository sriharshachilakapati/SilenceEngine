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

package com.shc.silenceengine.graphics.fonts;

import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.programs.FontProgram;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.functional.UniCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public class BitmapFontRenderer
{
    private DynamicRenderer renderer;
    private FontProgram     fontProgram;
    private boolean         ownsRenderer;

    private BitmapFontRenderer()
    {
    }

    public static void create(UniCallback<BitmapFontRenderer> callback)
    {
        create(callback, new DynamicRenderer(100), true);
    }

    public static void create(UniCallback<BitmapFontRenderer> callback, DynamicRenderer renderer)
    {
        create(callback, renderer, false);
    }

    private static void create(UniCallback<BitmapFontRenderer> callback, DynamicRenderer renderer, boolean ownsRenderer)
    {
        FontProgram.create(fontProgram ->
        {
            BitmapFontRenderer fontRenderer = new BitmapFontRenderer();
            fontRenderer.renderer = renderer;
            fontRenderer.ownsRenderer = ownsRenderer;
            fontRenderer.fontProgram = fontProgram;

            callback.invoke(fontRenderer);
        });
    }

    public void begin()
    {
        fontProgram.use();
        fontProgram.applyToRenderer(renderer);
        renderer.begin(Primitive.TRIANGLES);
    }

    public void flush()
    {
        renderer.flush();
    }

    public void end()
    {
        renderer.end();
    }

    public void render(BitmapFont font, String text, float x, float y, Color color)
    {
        final float startX = x;
        final float startY = y;

        BitmapFont.Char last;

        for (int page : font.pages.keySet())
        {
            x = startX;
            y = startY;
            last = null;

            end();
            font.pages.get(page).bind(0);
            begin();

            for (char ch : text.toCharArray())
            {
                if (ch == '\n')
                {
                    x = startX;
                    y += font.common.lineHeight;

                    continue;
                }

                BitmapFont.Char fChar = font.chars.get((int) ch);
                fChar = fChar == null ? font.chars.get((int) ' ') : fChar;

                if (last != null)
                    x += font.getKerning(last, fChar);

                if (page == fChar.page)
                {
                    float sLeft = fChar.x;
                    float sTop = fChar.y;
                    float sRight = fChar.x + fChar.width;
                    float sBot = fChar.y + fChar.height;

                    float dLeft = x + fChar.xOffset;
                    float dTop = y + fChar.yOffset;
                    float dRight = dLeft + fChar.width;
                    float dBot = dTop + fChar.height;

                    // Draw the character here
                    float u1 = sLeft / font.common.scaleW;
                    float v1 = sTop / font.common.scaleH;
                    float u2 = sRight / font.common.scaleW;
                    float v2 = sBot / font.common.scaleH;

                    renderer.flushOnOverflow(6);

                    renderer.vertex(dLeft, dTop);
                    renderer.texCoord(u1, v1);
                    renderer.color(color);

                    renderer.vertex(dRight, dTop);
                    renderer.texCoord(u2, v1);
                    renderer.color(color);

                    renderer.vertex(dLeft, dBot);
                    renderer.texCoord(u1, v2);
                    renderer.color(color);

                    renderer.vertex(dRight, dTop);
                    renderer.texCoord(u2, v1);
                    renderer.color(color);

                    renderer.vertex(dRight, dBot);
                    renderer.texCoord(u2, v2);
                    renderer.color(color);

                    renderer.vertex(dLeft, dBot);
                    renderer.texCoord(u1, v2);
                    renderer.color(color);
                }

                x += fChar.xAdvance;

                last = fChar;
            }
        }
    }

    public void render(BitmapFont font, String text, Vector2 position, Color color)
    {
        render(font, text, position.x, position.y, color);
    }

    public void render(BitmapFont font, String text, float x, float y)
    {
        render(font, text, x, y, Color.WHITE);
    }

    public void render(BitmapFont font, String text, Vector2 position)
    {
        render(font, text, position.x, position.y, Color.WHITE);
    }

    public void dispose()
    {
        if (ownsRenderer)
            renderer.dispose();
    }
}
