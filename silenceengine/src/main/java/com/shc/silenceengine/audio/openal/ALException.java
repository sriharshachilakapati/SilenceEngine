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

package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.core.SilenceException;

/**
 * Represents an OpenAL exception which is non contextual.
 *
 * @author Sri Harsha Chilakapati
 * @author Josh "ShadowLordAlpha"
 */
public class ALException extends SilenceException
{
    /**
     * Generated Serial UID
     */
    private static final long serialVersionUID = -1397973227073547050L;

    /**
     * Constructs the SilenceException with a message
     *
     * @param message The exception message
     */
    public ALException(String message)
    {
        super(message);
    }

    public ALException()
    {
    }

    public static class InvalidName extends ALException
    {
        /**
         * Generated Serial UID
         */
        private static final long serialVersionUID = -6371061578765451992L;

        public InvalidName()
        {
            super("Invalid name parameter passed");
        }
    }

    public static class InvalidEnum extends ALException
    {
        /**
         * Generated Serial UID
         */
        private static final long serialVersionUID = 1336361103299253274L;

        public InvalidEnum()
        {
            super("Invalid enum value");
        }
    }

    public static class InvalidValue extends ALException
    {
        /**
         * Generated Serial UID
         */
        private static final long serialVersionUID = 4542333447497154383L;

        public InvalidValue()
        {
            super("Invalid parameter value");
        }
    }

    public static class InvalidOperation extends ALException
    {
        /**
         * Generated Serial UID
         */
        private static final long serialVersionUID = 5542972229699100897L;

        public InvalidOperation()
        {
            super("Illegal call");
        }
    }

    public static class OutOfMemory extends ALException
    {
        /**
         * Generated Serial UID
         */
        private static final long serialVersionUID = -6955697438456108303L;

        public OutOfMemory()
        {
            super("OpenAL ran out of memory");
        }
    }
}
