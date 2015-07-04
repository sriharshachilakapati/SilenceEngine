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

package com.shc.silenceengine.graphics.opengl;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.FileUtils;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Texture
{
    private static int activeUnit;

    public static Texture CURRENT;
    public static Texture EMPTY;

    private int     id;
    private float   width;
    private float   height;
    private boolean disposed;

    public Texture()
    {
        id = glGenTextures();
        GLError.check();
    }

    public Texture(int id)
    {
        this.id = id;
    }

    public static int getActiveUnit()
    {
        return activeUnit;
    }

    public static void setActiveUnit(int unit)
    {
        if (unit == activeUnit)
            return;

        GLError.check();
        glActiveTexture(GL_TEXTURE0 + unit);
        GLError.check();

        activeUnit = unit;
    }

    public static Texture fromColor(Color c, int width, int height)
    {
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                buffer.put((byte) (c.getR() * 255f))
                        .put((byte) (c.getG() * 255f))
                        .put((byte) (c.getB() * 255f))
                        .put((byte) (c.getA() * 255f));
            }
        }

        buffer.flip();

        return fromByteBuffer(buffer, width, height, 4);
    }

    public static Texture fromByteBuffer(ByteBuffer buffer, int width, int height, int components)
    {
        Texture texture = new Texture();

        texture.bind();
        texture.setFilter(GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR);
        texture.image2d(buffer, GL_UNSIGNED_BYTE, components == 4 ? GL_RGBA : GL_RGB, width, height, GL_RGBA8);
        texture.generateMipMaps();

        return texture;
    }

    public static Texture fromFilePath(FilePath path)
    {
        try
        {
            return fromInputStream(path.getInputStream());
        }
        catch (IOException e)
        {
            SilenceException.reThrow(e);
        }

        return null;
    }

    public static Texture fromResource(String name)
    {
        return fromInputStream(FileUtils.getResource(name));
    }

    public static Texture fromInputStream(InputStream stream)
    {
        ByteBuffer imageBuffer = FileUtils.readToByteBuffer(stream);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer components = BufferUtils.createIntBuffer(1);

        ByteBuffer image = stbi_load_from_memory(imageBuffer, width, height, components, 0);

        if (image == null)
            throw new SilenceException("Failed to load image: " + stbi_failure_reason());

        Texture texture = fromByteBuffer(image, width.get(), height.get(), components.get());

        stbi_image_free(image);
        return texture;
    }

    public static Texture fromBufferedImage(BufferedImage img)
    {
        ByteBuffer buffer = BufferUtils.createByteBuffer(img.getWidth() * img.getHeight() * 4);

        for (int y = 0; y < img.getHeight(); y++)
        {
            for (int x = 0; x < img.getWidth(); x++)
            {
                // Select the pixel
                int pixel = img.getRGB(x, y);
                // Add the RED component
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                // Add the GREEN component
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                // Add the BLUE component
                buffer.put((byte) (pixel & 0xFF));
                // Add the ALPHA component
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.rewind();

        return fromByteBuffer(buffer, img.getWidth(), img.getHeight(), 4);
    }

    public void bind()
    {
        if (CURRENT == this)
            return;

        if (disposed)
            throw new GLException("Cannot bind a disposed texture!");

        glBindTexture(GL_TEXTURE_2D, id);
        GLError.check();

        CURRENT = this;
    }

    public void setFilter(int min, int mag)
    {
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, min);
        GLError.check();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mag);
        GLError.check();
    }

    public void image2d(ByteBuffer data, int type, int format, int width, int height, int internalFormat)
    {
        bind();

        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, data);
        GLError.check();

        this.width = width;
        this.height = height;
    }

    public void generateMipMaps()
    {
        bind();
        glGenerateMipmap(GL_TEXTURE_2D);
        GLError.check();
    }

    public void setWrapping(int s)
    {
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, s);
        GLError.check();
    }

    public void setWrapping(int s, int t)
    {
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, s);
        GLError.check();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, t);
        GLError.check();
    }

    public void setWrapping(int s, int t, int r)
    {
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, s);
        GLError.check();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, t);
        GLError.check();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, r);
        GLError.check();
    }

    public SubTexture getSubTexture(float minU, float minV, float maxU, float maxV)
    {
        return new SubTexture(this, minU, minV, maxU, maxV);
    }

    public SubTexture getSubTexture(Vector2 min, Vector2 max)
    {
        return new SubTexture(this, min, max);
    }

    public SubTexture getSubTexture(float minU, float minV, float maxU, float maxV, float width, float height)
    {
        return new SubTexture(this, minU, minV, maxU, maxV, width, height);
    }

    public ByteBuffer getImage2D(int format)
    {
        return getImage2D(format, GL_FLOAT);
    }

    public ByteBuffer getImage2D(int format, int type)
    {
        int size = 4;

        switch (format)
        {
            case GL_RGB:
            case GL_BGR:
                size = 3;
                break;
        }

        size = (int) (size * width * height * 4);

        ByteBuffer data = BufferUtils.createByteBuffer(size);
        return getImage2D(format, type, data);
    }

    public ByteBuffer getImage2D(int format, int type, ByteBuffer data)
    {
        glGetTexImage(GL_TEXTURE_2D, 0, format, type, data);
        GLError.check();

        return data;
    }

    public void dispose()
    {
        if (isDisposed())
            throw new SilenceException("This texture is already disposed.");

        EMPTY.bind();
        GLError.check();
        glDeleteTextures(id);
        GLError.check();
        disposed = true;
    }

    public int getId()
    {
        return id;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public float getMinU()
    {
        return 0;
    }

    public float getMaxU()
    {
        return 1;
    }

    public float getMinV()
    {
        return 0;
    }

    public float getMaxV()
    {
        return 1;
    }

    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public int hashCode()
    {
        int result = getId();
        result = 31 * result + (getWidth() != +0.0f ? Float.floatToIntBits(getWidth()) : 0);
        result = 31 * result + (getHeight() != +0.0f ? Float.floatToIntBits(getHeight()) : 0);
        result = 31 * result + (isDisposed() ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Texture texture = (Texture) o;

        return getId() == texture.getId() &&
               Float.compare(texture.getWidth(), getWidth()) == 0 &&
               Float.compare(texture.getHeight(), getHeight()) == 0 &&
               isDisposed() == texture.isDisposed();
    }
}
