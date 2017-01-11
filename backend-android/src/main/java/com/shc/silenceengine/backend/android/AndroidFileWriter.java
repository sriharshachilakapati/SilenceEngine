/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.backend.android;

import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileWriter;
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import com.shc.silenceengine.utils.functional.UniCallback;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author Sri Harsha Chilakapati
 */
class AndroidFileWriter extends FileWriter
{
    @Override
    public void write(String text, FilePath file, boolean append, SimpleCallback onSuccess, UniCallback<Throwable> onError)
    {
        AsyncRunner.runAsync(() ->
        {
            try
            {
                if (file.getType() == FilePath.Type.RESOURCE)
                    throw new IOException("Cannot write to resource files");

                try (
                        OutputStream outputStream = ((AndroidFilePath) file).getOutputStream(append);
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))
                )
                {
                    writer.write(text);
                    writer.flush();
                }

                return () -> TaskManager.runOnUpdate(onSuccess);
            }
            catch (IOException e)
            {
                return () -> onError.invoke(e);
            }
        });
    }

    @Override
    public void write(DirectBuffer buffer, FilePath file, boolean append, SimpleCallback onSuccess, UniCallback<Throwable> onError)
    {
        AsyncRunner.runAsync(() ->
        {
            try
            {
                if (file.getType() == FilePath.Type.RESOURCE)
                    throw new IOException("Cannot write to resource files");

                try (
                        OutputStream outputStream = ((AndroidFilePath) file).getOutputStream(append);
                        OutputStreamWriter writer = new OutputStreamWriter(outputStream)
                )
                {
                    for (int i = 0; i < buffer.sizeBytes(); i++)
                        writer.write(buffer.readByte(i));

                    writer.flush();
                }

                return () -> TaskManager.runOnUpdate(onSuccess);
            }
            catch (IOException e)
            {
                return () -> onError.invoke(e);
            }
        });
    }
}
