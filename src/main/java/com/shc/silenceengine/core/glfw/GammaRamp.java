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

import java.util.Arrays;

/**
 * An object based wrapper of the GLFWgammaramp structure that describes the Gamma Ramp
 *
 * @author Sri Harsha Chilakapati
 * @author Josh "ShadowLordAlpha"
 */
public class GammaRamp
{
	
	/**
	 * An array of value describing the response of the red channel.
	 */
    private short[] red;
    
    /**
     * An array of value describing the response of the green channel.
     */
    private short[] green;
    
    /**
     * An array of value describing the response of the blue channel.
     */
    private short[] blue;

    /**
     * The number of elements in each array.
     */
    private int size;

    /**
     * Create a new GammaRamp with size 0 and all arrays set to 0.
     */
    public GammaRamp()
    {
    	this(new short[0], new short[0], new short[0], 0);
    }

    /**
     * Create a new GammaRamp with the given arguments.
     * 
     * @param red An array of value describing the response of the red channel.
     * @param green An array of value describing the response of the blue channel.
     * @param blue An array of value describing the response of the blue channel.
     * @param size The number of elements in each array.
     */
    public GammaRamp(short[] red, short[] green, short[] blue, int size)
    {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setSize(size);
    }

    /**
     * Get the array of values describing the response of the red channel.
     * @return The array of values describing the response of the red channel.
     */
    public short[] getRed()
    {
        return red;
    }

    /**
     * Set the array of values describing the response of the red channel.
     * @param red The array of values describing the response of the red channel.
     */
    public void setRed(short... red)
    {
        this.red = red;
    }

    /**
     * Get the array of values describing the response of the green channel.
     * @return The array of values describing the response of the green channel.
     */
    public short[] getGreen()
    {
        return green;
    }

    /**
     * Set the array of values describing the response of the green channel.
     * @param red The array of values describing the response of the green channel.
     */
    public void setGreen(short... green)
    {
        this.green = green;
    }

    /**
     * Get the array of values describing the response of the blue channel.
     * @return The array of values describing the response of the blue channel.
     */
    public short[] getBlue()
    {
        return blue;
    }

    /**
     * Set the array of values describing the response of the blue channel.
     * @param red The array of values describing the response of the blue channel.
     */
    public void setBlue(short... blue)
    {
        this.blue = blue;
    }

    /**
     * Get the number of elements in each array.
     * @return The number of elements in each array.
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Set the number of elements in each array.
     * @param size The number of elements in each array.
     */
    public void setSize(int size)
    {
        this.size = size;
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
    public int hashCode()
    {
        int result = Arrays.hashCode(red);
        result = 31 * result + Arrays.hashCode(green);
        result = 31 * result + Arrays.hashCode(blue);
        result = 31 * result + size;
        return result;
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
