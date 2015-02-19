package com.shc.silenceengine.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Sri Harsha Chilakapati
 */
public enum Primitive
{
    POINTS(GL_POINTS),
    LINES(GL_LINES),
    LINE_LOOP(GL_LINE_LOOP),
    LINE_STRIP(GL_LINE_STRIP),
    TRIANGLES(GL_TRIANGLES),
    TRIANGLE_FAN(GL_TRIANGLE_FAN),
    TRIANGLE_STRIP(GL_TRIANGLE_STRIP);

    private int glPrimitive;

    Primitive(int glPrimitive)
    {
        this.glPrimitive = glPrimitive;
    }

    public int getGlPrimitive()
    {
        return glPrimitive;
    }
}
