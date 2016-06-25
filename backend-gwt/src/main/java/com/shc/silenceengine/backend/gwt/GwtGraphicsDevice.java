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

package com.shc.silenceengine.backend.gwt;

import com.google.gwt.typedarrays.client.Float32ArrayNative;
import com.google.gwt.typedarrays.client.Uint8ArrayNative;
import com.google.gwt.typedarrays.shared.ArrayBufferView;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.DirectFloatBuffer;
import com.shc.webgl4j.client.WebGL10;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtGraphicsDevice implements IGraphicsDevice
{
    @Override
    public int glGenBuffers()
    {
        return WebGL10.glCreateBuffer();
    }

    @Override
    public boolean glIsBuffer(int buffer)
    {
        return WebGL10.glIsBuffer(buffer);
    }

    @Override
    public void glBufferData(int value, DirectBuffer data, int usage)
    {
        WebGL10.glBufferData(value, (ArrayBufferView) data.nativeBuffer(), usage);
    }

    @Override
    public void glBufferSubData(int target, int offset, int size, DirectBuffer data)
    {
        WebGL10.glBufferSubData(target, offset, size, (ArrayBufferView) data.nativeBuffer());
    }

    @Override
    public void glBindBuffer(int target, int buffer)
    {
        WebGL10.glBindBuffer(target, buffer);
    }

    @Override
    public void glBufferData(int target, int capacity, int usage)
    {
        WebGL10.glBufferData(target, capacity, usage);
    }

    @Override
    public void glBufferSubData(int target, int offset, DirectBuffer data)
    {
        WebGL10.glBufferSubData(target, offset, data.sizeBytes(), (ArrayBufferView) data.nativeBuffer());
    }

    @Override
    public void glDeleteBuffers(int... buffer)
    {
        for (int bufferID : buffer)
            WebGL10.glDeleteBuffer(bufferID);
    }

    @Override
    public int glGenFramebuffers()
    {
        return WebGL10.glCreateFramebuffer();
    }

    @Override
    public boolean glIsFramebuffer(int framebuffer)
    {
        return WebGL10.glIsFramebuffer(framebuffer);
    }

    @Override
    public void glFramebufferTexture2D(int target, int attachment, int textureTarget, int texture, int level)
    {
        WebGL10.glFramebufferTexture2D(target, attachment, textureTarget, texture, level);
    }

    @Override
    public void glBindFramebuffer(int target, int framebuffer)
    {
        WebGL10.glBindFramebuffer(target, framebuffer);
    }

    @Override
    public void glViewport(int x, int y, int width, int height)
    {
        WebGL10.glViewport(x, y, width, height);
    }

    @Override
    public void glClear(int flags)
    {
        WebGL10.glClear(flags);
    }

    @Override
    public int glCheckFramebufferStatus(int target)
    {
        return WebGL10.glCheckFramebufferStatus(target);
    }

    @Override
    public void glDeleteFramebuffers(int... framebuffer)
    {
        for (int framebufferID : framebuffer)
            WebGL10.glDeleteFramebuffer(framebufferID);
    }

    @Override
    public void glDrawArrays(int primitive, int offset, int vertexCount)
    {
        WebGL10.glDrawArrays(primitive, offset, vertexCount);
    }

    @Override
    public void glDrawElements(int primitive, int vertexCount, int type, int offset)
    {
        WebGL10.glDrawElements(primitive, vertexCount, type, offset);
    }

    @Override
    public void glEnable(int capability)
    {
        WebGL10.glEnable(capability);
    }

    @Override
    public void glBlendFunc(int src, int dst)
    {
        WebGL10.glBlendFunc(src, dst);
    }

    @Override
    public void glDisable(int capability)
    {
        WebGL10.glDisable(capability);
    }

    @Override
    public void glClearColor(float r, float g, float b, float a)
    {
        WebGL10.glClearColor(r, g, b, a);
    }

    @Override
    public void glBindVertexArray(int vaoID)
    {
        VAOImpl.get().glBindVertexArray(vaoID);
    }

    @Override
    public void glDepthMask(boolean value)
    {
        WebGL10.glDepthMask(value);
    }

    @Override
    public void glDepthFunc(int func)
    {
        WebGL10.glDepthFunc(func);
    }

    @Override
    public void glCullFace(int mode)
    {
        WebGL10.glCullFace(mode);
    }

    @Override
    public int glGetError()
    {
        return WebGL10.glGetError();
    }

    @Override
    public int glCreateProgram()
    {
        return WebGL10.glCreateProgram();
    }

    @Override
    public void glAttachShader(int program, int shader)
    {
        WebGL10.glAttachShader(program, shader);
    }

    @Override
    public void glLinkProgram(int program)
    {
        WebGL10.glLinkProgram(program);
    }

    @Override
    public int glGetProgrami(int program, int param)
    {
        if (param == GL_LINK_STATUS ||
            param == GL_DELETE_STATUS ||
            param == GL_VALIDATE_STATUS)
            return WebGL10.<Boolean>glGetProgramParameter(program, param) ? GL_TRUE : GL_FALSE;

        return WebGL10.<Integer>glGetProgramParameter(program, param);
    }

    @Override
    public String glGetProgramInfoLog(int program)
    {
        return WebGL10.glGetProgramInfoLog(program);
    }

    @Override
    public int glGetAttribLocation(int program, String name)
    {
        return WebGL10.glGetAttribLocation(program, name);
    }

    @Override
    public void glUseProgram(int program)
    {
        WebGL10.glUseProgram(program);
    }

    @Override
    public int glGetUniformLocation(int program, String name)
    {
        return WebGL10.glGetUniformLocation(program, name);
    }

    @Override
    public void glUniform1i(int location, int value)
    {
        WebGL10.glUniform1i(location, value);
    }

    @Override
    public void glUniform2i(int location, int v1, int v2)
    {
        WebGL10.glUniform2i(location, v1, v2);
    }

    @Override
    public void glUniform3i(int location, int v1, int v2, int v3)
    {
        WebGL10.glUniform3i(location, v1, v2, v3);
    }

    @Override
    public void glUniform4i(int location, int v1, int v2, int v3, int v4)
    {
        WebGL10.glUniform4i(location, v1, v2, v3, v4);
    }

    @Override
    public void glUniform1f(int location, float value)
    {
        WebGL10.glUniform1f(location, value);
    }

    @Override
    public void glUniform2f(int location, float v1, float v2)
    {
        WebGL10.glUniform2f(location, v1, v2);
    }

    @Override
    public void glUniform3f(int location, float v1, float v2, float v3)
    {
        WebGL10.glUniform3f(location, v1, v2, v3);
    }

    @Override
    public void glUniform4f(int location, float v1, float v2, float v3, float v4)
    {
        WebGL10.glUniform4f(location, v1, v2, v3, v4);
    }

    @Override
    public void glUniformMatrix3fv(int location, boolean transpose, DirectFloatBuffer matrix)
    {
        WebGL10.glUniformMatrix3fv(location, transpose, Float32ArrayNative.create(((ArrayBufferView) matrix.getDirectBuffer().nativeBuffer()).buffer()));
    }

    @Override
    public void glUniformMatrix4fv(int location, boolean transpose, DirectFloatBuffer matrix)
    {
        WebGL10.glUniformMatrix4fv(location, transpose, Float32ArrayNative.create(((ArrayBufferView) matrix.getDirectBuffer().nativeBuffer()).buffer()));
    }

    @Override
    public void glDeleteProgram(int... id)
    {
        for (int program : id)
            WebGL10.glDeleteProgram(program);
    }

    @Override
    public int glCreateShader(int type)
    {
        return WebGL10.glCreateShader(type);
    }

    @Override
    public void glShaderSource(int shader, String... source)
    {
        String sources = "";

        for (String line : source)
            sources += line + "\n";

        WebGL10.glShaderSource(shader, sources);
    }

    @Override
    public void glCompileShader(int shader)
    {
        WebGL10.glCompileShader(shader);
    }

    @Override
    public int glGetShaderi(int shader, int param)
    {
        if (param == GL_SHADER_TYPE)
            return WebGL10.<Integer>glGetShaderParameter(shader, param);
        else
            return WebGL10.<Boolean>glGetShaderParameter(shader, param) ? Constants.GL_TRUE : Constants.GL_FALSE;
    }

    @Override
    public String glGetShaderInfoLog(int shader)
    {
        return WebGL10.glGetShaderInfoLog(shader);
    }

    @Override
    public void glDeleteShader(int... shader)
    {
        for (int id : shader)
            WebGL10.glDeleteShader(id);
    }

    @Override
    public int glGenTextures()
    {
        return WebGL10.glCreateTexture();
    }

    @Override
    public void glActiveTexture(int unit)
    {
        WebGL10.glActiveTexture(unit);
    }

    @Override
    public void glBindTexture(int target, int texture)
    {
        WebGL10.glBindTexture(target, texture);
    }

    @Override
    public void glTexParameteri(int target, int param, int value)
    {
        WebGL10.glTexParameteri(target, param, value);
    }

    @Override
    public void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, DirectBuffer pixels)
    {
        ArrayBufferView arrayBufferView = null;

        switch (type)
        {
            case GL_FLOAT:
                arrayBufferView = Float32ArrayNative.create(((ArrayBufferView) pixels.nativeBuffer()).buffer());
                break;

            case GL_UNSIGNED_BYTE:
                arrayBufferView = Uint8ArrayNative.create(((ArrayBufferView) pixels.nativeBuffer()).buffer());
                break;
        }

        WebGL10.glTexImage2D(target, level, internalFormat, width, height, border, format, type, arrayBufferView);
    }

    @Override
    public void glGenerateMipmap(int target)
    {
        WebGL10.glGenerateMipmap(target);
    }

    @Override
    public void glDeleteTextures(int... texture)
    {
        for (int id : texture)
            WebGL10.glDeleteTexture(id);
    }

    @Override
    public int glGenVertexArrays()
    {
        return VAOImpl.get().glGenVertexArrays();
    }

    @Override
    public boolean glIsVertexArray(int vertexArray)
    {
        return VAOImpl.get().glIsVertexArray(vertexArray);
    }

    @Override
    public void glEnableVertexAttribArray(int index)
    {
        VAOImpl.get().glEnableVertexAttribArray(index);
    }

    @Override
    public void glDisableVertexAttribArray(int index)
    {
        VAOImpl.get().glDisableVertexAttribArray(index);
    }

    @Override
    public void glVertexAttribPointer(int index, int count, int type, boolean normalized, int stride, long offset)
    {
        VAOImpl.get().glVertexAttribPointer(index, count, type, normalized, stride, offset);
    }

    @Override
    public void glDeleteVertexArrays(int... vertexArray)
    {
        for (int id : vertexArray)
            VAOImpl.get().glDeleteVertexArray(id);
    }
}
