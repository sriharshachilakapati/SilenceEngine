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
import com.shc.silenceengine.io.FilePath;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * @author Sri Harsha Chilakapati
 */
public final class FileUtils
{
    private FileUtils()
    {
    }

    public static String readLinesToString(FilePath path)
    {
        try
        {
            return readLinesToString(path.getInputStream());
        }
        catch (IOException e)
        {
            SilenceException.reThrow(e);
        }

        return null;
    }

    public static String readLinesToString(InputStream stream)
    {
        return String.join("\n", readLinesToStringArray(stream));
    }

    public static String[] readLinesToStringArray(FilePath path)
    {
        try
        {
            return readLinesToStringArray(path.getInputStream());
        }
        catch (IOException e)
        {
            SilenceException.reThrow(e);
        }

        return null;
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

    public static String getExtension(FilePath path)
    {
        return getExtension(path.getPath());
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

    public static ByteBuffer readToByteBuffer(FilePath filePath)
    {
        try
        {
            return readToByteBuffer(filePath.getInputStream());
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }

        return null;
    }

    public static ByteBuffer readToByteBuffer(InputStream inputStream)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];

        try
        {
            while (true)
            {
                int n = inputStream.read(buffer);

                if (n < 0)
                    break;

                outputStream.write(buffer, 0, n);
            }

            inputStream.close();
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }

        byte[] bytes = outputStream.toByteArray();

        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bytes.length);
        byteBuffer.put(bytes).flip();

        return byteBuffer;
    }
}
