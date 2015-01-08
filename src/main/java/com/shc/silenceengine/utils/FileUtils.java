package com.shc.silenceengine.utils;

import com.shc.silenceengine.core.SilenceException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Sri Harsha Chilakapati
 */
public final class FileUtils
{
    private FileUtils()
    {
    }

    public static String[] readLinesToStringArray(InputStream stream)
    {
        ArrayList<String> lines = new ArrayList<>();

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
        InputStream is =  FileUtils.class.getClassLoader().getResourceAsStream(name);

        if (is == null)
            throw new SilenceException("Resource not found: " + name);

        return is;
    }

    public static String getExtension(String name)
    {
        return name.split("\\.(?=[^\\.]+$)")[1];
    }
}
