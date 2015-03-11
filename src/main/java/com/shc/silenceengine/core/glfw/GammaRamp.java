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
public class GammaRamp
{
    private short red;
    private short green;
    private short blue;

    private int size;

    public GammaRamp(short red, short green, short blue, int size)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.size = size;
    }

    public short getRed()
    {
        return red;
    }

    public void setRed(short red)
    {
        this.red = red;
    }

    public short getGreen()
    {
        return green;
    }

    public void setGreen(short green)
    {
        this.green = green;
    }

    public short getBlue()
    {
        return blue;
    }

    public void setBlue(short blue)
    {
        this.blue = blue;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    @Override
    public String toString()
    {
        return "GammaRamp{" +
               "red=" + red +
               ", green=" + green +
               ", blue=" + blue +
               ", size=" + size +
               '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GammaRamp gammaRamp = (GammaRamp) o;

        if (blue != gammaRamp.blue) return false;
        if (green != gammaRamp.green) return false;
        if (red != gammaRamp.red) return false;
        if (size != gammaRamp.size) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (int) red;
        result = 31 * result + (int) green;
        result = 31 * result + (int) blue;
        result = 31 * result + size;
        return result;
    }
}
