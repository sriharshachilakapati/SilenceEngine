package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author Sri Harsha Chilakapati
 */
public class LwjglFileReader extends FileReader
{
    @Override
    public void readFile(FilePath file, OnComplete onComplete)
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

                onComplete.invoke(directBuffer);
            }
            catch (Exception e)
            {
                // TODO: ADD LOGGER
//                SilenceException.reThrow(e);
            }

        }).start();
    }
}
