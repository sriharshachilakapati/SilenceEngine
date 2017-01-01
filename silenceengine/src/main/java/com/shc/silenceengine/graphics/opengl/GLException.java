/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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
 * @author Josh "ShadowLordAlpha"
 */
public class GLException extends SilenceException
{
    /**
     * Generated Serial UID
     */
    private static final long serialVersionUID = -4233294051491634242L;

    /**
     * Constructs the SilenceException with a message
     *
     * @param message The exception message
     */
    public GLException(String message)
    {
        super(message);
    }

    public GLException()
    {
        super();
    }

    public static class InvalidEnum extends GLException
    {
        /**
         * Generated Serial UID
         */
        private static final long serialVersionUID = 2029433042086596990L;

        public InvalidEnum()
        {
            super("An unacceptable value is specified for an enumerated argument");
        }
    }

    public static class InvalidValue extends GLException
    {
        /**
         * Generated Serial UID
         */
        private static final long serialVersionUID = -8619110001750124988L;

        public InvalidValue()
        {
            super("A numeric argument is out of range");
        }
    }

    public static class InvalidOperation extends GLException
    {
        /**
         * Generated Serial UID
         */
        private static final long serialVersionUID = -8672095451587407513L;

        public InvalidOperation()
        {
            super("The specified operation is not allowed in current state");
        }
    }

    public static class InvalidFramebufferOperation extends GLException
    {
        /**
         * Generated Serial UID
         */
        private static final long serialVersionUID = -6321399771655108100L;

        public InvalidFramebufferOperation()
        {
            super("The FrameBuffer object is incomplete");
        }
    }

    public static class OutOfMemory extends GLException
    {
        /**
         * Generated Serial UID
         */
        private static final long serialVersionUID = -5726568667092289095L;

        public OutOfMemory()
        {
            super("There is not enough memory left to execute the command");
        }
    }
}
