package com.shc.silenceengine.utils;

import com.shc.silenceengine.SilenceException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Sri Harsha Chilakapati
 */
public class FileUtils
{
    public static String[] readLinesToStringArray(InputStream stream)
    {
        ArrayList<String> lines = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                lines.add(line);
            }
            reader.close();
        }
        catch (Exception e)
        {
            throw new SilenceException(e.getMessage());
        }

        String[] linesArray = new String[lines.size()];

        for (int i=0; i<lines.size(); i++)
            linesArray[i] = lines.get(i);

        return linesArray;
    }

    public static String readLinesToString(InputStream stream)
    {
        return String.join("\n", readLinesToStringArray(stream));
    }

    public static InputStream getResource(String name)
    {
        return FileUtils.class.getClassLoader().getResourceAsStream(name);
    }
}
