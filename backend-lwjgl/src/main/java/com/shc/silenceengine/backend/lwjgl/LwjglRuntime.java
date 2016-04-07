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
 *
 */

package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.backend.lwjgl.glfw.GLFW3;
import com.shc.silenceengine.backend.lwjgl.glfw.Window;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.FilePath;

/**
 * The LwjglRuntime initializes the LWJGL library, and starts the native event loop.
 *
 * @author Sri Harsha Chilakapati
 */
public final class LwjglRuntime
{
    private LwjglRuntime()
    {
    }

    public static void start(Game game)
    {
        SilenceEngine.log = new LwjglLogDevice();
        SilenceEngine.io = new LwjglIODevice();
        SilenceEngine.display = new LwjglDisplayDevice();
        SilenceEngine.input = new LwjglInputDevice();
        SilenceEngine.graphics = new LwjglGraphicsDevice();
        SilenceEngine.audio = new LwjglAudioDevice();

        // Set AWT fix on Mac OS X
        if (SilenceEngine.display.getPlatform() == SilenceEngine.Platform.MACOSX)
            System.setProperty("java.awt.headless", "true");

        Window window = ((LwjglDisplayDevice) SilenceEngine.display).window;

        SilenceEngine.display.setIcon(FilePath.getResourceFile("engine_resources/icon.png"));

        game.init();
        SilenceEngine.eventManager.raiseResizeEvent();

        while (!window.shouldClose())
        {
            GLFW3.pollEvents();
            SilenceEngine.gameLoop.performLoopFrame();
            window.swapBuffers();
        }

        SilenceEngine.eventManager.raiseDisposeEvent();
    }
}
