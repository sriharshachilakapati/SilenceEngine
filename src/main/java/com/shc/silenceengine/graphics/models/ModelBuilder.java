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

import com.shc.silenceengine.graphics.Material;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class ModelBuilder
{
    private Model     model;
    private Mesh      mesh;
    private Transform transform;

    public ModelBuilder()
    {
        model = new Model();
        transform = new Transform();
    }

    public ModelBuilder createNewMesh()
    {
        mesh = new Mesh();
        model.getMeshes().add(mesh);

        return this;
    }

    public ModelBuilder setMaterial(Material material)
    {
        if (mesh == null)
            createNewMesh();

        mesh.setMaterial(material);

        return this;
    }

    public ModelBuilder setTransform(Transform transform)
    {
        this.transform.reset().applySelf(transform);

        return this;
    }

    public ModelBuilder createBox(float width, float height, float thickness)
    {
        // Front faces of the box
        emitVertex(new Vector3(-width / 2, +height / 2, +thickness / 2), new Vector3(0, 0, 1), new Vector2(0, 0));
        emitVertex(new Vector3(-width / 2, -height / 2, +thickness / 2), new Vector3(0, 0, 1), new Vector2(0, 1));
        emitVertex(new Vector3(+width / 2, +height / 2, +thickness / 2), new Vector3(0, 0, 1), new Vector2(1, 0));
        emitFace();
        emitVertex(new Vector3(+width / 2, +height / 2, +thickness / 2), new Vector3(0, 0, 1), new Vector2(1, 0));
        emitVertex(new Vector3(-width / 2, -height / 2, +thickness / 2), new Vector3(0, 0, 1), new Vector2(0, 1));
        emitVertex(new Vector3(+width / 2, -height / 2, +thickness / 2), new Vector3(0, 0, 1), new Vector2(1, 1));
        emitFace();

        // Back faces of the box
        emitVertex(new Vector3(-width / 2, +height / 2, -thickness / 2), new Vector3(0, 0, -1), new Vector2(0, 0));
        emitVertex(new Vector3(+width / 2, +height / 2, -thickness / 2), new Vector3(0, 0, -1), new Vector2(1, 0));
        emitVertex(new Vector3(-width / 2, -height / 2, -thickness / 2), new Vector3(0, 0, -1), new Vector2(0, 1));
        emitFace();
        emitVertex(new Vector3(+width / 2, +height / 2, -thickness / 2), new Vector3(0, 0, -1), new Vector2(1, 0));
        emitVertex(new Vector3(+width / 2, -height / 2, -thickness / 2), new Vector3(0, 0, -1), new Vector2(1, 1));
        emitVertex(new Vector3(-width / 2, -height / 2, -thickness / 2), new Vector3(0, 0, -1), new Vector2(0, 1));
        emitFace();

        // Left faces of the box
        emitVertex(new Vector3(-width / 2, +height / 2, -thickness / 2), new Vector3(-1, 0, 0), new Vector2(0, 0));
        emitVertex(new Vector3(-width / 2, -height / 2, -thickness / 2), new Vector3(-1, 0, 0), new Vector2(0, 1));
        emitVertex(new Vector3(-width / 2, +height / 2, +thickness / 2), new Vector3(-1, 0, 0), new Vector2(1, 0));
        emitFace();
        emitVertex(new Vector3(-width / 2, +height / 2, +thickness / 2), new Vector3(-1, 0, 0), new Vector2(1, 0));
        emitVertex(new Vector3(-width / 2, -height / 2, -thickness / 2), new Vector3(-1, 0, 0), new Vector2(0, 1));
        emitVertex(new Vector3(-width / 2, -height / 2, +thickness / 2), new Vector3(-1, 0, 0), new Vector2(1, 1));
        emitFace();

        // Right faces of the box
        emitVertex(new Vector3(+width / 2, +height / 2, -thickness / 2), new Vector3(1, 0, 0), new Vector2(0, 0));
        emitVertex(new Vector3(+width / 2, +height / 2, +thickness / 2), new Vector3(1, 0, 0), new Vector2(1, 0));
        emitVertex(new Vector3(+width / 2, -height / 2, -thickness / 2), new Vector3(1, 0, 0), new Vector2(0, 1));
        emitFace();
        emitVertex(new Vector3(+width / 2, +height / 2, +thickness / 2), new Vector3(1, 0, 0), new Vector2(1, 0));
        emitVertex(new Vector3(+width / 2, -height / 2, +thickness / 2), new Vector3(1, 0, 0), new Vector2(1, 1));
        emitVertex(new Vector3(+width / 2, -height / 2, -thickness / 2), new Vector3(1, 0, 0), new Vector2(0, 1));
        emitFace();

        // Top faces of the box
        emitVertex(new Vector3(-width / 2, +height / 2, -thickness / 2), new Vector3(0, 1, 0), new Vector2(0, 0));
        emitVertex(new Vector3(-width / 2, +height / 2, +thickness / 2), new Vector3(0, 1, 0), new Vector2(0, 1));
        emitVertex(new Vector3(+width / 2, +height / 2, -thickness / 2), new Vector3(0, 1, 0), new Vector2(1, 0));
        emitFace();
        emitVertex(new Vector3(+width / 2, +height / 2, -thickness / 2), new Vector3(0, 1, 0), new Vector2(1, 0));
        emitVertex(new Vector3(-width / 2, +height / 2, +thickness / 2), new Vector3(0, 1, 0), new Vector2(0, 1));
        emitVertex(new Vector3(+width / 2, +height / 2, +thickness / 2), new Vector3(0, 1, 0), new Vector2(1, 1));
        emitFace();

        // Bottom faces of the box
        emitVertex(new Vector3(-width / 2, -height / 2, -thickness / 2), new Vector3(0, -1, 0), new Vector2(0, 0));
        emitVertex(new Vector3(+width / 2, -height / 2, -thickness / 2), new Vector3(0, -1, 0), new Vector2(1, 0));
        emitVertex(new Vector3(-width / 2, -height / 2, +thickness / 2), new Vector3(0, -1, 0), new Vector2(0, 1));
        emitFace();
        emitVertex(new Vector3(+width / 2, -height / 2, -thickness / 2), new Vector3(0, -1, 0), new Vector2(1, 0));
        emitVertex(new Vector3(+width / 2, -height / 2, +thickness / 2), new Vector3(0, -1, 0), new Vector2(1, 1));
        emitVertex(new Vector3(-width / 2, -height / 2, +thickness / 2), new Vector3(0, -1, 0), new Vector2(0, 1));
        emitFace();

        return this;
    }

    public ModelBuilder emitVertex(Vector3 position, Vector3 normal, Vector2 texcoords)
    {
        if (mesh == null)
            createNewMesh();

        mesh.getVertices().add(position.multiply(transform.getMatrix()));
        mesh.getNormals().add(normal);
        mesh.getTexcoords().add(texcoords);

        return this;
    }

    public ModelBuilder emitFace()
    {
        if (mesh == null)
            createNewMesh();

        Face face = new Face();
        face.vertexIndex.x = mesh.getVertices().size() - 3;
        face.vertexIndex.y = mesh.getVertices().size() - 2;
        face.vertexIndex.z = mesh.getVertices().size() - 1;

        face.normalIndex.x = mesh.getNormals().size() - 3;
        face.normalIndex.y = mesh.getNormals().size() - 2;
        face.normalIndex.z = mesh.getNormals().size() - 1;

        face.texcoordIndex.x = mesh.getTexcoords().size() - 3;
        face.texcoordIndex.y = mesh.getTexcoords().size() - 2;
        face.texcoordIndex.z = mesh.getTexcoords().size() - 1;

        mesh.getFaces().add(face);

        return this;
    }

    public Model build()
    {
        model.getMeshes().add(mesh);
        return model;
    }
}
