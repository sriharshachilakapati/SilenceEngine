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

import com.shc.silenceengine.core.IResource;
import com.shc.silenceengine.graphics.opengl.BufferObject;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.VertexArray;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.PrimitiveSize;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;

import java.util.List;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.GL_FLOAT;

/**
 * @author Sri Harsha Chilakapati
 */
public class MeshRenderer implements IResource
{
    public final VertexArray vertexArray;

    public final boolean hasColors;
    public final boolean hasNormals;
    public final boolean hasTexCoords;
    public final boolean hasTangents;
    public final boolean hasBiTangents;

    public final BufferObject vertexBuffer;
    public final BufferObject colorBuffer;
    public final BufferObject normalBuffer;
    public final BufferObject texCoordBuffer;
    public final BufferObject tangentBuffer;
    public final BufferObject biTangentBuffer;

    public int vertexLocation    = -1;
    public int normalLocation    = -1;
    public int uvLocation        = -1;
    public int tangentLocation   = -1;
    public int biTangentLocation = -1;

    public final Primitive renderMode;
    public final int       vertexCount;

    public MeshRenderer(DynamicRenderer renderer)
    {
        vertexArray = new VertexArray();
        vertexArray.bind();

        hasColors = renderer.getColorLocation() != -1;
        hasNormals = renderer.getNormalLocation() != -1;
        hasTexCoords = renderer.getTexCoordLocation() != -1;

        hasTangents = hasBiTangents = false;

        vertexBuffer = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        colorBuffer = hasColors ? new BufferObject(BufferObject.Target.ARRAY_BUFFER) : null;
        normalBuffer = hasNormals ? new BufferObject(BufferObject.Target.ARRAY_BUFFER) : null;
        texCoordBuffer = hasTexCoords ? new BufferObject(BufferObject.Target.ARRAY_BUFFER) : null;

        tangentBuffer = biTangentBuffer = null;

        vertexCount = renderer.getVertexCount();

        vertexBuffer.uploadData(vertexCount * DynamicRenderer.SIZE_OF_VERTEX, BufferObject.Usage.STATIC_DRAW);
        vertexBuffer.uploadSubData(renderer.getVBuffer(), 0, vertexCount * DynamicRenderer.SIZE_OF_VERTEX);

        if (hasNormals)
        {
            normalBuffer.uploadData(vertexCount * DynamicRenderer.SIZE_OF_NORMAL, BufferObject.Usage.STATIC_DRAW);
            normalBuffer.uploadSubData(renderer.getVBuffer(), 0, vertexCount * DynamicRenderer.SIZE_OF_NORMAL);
        }

        if (hasTexCoords)
        {
            texCoordBuffer.uploadData(vertexCount * DynamicRenderer.SIZE_OF_TEXCOORD, BufferObject.Usage.STATIC_DRAW);
            texCoordBuffer.uploadSubData(renderer.getVBuffer(), 0, vertexCount * DynamicRenderer.SIZE_OF_TEXCOORD);
        }

        if (hasColors)
        {
            colorBuffer.uploadData(vertexCount * DynamicRenderer.SIZE_OF_COLOR, BufferObject.Usage.STATIC_DRAW);
            colorBuffer.uploadSubData(renderer.getVBuffer(), 0, vertexCount * DynamicRenderer.SIZE_OF_COLOR);
        }

        renderMode = renderer.getBeginMode();
    }

    public MeshRenderer(Mesh mesh)
    {
        vertexArray = new VertexArray();
        vertexArray.bind();

        hasColors = !mesh.colors.isEmpty();
        hasNormals = !mesh.normals.isEmpty();
        hasTexCoords = !mesh.uvs.isEmpty();
        hasTangents = !mesh.tangents.isEmpty();
        hasBiTangents = !mesh.biTangents.isEmpty();

        vertexBuffer = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        colorBuffer = hasColors ? new BufferObject(BufferObject.Target.ARRAY_BUFFER) : null;
        normalBuffer = hasNormals ? new BufferObject(BufferObject.Target.ARRAY_BUFFER) : null;
        texCoordBuffer = hasTexCoords ? new BufferObject(BufferObject.Target.ARRAY_BUFFER) : null;
        tangentBuffer = hasTangents ? new BufferObject(BufferObject.Target.ARRAY_BUFFER) : null;
        biTangentBuffer = hasBiTangents ? new BufferObject(BufferObject.Target.ARRAY_BUFFER) : null;

        uploadTo4Layout(mesh.vertices, vertexBuffer, 1);
        if (hasNormals) uploadTo4Layout(mesh.normals, normalBuffer, 0);
        if (hasTangents) uploadTo4Layout(mesh.tangents, tangentBuffer, 0);
        if (hasBiTangents) uploadTo4Layout(mesh.biTangents, biTangentBuffer, 0);
        if (hasTexCoords) uploadTo2Layout(mesh.uvs, tangentBuffer);
        if (hasColors) uploadTo4Layout(mesh.colors, colorBuffer);

        vertexCount = mesh.vertices.size();

        renderMode = mesh.renderMode;
    }

