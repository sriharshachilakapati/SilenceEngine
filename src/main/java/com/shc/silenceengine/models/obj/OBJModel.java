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

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.GraphicsEngine;
import com.shc.silenceengine.graphics.Material;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.models.Model;
import com.shc.silenceengine.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
public class OBJModel extends Model
{
    private List<Vector3> vertices;
    private List<Vector3> normals;
    private List<Vector2> texCoords;

    private List<OBJFace>         faces;
    private Map<String, Material> materials;

    public OBJModel(String name)
    {
        vertices = new ArrayList<>();
        normals = new ArrayList<>();
        texCoords = new ArrayList<>();
        faces = new ArrayList<>();
        materials = new HashMap<>();

        parseOBJ(name);
    }

    private void parseOBJ(String name)
    {
        Material material = null;

        String[] lines = FileUtils.readLinesToStringArray(FileUtils.getResource(name));

        // Empty texture coords, just for safety
        texCoords.add(Vector2.ZERO);

        for (String line : lines)
        {
            if (line.startsWith("v "))
                parseVertex(line);

            else if (line.startsWith("vn "))
                parseNormal(line);

            else if (line.startsWith("vt "))
                parseTextureCoords(line);

            else if (line.startsWith("f "))
                parseFace(line, material);

            else if (line.startsWith("usemtl "))
                material = materials.get(line.replaceAll("usemtl ", "").trim());

            else if (line.startsWith("mtllib "))
                parseMaterialLib(name, line);
        }

        sortFaces();
    }

    private void parseVertex(String line)
    {
        String[] values = line.split(" ");

        float x = Float.parseFloat(values[1]);
        float y = Float.parseFloat(values[2]);
        float z = Float.parseFloat(values[3]);

        vertices.add(new Vector3(x, y, z));
    }

    private void parseNormal(String line)
    {
        String[] values = line.split(" ");

        float x = Float.parseFloat(values[1]);
        float y = Float.parseFloat(values[2]);
        float z = Float.parseFloat(values[3]);

        normals.add(new Vector3(x, y, z));
    }

    private void parseTextureCoords(String line)
    {
        String[] values = line.split(" ");

        float x = Float.parseFloat(values[1]);
        float y = Float.parseFloat(values[2]);

        texCoords.add(new Vector2(x, y));
    }

    private void parseFace(String line, Material material)
    {
        String[] values = line.split(" ");

        // Parse the vertex indices
        float v1 = Float.parseFloat(values[1].split("/")[0]);
        float v2 = Float.parseFloat(values[2].split("/")[0]);
        float v3 = Float.parseFloat(values[3].split("/")[0]);

        // Create the vertex
        Vector3 vertex = new Vector3(v1, v2, v3);

        // Parse the texture coord indices
        float vt1, vt2, vt3;

        if (material.getDiffuseMap() != Texture.EMPTY)
        {
            vt1 = Float.parseFloat(values[1].split("/")[1]);
            vt2 = Float.parseFloat(values[2].split("/")[1]);
            vt3 = Float.parseFloat(values[3].split("/")[1]);
        }
        else
        {
            vt1 = vt2 = vt3 = 0;
        }

        // Create the texture coord
        Vector3 texCoords = new Vector3(vt1, vt2, vt3);

        // Parse the normal indices
        float vn1 = Float.parseFloat(values[1].split("/")[2]);
        float vn2 = Float.parseFloat(values[2].split("/")[2]);
        float vn3 = Float.parseFloat(values[3].split("/")[2]);

        // Create the normal
        Vector3 normal = new Vector3(vn1, vn2, vn3);

        // Create the face
        faces.add(new OBJFace(vertex, normal, texCoords, material));
    }

    private void parseMaterialLib(String objFile, String mtlLine)
    {
        String objDir = objFile.substring(0, objFile.lastIndexOf('/'));
        String mtlLib = objDir + "/" + mtlLine.split(" ")[1].trim();
        mtlLib = mtlLib.trim();

        String[] lines = FileUtils.readLinesToStringArray(FileUtils.getResource(mtlLib));

        Material material = new Material();

        for (String line : lines)
        {
            if (line.startsWith("newmtl "))
            {
                materials.put(material.getName(), material);

                material = new Material();
                material.setName(line.split(" ", 2)[1]);
            }
            else if (line.startsWith("Ka "))
                parseMaterialAmbientColor(line, material);

            else if (line.startsWith("Kd "))
                parseMaterialDiffuseColor(line, material);

            else if (line.startsWith("Ks "))
                parseMaterialSpecularColor(line, material);

            else if (line.startsWith("d "))
                material.setDissolve(Float.parseFloat(line.split(" ")[1]));

            else if (line.startsWith("Ns "))
                material.setSpecularPower(Float.parseFloat(line.split(" ")[1]));

            else if (line.startsWith("map_Kd"))
                parseMaterialDiffuseMap(mtlLib, line, material);
        }

        materials.put(material.getName(), material);
    }

