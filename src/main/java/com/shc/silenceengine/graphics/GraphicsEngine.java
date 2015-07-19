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
import com.shc.silenceengine.graphics.models.Mesh;
import com.shc.silenceengine.graphics.models.StaticMesh;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.programs.DefaultProgram;
import com.shc.silenceengine.graphics.programs.DirectionalLightProgram;
import com.shc.silenceengine.graphics.programs.PointLightProgram;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class GraphicsEngine implements IEngine
{
    public static final Material DEFAULT_MATERIAL;

    private static Map<Mesh, StaticMesh> staticMeshMap;

    public float renderCalls         = 0;
    public float renderCallsPerFrame = 0;
    public float totalRenderCalls    = 0;

    private Color clearColor = Color.BLACK.copy();

    private Material    currentMaterial;
    private Batcher     batcher;
    private ModelBatch  modelBatch;
    private SpriteBatch spriteBatch;

    public Material getCurrentMaterial()
    {
        return currentMaterial;
    }

    @Override
    public void init()
    {
        Display.create();

        // Use the default material
        useMaterial(DEFAULT_MATERIAL);

        // Create the Batcher
        batcher = new Batcher();

        // Load default programs here
        Program.DEFAULT = DefaultProgram.getInstance();
        Program.POINT_LIGHT = PointLightProgram.getInstance();
        Program.DIRECTIONAL_LIGHT = DirectionalLightProgram.getInstance();

        Program.DEFAULT.use();

        // The default null texture
        Texture.EMPTY = Texture.fromColor(Color.TRANSPARENT, 16, 16);
        Texture.EMPTY.bind();
        Sprite.EMPTY = new Sprite(Texture.EMPTY);

        GL3Context.clearColor(Color.BLACK);
        GL3Context.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Initialize OpenGL
        GL3Context.enable(GL_BLEND);
        GL3Context.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GL3Context.viewport(0, 0, Display.getWidth(), Display.getHeight());

        setClearColor(clearColor);

        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();

        // Default font!!
        TrueTypeFont.DEFAULT = new TrueTypeFont("Verdana", TrueTypeFont.STYLE_NORMAL, 16);

        // Static Mesh map
        staticMeshMap = new HashMap<>();

        Display.show();
        Display.centerOnScreen();
    }

    @Override
    public void beginFrame()
    {
        GL3Context.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Texture.setActiveUnit(0);
        setClearColor(clearColor);
    }

    @Override
    public void endFrame()
    {
        Display.update();

        totalRenderCalls += renderCalls;
        renderCallsPerFrame = renderCalls;
        renderCalls = 0;
    }

    @Override
    public void dispose()
    {
        // Dispose static meshes
        staticMeshMap.values().forEach(StaticMesh::dispose);

        // Dispose the default font
        TrueTypeFont.DEFAULT.dispose();

        // Dispose the batcher
        batcher.dispose();

        // Dispose the default Programs
        Program.DEFAULT.dispose();
        Program.POINT_LIGHT.dispose();
        Program.DIRECTIONAL_LIGHT.dispose();

        // Dispose the empty texture
        Texture.EMPTY.dispose();

        // Destroy the display
        Display.destroy();
    }

    public StaticMesh getStaticMesh(Mesh mesh)
    {
        if (staticMeshMap.containsKey(mesh))
            return staticMeshMap.get(mesh);

        StaticMesh staticMesh = new StaticMesh(mesh);
        staticMeshMap.put(mesh, staticMesh);

        return staticMesh;
    }

    public void useMaterial(Material m)
    {
        currentMaterial = m;
    }

    public void setViewport(int x, int y, int width, int height)
    {
        GL3Context.viewport(x, y, width, height);
    }

    public void setClearColor(float r, float g, float b, float a)
    {
        clearColor.set(r, g, b, a);
        GL3Context.clearColor(r, g, b, a);
    }

    public Color getClearColor()
    {
        return clearColor;
    }

    public void setClearColor(Color color)
    {
        clearColor.set(color);
        GL3Context.clearColor(color);
    }

    public Batcher getBatcher()
    {
        return batcher;
    }

    public void setBatcher(Batcher batcher)
    {
        this.batcher = batcher;
    }

    public Graphics2D getGraphics2D()
    {
        return Graphics2D.getInstance();
    }

    public ModelBatch getModelBatch()
    {
        return modelBatch;
    }

    public SpriteBatch getSpriteBatch()
    {
        return spriteBatch;
    }

    public ContextCapabilities getGLCapabilities()
    {
        return GL.getCapabilities();
    }

    static
    {
        DEFAULT_MATERIAL = new Material();
        DEFAULT_MATERIAL.setAmbient(Color.WHITE);
        DEFAULT_MATERIAL.setSpecular(Color.TRANSPARENT);
    }
}
