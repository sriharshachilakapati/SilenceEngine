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
import com.shc.silenceengine.io.DirectFloatBuffer;
import com.shc.silenceengine.math.Matrix3;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.Vector4;

import java.util.HashMap;
import java.util.Map;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Program
{
    public static Program CURRENT;

    private int     id;
    private boolean disposed;

    private Map<String, Integer> uniformLocations;
    private Map<String, Integer> attributeLocations;

    private static DirectFloatBuffer m3Buffer;
    private static DirectFloatBuffer m4Buffer;

    public Program()
    {
        id = SilenceEngine.graphics.glCreateProgram();
        GLError.check();

        uniformLocations = new HashMap<>();
        attributeLocations = new HashMap<>();

        if (m3Buffer == null)
            m3Buffer = new DirectFloatBuffer(9);

        if (m4Buffer == null)
            m4Buffer = new DirectFloatBuffer(16);
    }

    public void attach(Shader shader)
    {
        SilenceEngine.graphics.glAttachShader(id, shader.getId());
        GLError.check();
    }

    public void link()
    {
        SilenceEngine.graphics.glLinkProgram(id);
        GLError.check();

        if (SilenceEngine.graphics.glGetProgrami(id, GL_LINK_STATUS) != GL_TRUE)
            throw new GLException("Unable to link program:\n" + getInfoLog());
    }

    public String getInfoLog()
    {
        return SilenceEngine.graphics.glGetProgramInfoLog(id);
    }

    public int getAttribute(String name)
    {
        use();

        if (attributeLocations.containsKey(name))
            return attributeLocations.get(name);

        int location = SilenceEngine.graphics.glGetAttribLocation(id, name);
        attributeLocations.put(name, location);

        GLError.check();
        return location;
    }

    public void use()
    {
        if (CURRENT == this)
            return;

        if (disposed)
            throw new GLException("Cannot use disposed Program");

        SilenceEngine.graphics.glUseProgram(id);
        GLError.check();

        CURRENT = this;

        prepareFrame();
    }

    public void prepareFrame()
    {
    }

    public int getUniform(String name)
    {
        use();

        if (uniformLocations.containsKey(name))
            return uniformLocations.get(name);

        int location = SilenceEngine.graphics.glGetUniformLocation(id, name);
        uniformLocations.put(name, location);

        GLError.check();
        return location;
    }

    public void setUniform(int location, int... values)
    {
        if (values.length > 4)
            throw new GLException("Uniform component cannot have more than 4 components");

        use();

        switch (values.length)
        {
            case 1:
                SilenceEngine.graphics.glUniform1i(location, values[0]);
                break;

            case 2:
                SilenceEngine.graphics.glUniform2i(location, values[0], values[1]);
                break;

            case 3:
                SilenceEngine.graphics.glUniform3i(location, values[0], values[1], values[2]);
                break;

            case 4:
                SilenceEngine.graphics.glUniform4i(location, values[0], values[1], values[2], values[3]);
                break;
        }

        GLError.check();
    }

    public void setUniform(String name, int... values)
    {
        setUniform(getUniform(name), values);
    }

    public void setUniform(String name, float... values)
    {
        setUniform(getUniform(name), values);
    }

    public void setUniform(int location, Vector2 value)
    {
        setUniform(location, value.x, value.y);
    }

    public void setUniform(int location, float... values)
    {
        if (values.length > 4)
            throw new GLException("Uniform component cannot have more than 4 components");

        use();

        switch (values.length)
        {
            case 1:
                SilenceEngine.graphics.glUniform1f(location, values[0]);
                break;

            case 2:
                SilenceEngine.graphics.glUniform2f(location, values[0], values[1]);
                break;

            case 3:
                SilenceEngine.graphics.glUniform3f(location, values[0], values[1], values[2]);
                break;

            case 4:
                SilenceEngine.graphics.glUniform4f(location, values[0], values[1], values[2], values[3]);
                break;
        }

        GLError.check();
    }

    public void setUniform(int location, Vector3 value)
    {
        setUniform(location, value.x, value.y, value.z);
    }

    public void setUniform(int location, Color value)
    {
        setUniform(location, value.r, value.g, value.b, value.a);
    }

    public void setUniform(int location, Vector4 value)
    {
        setUniform(location, value.x, value.y, value.z, value.w);
    }

    public void setUniform(String name, Vector2 value)
    {
        setUniform(name, value.x, value.y);
    }

    public void setUniform(String name, Vector3 value)
    {
        setUniform(name, value.x, value.y, value.z);
    }

    public void setUniform(String name, Vector4 value)
    {
        setUniform(name, value.x, value.y, value.z, value.w);
    }

    public void setUniform(String name, Color value)
    {
        setUniform(name, value.r, value.g, value.b, value.a);
    }

    public void setUniform(int location, Matrix3 value)
    {
        setUniform(location, false, value);
    }

    public void setUniform(int location, boolean transpose, Matrix3 value)
    {
        use();

        SilenceEngine.graphics.glUniformMatrix3fv(location, transpose, value.storeInto(m3Buffer));

        GLError.check();
    }

    public void setUniform(int location, Matrix4 value)
    {
        setUniform(location, false, value);
    }

    public void setUniform(int location, boolean transpose, Matrix4 value)
    {
        use();

        SilenceEngine.graphics.glUniformMatrix4fv(location, transpose, value.storeInto(m4Buffer));
        GLError.check();
    }

    public void setUniform(String name, boolean transpose, Matrix3 value)
    {
        setUniform(getUniform(name), transpose, value);
    }

    public void setUniform(String name, Matrix3 value)
    {
        setUniform(name, false, value);
    }

    public void setUniform(String name, boolean transpose, Matrix4 value)
    {
        setUniform(getUniform(name), transpose, value);
    }

    public void setUniform(String name, Matrix4 value)
    {
        setUniform(name, false, value);
    }

    public void dispose()
    {
        SilenceEngine.graphics.glDeleteProgram(id);
        GLError.check();
        disposed = true;
    }

    public int getId()
    {
        return id;
    }

    public boolean isDisposed()
    {
        return disposed;
    }
}
