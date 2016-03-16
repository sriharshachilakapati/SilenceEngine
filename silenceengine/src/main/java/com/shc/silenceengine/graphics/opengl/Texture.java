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

import com.shc.silenceengine.core.IResource;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.math.Vector2;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Texture implements IResource
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
        id = SilenceEngine.graphics.glGenTextures();
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
        SilenceEngine.graphics.glActiveTexture(GL_TEXTURE0 + unit);
        GLError.check();

        activeUnit = unit;
    }

    public static Texture fromColor(Color c, int width, int height)
    {
        DirectBuffer buffer = DirectBuffer.create(width * height * 4);

        int index = 0;

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                buffer.writeByte(index++, (byte) (c.getR() * 255f))
                        .writeByte(index++, (byte) (c.getG() * 255f))
                        .writeByte(index++, (byte) (c.getB() * 255f))
                        .writeByte(index++, (byte) (c.getA() * 255f));
            }
        }

        return fromDirectBuffer(buffer, width, height, 4);
    }

    public static Texture fromDirectBuffer(DirectBuffer buffer, int width, int height, int components)
    {
        Texture texture = new Texture();

        texture.bind();
        texture.setFilter(GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR);
        texture.image2d(buffer, GL_UNSIGNED_BYTE, components == 4 ? GL_RGBA : GL_RGB, width, height, GL_RGBA4);
        texture.generateMipMaps();

        return texture;
    }

    public void bind()
    {
        if (CURRENT == this)
            return;

        if (disposed)
            throw new GLException("Cannot bind a disposed texture!");

        SilenceEngine.graphics.glBindTexture(GL_TEXTURE_2D, id);
        GLError.check();

        CURRENT = this;
    }

    public void bind(int unit)
    {
        setActiveUnit(unit);
        bind();
    }

    public void setFilter(int min, int mag)
    {
        bind();

        SilenceEngine.graphics.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, min);
        GLError.check();
        SilenceEngine.graphics.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mag);
        GLError.check();
    }

    public void image2d(DirectBuffer data, int type, int format, int width, int height, int internalFormat)
    {
        image2d(data, 0, type, format, width, height, internalFormat);
    }

    public void image2d(DirectBuffer data, int level, int type, int format, int width, int height, int internalFormat)
    {
        bind();

        SilenceEngine.graphics.glTexImage2D(GL_TEXTURE_2D, level, internalFormat, width, height, 0, format, type, data);
        GLError.check();

        this.width = width;
        this.height = height;
    }

    public void generateMipMaps()
    {
        bind();
        SilenceEngine.graphics.glGenerateMipmap(GL_TEXTURE_2D);
        GLError.check();
    }

    public void setWrapping(int s)
    {
        bind();

        SilenceEngine.graphics.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, s);
        GLError.check();
    }

    public void setWrapping(int s, int t)
    {
        bind();

        SilenceEngine.graphics.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, s);
        GLError.check();
        SilenceEngine.graphics.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, t);
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

    public void dispose()
    {
        if (isDisposed())
            throw new SilenceException("This texture is already disposed.");

        EMPTY.bind();
        GLError.check();
        SilenceEngine.graphics.glDeleteTextures(id);
        GLError.check();
        disposed = true;
    }

    public int getID()
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
        int result = getID();
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

        return getID() == texture.getID() &&
               Float.compare(texture.getWidth(), getWidth()) == 0 &&
               Float.compare(texture.getHeight(), getHeight()) == 0 &&
               isDisposed() == texture.isDisposed();
    }
}
