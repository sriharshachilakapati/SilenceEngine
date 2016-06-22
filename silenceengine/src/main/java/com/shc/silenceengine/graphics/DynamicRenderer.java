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
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.VertexArray;
import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.PrimitiveSize;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.Vector4;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 * @author Heiko Brumme
 */
public class DynamicRenderer
{
    // The no. of components in vertex, normal, color and texcoord
    public static final int NUM_VERTEX_COMPONENTS   = 4;
    public static final int NUM_NORMAL_COMPONENTS   = 4;
    public static final int NUM_COLOR_COMPONENTS    = 4;
    public static final int NUM_TEXCOORD_COMPONENTS = 2;

    // The sizes of the components in bytes
    public static final int SIZE_OF_VERTEX   = PrimitiveSize.FLOAT * NUM_VERTEX_COMPONENTS;
    public static final int SIZE_OF_NORMAL   = PrimitiveSize.FLOAT * NUM_NORMAL_COMPONENTS;
    public static final int SIZE_OF_COLOR    = PrimitiveSize.FLOAT * NUM_COLOR_COMPONENTS;
    public static final int SIZE_OF_TEXCOORD = PrimitiveSize.FLOAT * NUM_TEXCOORD_COMPONENTS;

    // The maximum size of the batch, by default is 1024^2 = 10,48,576 vertices
    private int maxBatchSize;

    // The current size of the batch, starts at 4096 vertices
    private int batchSize;

    // Active state of this batcher
    private boolean active = false;

    // The mapped buffers to store the collected data
    private DirectBuffer vBuffer;
    private DirectBuffer cBuffer;
    private DirectBuffer tBuffer;
    private DirectBuffer nBuffer;

    // VAO and VBOs
    private VertexArray  vao;
    private BufferObject vboVert;
    private BufferObject vboCol;
    private BufferObject vboTex;
    private BufferObject vboNorm;

    // VBO index locations in shader
    private int vertexLocation   = -1;
    private int colorLocation    = -1;
    private int texCoordLocation = -1;
    private int normalLocation   = -1;

    // The no. of vertices in the current batch
    private int vertexCount;
    private int colorCount;
    private int texCoordCount;
    private int normalCount;

    // The rendering mode
    private Primitive beginMode;

    public DynamicRenderer()
    {
        this(4096, 1024 * 1024);
    }

    public DynamicRenderer(int batchSize)
    {
        this(batchSize, batchSize);
    }

    /**
     * Creates the DynamicRenderer, and initialises OpenGL
     */
    public DynamicRenderer(int batchSize, int maxBatchSize)
    {
        this.batchSize = batchSize;
        this.maxBatchSize = maxBatchSize;

        // Create the buffers
        vBuffer = SilenceEngine.io.create(batchSize * SIZE_OF_VERTEX);
        nBuffer = SilenceEngine.io.create(batchSize * SIZE_OF_NORMAL);
        cBuffer = SilenceEngine.io.create(batchSize * SIZE_OF_COLOR);
        tBuffer = SilenceEngine.io.create(batchSize * SIZE_OF_TEXCOORD);

        // Initialise OpenGL handles
        initGLHandles();
    }

    public int getMaxBatchSize()
    {
        return maxBatchSize;
    }

    public void setMaxBatchSize(int maxBatchSize)
    {
        if (maxBatchSize > this.maxBatchSize)
        {
            // Resize the VBOs data store
            vboVert.uploadData(maxBatchSize * SIZE_OF_VERTEX, BufferObject.Usage.STREAM_DRAW);
            vboNorm.uploadData(maxBatchSize * SIZE_OF_NORMAL, BufferObject.Usage.STREAM_DRAW);
            vboCol.uploadData(maxBatchSize * SIZE_OF_COLOR, BufferObject.Usage.STREAM_DRAW);
            vboTex.uploadData(maxBatchSize * SIZE_OF_TEXCOORD, BufferObject.Usage.STREAM_DRAW);
        }

        this.maxBatchSize = maxBatchSize;
    }

    public int getBatchSize()
    {
        return batchSize;
    }

    public void setBatchSize(int batchSize)
    {
        if (batchSize == this.batchSize)
            return;

        // Don't increase past the max batch size
        batchSize = Math.min(batchSize, maxBatchSize);

        // Free old buffers
        SilenceEngine.io.free(vBuffer);
        SilenceEngine.io.free(nBuffer);
        SilenceEngine.io.free(cBuffer);
        SilenceEngine.io.free(tBuffer);

        // Create new buffers
        vBuffer = SilenceEngine.io.create(batchSize * SIZE_OF_VERTEX);
        nBuffer = SilenceEngine.io.create(batchSize * SIZE_OF_NORMAL);
        cBuffer = SilenceEngine.io.create(batchSize * SIZE_OF_COLOR);
        tBuffer = SilenceEngine.io.create(batchSize * SIZE_OF_TEXCOORD);

        this.batchSize = batchSize;
    }

