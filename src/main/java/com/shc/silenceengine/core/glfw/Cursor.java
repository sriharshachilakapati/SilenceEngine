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

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.opengl.Texture;
import org.lwjgl.glfw.GLFWimage;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Cursor
{
    private long handle;

    public Cursor(BufferedImage image)
    {
        this(image, 0, 0);
    }

    public Cursor(BufferedImage image, int xHot, int yHot)
    {
        this(Texture.fromBufferedImage(image), xHot, yHot);
    }

    public Cursor(Texture image, int xHot, int yHot)
    {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        ByteBuffer pixels = GLFWimage.malloc(width, height, image.getImage2D(GL_RGBA, GL_UNSIGNED_BYTE));
        handle = glfwCreateCursor(pixels, xHot, yHot);

        if (handle == NULL)
            throw new SilenceException("Unable to load cursor from texture");
    }

    public Cursor(Texture image)
    {
        this(image, 0, 0);
    }

    public Cursor(Type standardType)
    {
        handle = glfwCreateStandardCursor(standardType.getType());

        if (handle == NULL)
            throw new SilenceException("Unable to load cursor from texture");
    }

    public void destroy()
    {
        glfwDestroyCursor(handle);
    }

    public long getHandle()
    {
        return handle;
    }

    public enum Type
    {
        ARROW(GLFW_ARROW_CURSOR),
        IBEAM(GLFW_IBEAM_CURSOR),
        CROSSHAIR(GLFW_CROSSHAIR_CURSOR),
        HAND(GLFW_HAND_CURSOR),
        HRESIZE(GLFW_HRESIZE_CURSOR),
        VRESIZE(GLFW_VRESIZE_CURSOR);

        private int type;

        Type(int type)
        {
            this.type = type;
        }

        public int getType()
        {
            return type;
        }
    }
}
