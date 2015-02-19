package com.shc.silenceengine.graphics.opengl;

import com.shc.silenceengine.geom2d.Rectangle;
import com.shc.silenceengine.graphics.Color;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * @author Sri Harsha Chilakapati
 */
public final class GL3Context
{
    private GL3Context()
    {
    }

    public static void enable(int capability)
    {
        glEnable(capability);
        GLError.check();
    }

    public static void blendFunc(int src, int dst)
    {
        glBlendFunc(src, dst);
        GLError.check();
    }

    public static void disable(int capability)
    {
        glDisable(capability);
        GLError.check();
    }

    public static void clearColor(Color color)
    {
        glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GLError.check();
    }

    public static void clear(int buffers)
    {
        glClear(buffers);
        GLError.check();
    }

    public static void drawArrays(VertexArray vao, Primitive mode, int offset, int vertexCount)
    {
        vao.bind();
        glDrawArrays(mode.getGlPrimitive(), offset, vertexCount);
        GLError.check();
    }

    public static void drawElements(VertexArray vao, Primitive mode, int offset, int vertexCount, int type)
    {
        vao.bind();
        glDrawElements(mode.getGlPrimitive(), vertexCount, type, offset);
        GLError.check();
    }

    public static void bindVertexArray(VertexArray vao)
    {
        if (vao == null)
        {
            glBindVertexArray(0);
            GLError.check();
            return;
        }

        vao.bind();
    }

    public static void bindVertexBuffer(BufferObject vbo)
    {
        if (vbo == null)
        {
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            GLError.check();
            return;
        }

        vbo.bind();
    }

    public static void viewport(Rectangle rect)
    {
        viewport(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public static void viewport(float x, float y, float width, float height)
    {
        glViewport((int) x, (int) y, (int) width, (int) height);
        GLError.check();
    }

    public static void depthMask(boolean value)
    {
        glDepthMask(value);
        GLError.check();
    }

    public static void depthFunc(int func)
    {
        glDepthFunc(func);
        GLError.check();
    }

    public static void cullFace(int mode)
    {
        glCullFace(mode);
        GLError.check();
    }
}
