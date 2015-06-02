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
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * A mesh is a part of a model, that takes a material and the geometry.
 *
 * @author Sri Harsha Chilakapati
 */
public class Mesh
{
    private Material material;

    private List<Face>    faces;
    private List<Vector3> vertices;
    private List<Vector3> normals;
    private List<Vector2> texcoords;

    public Mesh()
    {
        material = new Material("Default");

        faces = new ArrayList<>();
        vertices = new ArrayList<>();
        normals = new ArrayList<>();
        texcoords = new ArrayList<>();
    }

    public Material getMaterial()
    {
        return material;
    }

    public List<Face> getFaces()
    {
        return faces;
    }

    public List<Vector3> getVertices()
    {
        return vertices;
    }

    public List<Vector3> getNormals()
    {
        return normals;
    }

    public List<Vector2> getTexcoords()
    {
        return texcoords;
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }
}
