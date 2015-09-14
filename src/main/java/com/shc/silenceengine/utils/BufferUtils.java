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

package com.shc.silenceengine.utils;

import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.system.jemalloc.JEmalloc.*;

/**
 * @author Sri Harsha Chilakapati
 */
public final class BufferUtils
{
    public static final long NULL = 0L;

    private BufferUtils()
    {
    }

    public static ByteBuffer createByteBuffer(long size)
    {
        return je_malloc(size);
    }

    public static IntBuffer createIntBuffer(long size)
    {
        return createByteBuffer(size * Integer.BYTES).asIntBuffer();
    }

    public static CharBuffer createCharBuffer(long size)
    {
        return createByteBuffer(size * Character.BYTES).asCharBuffer();
    }

    public static ShortBuffer createShortBuffer(long size)
    {
        return createByteBuffer(size * Short.BYTES).asShortBuffer();
    }

    public static FloatBuffer createFloatBuffer(long size)
    {
        return createByteBuffer(size * Float.BYTES).asFloatBuffer();
    }

    public static DoubleBuffer createDoubleBuffer(long size)
    {
        return createByteBuffer(size * Double.BYTES).asDoubleBuffer();
    }

    public static LongBuffer createLongBuffer(long size)
    {
        return createByteBuffer(size * Long.BYTES).asLongBuffer();
    }

    public static long memAddress(Buffer buffer)
    {
        if (buffer instanceof ByteBuffer)
            return MemoryUtil.memAddress((ByteBuffer) buffer);

        if (buffer instanceof IntBuffer)
            return MemoryUtil.memAddress((IntBuffer) buffer);

        if (buffer instanceof CharBuffer)
            return MemoryUtil.memAddress((CharBuffer) buffer);

        if (buffer instanceof ShortBuffer)
            return MemoryUtil.memAddress((ShortBuffer) buffer);

        if (buffer instanceof FloatBuffer)
            return MemoryUtil.memAddress((FloatBuffer) buffer);

        if (buffer instanceof DoubleBuffer)
            return MemoryUtil.memAddress((DoubleBuffer) buffer);

        if (buffer instanceof LongBuffer)
            return MemoryUtil.memAddress((LongBuffer) buffer);

        return NULL;
    }

    public static void freeBuffer(Buffer buffer)
    {
        nje_free(memAddress(buffer));
    }

    public static void freeBuffer(long buffer)
    {
        nje_free(buffer);
    }

    public static ByteBuffer resizeBuffer(Buffer buffer, int newSize)
    {
        return MemoryUtil.memByteBuffer(nje_realloc(memAddress(buffer), newSize), newSize);
    }

    public static ByteBuffer resizeBuffer(long buffer, int newSize)
    {
        return MemoryUtil.memByteBuffer(nje_realloc(buffer, newSize), newSize);
    }
}
