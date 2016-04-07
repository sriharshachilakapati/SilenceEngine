/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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
 *
 */

package com.shc.silenceengine.core;

/**
 * Default Exception class of SilenceEngine.
 *
 * @author Sri Harsha Chilakapati
 * @author Josh "ShadowLordAlpha"
 */
public class SilenceException extends RuntimeException
{
    /**
     * Generated Serial UID
     */
    private static final long serialVersionUID = -2424650959040475391L;

    /**
     * Constructs the SilenceException with a message.
     *
     * @param message The message explaining the cause of this exception.
     */
    public SilenceException(String message)
    {
        super(message);
    }

    public SilenceException()
    {
        super();
    }

    /**
     * Rethrows a Throwable as a SilenceException. This method is useful if you want to turn any exception into a
     * runtime SilenceException. Use this method if you want to quit the game with a fatal error.
     *
     * @param throwable The thrown object containing the stacktrace.
     */
    public static void reThrow(Throwable throwable)
    {
        // Just pass on if the throwable is of type SilenceException
        if (throwable instanceof SilenceException)
            throw (SilenceException) throwable;
        else
            throw new SilenceException(throwable.toString());
    }
}