    /**
     * Initialises VAOs and VBOs and creates the data store to store the entire batch.
     */
    private void initGLHandles()
    {
        // Create a VAO
        vao = new VertexArray();
        vao.bind();

        // Create VBOs
        vboVert = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        vboCol = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        vboTex = new BufferObject(BufferObject.Target.ARRAY_BUFFER);
        vboNorm = new BufferObject(BufferObject.Target.ARRAY_BUFFER);

        // Initialize vertex-buffer
        vboVert.bind();
        vboVert.uploadData(maxBatchSize * SIZE_OF_VERTEX, BufferObject.Usage.STREAM_DRAW);

        // Initialize color-buffer
        vboCol.bind();
        vboCol.uploadData(maxBatchSize * SIZE_OF_COLOR, BufferObject.Usage.STREAM_DRAW);

        // Initialize texcoord-buffer
        vboTex.bind();
        vboTex.uploadData(maxBatchSize * SIZE_OF_TEXCOORD, BufferObject.Usage.STREAM_DRAW);

        // Initialize normal-buffer
        vboNorm.bind();
        vboNorm.uploadData(maxBatchSize * SIZE_OF_NORMAL, BufferObject.Usage.STREAM_DRAW);
    }

    /**
     * Begins the batcher and marks it active.
     *
     * @param beginMode The Mode to begin rendering with
     */
    public void begin(Primitive beginMode)
    {
        if (active)
            throw new IllegalStateException("Batcher Already Active!");

        active = true;

        vertexCount = 0;
        colorCount = 0;
        texCoordCount = 0;
        normalCount = 0;

        this.beginMode = beginMode;
    }

    public void begin()
    {
        begin(Primitive.TRIANGLES);
    }

    /**
     * Ends the batcher by flushing all the data to the OpenGL
     */
    public void end()
    {
        if (!active)
            throw new IllegalStateException("Batcher not Active!");

        active = false;

        flush();
    }

    /**
     * Flushes the data collected to the GPU.
     */
    public void flush()
    {
        // Avoid doing unnecessary flushes
        if (vertexCount == 0)
            return;

        if (vertexLocation == -1)
            return;

        // Fill the buffers
        fillBuffers();

        Program.CURRENT.prepareFrame();

        // Bind the VAO
        vao.bind();

        vao.enableAttributeArray(vertexLocation);
        if (colorLocation != -1) vao.enableAttributeArray(colorLocation);
        if (texCoordLocation != -1) vao.enableAttributeArray(texCoordLocation);
        if (normalLocation != -1) vao.enableAttributeArray(normalLocation);

        // Setup the buffers
        setupBuffers();

        // Do a rendering
        GLContext.drawArrays(vao, beginMode, 0, vertexCount);

        // Unbind the VAO
        vao.disableAttributeArray(vertexLocation);
        if (colorLocation != -1) vao.disableAttributeArray(colorLocation);
        if (texCoordLocation != -1) vao.disableAttributeArray(texCoordLocation);
        if (normalLocation != -1) vao.disableAttributeArray(normalLocation);

        GLContext.bindVertexArray(null);

        // Clear the vertex count
        vertexCount = 0;
        colorCount = 0;
        texCoordCount = 0;
        normalCount = 0;
    }

    private void setupBuffers()
    {
        vao.bind();

        vboVert.bind();
        vboVert.uploadSubData(vBuffer, 0);

        vboCol.bind();
        vboCol.uploadSubData(cBuffer, 0);

        vboNorm.bind();
        vboNorm.uploadSubData(nBuffer, 0);

        vboTex.bind();
        vboTex.uploadSubData(tBuffer, 0);

        if (vertexLocation != -1) vao.pointAttribute(vertexLocation, NUM_VERTEX_COMPONENTS, GL_FLOAT, vboVert);
        if (colorLocation != -1) vao.pointAttribute(colorLocation, NUM_COLOR_COMPONENTS, GL_FLOAT, vboCol);
        if (normalLocation != -1) vao.pointAttribute(normalLocation, NUM_NORMAL_COMPONENTS, GL_FLOAT, vboNorm);
        if (texCoordLocation != -1) vao.pointAttribute(texCoordLocation, NUM_TEXCOORD_COMPONENTS, GL_FLOAT, vboTex);
    }

    private void fillBuffers()
    {
        // Determine the fill color
        Color col = texCoordCount == vertexCount ? Color.TRANSPARENT : Color.WHITE;

        // Fill the color buffers
        while (colorCount < vertexCount)
            color(col);

        // Fill the texcoord buffers
        while (texCoordCount < vertexCount)
            texCoord(0, 0);

        // Fill the normal buffers
        while (normalCount < vertexCount)
            normal(0, 0, 0, 0);
    }

    public void vertex(float x, float y)
    {
        vertex(x, y, 0, 1);
    }

