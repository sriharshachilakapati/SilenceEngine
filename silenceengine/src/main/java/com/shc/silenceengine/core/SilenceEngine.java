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

package com.shc.silenceengine.core;

import com.shc.silenceengine.audio.AudioDevice;
import com.shc.silenceengine.core.gameloops.FixedTimeSteppedLoop;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.graphics.cameras.Camera;
import com.shc.silenceengine.graphics.cameras.NullCamera;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.InputDevice;
import com.shc.silenceengine.io.IODevice;
import com.shc.silenceengine.logging.ILogDevice;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * The SilenceEngine class is the core of the entire engine, and contains all the devices for a platform that we are
 * running upon. It also explains which platform are we running.
 *
 * @author Sri Harsha Chilakapati
 */
public final class SilenceEngine
{
    /**
     * The {@link IGameLoop} that generates events in the game.
     */
    public static IGameLoop gameLoop = new FixedTimeSteppedLoop();

    /**
     * The {@link IDisplayDevice} that handles the display, that is the window thing.
     */
    public static IDisplayDevice display;

    /**
     * The {@link IGraphicsDevice} that handles the graphics.
     */
    public static IGraphicsDevice graphics;

    /**
     * The {@link AudioDevice} that handles the audio.
     */
    public static AudioDevice audio;

    /**
     * The {@link IODevice} that handles IO operations.
     */
    public static IODevice io;

    /**
     * The {@link InputDevice} that handles game input.
     */
    public static InputDevice input;

    /**
     * The {@link ILogDevice} that handles logging.
     */
    public static ILogDevice log;

    /**
     * The {@link EventManager} that handles the events in SilenceEngine.
     */
    public static EventManager eventManager = new EventManager();

    private SilenceEngine()
    {
    }

    /**
     * Initializes SilenceEngine. Not to be called by the users, but will be called by the backends once the devices are
     * initialized.
     */
    public static void init()
    {
        // Create the Null camera
        Camera.CURRENT = new NullCamera();

        // Set the context to blend
        GLContext.enable(GL_BLEND);
        GLContext.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Initialize the empty texture
        Texture.EMPTY = Texture.fromColor(Color.TRANSPARENT, 32, 32);
        Texture.EMPTY.bind(0);
    }

    public enum Platform
    {
        WINDOWS_32,
        WINDOWS_64,
        LINUX_64,
        MACOSX,
        HTML5,
        ANDROID,
        UNKNOWN
    }
}
