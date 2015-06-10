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

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.models.Face;
import com.shc.silenceengine.graphics.models.Mesh;
import com.shc.silenceengine.graphics.models.Model;
import com.shc.silenceengine.graphics.models.StaticMesh;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * A ModelBatch is used to batch different models into batches, and reduce the draw calls.
 *
 * @author Sri Harsha Chilakapati
 */
public class ModelBatch
{
    private List<Mesh>      meshes;
    private List<Transform> transforms;
    private List<Integer>   indices;

    private List<StaticMesh> staticMeshes;
    private List<Transform>  staticMeshTransforms;
    private List<Integer>    staticMeshIndices;

    private Transform transform;

    private boolean active;

    public ModelBatch()
    {
        meshes = new ArrayList<>();
        transforms = new ArrayList<>();
        indices = new ArrayList<>();

        staticMeshes = new ArrayList<>();
        staticMeshTransforms = new ArrayList<>();
        staticMeshIndices = new ArrayList<>();
    }

    public void begin()
    {
        begin(null);
    }

    public void begin(Transform transform)
    {
        if (active)
            throw new SilenceException("ModelBatch already active");

        meshes.clear();
        transforms.clear();
        indices.clear();

        staticMeshes.clear();
        staticMeshTransforms.clear();
        staticMeshIndices.clear();

        this.transform = transform;

        active = true;
    }

    public void flush()
    {
        sortMeshes();

        if (meshes.size() > 0)
        {
            Batcher batcher = SilenceEngine.graphics.getBatcher();

            Material originalMaterial = SilenceEngine.graphics.getCurrentMaterial();
            Texture originalTexture = Texture.CURRENT;

            Material material = meshes.get(indices.get(0)).getMaterial();
            material.getDiffuseMap().bind();
            SilenceEngine.graphics.useMaterial(material);

            Vector3 temp = Vector3.REUSABLE_STACK.pop();

            if (transform != null) batcher.applyTransform(transform);
            batcher.begin();
            {
                for (int i : indices)
                {
                    Mesh mesh = meshes.get(i);

                    if (!mesh.getMaterial().equals(material))
                    {
                        batcher.end();

                        material = mesh.getMaterial();
                        material.getDiffuseMap().bind();
                        SilenceEngine.graphics.useMaterial(material);

                        if (transform != null) batcher.applyTransform(transform);
                        batcher.begin();
                    }

                    Transform transform = transforms.get(i);
                    Color color = mesh.getMaterial().getDiffuse();

                    for (Face face : mesh.getFaces())
                    {
                        batcher.vertex(temp.set(mesh.getVertices().get((int) face.vertexIndex.x)).multiplySelf(transform.getMatrix()));
                        batcher.normal(mesh.getNormals().get((int) face.normalIndex.x));
                        batcher.texCoord(mesh.getTexcoords().get((int) face.texcoordIndex.x));
                        batcher.color(color.x, color.y, color.z, mesh.getMaterial().getDissolve());

                        batcher.vertex(temp.set(mesh.getVertices().get((int) face.vertexIndex.y)).multiplySelf(transform.getMatrix()));
                        batcher.normal(mesh.getNormals().get((int) face.normalIndex.y));
                        batcher.texCoord(mesh.getTexcoords().get((int) face.texcoordIndex.y));
                        batcher.color(color.x, color.y, color.z, mesh.getMaterial().getDissolve());

                        batcher.vertex(temp.set(mesh.getVertices().get((int) face.vertexIndex.z)).multiplySelf(transform.getMatrix()));
                        batcher.normal(mesh.getNormals().get((int) face.normalIndex.z));
                        batcher.texCoord(mesh.getTexcoords().get((int) face.texcoordIndex.z));
                        batcher.color(color.x, color.y, color.z, mesh.getMaterial().getDissolve());
                    }
                }
            }
            batcher.end();

            Vector3.REUSABLE_STACK.push(temp);

            originalTexture.bind();
            SilenceEngine.graphics.useMaterial(originalMaterial);
        }

        meshes.clear();
        transforms.clear();
        indices.clear();

        if (staticMeshes.size() > 0)
        {
            Transform temp = Transform.REUSABLE_STACK.pop();

            for (int i : staticMeshIndices)
            {
                StaticMesh mesh = staticMeshes.get(i);
                Transform transform = staticMeshTransforms.get(i);

                mesh.render(this.transform == null ? transform : temp.set(transform).applySelf(this.transform));
            }

            Transform.REUSABLE_STACK.push(temp);
        }

        staticMeshes.clear();
        staticMeshTransforms.clear();
        staticMeshIndices.clear();
    }

    private void sortMeshes()
    {
        // Only sort the indices
        indices.sort((i, j) -> meshes.get(i).getMaterial().equals(meshes.get(j).getMaterial()) ? -1 : 1);
    }

    public void end()
    {
        if (!active)
            throw new SilenceException("ModelBatch is not active");

        flush();
        active = false;
    }

    public void addModel(Model model, Transform transform)
    {
        model.getMeshes().forEach(m -> addMesh(m, transform));
    }

    public void addMesh(Mesh mesh, Transform transform)
    {
        if (mesh.getNumberOfVertices() <= 900 && !mesh.prefersStatic())
        {
            meshes.add(mesh);
            transforms.add(transform);
            indices.add(meshes.size() - 1);
        }
        else
        {
            staticMeshes.add(SilenceEngine.graphics.getStaticMesh(mesh));
            staticMeshTransforms.add(transform);
            staticMeshIndices.add(staticMeshes.size() - 1);
        }
    }
}
