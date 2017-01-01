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

package com.shc.silenceengine.backend.gwt;

import com.google.gwt.animation.client.AnimationScheduler;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.FilePath;

/**
 * The GwtRuntime class creates the GWT configuration, creates the display. It starts the native event loop and emits
 * the events required for SilenceEngine to start the game loop.
 *
 * @author Sri Harsha Chilakapati
 */
public final class GwtRuntime
{
    private GwtRuntime()
    {
    }

    public static void start(Game game)
    {
        SilenceEngine.log = new GwtLogDevice();

        SilenceEngine.display = new GwtDisplayDevice();
        SilenceEngine.input = new GwtInputDevice();
        SilenceEngine.io = new GwtIODevice();
        SilenceEngine.graphics = new GwtGraphicsDevice();
        SilenceEngine.audio = new GwtAudioDevice();

        SilenceEngine.display.setTitle("SilenceEngine " + SilenceEngine.getVersionString());
        SilenceEngine.display.setIcon(FilePath.getResourceFile("engine_resources/icon.png"));

        SilenceEngine.init(() ->
        {
            game.init();

            // Prevent fullscreen requests in init
            SilenceEngine.display.setFullscreen(false);

            AnimationScheduler.get().requestAnimationFrame(GwtRuntime::frameLoop);
        });
    }

    private static void frameLoop(double timestamp)
    {
        GwtInputDevice.pollControllers();

        SilenceEngine.gameLoop.performLoopFrame();

        // Request another frame
        AnimationScheduler.get().requestAnimationFrame(GwtRuntime::frameLoop);
    }
}
