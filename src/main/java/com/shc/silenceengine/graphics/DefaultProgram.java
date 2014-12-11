package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Shader;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.utils.FileUtils;

import static org.lwjgl.opengl.GL20.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class DefaultProgram extends Program
{
    public DefaultProgram()
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

    public void setupUniforms()
    {
        setUniform("tex", Texture.getActiveUnit());
        setUniform("mTransform", Game.getBatcher().getTransform().getMatrix());
        setUniform("camProj", BaseCamera.projection);
        setUniform("camView", BaseCamera.view);
    }
}
