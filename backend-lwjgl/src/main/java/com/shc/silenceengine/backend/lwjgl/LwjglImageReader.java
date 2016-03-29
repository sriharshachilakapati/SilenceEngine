package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Image;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.ImageReader;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
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
            FloatBuffer pixels = imageBuffer.asFloatBuffer();

            for (int x = 0; x < image.getWidth(); x++)
            {
                for (int y = 0; y < image.getHeight(); y++)
                {
                    int start = y * height.get(0) + x;

                    float r = pixels.get(start);
                    float g = pixels.get(start + 1);
                    float b = pixels.get(start + 2);
                    float a = pixels.get(start + 3);

                    image.setPixel(x, y, new Color(r, g, b, a));
                }
            }

            stbi_image_free(imageBuffer);

            onComplete.invoke(image);
        }).start();
    }
}
