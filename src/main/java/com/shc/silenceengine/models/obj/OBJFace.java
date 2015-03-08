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

package com.shc.silenceengine.models.obj;

import com.shc.silenceengine.graphics.Material;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class OBJFace
{
    private Vector3 vertex;
    private Vector3 normal;
    private Vector3 texCoords;

    private Material material;

    public OBJFace(Vector3 vertex, Vector3 normal, Vector3 texCoords, Material material)
    {
        this.vertex = vertex;
        this.normal = normal;
        this.material = material;
        this.texCoords = texCoords;

        if (material == null)
        {
            // If there is no material, create a default one
            material = new Material();
            material.setName("Default");
        }
    }

    /**
     * @return The vertex indices
     */
    public Vector3 getVertex()
    {
        return vertex;
    }

    /**
     * @return The normal indices
     */
    public Vector3 getNormal()
    {
        return normal;
    }

    /**
     * @return The Texture Indices
     */
    public Vector3 getTexCoord()
    {
        return texCoords;
    }

    /**
     * @return The material of the face
     */
    public Material getMaterial()
    {
        return material;
    }
}
