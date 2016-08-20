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

package com.shc.silenceengine.graphics.opengl;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.DirectBuffer;

import java.util.HashMap;
import java.util.Map;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * This class encapsulates OpenGL Buffer Objects nicely and cleanly allowing you to use OpenGL in an Object Oriented
 * way. Buffer Objects in OpenGL allows to store data directly on the GPU. They are just regular OpenGL objects, and
 * hence you have to take care of their destruction on your own by calling the dispose() method. This class doesn't
 * store any data you upload to the GPU, it just exists to make the usage of OpenGL functions convenient.
 *
 * @author Sri Harsha Chilakapati
 * @author Heiko Brumme
 */
public class BufferObject
{
    static Map<Integer, BufferObject> current = new HashMap<>();

    private int id;
    private int capacity;

    private Target  target;
    private boolean disposed;

    /**
     * Creates a VertexBufferObject that binds to a target. Valid targets are {@code GL_ARRAY_BUFFER} and {@code
     * GL_ELEMENT_ARRAY_BUFFER}.
     *
     * @param target The target to bind this VertexBufferObject
     */
    public BufferObject(Target target)
    {
        id = SilenceEngine.graphics.glGenBuffers();
        this.target = target;

        GLError.check();
    }

    public static boolean isValid(int id)
    {
        boolean value = SilenceEngine.graphics.glIsBuffer(id);
        GLError.check();

        return value;
    }

    /**
     * Uploads the data in the specified NIO Buffer to this VertexBufferObject by calling the
     * <code>glBufferData()</code> function.
     *
     * @param data  The NIO buffer with data to be copied into the data store
     * @param usage How the is intended to be used. Valid usage values are GL_STREAM_DRAW, GL_STATIC_DRAW or
     *              GL_DYNAMIC_DRAW
     */
    public void uploadData(DirectBuffer data, Usage usage)
    {
        bind();
        capacity = data.sizeBytes();

        SilenceEngine.graphics.glBufferData(target.getValue(), data, usage.getValue());
        GLError.check();
    }

    /**
     * Binds this VertexBufferObject to the OpenGL binding point specified by the target you specified in the
     * constructor.
     */
    public void bind()
    {
        if (disposed)
            throw new GLException("BufferObject is already disposed!");

        // Prevent un-necessary bindings, they are costly
        if (current.containsKey(target.getValue()) && current.get(target.getValue()) == this)
            return;

        SilenceEngine.graphics.glBindBuffer(target.getValue(), id);
        current.put(target.getValue(), this);

        GLError.check();
    }

    /**
     * Uploads NULL data to this VertexBufferObject by calling the <code>glBufferData()</code> function.
     *
     * @param capacity The capacity of the data store to be created
     * @param usage    How the is intended to be used. Valid usage values are GL_STREAM_DRAW, GL_STATIC_DRAW or
     *                 GL_DYNAMIC_DRAW.
     */
    public void uploadData(int capacity, Usage usage)
    {
        bind();
        this.capacity = capacity;
        SilenceEngine.graphics.glBufferData(target.getValue(), capacity, usage.getValue());

        GLError.check();
    }

    /**
     * Updates a subset of data starting from the offset in the BufferObject's data store with the data from a direct
     * buffer
     *
     * @param data   The NIO buffer with data to be copied into the data store
     */
    public void uploadSubData(DirectBuffer data)
    {
        uploadSubData(data, 0, data.sizeBytes());
    }

    /**
     * Updates a subset of data starting from the offset in the BufferObject's data store with the data from a direct
     * buffer
     *
     * @param data   The NIO buffer with data to be copied into the data store
     * @param offset The starting index from which the data should be updated.
     */
    public void uploadSubData(DirectBuffer data, int offset)
    {
        uploadSubData(data, offset, data.sizeBytes());
    }

    /**
     * Updates a subset of data starting from the offset in the BufferObject's data store with the data from a direct
     * buffer
     *
     * @param data   The NIO buffer with data to be copied into the data store
     * @param offset The starting index from which the data should be updated.
     * @param size   The byte size of the region being replaced.
     */
    public void uploadSubData(DirectBuffer data, int offset, int size)
    {
        if (capacity < data.sizeBytes() - offset)
            throw new GLException("Not enough capacity");

        bind();

        SilenceEngine.graphics.glBufferSubData(target.getValue(), offset, size, data);
        GLError.check();
    }

    /**
     * Disposes this VertexBufferObject. This clears the memory used to store the data of this VertexBufferObject from
     * the GPU. This method should be called once you no longer need this VertexBufferObject. A disposed
     * VertexBufferObject can no longer be used.
     */
    public void dispose()
    {
        SilenceEngine.graphics.glBindBuffer(target.getValue(), 0);
        GLError.check();
        SilenceEngine.graphics.glDeleteBuffers(id);
        GLError.check();
        disposed = true;
    }

    public boolean isValid()
    {
        return isValid(id);
    }

    /**
     * @return The ID of this VertexBufferObject. Useful if you directly want to use any OpenGL function yourself.
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return The capacity of the data store currently allocated to store the data of this VertexBufferObject on the
     * GPU.
     */
    public int getCapacity()
    {
        return capacity;
    }

    /**
     * @return The binding target of this VertexBufferObject
     */
    public Target getTarget()
    {
        return target;
    }

    /**
     * @return True if this VertexBufferObject is Disposed, Else returns False.
     */
    public boolean isDisposed()
    {
        return disposed;
    }

    public enum Target
    {
        ARRAY_BUFFER(GL_ARRAY_BUFFER),
        ELEMENT_ARRAY_BUFFER(GL_ELEMENT_ARRAY_BUFFER);

        int value;

        Target(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    public enum Usage
    {
        STREAM_DRAW(GL_STREAM_DRAW),
        STATIC_DRAW(GL_STATIC_DRAW),
        DYNAMIC_DRAW(GL_DYNAMIC_DRAW);

        int value;

        Usage(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }
}
