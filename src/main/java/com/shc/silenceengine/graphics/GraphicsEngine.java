package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.IEngine;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.programs.DefaultProgram;
import com.shc.silenceengine.graphics.programs.PointLightProgram;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class GraphicsEngine implements IEngine
{
    @Override
    public void init()
    {
        Display.create();
        Display.show();
        Display.centerOnScreen();

        // Load default programs here
        Program.DEFAULT = DefaultProgram.getInstance();
        Program.POINT_LIGHT = PointLightProgram.getInstance();
        Program.DEFAULT.use();

        // The default null texture
        Texture.EMPTY = Texture.fromColor(Color.TRANSPARENT, 16, 16);
        Texture.EMPTY.bind();

        GL3Context.clearColor(Color.BLACK);
        GL3Context.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Initialize OpenGL
        GL3Context.enable(GL_BLEND);
        GL3Context.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GL3Context.cullFace(GL_FRONT_AND_BACK);
        GL3Context.viewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    @Override
    public void beginFrame()
    {
        GL3Context.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        Texture.setActiveUnit(0);
    }

    @Override
    public void endFrame()
    {
        Display.update();
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
