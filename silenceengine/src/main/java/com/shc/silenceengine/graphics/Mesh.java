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

import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.IDGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Mesh
{
    public final List<Vector3> vertices   = new ArrayList<>();
    public final List<Color>   colors     = new ArrayList<>();
    public final List<Vector3> normals    = new ArrayList<>();
    public final List<Vector3> tangents   = new ArrayList<>();
    public final List<Vector3> biTangents = new ArrayList<>();
    public final List<Vector2> uvs        = new ArrayList<>();

    public final long id = IDGenerator.generate();

    public Primitive renderMode = Primitive.TRIANGLES;

    public void computeNormals()
    {
        normals.clear();

        for (int i = 0; i < vertices.size(); i++)
            normals.add(new Vector3());

        switch (renderMode)
        {
            case TRIANGLES:
                for (int i = 0; i < vertices.size(); i += 3)
                {
                    Vector3 p1 = vertices.get(i);
                    Vector3 p2 = vertices.get(i + 1);
                    Vector3 p3 = vertices.get(i + 2);

                    Vector3 normal = calculateNormal(p1, p2, p3);

                    normals.get(i).add(normal);
                    normals.get(i + 1).add(normal);
                    normals.get(i + 2).add(normal);
                }
                break;

            case TRIANGLE_STRIP:
                for (int i = 0; i < vertices.size(); i++)
                {
                    Vector3 p1 = vertices.get(i);
                    Vector3 p2 = vertices.get((i + 1) % vertices.size());
                    Vector3 p3 = vertices.get((i + 2) % vertices.size());

                    Vector3 normal = calculateNormal(p1, p2, p3);

                    normals.get(i).add(normal);
                }
                break;

            case TRIANGLE_FAN:
                Vector3 p1 = vertices.get(0);

                for (int i = 1; i < vertices.size(); i += 2)
                {
                    Vector3 p2 = vertices.get(i);
                    Vector3 p3 = vertices.get(i + 1);

                    Vector3 normal = calculateNormal(p1, p2, p3);

                    normals.get(i).add(normal);
                    normals.get(i + 1).add(normal);
                }
                break;
        }

        for (int i = 0; i < vertices.size(); i++)
            normals.get(i).normalize();
    }

    private Vector3 calculateNormal(Vector3 p1, Vector3 p2, Vector3 p3)
    {
        Vector3 u = Vector3.REUSABLE_STACK.pop();
        Vector3 v = Vector3.REUSABLE_STACK.pop();

        Vector3 normal = new Vector3();

        u.set(p2).subtract(p1); // U = P2 - P1
        v.set(p3).subtract(p1); // V = P3 - P1

        normal.x = (-u.y * -v.z) - (-u.z * -v.y);
        normal.y = (-u.z * -v.x) - (-u.x * -v.z);
        normal.z = (-u.x * -v.y) - (-u.y * -v.x);

        Vector3.REUSABLE_STACK.push(u);
        Vector3.REUSABLE_STACK.push(v);

        return normal;
    }
}
