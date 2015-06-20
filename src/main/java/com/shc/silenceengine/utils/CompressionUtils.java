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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

/**
 * @author Sri Harsha Chilakapati
 */
public final class CompressionUtils
{
    private CompressionUtils()
    {
    }

    public static byte[] compressGZIP(byte[] data) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream  inputStream = new ByteArrayInputStream(data);

        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);

        byte[] buffer = new byte[1024];

        int count;
        while ((count = inputStream.read(buffer, 0, 1024)) != -1)
        {
            gzipOutputStream.write(buffer, 0, count);
        }

        gzipOutputStream.close();
        return outputStream.toByteArray();
    }

    public static byte[] decompressGZIP(byte[] data) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(data));

        byte[] buffer = new byte[1024];

        while (inputStream.read(buffer) != -1)
        {
            outputStream.write(buffer);
        }
        outputStream.close();

        return outputStream.toByteArray();
    }

    public static byte[] compressZLIB(byte[] data) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        while (!deflater.finished())
        {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        outputStream.close();
        return outputStream.toByteArray();
    }

    public static byte[] decompressZLIB(byte[] data) throws IOException, DataFormatException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        Inflater inflater = new Inflater();
        inflater.setInput(data);

        while (!inflater.finished())
        {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        outputStream.close();
        return outputStream.toByteArray();
    }
}
