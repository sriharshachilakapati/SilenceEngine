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

package com.shc.silenceengine.graphics.programs;

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.cameras.BaseCamera;
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

    public static Program getInstance()
    {
        if (instance == null)
            instance = new DefaultProgram();

        return instance;
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
        setUniform("ambient", SilenceEngine.graphics.getCurrentMaterial().getAmbient());

        // Batcher locations
        batcher.setVertexLocation(0);
        batcher.setColorLocation(1);
        batcher.setTexCoordLocation(2);
        batcher.setNormalLocation(3);
    }
}
