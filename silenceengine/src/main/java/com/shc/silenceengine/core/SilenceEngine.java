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
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import com.shc.silenceengine.utils.functional.UniCallback;

import java.util.LinkedList;
import java.util.List;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * The SilenceEngine class is the core of the entire engine, and contains all the devices for a platform that we are
 * running upon. It also explains which platform are we running.
 *
 * @author Sri Harsha Chilakapati
 */
public final class SilenceEngine
{
    // A list of callbacks (extensions) to be called after the engine done loading
    private static List<UniCallback<SimpleCallback>> callbacksOnInit = new LinkedList<>();

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
     * <p>Adds a callback to be run after the engine is initialized. There can be many callbacks added via this method,
     * and it is guaranteed that they will be called in the order they are added.</p>
     *
     * <pre>
     *     SilenceEngine.runOnInit((next) -&gt;
     *     {
     *         // Do the task here. After the task is done, invoke next,
     *         // which tells SilenceEngine that this is done successfully
     *         // and SilenceEngine can call the next on-init callback
     *
     *         next.invoke();
     *     });
     * </pre>
     *
     * <p>Note that this only works if called before the platform specific Runtime is started. To make sure these gets
     * called always, keep the calls to this method in the class initializer or the game's constructor.</p>
     *
     * <p>Placing these calls in the static constructor will not work on Android, since the activity might not be
     * destroyed from previous run and can cause issues.</p>
     *
     * @param callback The callback function to be invoked after the engine is initialized.
     */
    public static void runOnInit(UniCallback<SimpleCallback> callback)
    {
        // Callback chaining works by iterating all the callbacks, and hence, the chain will start executing from the
        // last callback, in the reverse order. To call them in the order of addition, we insert the callback at the
        // start of the list.
        callbacksOnInit.add(0, callback);
    }

    /*
     * (non-Javadoc)
     * Utility method to chain callbacks. Creates a chained callback that calls the next callback in the order.
     */
    private static UniCallback<SimpleCallback> chainCallback(UniCallback<SimpleCallback> newCB,
                                                             UniCallback<SimpleCallback> oldCB)
    {
        // Create a new callback that chains the new callback and the old callback
        return (finalCallback) ->
        {
            // If the new callback is null, that means we are at the end of the initializer chains.
            // So call the final callback.
            if (newCB == null)
                finalCallback.invoke();

                // Otherwise call the new callback which invokes the old callback and passes the finalCallback.
            else
                newCB.invoke(() -> oldCB.invoke(finalCallback));
        };
    }

    /**
     * Initializes SilenceEngine. Not to be called by the users, but will be called by the backends once the devices are
     * initialized.
     *
     * @param success Callback to be called after successful loading.
     */
    public static void init(SimpleCallback success)
    {
        // Queue the default initializations (we call add directly since engine callbacks need to be the first).
        // All the callbacks registered by the user are called after the engine init callbacks are executed.
        callbacksOnInit.add(SilenceEngine::initGraphics);

        // Build the callback list to be called after initialization
        UniCallback<SimpleCallback> callbackOnInit = chainCallback(null, null);

        for (UniCallback<SimpleCallback> callback : callbacksOnInit)
            callbackOnInit = chainCallback(callback, callbackOnInit);

        // Call all the callbacks that are registered by extensions
        callbackOnInit.invoke(() ->
        {
            SilenceEngine.log.getRootLogger().info("Finished initialization, starting game");
            success.invoke();
            callbacksOnInit.clear();
        });
    }

    /*
     * (non-Javadoc)
     * Initializes the graphics part of SilenceEngine
     */
    private static void initGraphics(SimpleCallback next)
    {
        SilenceEngine.log.getRootLogger().info("Initializing " + graphics.getClass().getSimpleName());

        // Create the Null camera
        Camera.CURRENT = new NullCamera();

        // Set the context to blend
        GLContext.enable(GL_BLEND);
        GLContext.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Initialize the empty texture
        Texture.EMPTY = Texture.fromColor(Color.TRANSPARENT, 32, 32);
        Texture.EMPTY.bind(0);

        // Invoke the next onInit callback in order
        next.invoke();
    }

    public static String getVersionString()
    {
        return "1.2.1";
    }

    public static Vector3 getVersion()
    {
        return new Vector3(1, 2, 1);
    }

    /**
     * An enumeration of platform identifiers. These are the platforms SilenceEngine runs on.
     */
    public enum Platform
    {
        /**
         * 32-bit Windows
         */
        WINDOWS_32,

        /**
         * 64-bit Windows
         */
        WINDOWS_64,

        /**
         * 64-bit Linux
         */
        LINUX_64,

        /**
         * MacOS X. There is no 32-bit version in MacOS X.
         */
        MACOSX,

        /**
         * HTML5, or the Web platform. We're running in a browser.
         */
        HTML5,

        /**
         * Android. Might be 64 bit or 32 bit.
         */
        ANDROID,

        /**
         * Unknown platform. SilenceEngine is either running in a custom backend,
         * or it failed to detect the platform.
         */
        UNKNOWN
    }
}
