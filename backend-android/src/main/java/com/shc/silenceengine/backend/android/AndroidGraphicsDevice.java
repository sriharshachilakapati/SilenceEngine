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

package com.shc.silenceengine.backend.android;

import android.opengl.GLES30;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.DirectFloatBuffer;

import java.nio.ByteBuffer;

/**
 * @author Sri Harsha Chilakapti
 */
public class AndroidGraphicsDevice implements IGraphicsDevice
{
    @Override
    public int glGenBuffers()
    {
        int[] buffer = new int[1];
        GLES30.glGenBuffers(1, buffer, 0);

        return buffer[0];
    }

    @Override
    public boolean glIsBuffer(int buffer)
    {
        return GLES30.glIsBuffer(buffer);
    }

    @Override
    public void glBufferData(int target, DirectBuffer data, int usage)
    {
        GLES30.glBufferData(target, data.sizeBytes(), (ByteBuffer) data.nativeBuffer(), usage);
    }

    @Override
    public void glBufferSubData(int target, int offset, int size, DirectBuffer data)
    {
        GLES30.glBufferSubData(target, offset, size, (ByteBuffer) data.nativeBuffer());
    }

    @Override
    public void glBindBuffer(int target, int buffer)
    {
        GLES30.glBindBuffer(target, buffer);
    }

    @Override
    public void glBufferData(int target, int capacity, int usage)
    {
        GLES30.glBufferData(target, capacity, null, usage);
    }

    @Override
    public void glBufferSubData(int target, int offset, DirectBuffer data)
    {
        GLES30.glBufferSubData(target, offset, data.sizeBytes(), (ByteBuffer) data.nativeBuffer());
    }

    @Override
    public void glDeleteBuffers(int... buffer)
    {
        GLES30.glDeleteBuffers(buffer.length, buffer, 0);
    }

    @Override
    public int glGenFramebuffers()
    {
        int[] fBuffer = new int[1];
        GLES30.glGenFramebuffers(1, fBuffer, 0);

        return fBuffer[0];
    }

    @Override
    public boolean glIsFramebuffer(int framebuffer)
    {
        return GLES30.glIsFramebuffer(framebuffer);
    }

    @Override
    public void glFramebufferTexture2D(int target, int attachment, int textureTarget, int texture, int level)
    {
        GLES30.glFramebufferTexture2D(target, attachment, textureTarget, texture, level);
    }

    @Override
    public void glBindFramebuffer(int target, int framebuffer)
    {
        GLES30.glBindFramebuffer(target, framebuffer);
    }

    @Override
    public void glViewport(int x, int y, int width, int height)
    {
        GLES30.glViewport(x, y, width, height);
    }

    @Override
    public void glClear(int flags)
    {
        IGraphicsDevice.super.glClear(flags);
        GLES30.glClear(flags);
    }

    @Override
    public int glCheckFramebufferStatus(int target)
    {
        return GLES30.glCheckFramebufferStatus(target);
    }

    @Override
    public void glDeleteFramebuffers(int... framebuffer)
    {
        GLES30.glDeleteFramebuffers(framebuffer.length, framebuffer, 0);
    }

    @Override
    public void glDrawArrays(int primitive, int offset, int vertexCount)
    {
        IGraphicsDevice.super.glDrawArrays(primitive, offset, vertexCount);
        GLES30.glDrawArrays(primitive, offset, vertexCount);
    }

    @Override
    public void glDrawElements(int primitive, int vertexCount, int type, int offset)
    {
        IGraphicsDevice.super.glDrawElements(primitive, vertexCount, type, offset);
        GLES30.glDrawElements(primitive, vertexCount, type, offset);
    }

    @Override
    public void glEnable(int capability)
    {
        GLES30.glEnable(capability);
    }

    @Override
    public void glBlendFunc(int src, int dst)
    {
        GLES30.glBlendFunc(src, dst);
    }

    @Override
    public void glDisable(int capability)
    {
        GLES30.glDisable(capability);
    }

    @Override
    public void glClearColor(float r, float g, float b, float a)
    {
        GLES30.glClearColor(r, g, b, a);
    }

    @Override
    public void glBindVertexArray(int vaoID)
    {
        GLES30.glBindVertexArray(vaoID);
    }

    @Override
    public void glDepthMask(boolean value)
    {
        GLES30.glDepthMask(value);
    }

    @Override
    public void glDepthFunc(int func)
    {
        GLES30.glDepthFunc(func);
    }

    @Override
    public void glCullFace(int mode)
    {
        GLES30.glCullFace(mode);
    }

    @Override
    public int glGetError()
    {
        return GLES30.glGetError();
    }

    @Override
    public int glCreateProgram()
    {
        return GLES30.glCreateProgram();
    }

    @Override
    public void glAttachShader(int program, int shader)
    {
        GLES30.glAttachShader(program, shader);
    }

    @Override
    public void glLinkProgram(int program)
    {
        GLES30.glLinkProgram(program);
    }

    @Override
    public int glGetProgrami(int program, int param)
    {
        int[] value = new int[1];
        GLES30.glGetProgramiv(program, param, value, 0);

        return value[0];
    }

