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

    public static void log(String... messages)
    {
        if (!Game.development)
            return;

        for (String message : messages)
            System.out.println("[INFO " + getTimeStamp() + "] " + message);
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
            System.err.println("[WARNING " + getTimeStamp() + "] " + message);
    }

    public static void error(String... messages)
    {
        for (String message : messages)
            System.err.println("[FATAL ERROR " + getTimeStamp() + "] " + message);

        System.err.println("Terminating with exception");
        throw new SilenceException("FATAL Error occurred, cannot continue further");
    }
}
