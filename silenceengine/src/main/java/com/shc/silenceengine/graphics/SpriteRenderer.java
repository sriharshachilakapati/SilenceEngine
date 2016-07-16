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

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.opengl.BufferObject;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.opengl.VertexArray;
import com.shc.silenceengine.graphics.programs.SpriteProgram;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Transforms;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.functional.UniCallback;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.GL_FLOAT;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteRenderer
{
    private static SpriteProgram program;
    private static int           instances;

    private VertexArray  vao;
    private BufferObject vbo;

    private boolean disposed;

    // Instantiation should happen only via static method
    private SpriteRenderer()
    {
        vao = new VertexArray();
        vao.bind();

        vbo = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        vbo.bind();

        float[] verts = {
                0, 0, 0, 0,
                1, 0, 1, 0,
                0, 1, 0, 1,
                1, 0, 1, 0,
                1, 1, 1, 1,
                0, 1, 0, 1
        };

        DirectBuffer vertBuf = DirectBuffer.wrap(verts);
        vbo.uploadData(vertBuf, BufferObject.Usage.STATIC_DRAW);
        DirectBuffer.free(vertBuf);

        program.use();
        program.setUniform("spriteTex", 0);
        program.setUniform("customTexCoords", false);

        int position = program.getAttribute("position");
        vao.enableAttributeArray(position);
        vao.pointAttribute(position, 4, GL_FLOAT, vbo);
    }

    public static void create(UniCallback<SpriteRenderer> onComplete)
    {
        instances++;

        if (program != null)
        {
            onComplete.invoke(new SpriteRenderer());
            return;
        }

        SpriteProgram.create(program ->
        {
            SpriteRenderer.program = program;
            onComplete.invoke(new SpriteRenderer());
        });
    }

    public void render(Sprite sprite, Transform transform)
    {
        vao.bind();

        Texture texture = sprite.getCurrentFrame();
        if (texture.getID() != Texture.CURRENT.getID())
            texture.bind(0);

        Vector3 scale = Vector3.REUSABLE_STACK.pop().set(texture.getWidth(), texture.getHeight(), 1);
        Matrix4 sprMat = Transforms.createScaling(scale, Matrix4.REUSABLE_STACK.pop()).multiply(transform.matrix);

        program.prepareFrame();
        program.setUniform("sprite", sprMat);

        Vector3.REUSABLE_STACK.push(scale);
        Matrix4.REUSABLE_STACK.push(sprMat);

        GLContext.drawArrays(vao, Primitive.TRIANGLES, 0, 6);
    }

    public void dispose()
    {
        if (disposed)
            throw new SilenceException("Cannot dispose an already disposed object");

        instances--;

        vao.dispose();
        vbo.dispose();

        if (instances == 0)
        {
            program.dispose();
            program = null;
        }

        disposed = true;
    }
}
