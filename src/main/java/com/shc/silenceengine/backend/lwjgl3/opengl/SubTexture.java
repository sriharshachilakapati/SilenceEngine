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

package com.shc.silenceengine.backend.lwjgl3.opengl;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class SubTexture extends Texture
{
    private Texture parent;

    private float minU;
    private float minV;
    private float maxU;
    private float maxV;

    private float width;
    private float height;

    public SubTexture(Texture parent, Vector2 min, Vector2 max)
    {
        this(parent, min.getX(), min.getY(), max.getX(), max.getY());
    }

    public SubTexture(Texture parent, float minU, float minV, float maxU, float maxV)
    {
        super(parent.getID());

        this.parent = parent;
        this.minU = minU;
        this.minV = minV;

        this.maxU = maxU;
        this.maxV = maxV;

        this.width = (maxU - minU) * parent.getWidth();
        this.height = (maxV - minV) * parent.getHeight();
    }

    public SubTexture(Texture parent, float minU, float minV, float maxU, float maxV, float width, float height)
    {
        this(parent, minU, minV, maxU, maxV);

        this.width = width;
        this.height = height;
    }

    public Texture getParent()
    {
        return parent;
    }

    @Override
    public SubTexture getSubTexture(float minU, float minV, float maxU, float maxV)
    {
        minU = (minU * getWidth()) / getParent().getWidth();
        minV = (minV * getHeight()) / getParent().getHeight();
        maxU = (maxU * getWidth()) / getParent().getWidth();
        maxV = (maxV * getHeight()) / getParent().getHeight();

        minU += this.minU;
        minV += this.minV;
        maxU += minU;
        maxV += minV;

        return new SubTexture(parent, minU, minV, maxU, maxV);
    }

    @Override
    public SubTexture getSubTexture(Vector2 min, Vector2 max)
    {
        return getSubTexture(min.x, min.y, max.x, max.y);
    }

    @Override
    public void dispose()
    {
        throw new SilenceException("Sub-Textures cannot be disposed! Just ignore them!");
    }

    @Override
    public float getWidth()
    {
        return width;
    }

    @Override
    public float getHeight()
    {
        return height;
    }

    @Override
    public float getMinU()
    {
        return minU;
    }

    @Override
    public float getMaxU()
    {
        return maxU;
    }

    @Override
    public float getMinV()
    {
        return minV;
    }

    @Override
    public float getMaxV()
    {
        return maxV;
    }
}
