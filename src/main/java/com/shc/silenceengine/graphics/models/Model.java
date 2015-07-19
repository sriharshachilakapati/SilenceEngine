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

import com.shc.silenceengine.core.IResource;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Material;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Transform;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Model implements IResource
{
    private List<Mesh> meshes;

    public Model()
    {
        meshes = new ArrayList<>();
    }

    public static Model load(String filename)
    {
        if (filename.endsWith(".obj"))
            return new OBJModel(filename);

        throw new SilenceException("The model type you are trying to load is unsupported.");
    }

    public static Model load(FilePath filePath)
    {
        if (filePath.getExtension().equalsIgnoreCase("obj"))
            return new OBJModel(filePath);

        throw new SilenceException("The model type you are trying to load is unsupported.");
    }

    public void render(Batcher batcher, Transform transform)
    {
        Mesh m = getMeshes().get(0);
        Material originalMaterial = SilenceEngine.graphics.getCurrentMaterial();
        Texture originalTexture = Texture.CURRENT;

        SilenceEngine.graphics.useMaterial(m.getMaterial());
        m.getMaterial().getDiffuseMap().bind();

        if (transform != null) batcher.applyTransform(transform);
        batcher.begin();
        {
            for (Mesh mesh : getMeshes())
            {
                if (!m.getMaterial().equals(mesh.getMaterial()))
                {
                    batcher.end();

                    SilenceEngine.graphics.useMaterial(mesh.getMaterial());
                    mesh.getMaterial().getDiffuseMap().bind();

                    m = mesh;
                    if (transform != null) batcher.applyTransform(transform);
                    batcher.begin();
                }

                Color color = mesh.getMaterial().getDiffuse();

                for (Face face : mesh.getFaces())
                {
                    batcher.vertex(mesh.getVertices().get((int) face.vertexIndex.x));
                    batcher.normal(mesh.getNormals().get((int) face.normalIndex.x));
                    batcher.texCoord(mesh.getTexcoords().get((int) face.texcoordIndex.x));
                    batcher.color(color.x, color.y, color.z, mesh.getMaterial().getDissolve());

                    batcher.vertex(mesh.getVertices().get((int) face.vertexIndex.y));
                    batcher.normal(mesh.getNormals().get((int) face.normalIndex.y));
                    batcher.texCoord(mesh.getTexcoords().get((int) face.texcoordIndex.y));
                    batcher.color(color.x, color.y, color.z, mesh.getMaterial().getDissolve());

                    batcher.vertex(mesh.getVertices().get((int) face.vertexIndex.z));
                    batcher.normal(mesh.getNormals().get((int) face.normalIndex.z));
                    batcher.texCoord(mesh.getTexcoords().get((int) face.texcoordIndex.z));
                    batcher.color(color.x, color.y, color.z, mesh.getMaterial().getDissolve());
                }
            }
        }
        batcher.end();

        SilenceEngine.graphics.useMaterial(originalMaterial);
        originalTexture.bind();
    }

    public void dispose()
    {
        for (Mesh mesh : getMeshes())
        {
            Material material = mesh.getMaterial();

            if (material.getDiffuseMap() != Texture.EMPTY)
                material.getDiffuseMap().dispose();

            if (material.getNormalMap() != Texture.EMPTY)
                material.getNormalMap().dispose();

            if (material.getSpecularMap() != Texture.EMPTY)
                material.getSpecularMap().dispose();
        }
    }

    public List<Mesh> getMeshes()
    {
        return meshes;
    }

    public boolean prefersStatic()
    {
        return meshes.stream().filter(Mesh::prefersStatic).count() > 0;
    }

    public void setPrefersStatic(boolean prefersStatic)
    {
        meshes.forEach(m -> m.setPreferStatic(prefersStatic));
    }
}
