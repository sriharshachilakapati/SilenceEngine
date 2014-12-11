package com.shc.silenceengine.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Encapsulates OpenGL Shader objects nicely in a Java Wrapper making
 * it easy to use in an object oriented way.
 *
 * TODO: Document this class and its methods.
 *
 * @author Sri Harsha Chilakapati
 */
public class Shader
{
    private int     id;
    private boolean disposed;

    public Shader(int type)
    {
        id = glCreateShader(type);
        GLError.check();
    }

    public void source(String source)
    {
        if (disposed)
            throw new GLException("Shader already disposed!");

        glShaderSource(id, source);
        GLError.check();
    }

    public void compile()
    {
        if (disposed)
            throw new GLException("Shader already disposed!");

        glCompileShader(id);
        GLError.check();

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
            throw new GLException("Unable to compile shader:\n" + getInfoLog());
    }

    public String getInfoLog()
    {
        if (disposed)
            throw new GLException("Shader already disposed!");

        return glGetShaderInfoLog(id);
    }

    public void dispose()
    {
        glDeleteShader(id);
        GLError.check();
        disposed = true;
    }

    public boolean isDisposed()
    {
        return disposed;
    }

    public int getId()
    {
        return id;
    }
}
