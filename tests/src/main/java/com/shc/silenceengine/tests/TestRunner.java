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

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Mouse;
import com.shc.silenceengine.input.Touch;

/**
 * @author Sri Harsha Chilakapati
 */
public class TestRunner extends Game
{
    private SilenceTest test;

    private boolean bypassEvents = false;

    public TestRunner(SilenceTest test)
    {
        if (test != null)
            this.test = test;
    }

    @Override
    public void init()
    {
        if (test != null && !bypassEvents)
            test.init();
    }

    @Override
    public void update(float deltaTime)
    {
        if (test != null && !bypassEvents)
            test.update(deltaTime);
    }

    @Override
    public void render(float delta)
    {
        if (test != null && !bypassEvents)
            test.render(delta);
    }

    @Override
    public void dispose()
    {
        if (test != null && !bypassEvents)
            test.dispose();
    }

    @Override
    public void resized()
    {
        if (test != null && !bypassEvents)
            test.resized();
    }

    public void changeTest(SilenceTest test)
    {
        bypassEvents = true;

        if (test != null)
            this.test.dispose();

        clearDisplay();
        clearInput();
        clearAudio();

        this.test = test;

        if (test != null)
            test.init();

        SilenceEngine.log.getRootLogger().info("Init done");
        bypassEvents = false;
    }

    private void clearDisplay()
    {
        GLContext.clearColor(Color.BLACK);
        SilenceEngine.display.setSize(800, 600);
        SilenceEngine.display.setTitle("SilenceEngine Window");
        SilenceEngine.display.setFullscreen(false);
    }

    private void clearInput()
    {
        for (int key = Keyboard.KEY_FIRST; key <= Keyboard.KEY_LAST; key++)
            SilenceEngine.input.postKeyEvent(key, false);

        for (int button = Mouse.BUTTON_FIRST; button <= Mouse.BUTTON_LAST; button++)
            SilenceEngine.input.postMouseEvent(button, false);

        for (int finger = Touch.FINGER_0; finger <= Touch.FINGER_9; finger++)
            SilenceEngine.input.postTouchEvent(finger, false, 0, 0);
    }

    private void clearAudio()
    {
        SilenceEngine.audio.scene.stopAllSources();
    }
}
