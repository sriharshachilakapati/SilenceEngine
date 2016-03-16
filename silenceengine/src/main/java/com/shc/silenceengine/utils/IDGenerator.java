package com.shc.silenceengine.utils;

/**
 * A utility class that generates IDs to be used internally in the engine. All the IDs are {@code long} primitives.
 *
 * @author Sri Harsha Chilakapati
 */
public final class IDGenerator
{
    private IDGenerator()
    {
    }

    private static long nextID = 1;

    public static long generate()
    {
        return nextID++;
    }
}
