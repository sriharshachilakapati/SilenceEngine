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

package com.shc.silenceengine.utils;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.utils.functional.SimpleCallback;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Sri Harsha Chilakapati
 */
public final class TaskManager
{
    private static Queue<SimpleCallback> updateTasks = new LinkedList<>();
    private static Queue<SimpleCallback> renderTasks = new LinkedList<>();

    private static boolean initialized = false;

    private final static Object updateTasksLock = new Object();
    private final static Object renderTasksLock = new Object();

    public static void runOnUpdate(SimpleCallback task)
    {
        synchronized (updateTasksLock)
        {
            updateTasks.add(task);
        }

        checkInitialized();
    }

    public static void runOnRender(SimpleCallback task)
    {
        synchronized (renderTasksLock)
        {
            renderTasks.add(task);
        }

        checkInitialized();
    }

    public static void clearUpdateTasks()
    {
        synchronized (updateTasksLock)
        {
            updateTasks.clear();
        }
    }

    public static void clearRenderTasks()
    {
        synchronized (renderTasksLock)
        {
            renderTasks.clear();
        }
    }

    public static void forceUpdateTasks(float deltaTime)
    {
        synchronized (updateTasksLock)
        {
            SimpleCallback task;

            while ((task = updateTasks.poll()) != null)
                task.invoke();
        }
    }

    public static void forceRenderTasks(float delta)
    {
        synchronized (renderTasksLock)
        {
            SimpleCallback task;

            while ((task = renderTasks.poll()) != null)
                task.invoke();
        }
    }

    private static void checkInitialized()
    {
        if (!initialized)
        {
            SilenceEngine.eventManager.addUpdateHandler(TaskManager::forceUpdateTasks);
            SilenceEngine.eventManager.addRenderHandler(TaskManager::forceRenderTasks);
            initialized = true;
        }
    }
}