    private void parseMaterialAmbientColor(String line, Material material)
    {
        String[] values = line.split(" ");

        float r = Float.parseFloat(values[1]);
        float g = Float.parseFloat(values[2]);
        float b = Float.parseFloat(values[3]);

        material.setAmbient(new Color(r, g, b));
    }

    private void parseMaterialDiffuseColor(String line, Material material)
    {
        String[] values = line.split(" ");

        float r = Float.parseFloat(values[1]);
        float g = Float.parseFloat(values[2]);
        float b = Float.parseFloat(values[3]);

        material.setDiffuse(new Color(r, g, b));
    }

    private void parseMaterialSpecularColor(String line, Material material)
    {
        String[] values = line.split(" ");

        float r = Float.parseFloat(values[1]);
        float g = Float.parseFloat(values[2]);
        float b = Float.parseFloat(values[3]);

        material.setSpecular(new Color(r, g, b));
    }

    private void parseMaterialDiffuseMap(String mtlLib, String line, Material material)
    {
        String mtlDir = mtlLib.substring(0, mtlLib.lastIndexOf('/'));
        String texture = mtlDir + "/" + line.split(" ")[1].trim();
        texture = texture.trim();

        material.setDiffuseMap(Texture.fromResource(texture));
    }

    private void sortFaces()
    {
        // We need to sort the faces so that all the faces with
        // the same material will be together, reducing the loadLWJGL
        // on the batcher.
        faces.sort((f1, f2) ->
        {
            if (f1.getMaterial() == f2.getMaterial())
                return 0;
            else
                return 1;
        });
    }

    @Override
    public void update(float delta)
    {
        // No need to update an OBJ Model, it is static
    }

    @Override
    public void render(float delta, Batcher batcher, Transform transform)
    {
        OBJFace triangle = faces.get(0);
        triangle.getMaterial().getDiffuseMap().bind();

        SilenceEngine.graphics.useMaterial(triangle.getMaterial());

        if (transform != null)
            batcher.applyTransform(transform);

        batcher.begin(Primitive.TRIANGLES);
        {
            for (OBJFace face : faces)
            {
                if (face.getMaterial() != triangle.getMaterial())
                {
                    batcher.end();

                    face.getMaterial().getDiffuseMap().bind();
                    triangle = face;

                    if (transform != null)
                        batcher.applyTransform(transform);

                    batcher.begin(Primitive.TRIANGLES);

                    SilenceEngine.graphics.useMaterial(face.getMaterial());
                }

                batcher.flushOnOverflow(3);

                Vector3 v1 = vertices.get((int) face.getVertex().x - 1);
                Vector3 v2 = vertices.get((int) face.getVertex().y - 1);
                Vector3 v3 = vertices.get((int) face.getVertex().z - 1);

                Color c = face.getMaterial().getDiffuse();

                Vector3 n1 = normals.get((int) face.getNormal().x - 1);
                Vector3 n2 = normals.get((int) face.getNormal().y - 1);
                Vector3 n3 = normals.get((int) face.getNormal().z - 1);

                Vector2 t1 = texCoords.get((int) face.getTexCoord().x);
                Vector2 t2 = texCoords.get((int) face.getTexCoord().y);
                Vector2 t3 = texCoords.get((int) face.getTexCoord().z);

                batcher.vertex(v1);
                batcher.normal(n1);
                batcher.texCoord(t1);
                batcher.color(c.getR(), c.getG(), c.getB(), face.getMaterial().getDissolve());

                batcher.vertex(v2);
                batcher.normal(n2);
                batcher.texCoord(t2);
                batcher.color(c.getR(), c.getG(), c.getB(), face.getMaterial().getDissolve());

                batcher.vertex(v3);
                batcher.normal(n3);
                batcher.texCoord(t3);
                batcher.color(c.getR(), c.getG(), c.getB(), face.getMaterial().getDissolve());
            }
        }
        batcher.end();

        SilenceEngine.graphics.useMaterial(GraphicsEngine.DEFAULT_MATERIAL);
    }

    @Override
    public void dispose()
    {
        // Dispose the textures loaded!
        materials.values().forEach((material) ->
        {
            if (material.getDiffuseMap() != Texture.EMPTY)
                material.getDiffuseMap().dispose();

            if (material.getNormalMap() != Texture.EMPTY)
                material.getNormalMap().dispose();

            if (material.getSpecularMap() != Texture.EMPTY)
                material.getSpecularMap().dispose();
        });
    }
}
