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

package com.shc.silenceengine.backend.android;

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.TimeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Sri Harsha Chilakapati
 */
public final class AndroidRuntime
{
    public static Game game;

    private AndroidRuntime()
    {
    }

    public static void start(Game game)
    {
        SilenceEngine.log = new AndroidLogDevice();
        SilenceEngine.io = new AndroidIODevice();
        SilenceEngine.graphics = new AndroidGraphicsDevice();
        SilenceEngine.audio = new AndroidAudioDevice();
        SilenceEngine.display = new AndroidDisplayDevice();
        SilenceEngine.input = new AndroidInputDevice();

        AndroidRuntime.game = game;

        // Notify the game loop that we got focus
        SilenceEngine.gameLoop.onFocusGain();

        // This is a hack to enable the TaskManager. The TaskManager fails to register upon
        // recreating the context, and or reopening the app without calling System.exit()
        // The same bug is also present in GameTimer utility class, so apply this hack for it too.
        try
        {
            Field field = TaskManager.class.getDeclaredField("initialized");
            field.setAccessible(true);
            field.setBoolean(null, false);

            Method method = TaskManager.class.getDeclaredMethod("checkInitialized");
            method.setAccessible(true);
            method.invoke(null);

            field = GameTimer.class.getDeclaredField("initialized");
            field.setAccessible(true);
            field.setBoolean(null, false);

            // Create a temporary timer so that the timing handler is registered
            new GameTimer(0, TimeUtils.Unit.SECONDS).start();
        }
        catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            e.printStackTrace();
        }

        // Clear any pending tasks in the TaskManager from the previous runs
        // due to persistent processes in Android
        TaskManager.clearUpdateTasks();
        TaskManager.clearRenderTasks();

        // Initialize the SilenceEngine
        SilenceEngine.init();

        // Now initialize the game
        game.init();
    }
}
