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