    private void uploadTo4Layout(List<Vector3> data, BufferObject bufferObject, int w)
    {
        DirectBuffer buffer = DirectBuffer.create(data.size() * 4 * PrimitiveSize.FLOAT);

        int i = 0;

        for (Vector3 v : data)
            buffer.writeFloat(i++, v.x)
                    .writeFloat(i++, v.y)
                    .writeFloat(i++, v.z)
                    .writeFloat(i++, w);

        bufferObject.uploadData(buffer, BufferObject.Usage.STATIC_DRAW);

        DirectBuffer.free(buffer);
    }

    private void uploadTo4Layout(List<Color> data, BufferObject bufferObject)
    {
        DirectBuffer buffer = DirectBuffer.create(data.size() * 4 * PrimitiveSize.FLOAT);

        int i = 0;

        for (Color c : data)
            buffer.writeFloat(i++, c.r)
                    .writeFloat(i++, c.g)
                    .writeFloat(i++, c.b)
                    .writeFloat(i++, c.a);

        bufferObject.uploadData(buffer, BufferObject.Usage.STATIC_DRAW);

        DirectBuffer.free(buffer);
    }

    private void uploadTo2Layout(List<Vector2> data, BufferObject bufferObject)
    {
        DirectBuffer buffer = DirectBuffer.create(data.size() * 2 * PrimitiveSize.FLOAT);

        int i = 0;

        for (Vector2 v : data)
            buffer.writeFloat(i++, v.x)
                    .writeFloat(i++, v.y);

        bufferObject.uploadData(buffer, BufferObject.Usage.STATIC_DRAW);

        DirectBuffer.free(buffer);
    }

    public void render(Material material)
    {
        material.prepareRenderer(this);

        vertexArray.bind();

        if (vertexLocation != -1) vertexArray.pointAttribute(vertexLocation, 4, GL_FLOAT, vertexBuffer);
        if (normalLocation != -1) vertexArray.pointAttribute(normalLocation, 4, GL_FLOAT, normalBuffer);
        if (uvLocation != -1) vertexArray.pointAttribute(uvLocation, 2, GL_FLOAT, texCoordBuffer);
        if (tangentLocation != -1) vertexArray.pointAttribute(tangentLocation, 4, GL_FLOAT, tangentBuffer);
        if (biTangentLocation != -1) vertexArray.pointAttribute(biTangentLocation, 4, GL_FLOAT, biTangentBuffer);

        if (vertexLocation != -1) vertexArray.enableAttributeArray(vertexLocation);
        if (normalLocation != -1) vertexArray.enableAttributeArray(normalLocation);
        if (uvLocation != -1) vertexArray.enableAttributeArray(uvLocation);
        if (tangentLocation != -1) vertexArray.enableAttributeArray(tangentLocation);
        if (biTangentLocation != -1) vertexArray.enableAttributeArray(biTangentLocation);

        GLContext.drawArrays(vertexArray, renderMode, 0, vertexCount);

        if (vertexLocation != -1) vertexArray.disableAttributeArray(vertexLocation);
        if (normalLocation != -1) vertexArray.disableAttributeArray(normalLocation);
        if (uvLocation != -1) vertexArray.disableAttributeArray(uvLocation);
        if (tangentLocation != -1) vertexArray.disableAttributeArray(tangentLocation);
        if (biTangentLocation != -1) vertexArray.disableAttributeArray(biTangentLocation);

        GLContext.bindVertexArray(null);
    }

    public void dispose()
    {
        vertexArray.dispose();
        vertexBuffer.dispose();
        if (hasColors) colorBuffer.dispose();
        if (hasNormals) normalBuffer.dispose();
        if (hasTexCoords) texCoordBuffer.dispose();
        if (hasTangents) tangentBuffer.dispose();
        if (hasBiTangents) biTangentBuffer.dispose();
    }
}
