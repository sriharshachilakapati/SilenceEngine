package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.core.SilenceException;

/**
 * An exception class that represents a OpenAL Context error. This exception is a runtime exception.
 *
 * @author Sri Harsha Chilakapati
 */
public class ALCException extends SilenceException
{
    /**
     * Constructs the SilenceException with a message
     *
     * @param message The exception message
     */
    public ALCException(String message)
    {
        super(message);
    }

    public static class InvalidDevice extends ALCException
    {
        public InvalidDevice()
        {
            super("Invalid or no device selected");
        }
    }

    public static class InvalidContext extends ALCException
    {
        public InvalidContext()
        {
            super("Invalid or no context selected");
        }
    }

    public static class InvalidEnum extends ALCException
    {
        public InvalidEnum()
        {
            super("Invalid enum value");
        }
    }

    public static class InvalidValue extends ALCException
    {
        public InvalidValue()
        {
            super("Invalid parameter value");
        }
    }

    public static class OutOfMemory extends ALCException
    {
        public OutOfMemory()
        {
            super("OpenAL ran out of memory");
        }
    }
}
