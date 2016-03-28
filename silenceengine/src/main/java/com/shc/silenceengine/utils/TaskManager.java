package com.shc.silenceengine.utils;

/**
 * A task manager to manage tasks. Tasks are like deferred commands that will be executed in either update or in render
 * by depending on the type of the task.
 *
 * @author Sri Harsha Chilakapati
 */
public final class TaskManager
{


    public enum TaskType
    {
        PRE_UPDATE,
        UPDATE,
        POST_UPDATE,

        PRE_RENDER,
        RENDER,
        POST_RENDER
    }

    @FunctionalInterface
    public interface Task
    {
        void run();
    }
}
