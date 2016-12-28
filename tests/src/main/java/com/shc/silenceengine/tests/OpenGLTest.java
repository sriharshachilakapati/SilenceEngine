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

package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.BufferObject;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Shader;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.opengl.VertexArray;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.DirectFloatBuffer;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector3;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class OpenGLTest extends SilenceTest
{
    private Texture texture;

    private VertexArray  vertexArray;
    private BufferObject vertexBuffer, texCoordBuffer;

    private Transform transform;

    private Program program;

    @Override
    public void init()
    {
        SilenceEngine.display.setTitle("OpenGLTest");

        transform = new Transform();

        vertexArray = new VertexArray();
        vertexArray.bind();

        GLContext.clearColor(Color.CORN_FLOWER_BLUE);
        GLContext.enable(GL_BLEND);
        GLContext.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        SilenceEngine.io.getImageReader().readImage(FilePath.getResourceFile("engine_resources/logo.png"), image ->
        {
            texture = Texture.fromImage(image);
            image.dispose();

            texture.bind(0);
            program.setUniform("texUnit", 0);
        });

        // The vertex shader source
        String vsSource = "uniform mat4 transform;                                 \n" +
                          "in vec2 position;                                       \n" +
                          "in vec2 texCoords;                                      \n" +
                          "                                                        \n" +
                          "out vec2 vTexCoords;                                    \n" +
                          "                                                        \n" +
                          "void main()                                             \n" +
                          "{                                                       \n" +
                          "    vTexCoords = texCoords;                             \n" +
                          "    gl_Position = transform * vec4(position, 0.0, 1.0); \n" +
                          "}";

        // The fragment shader source
        String fsSource = "uniform sampler2D texUnit;                        \n" +
                          "in vec2 vTexCoords;                               \n" +
                          "                                                  \n" +
                          "void main()                                       \n" +
                          "{                                                 \n" +
                          "    g_FragColor = texture2D(texUnit, vTexCoords); \n" +
                          "}";

        // Create the vertex shader
        Shader vertexShader = new Shader(Shader.Type.VERTEX_SHADER);
        vertexShader.source(vsSource);
        vertexShader.compile();

        // Create the fragment shader
        Shader fragmentShader = new Shader(Shader.Type.FRAGMENT_SHADER);
        fragmentShader.source(fsSource);
        fragmentShader.compile();

        // Create the program
        program = new Program();

        program.attach(vertexShader);
        program.attach(fragmentShader);
        program.link();

        program.use();

        // Create the positions VBO
        float[] vertices =
                {
                        -0.8f, +0.8f,
                        +0.8f, +0.8f,
                        -0.8f, -0.8f,

                        +0.8f, +0.8f,
                        +0.8f, -0.8f,
                        -0.8f, -0.8f
                };

        DirectFloatBuffer verticesBuffer = new DirectFloatBuffer(vertices.length);
        for (int i = 0; i < vertices.length; i++)
            verticesBuffer.write(i, vertices[i]);

        vertexBuffer = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        vertexBuffer.bind();
        vertexBuffer.uploadData(verticesBuffer.getDirectBuffer(), BufferObject.Usage.STATIC_DRAW);

        SilenceEngine.io.free(verticesBuffer.getDirectBuffer());

        int positionLoc = program.getAttribute("position");
        vertexArray.pointAttribute(positionLoc, 2, GL_FLOAT, vertexBuffer);
        vertexArray.enableAttributeArray(positionLoc);

        // Create the texCoords VBO
        float[] texCoords =
                {
                        0, 0,
                        1, 0,
                        0, 1,

                        1, 0,
                        1, 1,
                        0, 1
                };

        DirectFloatBuffer texCoordsBuffer = new DirectFloatBuffer(texCoords.length);
        for (int i = 0; i < texCoords.length; i++)
            texCoordsBuffer.write(i, texCoords[i]);

        texCoordBuffer = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        texCoordBuffer.bind();
        texCoordBuffer.uploadData(texCoordsBuffer.getDirectBuffer(), BufferObject.Usage.STATIC_DRAW);

        SilenceEngine.io.free(texCoordsBuffer.getDirectBuffer());

        int texCoordsLoc = program.getAttribute("texCoords");
        vertexArray.pointAttribute(texCoordsLoc, 2, GL_FLOAT, texCoordBuffer);
        vertexArray.enableAttributeArray(texCoordsLoc);
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        if (Keyboard.isKeyTapped(Keyboard.KEY_F))
            SilenceEngine.display.setFullscreen(!SilenceEngine.display.isFullscreen());

        SilenceEngine.display.setTitle("OpenGL Test | UPS: " + SilenceEngine.gameLoop.getUPS() +
                                       " | FPS: " + SilenceEngine.gameLoop.getFPS());

        transform.rotate(Vector3.AXIS_Y, 45 * delta);
        program.setUniform("transform", transform.matrix);
    }

    @Override
    public void render(float delta)
    {
        GLContext.drawArrays(vertexArray, Primitive.TRIANGLES, 0, 6);
    }

    @Override
    public void resized()
    {
        GLContext.viewport(0, 0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    @Override
    public void dispose()
    {
        GLContext.bindVertexArray(null);
        GLContext.bindVertexBuffer(null);

        program.dispose();
        vertexBuffer.dispose();
        texCoordBuffer.dispose();
        vertexArray.dispose();

        if (texture != null)
            texture.dispose();
    }
}
