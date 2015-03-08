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

import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;

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
    private static Map<Integer, BufferObject> current = new HashMap<>();
    private int     id;
    private int     capacity;
    private Target  target;
    private boolean disposed;

    /**
     * Creates a VertexBufferObject that binds to a target. Valid targets are GL_ARRAY_BUFFER​, GL_COPY_READ_BUFFER​,
     * GL_COPY_WRITE_BUFFER​, GL_ELEMENT_ARRAY_BUFFER​, GL_PIXEL_PACK_BUFFER​, GL_PIXEL_UNPACK_BUFFER​,
     * GL_TEXTURE_BUFFER​, GL_TRANSFORM_FEEDBACK_BUFFER​, or GL_UNIFORM_BUFFER​.
     *
     * @param target The target to bind this VertexBufferObject
     */
    public BufferObject(Target target)
    {
        id = glGenBuffers();
        this.target = target;

        GLError.check();
    }

    /**
     * Uploads the data in the specified NIO Buffer to this VertexBufferObject by calling the
     * <code>glBufferData()</code> function.
     *
     * @param data  The NIO buffer with data to be copied into the data store
     * @param usage How the is intended to be used. Valid usage values are GL_STREAM_DRAW​, GL_STREAM_READ​,
     *              GL_STREAM_COPY​, GL_STATIC_DRAW​, GL_STATIC_READ​, GL_STATIC_COPY​, GL_DYNAMIC_DRAW​,
     *              GL_DYNAMIC_READ​, or GL_DYNAMIC_COPY​.
     */
    public void uploadData(Buffer data, Usage usage)
    {
        bind();
        capacity = data.capacity();

        if (data instanceof ByteBuffer)
            glBufferData(target.getValue(), (ByteBuffer) data, usage.getValue());

        else if (data instanceof IntBuffer)
            glBufferData(target.getValue(), (IntBuffer) data, usage.getValue());

        else if (data instanceof FloatBuffer)
            glBufferData(target.getValue(), (FloatBuffer) data, usage.getValue());

        else if (data instanceof DoubleBuffer)
            glBufferData(target.getValue(), (DoubleBuffer) data, usage.getValue());

        else if (data instanceof ShortBuffer)
            glBufferData(target.getValue(), (ShortBuffer) data, usage.getValue());

        GLError.check();
    }

    /**
     * Binds this VertexBufferObject to the OpenGL binding point specified by the target you specified in the
     * constructor.
     */
    public void bind()
    {
        if (disposed)
            throw new GLException("VertexBufferObject is already disposed!");

        // Prevent un-necessary bindings, they are costly
        if (current.containsKey(target.getValue()) && current.get(target.getValue()) == this)
            return;

        glBindBuffer(target.getValue(), id);
        current.put(target.getValue(), this);

        GLError.check();
    }

    /**
     * Uploads NULL data to this VertexBufferObject by calling the <code>glBufferData()</code> function.
     *
     * @param capacity The capacity of the data store to be created
     * @param usage    How the is intended to be used. Valid usage values are GL_STREAM_DRAW​, GL_STREAM_READ​,
     *                 GL_STREAM_COPY​, GL_STATIC_DRAW​, GL_STATIC_READ​, GL_STATIC_COPY​, GL_DYNAMIC_DRAW​,
     *                 GL_DYNAMIC_READ​, or GL_DYNAMIC_COPY​.
     */
    public void uploadData(int capacity, Usage usage)
    {
        bind();
        this.capacity = capacity;
        glBufferData(target.getValue(), capacity, usage.getValue());

        GLError.check();
    }

    /**
     * Updates a subset of data starting from the offset in the VertexBufferObject's data store with the data from a NIO
     * Buffer
     *
     * @param data   The NIO buffer with data to be copied into the data store
     * @param offset The starting index from which the data should be updated.
     */
    public void uploadSubData(Buffer data, int offset)
    {
        if (capacity < data.capacity() - offset)
            throw new GLException("Not enough capacity");

        bind();

        if (data instanceof ByteBuffer)
            glBufferSubData(target.getValue(), offset, (ByteBuffer) data);

        else if (data instanceof IntBuffer)
            glBufferSubData(target.getValue(), offset, (IntBuffer) data);

        else if (data instanceof FloatBuffer)
            glBufferSubData(target.getValue(), offset, (FloatBuffer) data);

        else if (data instanceof DoubleBuffer)
            glBufferSubData(target.getValue(), offset, (DoubleBuffer) data);

        else if (data instanceof ShortBuffer)
            glBufferSubData(target.getValue(), offset, (ShortBuffer) data);

        GLError.check();
    }

    /**
     * Returns some or all of the data from the data store of this VertexBufferObject.
     *
     * @param offset Specifies the offset into the buffer object's data store from which data will be returned, measured
     *               in bytes.
     * @param length Specifies the size in bytes of the data store region being returned.
     *
     * @return The data as a NIO ByteBuffer.
     */
    public ByteBuffer getSubData(int offset, int length)
    {
        return getSubData(offset, length, BufferUtils.createByteBuffer(length));
    }

    /**
     * Returns some or all of the data from the data store of this VertexBufferObject.
     *
     * @param offset Specifies the offset into the buffer object's data store from which data will be returned, measured
     *               in bytes.
     * @param length Specifies the size in bytes of the data store region being returned.
     * @param data   A NIO ByteBuffer used to store the retrieved data.
     *
     * @return The data as a NIO ByteBuffer.
     */
    public ByteBuffer getSubData(int offset, int length, ByteBuffer data)
    {
        bind();
        glGetBufferSubData(target.getValue(), offset, length, data);

        GLError.check();

        return data;
    }

    /**
     * @return All the data present in the data store of this VertexBufferObject.
     */
    public ByteBuffer getData()
    {
        return getData(BufferUtils.createByteBuffer(capacity));
    }

    /**
     * Returns all the data present in the data store of this VertexBufferObject.
     *
     * @param data A NIO ByteBuffer used to store the retrieved data.
     *
     * @return The data as a NIO ByteBuffer
     */
    public ByteBuffer getData(ByteBuffer data)
    {
        return getSubData(0, capacity, data);
    }

    /**
     * Maps the buffer object's data store.
     *
     * @param access The access policy. Valid accesses are READ_ONLY, WRITE_ONLY or READ_WRITE.
     *
     * @return A pointer to the buffer object's data store as a NIO ByteBuffer.
     */
    public ByteBuffer map(MapAccess access)
    {
        return map(access, BufferUtils.createByteBuffer(capacity));
    }

    /**
     * Maps the buffer object's data store.
     *
     * @param access  The access policy. Valid accesses are READ_ONLY, WRITE_ONLY or READ_WRITE.
     * @param pointer A NIO ByteBuffer used for the data store.
     *
     * @return A pointer to the buffer object's data store as a NIO ByteBuffer.
     */
    public ByteBuffer map(MapAccess access, ByteBuffer pointer)
    {
        bind();
        pointer = glMapBuffer(target.getValue(), access.getValue(), pointer.capacity(), pointer);

        GLError.check();

        return pointer;
    }

    /**
     * Maps a section of a buffer object's data store.
     *
     * @param offset The starting offset within the buffer of the range to be mapped.
     * @param length The length of the range to be mapped.
     * @param access Combination of access flags indicating the desired access to the range. One or more of:
     *               MAP_READ_BIT, MAP_WRITE_BIT, MAP_INVALIDATE_RANGE_BIT, MAP_INVALIDATE_BUFFER_BIT,
     *               MAP_FLUSH_EXPLICIT_BIT, MAP_UNSYNCHRONIZED_BIT.
     *
     * @return A pointer to the buffer object's data store as a NIO ByteBuffer.
     */
    public ByteBuffer mapRange(long offset, int length, MapAccess access)
    {
        return mapRange(offset, access, BufferUtils.createByteBuffer(length));
    }

    /**
     * Maps a section of a buffer object's data store.
     *
     * @param offset  The starting offset within the buffer of the range to be mapped.
     * @param access  Combination of access flags indicating the desired access to the range. One or more of:
     *                MAP_READ_BIT, MAP_WRITE_BIT, MAP_INVALIDATE_RANGE_BIT, MAP_INVALIDATE_BUFFER_BIT,
     *                MAP_FLUSH_EXPLICIT_BIT, MAP_UNSYNCHRONIZED_BIT.
     * @param pointer A NIO ByteBuffer used for the data store.
     *
     * @return A pointer to the buffer object's data store as a NIO ByteBuffer.
     */
    public ByteBuffer mapRange(long offset, MapAccess access, ByteBuffer pointer)
    {
        bind();
        pointer = glMapBufferRange(target.getValue(), offset, pointer.capacity(), access.getValue());

        GLError.check();

        return pointer;
    }

    /**
     * Unmaps the buffer object and invalidates the pointer to its data store.
     *
     * @return Returns true if the unmapping was successful.
     */
    public boolean unMap()
    {
        bind();
        boolean unmapped = glUnmapBuffer(target.getValue());

        GLError.check();

        return unmapped;
    }

    /**
     * Disposes this VertexBufferObject. This clears the memory used to store the data of this VertexBufferObject from
     * the GPU. This method should be called once you no longer need this VertexBufferObject. A disposed
     * VertexBufferObject can no longer be used.
     */
    public void dispose()
    {
        glBindBuffer(target.getValue(), 0);
        GLError.check();
        glDeleteBuffers(id);
        GLError.check();
        disposed = true;
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

    public static enum Target
    {
        ARRAY_BUFFER(GL_ARRAY_BUFFER),
        COPY_READ_BUFFER(GL_COPY_READ_BUFFER),
        COPY_WRITE_BUFFER(GL_COPY_WRITE_BUFFER),
        ELEMENT_ARRAY_BUFFER(GL_ELEMENT_ARRAY_BUFFER),
        PIXEL_PACK_BUFFER(GL_PIXEL_PACK_BUFFER),
        PIXEL_UNPACK_BUFFER(GL_PIXEL_UNPACK_BUFFER),
        TEXTURE_BUFFER(GL_TEXTURE_BUFFER),
        TRANSFORM_FEEDBACK_BUFFER(GL_TRANSFORM_FEEDBACK_BUFFER),
        UNIFORM_BUFFER(GL_UNIFORM_BUFFER);

        private int value;

        private Target(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    public static enum Usage
    {
        STREAM_DRAW(GL_STREAM_DRAW),
        STREAM_READ(GL_STREAM_READ),
        STREAM_COPY(GL_STREAM_COPY),
        STATIC_DRAW(GL_STATIC_DRAW),
        STATIC_READ(GL_STATIC_COPY),
        STATIC_COPY(GL_STATIC_COPY),
        DYNAMIC_DRAW(GL_DYNAMIC_DRAW),
        DYNAMIC_READ(GL_DYNAMIC_READ),
        DYNAMIC_COPY(GL_DYNAMIC_COPY);

        private int value;

        private Usage(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    public static enum MapAccess
    {
        READ_ONLY(GL_READ_ONLY),
        WRITE_ONLY(GL_WRITE_ONLY),
        READ_WRITE(GL_READ_WRITE);

        private int value;

        private MapAccess(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }
}
