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

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public final class GLContext
{
    private GLContext()
    {
    }

    public static void enable(int capability)
    {
        SilenceEngine.graphics.glEnable(capability);
        GLError.check();
    }

    public static void blendFunc(int src, int dst)
    {
        SilenceEngine.graphics.glBlendFunc(src, dst);
        GLError.check();
    }

    public static void disable(int capability)
    {
        SilenceEngine.graphics.glDisable(capability);
        GLError.check();
    }

    /**
     * Sets the background color of the window
     *
     * @param color Background color
     */
    public static void clearColor(Color color)
    {
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
        SilenceEngine.graphics.glClearColor(r, g, b, a);
        GLError.check();
    }

    /**
     * Calls <code>glClear()</code> on <code>buffers</code>
     *
     * @param buffers The buffer to be cleared
     */
    public static void clear(int buffers)
    {
        SilenceEngine.graphics.glClear(buffers);
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
        SilenceEngine.graphics.glDrawArrays(mode.getGlPrimitive(), offset, vertexCount);
        GLError.check();

//        SilenceEngine.graphics.renderCalls++;
    }

    public static void drawElements(VertexArray vao, Primitive mode, int offset, int vertexCount, int type)
    {
        vao.bind();
        SilenceEngine.graphics.glDrawElements(mode.getGlPrimitive(), vertexCount, type, offset);
        GLError.check();

//        SilenceEngine.graphics.renderCalls++;
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
            SilenceEngine.graphics.glBindVertexArray(0);
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
            SilenceEngine.graphics.glBindBuffer(GL_ARRAY_BUFFER, 0);
            GLError.check();
            return;
        }

        vbo.bind();
    }

    /**
     * Set the display to use the viewport specified
     *
     * @param x      X coordinate
     * @param y      Y coordinate
     * @param width  Width
     * @param height Height
     */
    public static void viewport(int x, int y, int width, int height)
    {
        SilenceEngine.graphics.glViewport(x, y, width, height);
        GLError.check();
    }

    /**
     * Sets whether or not OpenGL should use a depth mask
     *
     * @param value Depth mask
     */
    public static void depthMask(boolean value)
    {
        SilenceEngine.graphics.glDepthMask(value);
        GLError.check();
    }

    public static void depthFunc(int func)
    {
        SilenceEngine.graphics.glDepthFunc(func);
        GLError.check();
    }

    public static void cullFace(int mode)
    {
        SilenceEngine.graphics.glCullFace(mode);
        GLError.check();
    }
}
