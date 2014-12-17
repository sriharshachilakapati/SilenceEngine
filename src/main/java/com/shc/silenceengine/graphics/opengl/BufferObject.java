package com.shc.silenceengine.graphics.opengl;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;

/**
 * This class encapsulates OpenGL Buffer Objects nicely and cleanly
 * allowing you to use OpenGL in an Object Oriented way.
 * <p>
 * Buffer Objects in OpenGL allows to store data directly on the GPU.
 * They are just regular OpenGL objects, and hence you have to take
 * care of their destruction on your own by calling the dispose()
 * method.
 * <p>
 * This class doesn't store any data you upload to the GPU, it just
 * exists to make the usage of OpenGL functions convenient.
 *
 * @author Sri Harsha Chilakapati
 */
public class BufferObject
{
    private int id;
    private int capacity;
    private int target;

    private boolean disposed;

    private static Map<Integer, BufferObject> current = new HashMap<>();

    /**
     * Creates a VertexBufferObject that binds to a target. Valid targets
     * are GL_ARRAY_BUFFER​, GL_ATOMIC_COUNTER_BUFFER​, GL_COPY_READ_BUFFER​,
     * GL_COPY_WRITE_BUFFER​, GL_DRAW_INDIRECT_BUFFER​, GL_DISPATCH_INDIRECT_BUFFER​,
     * GL_ELEMENT_ARRAY_BUFFER​, GL_PIXEL_PACK_BUFFER​, GL_PIXEL_UNPACK_BUFFER​,
     * GL_QUERY_BUFFER​, GL_SHADER_STORAGE_BUFFER​, GL_TEXTURE_BUFFER​,
     * GL_TRANSFORM_FEEDBACK_BUFFER​, or GL_UNIFORM_BUFFER​.
     *
     * @param target The target to bind this VertexBufferObject
     */
    public BufferObject(int target)
    {
        id = glGenBuffers();
        this.target = target;

        GLError.check();
    }

    /**
     * Binds this VertexBufferObject to the OpenGL binding point specified
     * by the target you specified in the constructor.
     */
    public void bind()
    {
        if (disposed)
            throw new GLException("VertexBufferObject is already disposed!");

        // Prevent un-necessary bindings, they are costly
        if (current.containsKey(target) && current.get(target) == this)
            return;

        glBindBuffer(target, id);
        current.put(target, this);

        GLError.check();
    }

    /**
     * Uploads the data in the specified NIO Buffer to this VertexBufferObject
     * by calling the <code>glBufferData()</code> function.
     *
     * @param data  The NIO buffer with data to be copied into the data store
     * @param usage How the is intended to be used. Valid usage values are
     *              GL_STREAM_DRAW​, GL_STREAM_READ​, GL_STREAM_COPY​, GL_STATIC_DRAW​,
     *              GL_STATIC_READ​, GL_STATIC_COPY​, GL_DYNAMIC_DRAW​, GL_DYNAMIC_READ​,
     *              or GL_DYNAMIC_COPY​.
     */
    public void uploadData(Buffer data, int usage)
    {
        bind();
        capacity = data.capacity();

        if (data instanceof ByteBuffer)
            glBufferData(target, (ByteBuffer) data, usage);

        else if (data instanceof IntBuffer)
            glBufferData(target, (IntBuffer) data, usage);

        else if (data instanceof FloatBuffer)
            glBufferData(target, (FloatBuffer) data, usage);

        else if (data instanceof DoubleBuffer)
            glBufferData(target, (DoubleBuffer) data, usage);

        else if (data instanceof ShortBuffer)
            glBufferData(target, (ShortBuffer) data, usage);

        GLError.check();
    }

    /**
     * Uploads NULL data to this VertexBufferObject by calling the
     * <code>glBufferData()</code> function.
     *
     * @param capacity The capacity of the data store to be created
     * @param usage How the is intended to be used. Valid usage values are
     *              GL_STREAM_DRAW​, GL_STREAM_READ​, GL_STREAM_COPY​, GL_STATIC_DRAW​,
     *              GL_STATIC_READ​, GL_STATIC_COPY​, GL_DYNAMIC_DRAW​, GL_DYNAMIC_READ​,
     *              or GL_DYNAMIC_COPY​.
     */
    public void uploadData(int capacity, int usage)
    {
        bind();
        this.capacity = capacity;
        glBufferData(target, capacity, usage);

        GLError.check();
    }

    /**
     * Updates a subset of data starting from the offset in the
     * VertexBufferObject's data store with the data from a NIO Buffer
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
            glBufferSubData(target, offset, (ByteBuffer) data);

        else if (data instanceof IntBuffer)
            glBufferSubData(target, offset, (IntBuffer) data);

        else if (data instanceof FloatBuffer)
            glBufferSubData(target, offset, (FloatBuffer) data);

        else if (data instanceof DoubleBuffer)
            glBufferSubData(target, offset, (DoubleBuffer) data);

        else if (data instanceof ShortBuffer)
            glBufferSubData(target, offset, (ShortBuffer) data);

        GLError.check();
    }

    /**
     * Returns some or all of the data from the data store of this VertexBufferObject.
     *
     * @param offset Specifies the offset into the buffer object's data store
     *               from which data will be returned, measured in bytes.
     * @param length Specifies the size in bytes of the data store region
     *               being returned.
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
     * @param offset Specifies the offset into the buffer object's data store
     *               from which data will be returned, measured in bytes.
     * @param length Specifies the size in bytes of the data store region
     *               being returned.
     * @param data   A NIO ByteBuffer used to store the retrieved data.
     *
     * @return The data as a NIO ByteBuffer.
     */
    public ByteBuffer getSubData(int offset, int length, ByteBuffer data)
    {
        bind();
        glGetBufferSubData(target, offset, length, data);

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
     * Disposes this VertexBufferObject. This clears the memory used to store
     * the data of this VertexBufferObject from the GPU. This method should be
     * called once you no longer need this VertexBufferObject. A disposed
     * VertexBufferObject can no longer be used.
     */
    public void dispose()
    {
        glBindBuffer(target, 0);
        GLError.check();
        glDeleteBuffers(id);
        GLError.check();
        disposed = true;
    }

    /**
     * @return The ID of this VertexBufferObject. Useful if you directly
     *         want to use any OpenGL function yourself.
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return The capacity of the data store currently allocated
     *         to store the data of this VertexBufferObject on the GPU.
     */
    public int getCapacity()
    {
        return capacity;
    }

    /**
     * @return The binding target of this VertexBufferObject
     */
    public int getTarget()
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
}
