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

import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Material;
import com.shc.silenceengine.backend.lwjgl3.opengl.Texture;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
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
    private List<Vector2> texcoords;

    private Map<String, Material> materials;

    public OBJModel(FilePath path)
    {
        super();

        vertices = new ArrayList<>();
        normals = new ArrayList<>();
        texcoords = new ArrayList<>();
        materials = new HashMap<>();

        vertices.add(new Vector3());
        normals.add(new Vector3());
        texcoords.add(new Vector2());

        parseOBJModel(path);
    }

    public OBJModel(String path)
    {
        this(FilePath.getResourceFile(path));
    }

    private void parseOBJModel(FilePath path)
    {
        Material material;
        Mesh mesh = null;

        String[] lines = FileUtils.readLinesToStringArray(path);

        if (lines != null)
        {
            for (String line : lines)
            {
                if (line.startsWith("v "))
                    parseVertex(line);

                else if (line.startsWith("vn "))
                    parseNormal(line);

                else if (line.startsWith("vt "))
                    parseTextureCoords(line);

                else if (line.startsWith("f "))
                    parseFace(line, mesh);

                else if (line.startsWith("usemtl "))
                {
                    material = materials.get(line.replaceAll("usemtl ", "").trim());
                    if (mesh != null) getMeshes().add(mesh);
                    mesh = new Mesh();
                    mesh.setMaterial(material);
                }

                else if (line.startsWith("mtllib "))
                    parseMaterialLib(path, line);
            }
        }

        getMeshes().add(mesh);

        vertices = null;
        normals = null;
        texcoords = null;
        materials = null;
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

        texcoords.add(new Vector2(x, 1 - y));
    }

    private void parseFace(String line, Mesh mesh)
    {
        String[] values = line.split(" ");

        // Parse the vertex indices
        float v1 = Float.parseFloat(values[1].split("/")[0]);
        float v2 = Float.parseFloat(values[2].split("/")[0]);
        float v3 = Float.parseFloat(values[3].split("/")[0]);

        // Parse the texture coord indices
        float vt1, vt2, vt3;

        Material material = mesh.getMaterial();

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

        // Parse the normal indices
        float vn1 = Float.parseFloat(values[1].split("/")[2]);
        float vn2 = Float.parseFloat(values[2].split("/")[2]);
        float vn3 = Float.parseFloat(values[3].split("/")[2]);

        // Copy the vertices into the mesh
        mesh.getVertices().add(vertices.get((int) v1));
        mesh.getVertices().add(vertices.get((int) v2));
        mesh.getVertices().add(vertices.get((int) v3));

        mesh.getNormals().add(normals.get((int) vn1));
        mesh.getNormals().add(normals.get((int) vn2));
        mesh.getNormals().add(normals.get((int) vn3));

        mesh.getTexcoords().add(texcoords.get((int) vt1));
        mesh.getTexcoords().add(texcoords.get((int) vt2));
        mesh.getTexcoords().add(texcoords.get((int) vt3));

        // Create the face and compute the new indices
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
    }

    private void parseMaterialLib(FilePath objFile, String mtlLine)
    {
        FilePath mtlLib = objFile.getParent().getChild(mtlLine.split(" ", 2)[1].trim());

        String[] lines = FileUtils.readLinesToStringArray(mtlLib);

        Material material = null;

        if (lines != null)
        {
            for (String line : lines)
            {
                if (line.startsWith("newmtl "))
                {
                    if (material != null)
                        materials.put(material.getName(), material);

                    material = new Material();
                    material.setName(line.split(" ", 2)[1]);
                }

                if (line.startsWith("Ka "))
                    parseMaterialAmbientColor(line, material);

                if (line.startsWith("Kd "))
                    parseMaterialDiffuseColor(line, material);

                if (line.startsWith("Ks "))
                    parseMaterialSpecularColor(line, material);

                if (line.startsWith("d "))
                    if (material != null)
                        material.setDissolve(Float.parseFloat(line.split(" ")[1]));

                if (line.startsWith("Ns "))
                    if (material != null)
                        material.setSpecularPower(Float.parseFloat(line.split(" ")[1]));

                if (line.startsWith("map_Kd"))
                    if (material != null)
                        parseMaterialDiffuseMap(mtlLib, line, material);
            }
        }

        if (material != null)
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

    private void parseMaterialDiffuseMap(FilePath mtlLib, String line, Material material)
    {
        String fileName = line.split(" ", 2)[1].trim();
        FilePath filePath = fileName.contains(":") ? FilePath.getExternalFile(fileName)     // Absolute File
                                                   : mtlLib.getParent().getChild(fileName); // Relative File

        material.setDiffuseMap(Texture.fromFilePath(filePath));
    }
}
