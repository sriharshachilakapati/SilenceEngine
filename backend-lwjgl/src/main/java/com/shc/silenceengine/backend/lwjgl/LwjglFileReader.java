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

package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.UniCallback;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Sri Harsha Chilakapati
 */
class LwjglFileReader extends FileReader
{
    @Override
    public void readBinaryFile(FilePath file, UniCallback<DirectBuffer> onComplete, UniCallback<Throwable> onError)
    {
        new Thread(() ->
        {
            try
            {
                InputStream inputStream = ((LwjglFilePath) file).getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                byte[] buffer = new byte[4096];

                while (true)
                {
                    int n = inputStream.read(buffer);

                    if (n < 0)
                        break;

                    outputStream.write(buffer, 0, n);
                }

                inputStream.close();

                byte[] bytes = outputStream.toByteArray();

                DirectBuffer directBuffer = new LwjglDirectBuffer(bytes.length);

                for (int i = 0; i < bytes.length; i++)
                    directBuffer.writeByte(i, bytes[i]);

                TaskManager.runOnUpdate(() -> onComplete.invoke(directBuffer));
            }
            catch (Throwable e)
            {
                onError.invoke(e);
            }
        }).start();
    }

    @Override
    public void readTextFile(FilePath file, UniCallback<String> onComplete, UniCallback<Throwable> onError)
    {
        new Thread(() ->
        {
            try (
                    InputStream inputStream = ((LwjglFilePath) file).getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
            )
            {
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line).append("\n");

                TaskManager.runOnUpdate(() -> onComplete.invoke(stringBuilder.toString()));
            }
            catch (Throwable e)
            {
                onError.invoke(e);
            }

        }).start();
    }
}
