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
        super(parent.getId());

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

    public SubTexture getSubTexture(float minU, float minV, float maxU, float maxV)
    {
        minU = (minU * getWidth()) / parent.getWidth();
        minV = (minV * getHeight()) / parent.getHeight();
        maxU = (maxU * getWidth()) / parent.getWidth();
        maxV = (maxV * getHeight()) / parent.getHeight();

        minU += this.minU;
        minV += this.minV;
        maxU += minU;
        maxV += minV;

        return new SubTexture(parent, minU, minV, maxU, maxV);
    }

    public SubTexture getSubTexture(Vector2 min, Vector2 max)
    {
        return getSubTexture(min.x, min.y, max.x, max.y);
    }

    public float getMinU()
    {
        return minU;
    }

    public float getMaxU()
    {
        return maxU;
    }

    public float getMinV()
    {
        return minV;
    }

    public float getMaxV()
    {
        return maxV;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }


    public void dispose()
    {
        throw new SilenceException("Sub-Textures cannot be disposed! Just ignore them!");
    }
}
