package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.core.SilenceException;

/**
 * Represents an OpenAL exception which is non contextual.
 *
 * @author Sri Harsha Chilakapati
 */
public class ALException extends SilenceException
{
    /**
     * Constructs the SilenceException with a message
     *
     * @param message The exception message
     */
    public ALException(String message)
    {
        super(message);
    }

    public static class InvalidName extends ALException
    {
        public InvalidName()
        {
            super("Invalid name parameter passed");
        }
    }

    public static class InvalidEnum extends ALException
    {
        public InvalidEnum()
        {
            super("Invalid enum value");
        }
    }

    public static class InvalidValue extends ALException
    {
        public InvalidValue()
        {
            super("Invalid parameter value");
        }
    }

    public static class InvalidOperation extends ALException
    {
        public InvalidOperation()
        {
            super("Illegal call");
        }
    }

    public static class OutOfMemory extends ALException
    {
        public OutOfMemory()
        {
            super("OpenAL ran out of memory");
        }
    }
}
