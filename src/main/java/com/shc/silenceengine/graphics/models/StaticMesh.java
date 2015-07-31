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

package com.shc.silenceengine.graphics.models;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Material;
import com.shc.silenceengine.graphics.opengl.BufferObject;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.opengl.VertexArray;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.Vector4;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class StaticMesh
{
    private Material material;

    private VertexArray vao;

    private BufferObject vbo;
    private BufferObject nbo;
    private BufferObject tbo;
    private BufferObject cbo;

    private int numVertices;

    public StaticMesh(Mesh mesh)
    {
        List<Float> vertices = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Float> colors = new ArrayList<>();
        List<Float> texcoords = new ArrayList<>();

        material = mesh.getMaterial();
        numVertices = mesh.getNumberOfVertices();

        for (Face face : mesh.getFaces())
        {
            Vector3 v1 = mesh.getVertices().get((int) face.vertexIndex.x);
            Vector3 v2 = mesh.getVertices().get((int) face.vertexIndex.y);
            Vector3 v3 = mesh.getVertices().get((int) face.vertexIndex.z);

            Vector3 n1 = mesh.getNormals().get((int) face.normalIndex.x);
            Vector3 n2 = mesh.getNormals().get((int) face.normalIndex.y);
            Vector3 n3 = mesh.getNormals().get((int) face.normalIndex.z);

            Vector2 t1 = mesh.getTexcoords().get((int) face.texcoordIndex.x);
            Vector2 t2 = mesh.getTexcoords().get((int) face.texcoordIndex.y);
            Vector2 t3 = mesh.getTexcoords().get((int) face.texcoordIndex.z);

            Color color = material.getDiffuse();

            addVector(v1, vertices);
            addVector(v2, vertices);
            addVector(v3, vertices);

            addVector(n1, normals);
            addVector(n2, normals);
            addVector(n3, normals);

            addVector(t1, texcoords);
            addVector(t2, texcoords);
            addVector(t3, texcoords);

            for (int i = 0; i < 3; i++)
                addVector(mesh.getMaterial().getDiffuseMap().getID() == Texture.EMPTY.getID() ? color : Color.TRANSPARENT, colors);
        }

        vao = new VertexArray();
        vao.bind(true);

        vbo = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        vbo.uploadData(wrapBuffer(vertices), BufferObject.Usage.STATIC_DRAW);

        nbo = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        nbo.uploadData(wrapBuffer(normals), BufferObject.Usage.STATIC_DRAW);

        tbo = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        tbo.uploadData(wrapBuffer(texcoords), BufferObject.Usage.STATIC_DRAW);

        cbo = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        cbo.uploadData(wrapBuffer(colors), BufferObject.Usage.STATIC_DRAW);
    }

    private void addVector(Vector3 v, List<Float> list)
    {
        list.add(v.x);
        list.add(v.y);
        list.add(v.z);
    }

    private void addVector(Vector2 v, List<Float> list)
    {
        list.add(v.x);
        list.add(v.y);
    }

    private void addVector(Vector4 v, List<Float> list)
    {
        list.add(v.x);
        list.add(v.y);
        list.add(v.z);
        list.add(v.w);
    }

    private Buffer wrapBuffer(List<Float> floats)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(floats.size());
        floats.forEach(buffer::put);
        return buffer.flip();
    }

    public void render(Transform transform)
    {
        // Get the batcher to know the program locations
        Batcher batcher = SilenceEngine.graphics.getBatcher();

        // Validate the VAO
        if (!vao.isValid())
        {
            vao = new VertexArray();
            vao.bind(true);
        }

        // Backup the original VAO
        VertexArray originalVAO = VertexArray.CURRENT;

        // Bind our VAO
        vao.bind();

        // Material of this mesh
        Material originalMaterial = SilenceEngine.graphics.getCurrentMaterial();
        Texture originalTexture = Texture.CURRENT;
        SilenceEngine.graphics.useMaterial(material);
        material.getDiffuseMap().bind();

        // Uniforms
        Program program = Program.CURRENT;
        program.prepareFrame();

        program.setUniform("mTransform", transform.getMatrix());

        // Enable the attribute locations
        vao.enableAttributeArray(batcher.getVertexLocation());
        vao.enableAttributeArray(batcher.getNormalLocation());
        vao.enableAttributeArray(batcher.getColorLocation());
        vao.enableAttributeArray(batcher.getTexCoordLocation());

        // Point the attributes to buffer objects
        vao.pointAttribute(batcher.getVertexLocation(), 3, GL11.GL_FLOAT, vbo);
        vao.pointAttribute(batcher.getNormalLocation(), 3, GL11.GL_FLOAT, nbo);
        vao.pointAttribute(batcher.getColorLocation(), 4, GL11.GL_FLOAT, cbo);
        vao.pointAttribute(batcher.getTexCoordLocation(), 2, GL11.GL_FLOAT, tbo);

        // Make a draw call
        GL3Context.drawArrays(vao, Primitive.TRIANGLES, 0, numVertices);

        // Disable the attribute locations
        vao.disableAttributeArray(batcher.getVertexLocation());
        vao.disableAttributeArray(batcher.getNormalLocation());
        vao.disableAttributeArray(batcher.getColorLocation());
        vao.disableAttributeArray(batcher.getTexCoordLocation());

        SilenceEngine.graphics.useMaterial(originalMaterial);
        originalTexture.bind();

        // Bind the original VAO if it is valid
        if (originalVAO.isValid())
            originalVAO.bind();
    }

    public void dispose()
    {
        vao.dispose();
        vbo.dispose();
        nbo.dispose();
        tbo.dispose();
        cbo.dispose();
    }
}
