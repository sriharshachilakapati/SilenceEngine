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

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * This class encapsulates OpenGL Vertex Array Objects nicely and cleanly allowing you to use OpenGL in an object
 * oriented way. <p> An OpenGL VAO is an OpenGL object that keeps track of the links between the attributes and
 * VertexBufferObjects.
 *
 * @author Sri Harsha Chilakapati
 */
public class VertexArray
{
    private int     id;
    private boolean disposed;

    /**
     * Constructs a VertexArrayObject.
     */
    public VertexArray()
    {
        id = glGenVertexArrays();
        GLError.check();
    }

    /**
     * Specifies the index of the vertex attribute to be enabled
     *
     * @param index The index which should be enabled
     */
    public void enableAttributeArray(int index)
    {
        bind();
        glEnableVertexAttribArray(index);
        GLError.check();
    }

    /**
     * Binds this VertexArray to the OpenGL context
     */
    public void bind()
    {
        if (disposed)
            throw new GLException("VertexArray is disposed!");

        glBindVertexArray(id);
        GLError.check();
    }

    /**
     * Specifies the index of the vertex attribute to be disabled
     *
     * @param index The index which should be disabled
     */
    public void disableAttributeArray(int index)
    {
        bind();
        glDisableVertexAttribArray(index);
        GLError.check();
    }

    /**
     * Points a VertexBufferObject towards a specified location
     *
     * @param index  The location to point the VertexBufferObject at
     * @param count  The number of components in a tuple. Must be in range of 0-4
     * @param type   The type of data in the VertexBufferObject. Valid types are GL_BYTE, GL_UNSIGNED_BYTE, GL_SHORT,
     *               GL_UNSIGNED_SHORT, GL_FIXED, or GL_FLOAT
     * @param buffer The VertexBufferObject to point towards the location
     */
    public void pointAttribute(int index, int count, int type, BufferObject buffer)
    {
        pointAttribute(index, count, type, false, buffer);
    }

    /**
     * Points a VertexBufferObject towards a specified location
     *
     * @param index      The location to point the VertexBufferObject at
     * @param count      The number of components in a tuple. Must be in range of 0-4
     * @param type       The type of data in the VertexBufferObject. Valid types are GL_BYTE, GL_UNSIGNED_BYTE,
     *                   GL_SHORT, GL_UNSIGNED_SHORT, GL_FIXED, or GL_FLOAT
     * @param normalized Specifies whether the values in the buffer data should be normalized
     * @param buffer     The VertexBufferObject to point towards the location
     */
    public void pointAttribute(int index, int count, int type, boolean normalized, BufferObject buffer)
    {
        pointAttribute(index, count, type, normalized, 0, 0, buffer);
    }

    /**
     * Points a VertexBufferObject towards a specified location
     *
     * @param index      The location to point the VertexBufferObject at
     * @param count      The number of components in a tuple. Must be in range of 0-4
     * @param type       The type of data in the VertexBufferObject. Valid types are GL_BYTE, GL_UNSIGNED_BYTE,
     *                   GL_SHORT, GL_UNSIGNED_SHORT, GL_FIXED, or GL_FLOAT
     * @param normalized Specifies whether the values in the buffer data should be normalized
     * @param stride     Specifies the byte offset between consecutive generic vertex attributes. If stride is 0, the
     *                   generic vertex attributes are understood to be tightly packed in the array.
     * @param offset     Specifies the index of the first component in the array.
     * @param buffer     The VertexBufferObject to point towards the location
     */
    public void pointAttribute(int index, int count, int type, boolean normalized, int stride, long offset, BufferObject buffer)
    {
        bind();
        buffer.bind();
        glVertexAttribPointer(index, count, type, normalized, stride, offset);
        GLError.check();
    }

    /**
     * Disposes this VertexArrayObject. This method should be called once you no longer need to use this
     * VertexArrayObject. Disposed vertex array objects throw GLException if used again.
     */
    public void dispose()
    {
        glBindVertexArray(0);
        GLError.check();
        glDeleteVertexArrays(id);
        GLError.check();
        disposed = true;
    }

    /**
     * @return The OpenGL ID of this VertexArrayObject
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return True if this VertexArrayObject is Disposed, Else returns False.
     */
    public boolean isDisposed()
    {
        return disposed;
    }
}
