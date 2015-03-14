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

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.IEngine;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.programs.DefaultProgram;
import com.shc.silenceengine.graphics.programs.PointLightProgram;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class GraphicsEngine implements IEngine
{
    public static final Material DEFAULT_MATERIAL;
    private             Material currentMaterial;

    static
    {
        DEFAULT_MATERIAL = new Material();
        DEFAULT_MATERIAL.setAmbient(Color.WHITE);
        DEFAULT_MATERIAL.setSpecular(Color.TRANSPARENT);
    }

    public Material getCurrentMaterial()
    {
        return currentMaterial;
    }

    @Override
    public void init()
    {
        Display.create();
        Display.show();
        Display.centerOnScreen();

        // Use the default material
        useMaterial(DEFAULT_MATERIAL);

        // Load default programs here
        Program.DEFAULT = DefaultProgram.getInstance();
        Program.POINT_LIGHT = PointLightProgram.getInstance();
        Program.DEFAULT.use();

        // The default null texture
        Texture.EMPTY = Texture.fromColor(Color.TRANSPARENT, 16, 16);
        Texture.EMPTY.bind();

        GL3Context.clearColor(Color.BLACK);
        GL3Context.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Initialize OpenGL
        GL3Context.enable(GL_BLEND);
        GL3Context.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GL3Context.cullFace(GL_FRONT_AND_BACK);
        GL3Context.viewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public void useMaterial(Material m)
    {
        currentMaterial = m;
    }

    @Override
    public void beginFrame()
    {
        GL3Context.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Texture.setActiveUnit(0);
    }

    @Override
    public void endFrame()
    {
        Display.update();
    }

    public void setViewport(int x, int y, int width, int height)
    {
        GL3Context.viewport(x, y, width, height);
    }

    public void setClearColor(float r, float g, float b, float a)
    {
        GL3Context.clearColor(r, g, b, a);
    }

    public void setClearColor(Color color)
    {
        GL3Context.clearColor(color);
    }

    @Override
    public void dispose()
    {
        // Dispose the default Programs
        Program.DEFAULT.dispose();
        Program.POINT_LIGHT.dispose();

        // Dispose the empty texture
        Texture.EMPTY.dispose();

        // Destroy the display
        Display.destroy();
    }
}
