package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Image;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.ImageReader;
import com.shc.silenceengine.utils.TaskManager;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class LwjglImageReader extends ImageReader
{
    @Override
    public void readImage(DirectBuffer memory, OnComplete onComplete)
    {
        new Thread(() ->
        {
            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer components = BufferUtils.createIntBuffer(1);

            ByteBuffer imageBuffer = stbi_load_from_memory((ByteBuffer) memory.nativeBuffer(), width, height, components, 4);

            if (imageBuffer == null)
                throw new SilenceException("Failed to load image: " + stbi_failure_reason());

            Image image = new Image(width.get(0), height.get(0));

            for (int y = 0; y < image.getHeight(); y++)
            {
                for (int x = 0; x < image.getWidth(); x++)
                {
                    int start = 4 * (y * image.getWidth() + x);

                    float r = (imageBuffer.get(start) & 0xff) / 255f;
                    float g = (imageBuffer.get(start + 1) & 0xff) / 255f;
                    float b = (imageBuffer.get(start + 2) & 0xff) / 255f;
                    float a = 1 - (imageBuffer.get(start + 3) & 0xff) / 255f;

                    image.setPixel(x, y, new Color(r, g, b, a));
                }
            }

            stbi_image_free(imageBuffer);

            TaskManager.addUpdateTask(() -> onComplete.invoke(image));
        }).start();
    }
}
