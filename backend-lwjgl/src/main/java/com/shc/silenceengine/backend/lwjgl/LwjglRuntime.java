package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.backend.lwjgl.glfw.GLFW3;
import com.shc.silenceengine.backend.lwjgl.glfw.Window;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;

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

    public static void start()
    {
        SilenceEngine.log = new LwjglLogDevice();
        SilenceEngine.io = new LwjglIODevice();
        SilenceEngine.display = new LwjglDisplayDevice();
        SilenceEngine.input = new LwjglInputDevice();
        SilenceEngine.graphics = new LwjglGraphicsDevice();

        Window window = ((LwjglDisplayDevice) SilenceEngine.display).window;

        Game.INSTANCE.init();

        while (!window.shouldClose())
        {
            GLFW3.pollEvents();
            SilenceEngine.gameLoop.performLoopFrame();
            window.swapBuffers();
        }

        Game.INSTANCE.dispose();

        ((LwjglDisplayDevice) SilenceEngine.display).cleanUp();
    }
}
