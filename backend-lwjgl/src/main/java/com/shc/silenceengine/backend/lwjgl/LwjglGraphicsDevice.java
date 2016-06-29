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

package com.shc.silenceengine.backend.lwjgl;

import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.DirectFloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

/**
 * An implementation of {@link IGraphicsDevice}
 *
 * @author Sri Harsha Chilakapati
 */
public class LwjglGraphicsDevice implements IGraphicsDevice
{
    @Override
    public int glGenBuffers()
    {
        return GL15.glGenBuffers();
    }

    @Override
    public boolean glIsBuffer(int buffer)
    {
        return GL15.glIsBuffer(buffer);
    }

    @Override
    public void glBufferData(int value, DirectBuffer data, int usage)
    {
        GL15.glBufferData(value, (ByteBuffer) data.nativeBuffer(), usage);
    }

    @Override
    public void glBufferSubData(int target, int offset, int size, DirectBuffer data)
    {
        GL15.nglBufferSubData(target, offset, size, MemoryUtil.memAddress((ByteBuffer) data.nativeBuffer()));
    }

    @Override
    public void glBindBuffer(int target, int buffer)
    {
        GL15.glBindBuffer(target, buffer);
    }

    @Override
    public void glBufferData(int target, int capacity, int usage)
    {
        GL15.glBufferData(target, capacity, usage);
    }

    @Override
    public void glBufferSubData(int target, int offset, DirectBuffer data)
    {
        GL15.glBufferSubData(target, offset, (ByteBuffer) data.nativeBuffer());
    }

    @Override
    public void glDeleteBuffers(int... buffer)
    {
        for (int bufferID : buffer)
            GL15.glDeleteBuffers(bufferID);
    }

    @Override
    public int glGenFramebuffers()
    {
        return GL30.glGenFramebuffers();
    }

    @Override
    public boolean glIsFramebuffer(int framebuffer)
    {
        return GL30.glIsFramebuffer(framebuffer);
    }

    @Override
    public void glFramebufferTexture2D(int target, int attachment, int textureTarget, int texture, int level)
    {
        GL30.glFramebufferTexture2D(target, attachment, textureTarget, texture, level);
    }

    @Override
    public void glBindFramebuffer(int target, int framebuffer)
    {
        GL30.glBindFramebuffer(target, framebuffer);
    }

    @Override
    public void glViewport(int x, int y, int width, int height)
    {
        GL11.glViewport(x, y, width, height);
    }

    @Override
    public void glClear(int flags)
    {
        IGraphicsDevice.super.glClear(flags);
        GL11.glClear(flags);
    }

    @Override
    public int glCheckFramebufferStatus(int target)
    {
        return GL30.glCheckFramebufferStatus(target);
    }

    @Override
    public void glDeleteFramebuffers(int... framebuffer)
    {
        for (int framebufferID : framebuffer)
            GL30.glDeleteFramebuffers(framebufferID);
    }

    @Override
    public void glDrawArrays(int primitive, int offset, int vertexCount)
    {
        IGraphicsDevice.super.glDrawArrays(primitive, offset, vertexCount);
        GL11.glDrawArrays(primitive, offset, vertexCount);
    }

    @Override
    public void glDrawElements(int primitive, int vertexCount, int type, int offset)
    {
        IGraphicsDevice.super.glDrawElements(primitive, vertexCount, type, offset);
        GL11.glDrawElements(primitive, vertexCount, type, offset);
    }

    @Override
    public void glEnable(int capability)
    {
        GL11.glEnable(capability);
    }

    @Override
    public void glBlendFunc(int src, int dst)
    {
        GL11.glBlendFunc(src, dst);
    }

    @Override
    public void glDisable(int capability)
    {
        GL11.glDisable(capability);
    }

    @Override
    public void glClearColor(float r, float g, float b, float a)
    {
        GL11.glClearColor(r, g, b, a);
    }

    @Override
    public void glBindVertexArray(int vaoID)
    {
        GL30.glBindVertexArray(vaoID);
    }

    @Override
    public void glDepthMask(boolean value)
    {
        GL11.glDepthMask(value);
    }

    @Override
    public void glDepthFunc(int func)
    {
        GL11.glDepthFunc(func);
    }

    @Override
    public void glCullFace(int mode)
    {
        GL11.glCullFace(mode);
    }

    @Override
    public int glGetError()
    {
        return GL11.glGetError();
    }

    @Override
    public int glCreateProgram()
    {
        return GL20.glCreateProgram();
    }

    @Override
    public void glAttachShader(int program, int shader)
    {
        GL20.glAttachShader(program, shader);
    }

    @Override
    public void glLinkProgram(int program)
    {
        GL20.glLinkProgram(program);
    }

    @Override
    public int glGetProgrami(int program, int param)
    {
        return GL20.glGetProgrami(program, param);
    }

