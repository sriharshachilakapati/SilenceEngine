/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Image;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.ImageReader;
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.UniCallback;
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
    public void readImage(DirectBuffer memory, UniCallback<Image> onComplete)
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
                    float a = (imageBuffer.get(start + 3) & 0xff) / 255f;

                    image.setPixel(x, y, new Color(r, g, b, a));
                }
            }

            stbi_image_free(imageBuffer);

            TaskManager.runOnUpdate(() -> onComplete.invoke(image));
        }).start();
    }
}
