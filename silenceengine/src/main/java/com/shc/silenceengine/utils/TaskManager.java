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
    private static Queue<Task>  renderTasks = new LinkedList<>();

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
        while (!updateTasks.isEmpty())
            updateTasks.remove().perform();
    }

    private static void render(float delta)
    {
        while (!renderTasks.isEmpty())
            renderTasks.remove().perform();
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
