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

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.IResource;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.DirectFloatBuffer;

/**
 * Represents an image in memory. This is not a texture. The main difference between the image and texture is that image
 * resides in the main memory that is RAM, and the texture is it's copy which resides in the video memory, that is VRAM.
 * Textures are created using images.
 *
 * @author Sri Harsha Chilakapati
 */
public class Image implements IResource
{
    private boolean isDisposed;

    private int width;
    private int height;

    private int originalWidth;
    private int originalHeight;

    private DirectFloatBuffer imageData;

    public Image(int width, int height)
    {
        this(width, height, width, height);
    }

    public Image(int width, int height, int originalWidth, int originalHeight)
    {
        this.width = width;
        this.height = height;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;

        imageData = new DirectFloatBuffer(width * height * 4);
    }

    public Image setPixel(int x, int y, Color pixel)
    {
        if (pixel == null)
            throw new IllegalArgumentException("pixel cannot be null.");

        int start = 4 * (width * y + x);

        imageData.write(start, pixel.r)
                .write(start + 1, pixel.g)
                .write(start + 2, pixel.b)
                .write(start + 3, pixel.a);

        return this;
    }

    public Color getPixel(int x, int y, Color pixelOut)
    {
        if (pixelOut == null)
            throw new IllegalArgumentException("pixelOut cannot be null.");

        int start = 4 * (width * y + x);

        pixelOut.r = imageData.read(start);
        pixelOut.g = imageData.read(start + 1);
        pixelOut.b = imageData.read(start + 2);
        pixelOut.a = imageData.read(start + 3);

        return pixelOut;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getOriginalWidth()
    {
        return originalWidth;
    }

    public int getOriginalHeight()
    {
        return originalHeight;
    }

    public DirectBuffer getImageData()
    {
        return imageData.getDirectBuffer();
    }

    public void dispose()
    {
        if (isDisposed)
        {
            SilenceEngine.log.getRootLogger().error("Cannot dispose more than once");
            return;
        }

        SilenceEngine.io.free(imageData.getDirectBuffer());
        isDisposed = true;
    }
}
