/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Shader;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class DynamicRendererTest extends SilenceTest
{
    private DynamicRenderer renderer;
    private Program         program;
    private Transform       transform;

    @Override
    public void init()
    {
        renderer = new DynamicRenderer();
        transform = new Transform();

        // The vertex shader source
        String vsSource = "uniform mat4 transform;                 \n" +

                          "in vec4 position;                       \n" +
                          "in vec4 color;                          \n" +

                          "out vec4 vColor;                        \n" +

                          "void main()                             \n" +
                          "{                                       \n" +
                          "    vColor = color;                     \n" +
                          "    gl_Position = transform * position; \n" +
                          "}";

        // The fragment shader source
        String fsSource = "in vec4 vColor;         \n" +

                          "void main()             \n" +
                          "{                       \n" +
                          "    g_FragColor = vColor; \n" +
                          "}";

        Shader vertexShader = new Shader(Shader.Type.VERTEX_SHADER);
        vertexShader.source(vsSource);
        vertexShader.compile();

        Shader fragmentShader = new Shader(Shader.Type.FRAGMENT_SHADER);
        fragmentShader.source(fsSource);
        fragmentShader.compile();

        program = new Program();

        program.attach(vertexShader);
        program.attach(fragmentShader);
        program.link();

        program.use();

        renderer.setVertexLocation(program.getAttribute("position"));
        renderer.setColorLocation(program.getAttribute("color"));
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        transform.rotate(Vector3.AXIS_Y, 45 * deltaTime);
    }

    @Override
    public void render(float deltaTime)
    {
        program.setUniform("transform", transform);

        renderer.begin(Primitive.TRIANGLES);
        {
            renderer.vertex(+0.0f, +0.8f);
            renderer.color(Color.RED);

            renderer.vertex(+0.8f, -0.8f);
            renderer.color(Color.GREEN);

            renderer.vertex(-0.8f, -0.8f);
            renderer.color(Color.BLUE);
        }
        renderer.end();
    }

    @Override
    public void resized()
    {
        GLContext.viewport(0, 0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    @Override
    public void dispose()
    {
        renderer.dispose();
        program.dispose();
    }
}
