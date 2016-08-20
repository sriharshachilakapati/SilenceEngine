/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Animation
{
    private List<Texture> frames;
    private List<Float>   frameLengths;

    public Animation()
    {
        frames = new ArrayList<>();
        frameLengths = new ArrayList<>();
    }

    public Animation addFrame(Texture texture, float length, TimeUtils.Unit unit)
    {
        frames.add(texture);
        frameLengths.add((float) TimeUtils.convert(length, unit, TimeUtils.getDefaultTimeUnit()));
        return this;
    }

    public Animation copy()
    {
        return new Animation().set(this);
    }

    public Animation set(Animation other)
    {
        clearFrames();

        for (int i = 0; i < other.frames.size(); i++)
        {
            addFrame(other.frames.get(i), other.frameLengths.get(i), TimeUtils.getDefaultTimeUnit());
        }

        return this;
    }

    public Animation clearFrames()
    {
        frames.clear();
        frameLengths.clear();
        return this;
    }

    public int size()
    {
        return frames.size();
    }

    public Texture getFrameTexture(int i)
    {
        return frames.get(i);
    }

    public float getFrameLength(int i)
    {
        return frameLengths.get(i);
    }
}
