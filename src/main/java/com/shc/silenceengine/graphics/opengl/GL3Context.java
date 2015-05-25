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

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.math.geom2d.Rectangle;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Sri Harsha Chilakapati
 */
public final class GL3Context
{
    private GL3Context()
    {
    }

    public static void enable(int capability)
    {
        glEnable(capability);
        GLError.check();
    }

    public static void blendFunc(int src, int dst)
    {
        glBlendFunc(src, dst);
        GLError.check();
    }

    public static void disable(int capability)
    {
        glDisable(capability);
        GLError.check();
    }

    /**
     * Sets the background color of the window
     *
     * @param color Background color
     */
    public static void clearColor(Color color)
    {
        SilenceEngine.graphics.getClearColor().set(color);
        clearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Sets the background color of the window
     *
     * @param r Red
     * @param g Green
     * @param b Blue
     * @param a Alpha
     */
    public static void clearColor(float r, float g, float b, float a)
    {
        SilenceEngine.graphics.getClearColor().set(r, g, b, a);
        glClearColor(r, g, b, a);
        GLError.check();
    }

    /**
     * Calls <code>glClear()</code> on <code>buffers</code>
     *
     * @param buffers The buffer to be cleared
     */
    public static void clear(int buffers)
    {
        glClear(buffers);
        GLError.check();
    }

    /**
     * Binds a {@link VertexArray} and draws it to the screen
     *
     * @param vao         The <code>VertexArray</code> to be drawn
     * @param mode        The GL Primitive to be used for drawing (Primitive.TRIANGLE is recommended)
     * @param offset      The position in the <code>vao</code> to begin drawing (Use 0 for the beginning)
     * @param vertexCount The amount of vertices in the <code>vao</code>
     */
    public static void drawArrays(VertexArray vao, Primitive mode, int offset, int vertexCount)
    {
        vao.bind();
        glDrawArrays(mode.getGlPrimitive(), offset, vertexCount);
        GLError.check();

        SilenceEngine.graphics.renderCalls++;
    }

    public static void drawElements(VertexArray vao, Primitive mode, int offset, int vertexCount, int type)
    {
        vao.bind();
        glDrawElements(mode.getGlPrimitive(), vertexCount, type, offset);
        GLError.check();

        SilenceEngine.graphics.renderCalls++;
    }

    /**
     * Bind the VertexArray for use in OpenGL.
     *
     * @param vao The VertexArray to be bound. If null the currently bound VertexArray will be unbound
     */
    public static void bindVertexArray(VertexArray vao)
    {
        if (vao == null)
        {
            glBindVertexArray(0);
            GLError.check();
            return;
        }

        vao.bind();
    }

    /**
     * Bind the {@link BufferObject} for use in OpenGL.
     *
     * @param vbo The BufferObject to be bound. If null the currently bound BufferObject will be unbound
     */
    public static void bindVertexBuffer(BufferObject vbo)
    {
        if (vbo == null)
        {
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            GLError.check();
            return;
        }

        vbo.bind();
    }

    /**
     * Set the display to use the viewport specified with <code>rect</code>
     *
     * @param rect The viewport location and size
     */
    public static void viewport(Rectangle rect)
    {
        viewport(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    /**
     * Set the display to use the viewport specified
     *
     * @param x      X coordinate
     * @param y      Y coordinate
     * @param width  Width
     * @param height Height
     */
    public static void viewport(float x, float y, float width, float height)
    {
        glViewport((int) x, (int) y, (int) width, (int) height);
        GLError.check();
    }

    /**
     * Sets whether or not OpenGL should use a depth mask
     *
     * @param value Depth mask
     */
    public static void depthMask(boolean value)
    {
        glDepthMask(value);
        GLError.check();
    }

    public static void depthFunc(int func)
    {
        glDepthFunc(func);
        GLError.check();
    }

    public static void cullFace(int mode)
    {
        glCullFace(mode);
        GLError.check();
    }
}
