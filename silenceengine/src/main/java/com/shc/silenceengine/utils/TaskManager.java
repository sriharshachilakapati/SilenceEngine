package com.shc.silenceengine.utils;

import com.shc.silenceengine.core.SilenceEngine;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public final class TaskManager
{
    private static List<Task> updateTasks = new LinkedList<>();
    private static List<Task> renderTasks = new LinkedList<>();

    private static boolean initialized = false;

    public static void addUpdateTask(Task task)
    {
        updateTasks.add(task);
        checkInitialized();
    }

    public static void addRenderTask(Task task)
    {
        renderTasks.add(task);
        checkInitialized();
    }

    private static void update(float deltaTime)
    {
        for (Task task : updateTasks)
            task.perform();

        updateTasks.clear();
    }

    private static void render(float delta)
    {
        for (Task task : renderTasks)
            task.perform();

        renderTasks.clear();
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
