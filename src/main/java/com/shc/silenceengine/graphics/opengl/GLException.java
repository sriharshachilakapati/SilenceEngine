/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
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
