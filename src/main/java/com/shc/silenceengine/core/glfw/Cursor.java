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
 * A cursor is the visual representation of the mouse pointer. You can create a cursor using an image, OpenGL texture,
 * or also choose one from the ones provided by the operating system.
 *
 * @author Sri Harsha Chilakapati
 */
public class Cursor
{
    private long handle;

    /**
     * Constructs a cursor object using the pixels from a BufferedImage.
     *
     * @param image The BufferedImage that contains the cursor image.
     */
    public Cursor(BufferedImage image)
    {
        this(image, 0, 0);
    }

    /**
     * Constructs a cursor object using the pixels from a BufferedImage and also the location of the pointer hot point
     * in the image specified in pixels.
     *
     * @param image The BufferedImage that contains the cursor image.
     * @param xHot  The x-coordinate of the cursor hotspot in pixels.
     * @param yHot  The y-coordinate of the cursor hotspot in pixels.
     */
    public Cursor(BufferedImage image, int xHot, int yHot)
    {
        this(Texture.fromBufferedImage(image), xHot, yHot);
    }

    /**
     * Constructs a cursor object using the pixels from an OpenGL Texture object and also the location of the pointer
     * hot point in the image pixels retrieved from the OpenGL Texture.
     *
     * @param image The OpenGL Texture object to retrieve the image data from.
     * @param xHot  The x-coordinate of the cursor hotspot in pixels.
     * @param yHot  The y-coordinate of the cursor hotspot in pixels.
     */
    public Cursor(Texture image, int xHot, int yHot)
    {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        ByteBuffer pixels = GLFWimage.malloc(width, height, image.getImage2D(GL_RGBA, GL_UNSIGNED_BYTE));
        handle = glfwCreateCursor(pixels, xHot, yHot);

        if (handle == NULL)
            throw new SilenceException("Unable to load cursor from texture");
    }

    /**
     * Constructs a cursor object using the pixels from an OpenGL Texture object.
     *
     * @param image The OpenGL Texture object to retrieve the image data from.
     */
    public Cursor(Texture image)
    {
        this(image, 0, 0);
    }

    /**
     * Constructs a cursor object that uses a predefined cursor defined by the operating system.
     *
     * @param standardType The type of the default cursor that this cursor object should look like.
     */
    public Cursor(Type standardType)
    {
        handle = glfwCreateStandardCursor(standardType.getType());

        if (handle == NULL)
            throw new SilenceException("Unable to load cursor from texture");
    }

    /**
     * Destroys this cursor object.
     */
    public void destroy()
    {
        glfwDestroyCursor(handle);
    }

    /**
     * @return The native handle of this cursor object.
     */
    public long getHandle()
    {
        return handle;
    }

    /**
     * The type of the OS defined cursors.
     */
    public enum Type
    {
        /**
         * The regular arrow cursor shape.
         */
        ARROW(GLFW_ARROW_CURSOR),

        /**
         * The text-input I-Beam cursor shape.
         */
        IBEAM(GLFW_IBEAM_CURSOR),

        /**
         * The crosshair cursor shape.
         */
        CROSSHAIR(GLFW_CROSSHAIR_CURSOR),

        /**
         * The hand cursor shape.
         */
        HAND(GLFW_HAND_CURSOR),

        /**
         * The horizontal resize arrow cursor shape.
         */
        HRESIZE(GLFW_HRESIZE_CURSOR),

        /**
         * The vertical resize arrow cursor shape.
         */
        VRESIZE(GLFW_VRESIZE_CURSOR);

        private int type;

        Type(int type)
        {
            this.type = type;
        }

        /**
         * @return The native value of the enum field.
         */
        public int getType()
        {
            return type;
        }
    }
}
