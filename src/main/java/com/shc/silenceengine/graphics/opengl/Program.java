package com.shc.silenceengine.graphics.opengl;

import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DefaultProgram;
import com.shc.silenceengine.math.Matrix3;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.Vector4;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Program
{
    private int     id;
    private boolean disposed;

    public static Program CURRENT;
    public static Program DEFAULT;

    public Program()
    {
        id = glCreateProgram();
        GLError.check();
    }

    public void use()
    {
        if (CURRENT == this)
            return;

        if (disposed)
            throw new GLException("Cannot use disposed Program");

        glUseProgram(id);
        GLError.check();

        CURRENT = this;

        prepareFrame();
    }

    public void prepareFrame(){}

    public void attach(Shader shader)
    {
        glAttachShader(id, shader.getId());
        GLError.check();
    }

    public void link()
    {
        glLinkProgram(id);
        GLError.check();

        if (glGetProgrami(id, GL_LINK_STATUS) != GL_TRUE)
            throw new GLException("Unable to link program:\n" + getInfoLog());
    }

    public int getAttribute(String name)
    {
        use();
        int location = glGetAttribLocation(id, name);

        GLError.check();
        return location;
    }

    public int getUniform(String name)
    {
        use();
        int location = glGetUniformLocation(id, name);

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
                glUniform1i(location, values[0]);
                break;

            case 2:
                glUniform2i(location, values[0], values[1]);
                break;

            case 3:
                glUniform3i(location, values[0], values[1], values[2]);
                break;

            case 4:
                glUniform4i(location, values[0], values[1], values[2], values[3]);
                break;
        }

        GLError.check();
    }

    public void setUniform(int location, float... values)
    {
        if (values.length > 4)
            throw new GLException("Uniform component cannot have more than 4 components");

        use();

        switch (values.length)
        {
            case 1:
                glUniform1f(location, values[0]);
                break;

            case 2:
                glUniform2f(location, values[0], values[1]);
                break;

            case 3:
                glUniform3f(location, values[0], values[1], values[2]);
                break;

            case 4:
                glUniform4f(location, values[0], values[1], values[2], values[3]);
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
        setUniform(location, value.getX(), value.getY());
    }

    public void setUniform(int location, Vector3 value)
    {
        setUniform(location, value.getX(), value.getY(), value.getZ());
    }

    public void setUniform(int location, Vector4 value)
    {
        setUniform(location, value.getX(), value.getY(), value.getZ(), value.getW());
    }

    public void setUniform(int location, Color value)
    {
        setUniform(location, (Vector4) value);
    }

    public void setUniform(String name, Vector2 value)
    {
        setUniform(name, value.getX(), value.getY());
    }

    public void setUniform(String name, Vector3 value)
    {
        setUniform(name, value.getX(), value.getY(), value.getZ());
    }

    public void setUniform(String name, Vector4 value)
    {
        setUniform(name, value.getX(), value.getY(), value.getZ(), value.getW());
    }

    public void setUniform(String name, Color value)
    {
        setUniform(name, (Vector4) value);
    }

    public void setUniform(int location, boolean transpose, Matrix3 value)
    {
        use();

        FloatBuffer buffer = BufferUtils.createFloatBuffer(9);

        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                buffer.put(value.get(i, j));
            }
        }

        buffer.flip();

        glUniformMatrix3(location, transpose, buffer);
        GLError.check();
    }

    public void setUniform(int location, boolean transpose, Matrix4 value)
    {
        use();

        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

        for (int i=0; i<4; i++)
        {
            for (int j=0; j<4; j++)
            {
                buffer.put(value.get(i, j));
            }
        }

        buffer.flip();

        glUniformMatrix4(location, transpose, buffer);
        GLError.check();
    }

    public void setUniform(int location, Matrix3 value)
    {
        setUniform(location, false, value);
    }

    public void setUniform(int location, Matrix4 value)
    {
        setUniform(location, false, value);
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

    public String getInfoLog()
    {
        return glGetProgramInfoLog(id);
    }

    public static void loadDefaultProgram()
    {
        DEFAULT = new DefaultProgram();
        DEFAULT.use();
    }

    public void dispose()
    {
        glDeleteProgram(id);
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
