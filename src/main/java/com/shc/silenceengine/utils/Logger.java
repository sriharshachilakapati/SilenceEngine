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

package com.shc.silenceengine.utils;

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.io.FilePath;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simplified logging utility class used by SilenceEngine. Supports logging at three levels, INFO, WARNING and ERROR.
 *
 * @author Sri Harsha Chilakapati
 * @author Gamefreak0
 */
public final class Logger
{
    /**
     * Whether time stamps should be printed
     */
    private static boolean printTimeStamps;

    private static boolean printToConsole;

    /**
     * The format time stamps are printed in
     */
    private static SimpleDateFormat timeStampFormat;

    private static List<PrintStream> printStreams;

    /**
     * Prevent instantiation
     */
    private Logger()
    {
    }

    /**
     * Logs <code>messages</code> to {@link java.lang.System#out} with the current time in the format provided by
     * <code>setTimeStampFormat()</code> if <code>setPrintTimeStamps()</code> is <code>true</code>.
     *
     * @param messages An array of <code>Object</code> to be printed.
     *
     * @see java.lang.System#out
     */
    public static void log(Object... messages)
    {
        if (!Game.development)
            return;

        for (Object message : messages)
        {
            String info = ((printTimeStamps ? "[INFO " + getTimeStamp() + "] " : "") + message)
                                             .replaceAll("\r\n", "\n").replaceAll("\n", "\r\n");

            for (PrintStream stream : printStreams)
                stream.println(info);

            if (printToConsole)
                System.out.println(info);
        }
    }

    /**
     * @return The current time in the format provided by <code>setTimeStampFormat()</code>
     */
    public static String getTimeStamp()
    {
        return timeStampFormat.format(new Date());
    }

    /**
     * Logs <code>messages</code> to {@link java.lang.System#err} with the current time in the format provided by
     * <code>setTimeStampFormat()</code> if <code>setPrintTimeStamps()</code> is <code>true</code>.
     *
     * @param messages An array of <code>Object</code> to be printed.
     *
     * @see java.lang.System#err
     */
    public static void warn(Object... messages)
    {
        if (!Game.development)
            return;

        for (Object message : messages)
        {
            String warning = ((printTimeStamps ? "[INFO " + getTimeStamp() + "] " : "") + message)
                    .replaceAll("\r\n", "\n").replaceAll("\n", "\r\n");

            for (PrintStream stream : printStreams)
                stream.println(warning);

            if (printToConsole)
                System.err.println(warning);
        }
    }

    /**
     * Logs <code>messages</code> to {@link java.lang.System#err} with the current time in the format provided by
     * <code>setTimeStampFormat()</code> if <code>setPrintTimeStamps()</code> is <code>true</code>. Also throws a {@link
     * com.shc.silenceengine.core.SilenceException SilenceException}
     *
     * @param messages An array of <code>Object</code> to be printed.
     *
     * @see java.lang.System#err
     */
    public static void error(Object... messages)
    {
        if (!Game.development)
            return;

        for (Object message : messages)
        {
            String error = ((printTimeStamps ? "[INFO " + getTimeStamp() + "] " : "") + message)
                    .replaceAll("\r\n", "\n").replaceAll("\n", "\r\n");

            for (PrintStream stream : printStreams)
                stream.println(error);

            if (printToConsole)
                System.err.println(error);
        }

        warn("Terminating with Exception");

        throw new SilenceException("FATAL Error occurred, cannot continue further");
    }

    /**
     * Sets the format of time stamps printed with <code>log()</code>, <code>warn()</code> and <code>error()</code>.
     * This will not be used if {@link com.shc.silenceengine.utils.Logger#setPrintTimeStamps(boolean)
     * setPrintTimeStamps()} is false.
     *
     * @param timeStampFormat The format for Time Stamps
     */
    public static void setTimeStampFormat(SimpleDateFormat timeStampFormat)
    {
        Logger.timeStampFormat = timeStampFormat;
    }

    /**
     * Change whether or not Time Stamps should be printed when using <code>log()</code>, <code>warn()</code> and
     * <code>error()</code>.
     *
     * @param printTimeStamps Whether or not to print time stamps
     */
    public static void setPrintTimeStamps(boolean printTimeStamps)
    {
        Logger.printTimeStamps = printTimeStamps;
    }

    public static void addLogStream(PrintStream stream)
    {
        printStreams.add(stream);
    }

    public static void removeLogStream(PrintStream stream)
    {
        if (!printStreams.contains(stream))
            return;

        printStreams.remove(stream);
    }

    public static void addLogStream(FilePath path) throws IOException
    {
        addLogStream(new PrintStream(path.getOutputStream(), true));
    }

    public static void setPrintToConsole(boolean printToConsole)
    {
        Logger.printToConsole = printToConsole;
    }

    static
    {
        printStreams = new ArrayList<>();
        setPrintTimeStamps(true);
        setPrintToConsole(true);
        setTimeStampFormat(new SimpleDateFormat("MM/dd/yyyy h:mm:ss a"));
    }
}
