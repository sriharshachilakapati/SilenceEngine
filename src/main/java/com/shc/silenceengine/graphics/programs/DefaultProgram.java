package com.shc.silenceengine.graphics.programs;

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.cameras.BaseCamera;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.RenderContext;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Shader;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.utils.FileUtils;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL20.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class DefaultProgram extends Program
{
    private static DefaultProgram instance;

    public static Program getInstance()
    {
        if (instance == null)
            instance = new DefaultProgram();

        return instance;
    }

    private DefaultProgram()
    {
        Shader vs = new Shader(GL_VERTEX_SHADER);
        vs.source(FileUtils.readLinesToString(FileUtils.getResource("resources/default-shader.vert")));
        vs.compile();

        Shader fs = new Shader(GL_FRAGMENT_SHADER);
        fs.source(FileUtils.readLinesToString(FileUtils.getResource("resources/default-shader.frag")));
        fs.compile();

        attach(vs);
        attach(fs);
        link();

        vs.dispose();
        fs.dispose();

        Program.DEFAULT = this;
        use();
    }

    public void prepareFrame()
    {
        Batcher batcher = Game.getBatcher();

        GL3Context.enable(GL11.GL_BLEND);
        GL3Context.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Uniforms
        setUniform("textureID", Texture.getActiveUnit());
        setUniform("mTransform", batcher.getTransform().getMatrix());
        setUniform("camProj", BaseCamera.CURRENT.getProjection());
        setUniform("camView", BaseCamera.CURRENT.getView());
        setUniform("ambient", RenderContext.CURRENT_MATERIAL.getAmbient());

        // Batcher locations
        batcher.setVertexLocation(0);
        batcher.setColorLocation(1);
        batcher.setTexCoordLocation(2);
        batcher.setNormalLocation(3);
    }
}
