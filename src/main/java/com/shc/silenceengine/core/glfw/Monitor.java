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

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Monitor
{
    private static Monitor primary;
    private static List<Monitor> monitors;

    private long handle;

    private List<VideoMode> videoModes;

    private Monitor(long handle)
    {
        this.handle = handle;
    }

    public static Monitor getPrimaryMonitor()
    {
        if (primary == null)
            primary = new Monitor(glfwGetPrimaryMonitor());

        return primary;
    }

    public static List<Monitor> getMonitors()
    {
        if (monitors == null)
        {
            monitors = new ArrayList<>();

            PointerBuffer buffer = glfwGetMonitors();

            while (buffer.hasRemaining())
                monitors.add(new Monitor(buffer.get()));
        }

        return monitors;
    }

    public List<VideoMode> getVideoModes()
    {
        if (videoModes == null)
        {
            videoModes = new ArrayList<>();

            IntBuffer  count = BufferUtils.createIntBuffer(1);
            ByteBuffer modes = glfwGetVideoModes(handle, count);

            for (int i = 0; i < count.get(0); i++)
            {
                int width       = modes.getInt();
                int height      = modes.getInt();
                int redBits     = modes.getInt();
                int greenBits   = modes.getInt();
                int blueBits    = modes.getInt();
                int refreshRate = modes.getInt();

                videoModes.add(new VideoMode(width, height, redBits, greenBits, blueBits, refreshRate));
            }
        }

        return videoModes;
    }

    public long getHandle()
    {
        return handle;
    }

    public boolean isPrimary()
    {
        return monitors.get(0).equals(this);
    }

    public String getName()
    {
        return glfwGetMonitorName(handle);
    }

    @Override
    public String toString()
    {
        return "Monitor{" +
               "handle=" + handle + "," +
               "name=\"" + getName() + "\"," +
               "primary=" + isPrimary() +
               '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Monitor monitor = (Monitor) o;

        return handle == monitor.handle;
    }

    @Override
    public int hashCode()
    {
        return (int) (handle ^ (handle >>> 32));
    }
}