    @Override
    public String glGetProgramInfoLog(int program)
    {
        return GL20.glGetProgramInfoLog(program);
    }

    @Override
    public int glGetAttribLocation(int program, String name)
    {
        return GL20.glGetAttribLocation(program, name);
    }

    @Override
    public void glUseProgram(int program)
    {
        GL20.glUseProgram(program);
    }

    @Override
    public int glGetUniformLocation(int program, String name)
    {
        return GL20.glGetUniformLocation(program, name);
    }

    @Override
    public void glUniform1i(int location, int value)
    {
        GL20.glUniform1i(location, value);
    }

    @Override
    public void glUniform2i(int location, int v1, int v2)
    {
        GL20.glUniform2i(location, v1, v2);
    }

    @Override
    public void glUniform3i(int location, int v1, int v2, int v3)
    {
        GL20.glUniform3i(location, v1, v2, v3);
    }

    @Override
    public void glUniform4i(int location, int v1, int v2, int v3, int v4)
    {
        GL20.glUniform4i(location, v1, v2, v3, v4);
    }

    @Override
    public void glUniform1f(int location, float value)
    {
        GL20.glUniform1f(location, value);
    }

    @Override
    public void glUniform2f(int location, float v1, float v2)
    {
        GL20.glUniform2f(location, v1, v2);
    }

    @Override
    public void glUniform3f(int location, float v1, float v2, float v3)
    {
        GL20.glUniform3f(location, v1, v2, v3);
    }

    @Override
    public void glUniform4f(int location, float v1, float v2, float v3, float v4)
    {
        GL20.glUniform4f(location, v1, v2, v3, v4);
    }

    @Override
    public void glUniformMatrix3fv(int location, boolean transpose, DirectFloatBuffer matrix)
    {
        GL20.glUniformMatrix3fv(location, transpose, ((ByteBuffer) matrix.getDirectBuffer().nativeBuffer()).asFloatBuffer());
    }

    @Override
    public void glUniformMatrix4fv(int location, boolean transpose, DirectFloatBuffer matrix)
    {
        GL20.glUniformMatrix4fv(location, transpose, ((ByteBuffer) matrix.getDirectBuffer().nativeBuffer()).asFloatBuffer());
    }

    @Override
    public void glDeleteProgram(int... id)
    {
        for (int program : id)
            GL20.glDeleteProgram(program);
    }

    @Override
    public int glCreateShader(int type)
    {
        return GL20.glCreateShader(type);
    }

    @Override
    public void glShaderSource(int shader, String... source)
    {
        GL20.glShaderSource(shader, (CharSequence[]) source);
    }

    @Override
    public void glCompileShader(int shader)
    {
        GL20.glCompileShader(shader);
    }

    @Override
    public int glGetShaderi(int shader, int param)
    {
        return GL20.glGetShaderi(shader, param);
    }

    @Override
    public String glGetShaderInfoLog(int shader)
    {
        return GL20.glGetShaderInfoLog(shader);
    }

    @Override
    public void glDeleteShader(int... shader)
    {
        for (int id : shader)
            GL20.glDeleteShader(id);
    }

    @Override
    public int glGenTextures()
    {
        return GL11.glGenTextures();
    }

    @Override
    public void glActiveTexture(int unit)
    {
        GL13.glActiveTexture(unit);
    }

    @Override
    public void glBindTexture(int target, int texture)
    {
        GL11.glBindTexture(target, texture);
    }

    @Override
    public void glTexParameteri(int target, int param, int value)
    {
        GL11.glTexParameteri(target, param, value);
    }

    @Override
    public void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, DirectBuffer pixels)
    {
        GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, type, (ByteBuffer) pixels.nativeBuffer());
    }

    @Override
    public void glGenerateMipmap(int target)
    {
        GL30.glGenerateMipmap(target);
    }

    @Override
    public void glDeleteTextures(int... texture)
    {
        for (int id : texture)
            GL11.glDeleteTextures(id);
    }

    @Override
    public int glGenVertexArrays()
    {
        return GL30.glGenVertexArrays();
    }

    @Override
    public boolean glIsVertexArray(int vertexArray)
    {
        return GL30.glIsVertexArray(vertexArray);
    }

    @Override
    public void glEnableVertexAttribArray(int index)
    {
        GL20.glEnableVertexAttribArray(index);
    }

    @Override
    public void glDisableVertexAttribArray(int index)
    {
        GL20.glEnableVertexAttribArray(index);
    }

    @Override
    public void glVertexAttribPointer(int index, int count, int type, boolean normalized, int stride, long offset)
    {
        GL20.glVertexAttribPointer(index, count, type, normalized, stride, offset);
    }

    @Override
    public void glDeleteVertexArrays(int... vertexArray)
    {
        for (int id : vertexArray)
            GL30.glDeleteVertexArrays(id);
    }
}
