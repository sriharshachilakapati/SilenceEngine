package com.shc.silenceengine.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Default Exception class of SilenceEngine.
 *
 * @author Sri Harsha Chilakapati
 */
public class SilenceException extends RuntimeException
{
    /**
     * Constructs the SilenceException with a message
     */
    public SilenceException(String message)
    {
        super(message);
    }

    public static void reThrow(Throwable t)
    {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        t.printStackTrace(printWriter);

        throw new SilenceException(result.toString());
    }
}
