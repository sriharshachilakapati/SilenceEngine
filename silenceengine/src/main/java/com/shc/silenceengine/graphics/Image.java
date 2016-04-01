package com.shc.silenceengine.graphics;

import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.DirectFloatBuffer;

/**
 * Represents an image in memory. This is not a texture. The main difference between the image and texture is that image
 * resides in the main memory that is RAM, and the texture is it's copy which resides in the video memory, that is VRAM.
 * Textures are created using images.
 *
 * @author Sri Harsha Chilakapati
 */
public class Image
{
    private int width;
    private int height;

    private DirectFloatBuffer imageData;

    public Image(int width, int height)
    {
        this.width = width;
        this.height = height;

        imageData = new DirectFloatBuffer(width * height * 4);
    }

    public Image setPixel(int x, int y, Color pixel)
    {
        if (pixel == null)
            throw new IllegalArgumentException("pixel cannot be null.");

        int start = 4 * (width * y + x);

        imageData.write(start, pixel.x)
                .write(start + 1, pixel.y)
                .write(start + 2, pixel.z)
                .write(start + 3, pixel.w);

        return this;
    }

    public Color getPixel(int x, int y, Color pixelOut)
    {
        if (pixelOut == null)
            throw new IllegalArgumentException("pixelOut cannot be null.");

        int start = 4 * (width * y + x);

        pixelOut.x = imageData.read(start);
        pixelOut.y = imageData.read(start + 1);
        pixelOut.z = imageData.read(start + 2);
        pixelOut.w = imageData.read(start + 3);

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

    public DirectBuffer getImageData()
    {
        return imageData.getDirectBuffer();
    }
}
