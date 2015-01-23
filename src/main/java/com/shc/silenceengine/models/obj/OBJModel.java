package com.shc.silenceengine.models.obj;

import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.models.Material;
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

    private List<OBJFace> faces;
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
        float vt1 = Float.parseFloat(values[1].split("/")[1]);
        float vt2 = Float.parseFloat(values[2].split("/")[1]);
        float vt3 = Float.parseFloat(values[3].split("/")[1]);

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
        String mtllib = objDir + mtlLine.split(" ")[1].trim();
        mtllib = mtllib.trim();

        String[] lines = FileUtils.readLinesToStringArray(FileUtils.getResource(mtllib));

        Material material = null;

        for (String line : lines)
        {
            if (line.startsWith("newmtl "))
            {
                if (material != null)
                    materials.put(material.getName(), material);

                material = new Material();
                material.setName(line.split(" ", 2)[1]);
            }
            else if (line.startsWith("Kd "))
            {

            }
        }
    }

    @Override
    public void update(float delta)
    {
        // No need to update an OBJ Model, it is static
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        OBJFace face = faces.get(0);
        face.getMaterial().getDiffuseMap().bind();

        batcher.applyTransform(getTransform());
        batcher.begin(Primitive.TRIANGLES);
        {
            for (OBJFace triangle : faces)
            {
                if (face.getMaterial() != triangle.getMaterial())
                {
                    batcher.end();

                    triangle.getMaterial().getDiffuseMap().bind();
                    face = triangle;

                    batcher.applyTransform(getTransform());
                    batcher.begin(Primitive.TRIANGLES);
                }

                // TODO: FIX MODEL CLASSES
            }
        }
        batcher.end();
    }
}
