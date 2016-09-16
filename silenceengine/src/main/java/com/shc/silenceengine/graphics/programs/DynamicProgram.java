/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.cameras.Camera;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Shader;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.io.FileReader;
import com.shc.silenceengine.utils.functional.UniCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public class DynamicProgram extends Program
{
    public static final String VERTEX_ATTRIB   = "position";
    public static final String COLOR_ATTRIB    = "color";
    public static final String TEXCOORD_ATTRIB = "texCoords";

    public static void create(UniCallback<DynamicProgram> uniCallback)
    {
        FileReader fileReader = SilenceEngine.io.getFileReader();

        fileReader.readTextFile(FilePath.getResourceFile("engine_resources/shaders/dynamic.vert"), vSource ->
                fileReader.readTextFile(FilePath.getResourceFile("engine_resources/shaders/dynamic.frag"), fSource ->
                {
                    DynamicProgram program = new DynamicProgram();

                    Shader vShader = new Shader(Shader.Type.VERTEX_SHADER);
                    vShader.source(vSource);
                    vShader.compile();

                    Shader fShader = new Shader(Shader.Type.FRAGMENT_SHADER);
                    fShader.source(fSource);
                    fShader.compile();

                    program.attach(vShader);
                    program.attach(fShader);
                    program.link();

                    vShader.dispose();
                    fShader.dispose();

                    uniCallback.invoke(program);
                })
        );
    }

    public void applyToRenderer(DynamicRenderer dynamicRenderer)
    {
        dynamicRenderer.setVertexLocation(getAttribute(VERTEX_ATTRIB));
        dynamicRenderer.setColorLocation(getAttribute(COLOR_ATTRIB));
        dynamicRenderer.setTexCoordLocation(getAttribute(TEXCOORD_ATTRIB));
        dynamicRenderer.setNormalLocation(-1);
    }

    @Override
    public void prepareFrame()
    {
        use();

        setUniform("proj", Camera.CURRENT.getProjection());
        setUniform("view", Camera.CURRENT.getView());
    }
}
