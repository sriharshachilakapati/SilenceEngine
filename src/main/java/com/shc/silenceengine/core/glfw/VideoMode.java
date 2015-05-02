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

import java.nio.ByteBuffer;

/**
 * An object based wrapper of the GLFWvidmode structure that describes a single video mode.
 * 
 * @author Sri Harsha Chilakapati
 * @author Josh "ShadowLordAlpha"
 */
public class VideoMode
{
	
	/**
	 * The width, in screen coordinates, of the video mode.
	 */
    private int width;
    
    /**
     * The height, in screen coordinates, of the video mode.
     */
    private int height;
    
    /**
     * The bit depth of the red channel of the video mode.
     */
    private int redBits;
    
    /**
     * The bit depth of the green channel of the video mode.
     */
    private int greenBits;
    
    /**
     * The bit depth of the blue channel of the video mode.
     */
    private int blueBits;
    
    /**
     * The refresh rate, in Hz, of the video mode.
     */
    private int refreshRate;

    /**
     * Creates a new VideoMode with the given argument.
     * 
     * @param mode The ByteBuffer containing the needed arguments
     */
    VideoMode(ByteBuffer mode)
    {
    	this(mode.getInt(), mode.getInt(), mode.getInt(), mode.getInt(), mode.getInt(), mode.getInt());
    }
    
    /**
     * Create a new VideoMode with the given arguments.
     * 
     * @param width The width, in screen coordinates, of the video mode.
     * @param height The height, in screen coordinates, of the video mode.
     * @param redBits The bit depth of the red channel of the video mode.
     * @param greenBits The bit depth of the green channel of the video mode.
     * @param blueBits The bit depth of the blue channel of the video mode.
     * @param refreshRate The refresh rate, in Hz, of the video mode.
     */
    VideoMode(int width, int height, int redBits, int greenBits, int blueBits, int refreshRate)
    {
        this.width = width;
        this.height = height;
        this.redBits = redBits;
        this.greenBits = greenBits;
        this.blueBits = blueBits;
        this.refreshRate = refreshRate;
    }

    /**
     * Get the width, in screen coordinates, of the video mode.
     * @return The width, in screen coordinates, of the video mode.
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Set the width, in screen coordinates, of the video mode.
     * @param width The width, in screen coordinates, of the video mode.
     * @deprecated This Method does nothing and may be removed in later versions
     */
    public void setWidth(int width)
    {
        //this.width = width;
    }

    /**
     * Get the height, in screen coordinates, of the video mode.
     * @return The height, in screen coordinates, of the video mode.
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Set the height, in screen coordinates, of the video mode.
     * @param height The height, in screen coordinates, of the video mode.
     * @deprecated This Method does nothing and may be removed in later versions
     */
    public void setHeight(int height)
    {
        //this.height = height;
    }

    /**
     * Get the bit depth of the red channel of the video mode.
     * @return The bit depth of the red channel of the video mode.
     */
    public int getRedBits()
    {
        return redBits;
    }

    /**
     * Set the bit depth of the red channel of the video mode.
     * @param redBits The bit depth of the red channel of the video mode.
     * @deprecated This Method does nothing and may be removed in later versions
     */
    public void setRedBits(int redBits)
    {
        //this.redBits = redBits;
    }

    /**
     * Get the bit depth of the green channel of the video mode.
     * @return The bit depth of the green channel of the video mode.
     */
    public int getGreenBits()
    {
        return greenBits;
    }

    /**
     * Set the bit depth of the green channel of the video mode.
     * @param greenBits The bit depth of the green channel of the video mode.
     * @deprecated This Method does nothing and may be removed in later versions
     */
    public void setGreenBits(int greenBits)
    {
        //this.greenBits = greenBits;
    }

    /**
     * Get the bit depth of the blue channel of the video mode.
     * @return The bit depth of the blue channel of the video mode.
     */
    public int getBlueBits()
    {
        return blueBits;
    }

    /**
     * Set the bit depth of the blue channel of the video mode.
     * @param blueBits The bit depth of the blue channel of the video mode.
     * @deprecated This Method does nothing and may be removed in later versions
     */
    public void setBlueBits(int blueBits)
    {
        //this.blueBits = blueBits;
    }

    /**
     * Get the refresh rate, in Hz, of the video mode.
     * @return The refresh rate, in Hz, of the video mode.
     */
    public int getRefreshRate()
    {
        return refreshRate;
    }

    /**
     * Set the refresh rate, in Hz, of the video mode.
     * @param refreshRate The refresh rate, in Hz, of the video mode.
     * @deprecated This Method does nothing and may be removed in later versions
     */
    public void setRefreshRate(int refreshRate)
    {
        //this.refreshRate = refreshRate;
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
