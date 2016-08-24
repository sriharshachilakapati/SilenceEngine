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

package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.backend.lwjgl.glfw.GLFW3;
import com.shc.silenceengine.backend.lwjgl.glfw.Window;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import org.lwjgl.system.Configuration;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

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
        Configuration.DEBUG.set(Game.DEVELOPMENT);

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

        final SimpleCallback[] performLoopFrame = {
                () ->
                {
                    // Assume 100 seconds so that tasks are force flushed
                    TaskManager.forceUpdateTasks(100);
                    TaskManager.forceRenderTasks(100);
                }
        };

        // Initiate setting the icon
        SilenceEngine.display.setIcon(FilePath.getResourceFile("engine_resources/icon.png"), () ->
        {
            // Call the game's init method
            game.init();

            // Replace the callback so that we now perform the game loop instead of flushing
            // tasks in TaskManager.
            performLoopFrame[0] = SilenceEngine.gameLoop::performLoopFrame;

            // Raise a resize event now
            SilenceEngine.eventManager.raiseResizeEvent();
        });

        // Set the context to blend
        GLContext.enable(GL_BLEND);
        GLContext.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // The native event loop
        while (!window.shouldClose())
        {
            GLFW3.pollEvents();
            performLoopFrame[0].invoke();
            window.swapBuffers();
        }

        // Raise the dispose event finally
        SilenceEngine.eventManager.raiseDisposeEvent();
    }
}