    public void vertex(float x, float y, float z, float w)
    {
        if (vertexCount >= batchSize)
        {
            if (batchSize >= maxBatchSize)
                // Don't resized more than the max batch size
                flush();
            else
                // Resize the batch by adding a block of vertices at the end
                setBatchSize(batchSize + Math.min(4096, maxBatchSize - batchSize));
        }

        fillBuffers();

        vBuffer.writeFloat(vertexCount * NUM_VERTEX_COMPONENTS * PrimitiveSize.FLOAT, x)
                .writeFloat(vertexCount * NUM_VERTEX_COMPONENTS * PrimitiveSize.FLOAT + 4, y)
                .writeFloat(vertexCount * NUM_VERTEX_COMPONENTS * PrimitiveSize.FLOAT + 8, z)
                .writeFloat(vertexCount * NUM_VERTEX_COMPONENTS * PrimitiveSize.FLOAT + 12, w);

        vertexCount++;
    }

    public void flushOnOverflow(int capacity)
    {
        if (vertexCount + capacity >= maxBatchSize)
            flush();
    }

    public void vertex(float x, float y, float z)
    {
        vertex(x, y, z, 1);
    }

    public void vertex(Vector2 v)
    {
        vertex(v.x, v.y, 0, 1);
    }

    public void vertex(Vector3 v)
    {
        vertex(v.x, v.y, v.z, 1);
    }

    public void vertex(Vector4 v)
    {
        vertex(v.x, v.y, v.z, v.w);
    }

    public void color(Color c)
    {
        color(c.r, c.g, c.b, c.a);
    }

    public void color(Vector4 c)
    {
        color(c.x, c.y, c.z, c.w);
    }

    public void color(float r, float g, float b, float a)
    {
        // Add the specified color
        cBuffer.writeFloat(colorCount * NUM_COLOR_COMPONENTS * PrimitiveSize.FLOAT, r)
                .writeFloat(colorCount * NUM_COLOR_COMPONENTS * PrimitiveSize.FLOAT + 4, g)
                .writeFloat(colorCount * NUM_COLOR_COMPONENTS * PrimitiveSize.FLOAT + 8, b)
                .writeFloat(colorCount * NUM_COLOR_COMPONENTS * PrimitiveSize.FLOAT + 12, a);

        colorCount++;
    }

    public void texCoord(Vector2 v)
    {
        texCoord(v.x, v.y);
    }

    public void texCoord(float u, float v)
    {
        // Add the specified texcoord
        tBuffer.writeFloat(texCoordCount * NUM_TEXCOORD_COMPONENTS * PrimitiveSize.FLOAT, u)
                .writeFloat(texCoordCount * NUM_TEXCOORD_COMPONENTS * PrimitiveSize.FLOAT + 4, v);

        texCoordCount++;
    }

    public void normal(float x, float y, float z)
    {
        normal(x, y, z, 0);
    }

    public void normal(float x, float y, float z, float w)
    {
        nBuffer.writeFloat(normalCount * NUM_NORMAL_COMPONENTS * PrimitiveSize.FLOAT, x)
                .writeFloat(normalCount * NUM_NORMAL_COMPONENTS * PrimitiveSize.FLOAT + 4, y)
                .writeFloat(normalCount * NUM_NORMAL_COMPONENTS * PrimitiveSize.FLOAT + 8, z)
                .writeFloat(normalCount * NUM_NORMAL_COMPONENTS * PrimitiveSize.FLOAT + 12, w);

        normalCount++;
    }

    public void normal(Vector3 n)
    {
        normal(n.x, n.y, n.z, 0);
    }

    public void dispose()
    {
        GLContext.bindVertexArray(null);
        vao.dispose();

        GLContext.bindVertexBuffer(null);
        vboVert.dispose();
        vboCol.dispose();
        vboTex.dispose();
        vboNorm.dispose();

        SilenceEngine.io.free(vBuffer);
        SilenceEngine.io.free(nBuffer);
        SilenceEngine.io.free(cBuffer);
        SilenceEngine.io.free(tBuffer);
    }

    public int getVertexLocation()
    {
        return vertexLocation;
    }

    public void setVertexLocation(int vertexLocation)
    {
        this.vertexLocation = vertexLocation;
    }

    public int getColorLocation()
    {
        return colorLocation;
    }

    public void setColorLocation(int colorLocation)
    {
        this.colorLocation = colorLocation;
    }

    public int getTexCoordLocation()
    {
        return texCoordLocation;
    }

    public void setTexCoordLocation(int texCoordLocation)
    {
        this.texCoordLocation = texCoordLocation;
    }

    public int getNormalLocation()
    {
        return normalLocation;
    }

    public void setNormalLocation(int normalLocation)
    {
        this.normalLocation = normalLocation;
    }

    public boolean isActive()
    {
        return active;
    }

    public Primitive getBeginMode()
    {
        return beginMode;
    }
}
