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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Logger
{
    private Logger()
    {
    }

    static
    {
        setPrintTimeStamps(true);
    }

    private static boolean printTimeStamps;

    public static void log(String... messages)
    {
        if (!Game.development)
            return;

        for (String message : messages)
            System.out.println((printTimeStamps ? "[INFO " + getTimeStamp() + "] " : "") + message);
    }

    public static String getTimeStamp()
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");

        return sdf.format(date);
    }

    public static void warn(String... messages)
    {
        if (!Game.development)
            return;

        for (String message : messages)
            System.err.println((printTimeStamps ? "[WARNING " + getTimeStamp() + "] " : "") + message);
    }

    public static void error(String... messages)
    {
        for (String message : messages)
            System.err.println((printTimeStamps ? "[FATAL ERROR " + getTimeStamp() + "] " : "") + message);

        System.err.println("Terminating with exception");
        throw new SilenceException("FATAL Error occurred, cannot continue further");
    }

    public static void setPrintTimeStamps(boolean printTimeStamps)
    {
        Logger.printTimeStamps = printTimeStamps;
    }
}
