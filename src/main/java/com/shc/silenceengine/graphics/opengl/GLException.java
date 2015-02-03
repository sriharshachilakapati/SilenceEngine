package com.shc.silenceengine.graphics.opengl;

import com.shc.silenceengine.core.SilenceException;

/**
 * @author Sri Harsha Chilakapati
 */
public class GLException extends SilenceException
{
    /**
     * Constructs the SilenceException with a message
     *
     * @param message The exception message
     */
    public GLException(String message)
    {
        super(message);
    }

    public static class InvalidEnum extends GLException
    {
        public InvalidEnum()
        {
            super("An unacceptable value is specified for an enumerated argument");
        }
    }

    public static class InvalidValue extends GLException
    {
        public InvalidValue()
        {
            super("A numeric argument is out of range");
        }
    }

    public static class InvalidOperation extends GLException
    {
        public InvalidOperation()
        {
            super("The specified operation is not allowed in current state");
        }
    }

    public static class InvalidFramebufferOperation extends GLException
    {
        public InvalidFramebufferOperation()
        {
            super("The FrameBuffer object is incomplete");
        }
    }

    public static class OutOfMemory extends GLException
    {
        public OutOfMemory()
        {
            super("There is not enough memory left to execute the command");
        }
    }

    public static class StackUnderflow extends GLException
    {
        public StackUnderflow()
        {
            super("An attempt has been made to perform an operation that would cause an internal stack to underflow");
        }
    }

    public static class StackOverflow extends GLException
    {
        public StackOverflow()
        {
            super("An attempt has been made to perform an operation that would cause an internal stack to overflow");
        }
    }
}
