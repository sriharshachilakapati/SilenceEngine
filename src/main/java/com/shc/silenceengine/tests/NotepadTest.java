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
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.backend.lwjgl3.opengl.GL3Context;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class NotepadTest extends Game
{
    private StringBuilder stringBuilder;
    private TrueTypeFont  consolas;

    public static void main(String[] args)
    {
        new NotepadTest().start();
    }

    @Override
    public void init()
    {
        Display.setTitle("Notepad Test");
        Keyboard.registerTextListener((chars, codePoint, mods) -> stringBuilder.append(chars));

        stringBuilder = new StringBuilder();
        GL3Context.clearColor(Color.WHITE_SMOKE);

        consolas = new TrueTypeFont("Consolas", TrueTypeFont.STYLE_NORMAL, 16);
        SilenceEngine.graphics.getGraphics2D().setFont(consolas);

        GameTimer timer = new GameTimer(0.1, TimeUtils.Unit.SECONDS);
        timer.setCallback(() ->
        {
            if (!isRunning())
                return;

            if (Keyboard.isPressed(Keyboard.KEY_BACKSPACE))
            {
                if (stringBuilder.length() != 0)
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }

            if (Keyboard.isPressed(Keyboard.KEY_TAB))
                stringBuilder.append("    ");

            if (Keyboard.isPressed(Keyboard.KEY_ENTER) || Keyboard.isPressed(Keyboard.KEY_KP_ENTER))
                stringBuilder.append('\n');

            if (Keyboard.isPressed('V', Keyboard.KEY_LEFT_CONTROL) || Keyboard.isPressed('V', Keyboard.KEY_RIGHT_CONTROL))
                stringBuilder.append(Display.getWindow().getClipboardString());

            if (isRunning())
                timer.start();
        });
        timer.start();
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.end();
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.setColor(Color.BLACK);
        g2d.drawString(stringBuilder.toString() + ((int) (TimeUtils.currentSeconds() * 2) % 2 == 0 ? "_" : ""), 10, 10);
    }

    @Override
    public void dispose()
    {
        consolas.dispose();
    }
}