    @Override
    public String glGetProgramInfoLog(int program)
    {
        return GLES30.glGetProgramInfoLog(program);
    }

    @Override
    public int glGetAttribLocation(int program, String name)
    {
        return GLES30.glGetAttribLocation(program, name);
    }

    @Override
    public void glUseProgram(int program)
    {
        GLES30.glUseProgram(program);
    }

    @Override
    public int glGetUniformLocation(int program, String name)
    {
        return GLES30.glGetUniformLocation(program, name);
    }

    @Override
    public void glUniform1i(int location, int value)
    {
        GLES30.glUniform1i(location, value);
    }

    @Override
    public void glUniform2i(int location, int v1, int v2)
    {
        GLES30.glUniform2i(location, v1, v2);
    }

    @Override
    public void glUniform3i(int location, int v1, int v2, int v3)
    {
        GLES30.glUniform3i(location, v1, v2, v3);
    }

    @Override
    public void glUniform4i(int location, int v1, int v2, int v3, int v4)
    {
        GLES30.glUniform4i(location, v1, v2, v3, v4);
    }

    @Override
    public void glUniform1f(int location, float value)
    {
        GLES30.glUniform1f(location, value);
    }

    @Override
    public void glUniform2f(int location, float v1, float v2)
    {
        GLES30.glUniform2f(location, v1, v2);
    }

    @Override
    public void glUniform3f(int location, float v1, float v2, float v3)
    {
        GLES30.glUniform3f(location, v1, v2, v3);
    }

    @Override
    public void glUniform4f(int location, float v1, float v2, float v3, float v4)
    {
        GLES30.glUniform4f(location, v1, v2, v3, v4);
    }

    @Override
    public void glUniformMatrix3fv(int location, boolean transpose, DirectFloatBuffer matrix)
    {
        GLES30.glUniformMatrix3fv(location, 1, transpose, ((ByteBuffer) matrix.getDirectBuffer().nativeBuffer()).asFloatBuffer());
    }

    @Override
    public void glUniformMatrix4fv(int location, boolean transpose, DirectFloatBuffer matrix)
    {
        GLES30.glUniformMatrix4fv(location, 1, transpose, ((ByteBuffer) matrix.getDirectBuffer().nativeBuffer()).asFloatBuffer());
    }

    @Override
    public void glDeleteProgram(int... id)
    {
        for (int program : id)
            GLES30.glDeleteProgram(program);
    }

    @Override
    public int glCreateShader(int type)
    {
        return GLES30.glCreateShader(type);
    }

    @Override
    public void glShaderSource(int shader, String... source)
    {
        String sources = "";

        for (String src : source)
            sources += src;

        GLES30.glShaderSource(shader, sources);
    }

    @Override
    public void glCompileShader(int shader)
    {
        GLES30.glCompileShader(shader);
    }

    @Override
    public int glGetShaderi(int shader, int param)
    {
        int[] value = new int[1];
        GLES30.glGetShaderiv(shader, param, value, 0);

        return value[0];
    }

    @Override
    public String glGetShaderInfoLog(int shader)
    {
        return GLES30.glGetShaderInfoLog(shader);
    }

    @Override
    public void glDeleteShader(int... shaders)
    {
        for (int shader : shaders)
            GLES30.glDeleteShader(shader);
    }

    @Override
    public int glGenTextures()
    {
        int[] texture = new int[1];
        GLES30.glGenTextures(1, texture, 0);

        return texture[0];
    }

    @Override
    public void glActiveTexture(int unit)
    {
        GLES30.glActiveTexture(unit);
    }

    @Override
    public void glBindTexture(int target, int texture)
    {
        GLES30.glBindTexture(target, texture);
    }

    @Override
    public void glTexParameteri(int target, int param, int value)
    {
        GLES30.glTexParameteri(target, param, value);
    }

    @Override
    public void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, DirectBuffer pixels)
    {
        GLES30.glTexImage2D(target, level, internalFormat, width, height, border, format, type, (ByteBuffer) pixels.nativeBuffer());
    }

    @Override
    public void glGenerateMipmap(int target)
    {
        GLES30.glGenerateMipmap(target);
    }

    @Override
    public void glDeleteTextures(int... texture)
    {
        GLES30.glDeleteTextures(texture.length, texture, 0);
    }

    @Override
    public int glGenVertexArrays()
    {
        int[] va = new int[1];
        GLES30.glGenVertexArrays(1, va, 0);

        return va[0];
    }

    @Override
    public boolean glIsVertexArray(int vertexArray)
    {
        return GLES30.glIsVertexArray(vertexArray);
    }

    @Override
    public void glEnableVertexAttribArray(int index)
    {
        GLES30.glEnableVertexAttribArray(index);
    }

    @Override
    public void glDisableVertexAttribArray(int index)
    {
        GLES30.glDisableVertexAttribArray(index);
    }

    @Override
    public void glVertexAttribPointer(int index, int count, int type, boolean normalized, int stride, long offset)
    {
        GLES30.glVertexAttribPointer(index, count, type, normalized, stride, (int) offset);
    }

    @Override
    public void glDeleteVertexArrays(int... vertexArray)
    {
        GLES30.glDeleteVertexArrays(vertexArray.length, vertexArray, 0);
    }
}
