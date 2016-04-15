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

package com.shc.silenceengine.utils;

import com.shc.silenceengine.core.SilenceEngine;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Sri Harsha Chilakapati
 */
public final class TaskManager
{
    private static Queue<Task> updateTasks = new LinkedList<>();
    private static Queue<Task> renderTasks = new LinkedList<>();

    private static boolean initialized = false;

    public static void runOnUpdate(Task task)
    {
        updateTasks.add(task);
        checkInitialized();
    }

    public static void runOnRender(Task task)
    {
        renderTasks.add(task);
        checkInitialized();
    }

    private static void update(float deltaTime)
    {
        while (!updateTasks.isEmpty())
        {
            Task task;
            if ((task = updateTasks.poll()) != null)
                task.perform();
        }
    }

    private static void render(float delta)
    {
        while (!renderTasks.isEmpty())
        {
            Task task;
            if ((task = renderTasks.poll()) != null)
                task.perform();
        }
    }

    private static void checkInitialized()
    {
        if (!initialized)
        {
            SilenceEngine.eventManager.addUpdateHandler(TaskManager::update);
            SilenceEngine.eventManager.addRenderHandler(TaskManager::render);
            initialized = true;
        }
    }

    @FunctionalInterface
    public interface Task
    {
        void perform();
    }
}
