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

package com.shc.silenceengine.backend.lwjgl.glfw;

import java.util.Arrays;

/**
 * <p> This class describes the Gamma Ramp of a monitor. To set the gamma ramp, you have to use the
 * <code>setGammaRamp</code> method on the <code>Monitor</code> object.</p>
 *
 * <pre>
 *     short[] red   = new short[256];
 *     short[] green = new short[256];
 *     short[] blue  = new short[256];
 *
 *     for (int i = 0; i &lt; 256; i++)
 *     {
 *         // Fill out the gamma ramp arrays as desired
 *     }
 *
 *     GammaRamp ramp = new GammaRamp(red, green, blue);
 *     monitor.setGammaRamp(ramp);
 * </pre>
 *
 * <p> It is recommended to use GammaRamps of size 256, as that is the size supported by the graphic cards of all the
 * platforms, and on windows, the size must be 256.</p>
 *
 * @author Sri Harsha Chilakapati
 */
public class GammaRamp
{
    // The gamma ramp arrays
    private short[] red;
    private short[] green;
    private short[] blue;

    // The size of the gamma ramp, elements in the arrays
    private int size;

    /**
     * Constructs a GammaRamp for a Monitor object using the gamma values from the red, green and blue channels.
     *
     * @param red   The gamma values of the red channel.
     * @param green The gamma values of the green channel.
     * @param blue  The gamma values of the blue channel.
     */
    public GammaRamp(short[] red, short[] green, short[] blue)
    {
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }

    /**
     * @return An array of values describing the response of the red channel.
     */
    public short[] getRed()
    {
        return red;
    }

    /**
     * Sets the array of gamma values that describe the red channel.
     *
     * @param red The array of values describing the response of the red channel.
     */
    public void setRed(short... red)
    {
        size = Math.max(size, red.length);
        this.red = Arrays.copyOf(red, size);
    }

    /**
     * @return An array of values describing the response of the green channel.
     */
    public short[] getGreen()
    {
        return green;
    }

    /**
     * Sets the array of gamma values that describe the green channel.
     *
     * @param green The array of values describing the response of the green channel.
     */
    public void setGreen(short... green)
    {
        size = Math.max(size, green.length);
        this.green = Arrays.copyOf(green, size);
    }

    /**
     * @return An array of values describing the response of the blue channel.
     */
    public short[] getBlue()
    {
        return blue;
    }

    /**
     * Sets the array of gamma values that describe the blue channel.
     *
     * @param blue The array of values describing the response of the blue channel.
     */
    public void setBlue(short... blue)
    {
        size = Math.max(size, blue.length);
        this.blue = Arrays.copyOf(blue, size);
    }

    /**
     * @return The number of elements in each array.
     */
    public int getSize()
    {
        return size;
    }

    @Override
    public int hashCode()
    {
        int result = Arrays.hashCode(red);
        result = 31 * result + Arrays.hashCode(green);
        result = 31 * result + Arrays.hashCode(blue);
        result = 31 * result + size;
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GammaRamp gammaRamp = (GammaRamp) o;

        return size == gammaRamp.size &&
               Arrays.equals(red, gammaRamp.red) &&
               Arrays.equals(green, gammaRamp.green) &&
               Arrays.equals(blue, gammaRamp.blue);
    }

    @Override
    public String toString()
    {
        return "GammaRamp{" +
               "red=" + Arrays.toString(red) +
               ",\ngreen=" + Arrays.toString(green) +
               ",\nblue=" + Arrays.toString(blue) +
               ",\nsize=" + size +
               '}';
    }
}
