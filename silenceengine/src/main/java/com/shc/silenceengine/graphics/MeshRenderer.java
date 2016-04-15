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

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.opengl.BufferObject;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.VertexArray;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.PrimitiveSize;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;

import java.util.List;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class MeshRenderer
{
    public int vertexLocation    = -1;
    public int normalLocation    = -1;
    public int uvLocation        = -1;
    public int tangentLocation   = -1;
    public int biTangentLocation = -1;

    private VertexArray vao;
    private Primitive   renderMode;

    private BufferObject vertexBuffer;
    private BufferObject normalBuffer;
    private BufferObject uvBuffer;
    private BufferObject tangentBuffer;
    private BufferObject biTangentBuffer;

    private int vertexCount;

    public MeshRenderer(Mesh mesh)
    {
        vao = new VertexArray();
        vao.bind();

        vertexBuffer = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        normalBuffer = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        uvBuffer = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        tangentBuffer = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        biTangentBuffer = new BufferObject(BufferObject.Target.ARRAY_BUFFER);

        vertexCount = mesh.vertices.size();
        renderMode = mesh.renderMode;

        DirectBuffer data = wrapVec3(mesh.vertices);
        vertexBuffer.uploadData(data, BufferObject.Usage.STATIC_DRAW);
        SilenceEngine.io.free(data);

        data = wrapVec3(mesh.normals);
        normalBuffer.uploadData(data, BufferObject.Usage.STATIC_DRAW);
        SilenceEngine.io.free(data);

        data = wrapVec2(mesh.uvs);
        uvBuffer.uploadData(data, BufferObject.Usage.STATIC_DRAW);
        SilenceEngine.io.free(data);

        data = wrapVec3(mesh.tangents);
        tangentBuffer.uploadData(data, BufferObject.Usage.STATIC_DRAW);
        SilenceEngine.io.free(data);

        data = wrapVec3(mesh.biTangents);
        biTangentBuffer.uploadData(data, BufferObject.Usage.STATIC_DRAW);
        SilenceEngine.io.free(data);
    }

    private DirectBuffer wrapVec3(List<Vector3> list)
    {
        DirectBuffer buffer = SilenceEngine.io.create(3 * PrimitiveSize.FLOAT * list.size());

        int index = 0;

        for (Vector3 v : list)
            buffer.writeFloat(index++, v.x)
                    .writeFloat(index + 1, v.y)
                    .writeFloat(index + 2, v.z);

        return buffer;
    }

    private DirectBuffer wrapVec2(List<Vector2> list)
    {
        DirectBuffer buffer = SilenceEngine.io.create(2 * PrimitiveSize.FLOAT * list.size());

        int index = 0;

        for (Vector2 v : list)
            buffer.writeFloat(index++, v.x)
                    .writeFloat(index + 1, v.y);

        return buffer;
    }

    public void render(Material material)
    {
        material.prepareRenderer(this);

        vao.bind();

        if (vertexLocation != -1) vao.pointAttribute(vertexLocation, 3, GL_FLOAT, vertexBuffer);
        if (normalLocation != -1) vao.pointAttribute(normalLocation, 3, GL_FLOAT, normalBuffer);
        if (uvLocation != -1) vao.pointAttribute(uvLocation, 2, GL_FLOAT, uvBuffer);
        if (tangentLocation != -1) vao.pointAttribute(tangentLocation, 3, GL_FLOAT, tangentBuffer);
        if (biTangentLocation != -1) vao.pointAttribute(biTangentLocation, 3, GL_FLOAT, biTangentBuffer);

        if (vertexLocation != -1) vao.enableAttributeArray(vertexLocation);
        if (normalLocation != -1) vao.enableAttributeArray(normalLocation);
        if (uvLocation != -1) vao.enableAttributeArray(uvLocation);
        if (tangentLocation != -1) vao.enableAttributeArray(tangentLocation);
        if (biTangentLocation != -1) vao.enableAttributeArray(biTangentLocation);

        GLContext.drawArrays(vao, renderMode, 0, vertexCount);

        if (vertexLocation != -1) vao.disableAttributeArray(vertexLocation);
        if (normalLocation != -1) vao.disableAttributeArray(normalLocation);
        if (uvLocation != -1) vao.disableAttributeArray(uvLocation);
        if (tangentLocation != -1) vao.disableAttributeArray(tangentLocation);
        if (biTangentLocation != -1) vao.disableAttributeArray(biTangentLocation);

        GLContext.bindVertexArray(null);
    }
}
