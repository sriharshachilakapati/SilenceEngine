package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;
import com.shc.silenceengine.utils.TaskManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Sri Harsha Chilakapati
 */
public class LwjglFileReader extends FileReader
{
    @Override
    public void readBinaryFile(FilePath file, OnComplete<DirectBuffer> onComplete)
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

                TaskManager.addUpdateTask(() -> onComplete.invoke(directBuffer));
            }
            catch (Exception e)
            {
                SilenceEngine.log.getRootLogger().error(e);
            }
        }).start();
    }

    @Override
    public void readTextFile(FilePath file, OnComplete<String> onComplete)
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

                TaskManager.addUpdateTask(() -> onComplete.invoke(stringBuilder.toString()));
            }
            catch (Exception e)
            {
                SilenceEngine.log.getRootLogger().error(e);
            }

        }).start();
    }
}
