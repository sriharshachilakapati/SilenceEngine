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

package com.shc.silenceengine.core.glfw;

/**
 * @author Sri Harsha Chilakapati
 */
public class VideoMode
{
    private int width;
    private int height;
    private int redBits;
    private int greenBits;
    private int blueBits;
    private int refreshRate;

    public VideoMode(int width, int height, int redBits, int greenBits, int blueBits, int refreshRate)
    {
        this.width = width;
        this.height = height;
        this.redBits = redBits;
        this.greenBits = greenBits;
        this.blueBits = blueBits;
        this.refreshRate = refreshRate;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getRedBits()
    {
        return redBits;
    }

    public void setRedBits(int redBits)
    {
        this.redBits = redBits;
    }

    public int getGreenBits()
    {
        return greenBits;
    }

    public void setGreenBits(int greenBits)
    {
        this.greenBits = greenBits;
    }

    public int getBlueBits()
    {
        return blueBits;
    }

    public void setBlueBits(int blueBits)
    {
        this.blueBits = blueBits;
    }

    public int getRefreshRate()
    {
        return refreshRate;
    }

    public void setRefreshRate(int refreshRate)
    {
        this.refreshRate = refreshRate;
    }

    @Override
    public int hashCode()
    {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + redBits;
        result = 31 * result + greenBits;
        result = 31 * result + blueBits;
        result = 31 * result + refreshRate;
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VideoMode videoMode = (VideoMode) o;

        return blueBits == videoMode.blueBits &&
               greenBits == videoMode.greenBits &&
               height == videoMode.height &&
               redBits == videoMode.redBits &&
               refreshRate == videoMode.refreshRate &&
               width == videoMode.width;
    }

    @Override
    public String toString()
    {
        return "VideoMode{" +
               "width=" + width +
               ", height=" + height +
               ", redBits=" + redBits +
               ", greenBits=" + greenBits +
               ", blueBits=" + blueBits +
               ", refreshRate=" + refreshRate +
               '}';
    }
}
