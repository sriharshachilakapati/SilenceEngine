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

    public static String readLinesToString(InputStream stream)
    {
        return String.join("\n", readLinesToStringArray(stream));
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

        for (int i = 0; i < lines.size(); i++)
            linesArray[i] = lines.get(i);

        return linesArray;
    }

    public static InputStream getResource(String name)
    {
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(name);

        if (is == null)
            throw new SilenceException("Resource not found: " + name);

        return is;
    }

    public static String getExtension(String name)
    {
        return name.split("\\.(?=[^\\.]+$)")[1];
    }

    public static boolean resourceExists(String name)
    {
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(name);
        return is != null;
    }
}
