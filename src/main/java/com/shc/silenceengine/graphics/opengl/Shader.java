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

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Encapsulates OpenGL Shader objects nicely in a Java Wrapper making it easy to use in an object oriented way.
 *
 * @author Sri Harsha Chilakapati
 * @author Heiko Brumme
 */
public class Shader
{
    private int     id;
    private boolean disposed;

    /**
     * Creates a shader with a specified type. Valid types are GL_VERTEX_SHADER, GL_GEOMETRY_SHADER or
     * GL_FRAGMENT_SHADER.
     *
     * @param type The type of this shader
     */
    public Shader(int type)
    {
        id = glCreateShader(type);
        GLError.check();
    }

    /**
     * Sets the source code for this shader. Any source code previously stored in the shader object is completely
     * replaced.
     *
     * @param source The source code for this shader
     */
    public void source(String source)
    {
        if (disposed)
            throw new GLException("Shader already disposed!");

        glShaderSource(id, source);
        GLError.check();
    }

    /**
     * Compiles the shader and checks its compile status afterwards.
     */
    public void compile()
    {
        if (disposed)
            throw new GLException("Shader already disposed!");

        glCompileShader(id);
        GLError.check();

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
            throw new GLException("Unable to compile shader:\n" + getInfoLog());
    }

    /**
     * Returns the information log for a shader object.
     *
     * @return Information log for this shader
     */
    public String getInfoLog()
    {
        if (disposed)
            throw new GLException("Shader already disposed!");

        return glGetShaderInfoLog(id);
    }

    /**
     * Disposes the shader. You can't use the shader after disposing.
     */
    public void dispose()
    {
        glDeleteShader(id);
        GLError.check();
        disposed = true;
    }

    /**
     * Tells if the shader is disposed.
     *
     * @return True if this Shader is disposed, else it returns false
     */
    public boolean isDisposed()
    {
        return disposed;
    }

    /**
     * Gets the ID of the shader.
     *
     * @return The ID of this Shader. Useful if you directly want to use any OpenGL function yourself.
     */
    public int getId()
    {
        return id;
    }
}
