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

import com.shc.silenceengine.core.IUpdatable;
import com.shc.silenceengine.graphics.Animation;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.backend.lwjgl3.opengl.Texture;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class BillBoardModel extends Model implements IUpdatable
{
    private Sprite sprite;

    public BillBoardModel(Texture texture)
    {
        this(new Sprite(texture));
    }

    public BillBoardModel(Texture texture, float width, float height)
    {
        this(new Sprite(texture), width, height);
    }

    public BillBoardModel(Animation animation)
    {
        this(new Sprite(animation));
    }

    public BillBoardModel(Animation animation, float width, float height)
    {
        this(new Sprite(animation), width, height);
    }

    public BillBoardModel(Sprite sprite)
    {
        this(sprite, sprite.getTexture().getWidth(), sprite.getTexture().getHeight());
    }

    public BillBoardModel(Sprite sprite, float width, float height)
    {
        this.sprite = sprite;

        // Create a new mesh
        Mesh mesh = new Mesh();
        mesh.getMaterial().setDiffuseMap(sprite.getTexture());

        // Create mesh vertices
        mesh.getVertices().add(new Vector3(-width / 2, +height / 2, 0));
        mesh.getVertices().add(new Vector3(+width / 2, +height / 2, 0));
        mesh.getVertices().add(new Vector3(+width / 2, -height / 2, 0));
        mesh.getVertices().add(new Vector3(-width / 2, -height / 2, 0));

        // Create mesh normals and texture coordinates
        for (int i = 0; i < 4; i++)
        {
            mesh.getNormals().add(new Vector3(0, 0, 1));
            mesh.getTexcoords().add(new Vector2(0, 0));
        }

        // Create the mesh faces
        Face face1 = new Face();
        Face face2 = new Face();

        face1.vertexIndex.set(face1.normalIndex.set(face1.texcoordIndex.set(0, 1, 3)));
        face2.vertexIndex.set(face2.normalIndex.set(face2.texcoordIndex.set(1, 2, 3)));

        // Add the faces to the mesh
        mesh.getFaces().add(face1);
        mesh.getFaces().add(face2);

        // Add the mesh to the model
        getMeshes().add(mesh);
    }

    public void update(float delta)
    {
        sprite.update(delta);

        Texture texture = sprite.getTexture();
        Mesh mesh = getMeshes().get(0);

        mesh.getMaterial().setDiffuseMap(texture);

        // Set up the texture coordinates too
        mesh.getTexcoords().get(0).set(texture.getMinU(), texture.getMinV());
        mesh.getTexcoords().get(1).set(texture.getMaxU(), texture.getMinV());
        mesh.getTexcoords().get(2).set(texture.getMaxU(), texture.getMaxV());
        mesh.getTexcoords().get(3).set(texture.getMinU(), texture.getMaxV());
    }
}
