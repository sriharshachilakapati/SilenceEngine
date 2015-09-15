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

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.graphics.opengl.BufferObject;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.opengl.VertexArray;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.Vector4;
import com.shc.silenceengine.utils.BufferUtils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * A simple class which eases the rendering of Graphics by batching the vertices, colors, textures to the shaders using
 * VAOs and VBOs. The Batcher is where, all the rendering takes place in SilenceEngine. <p> The batcher is just
 * instantiated like an object, usually only once in a game. Then, you can use the methods defined to add various
 * shapes, or even raw vertices. They get rendered whenever the end method is called. <p> A batcher can also take
 * transforms, and Cameras and render using them. To use them, either pass them to the begin method, or applySelf them
 * manually using the apply[Camera/Transform] methods. <p> After the game completes, before the termination, the dispose
 * method should be called by the user. Better place it in the dispose method of the game.
 *
 * @author Sri Harsha Chilakapati
 * @author Heiko Brumme
 */
public class Batcher
{
    // The no. of components in vertex, normal, color and texcoord
    public static final int NUM_VERTEX_COMPONENTS   = 4;
    public static final int NUM_NORMAL_COMPONENTS   = 4;
    public static final int NUM_COLOR_COMPONENTS    = 4;
    public static final int NUM_TEXCOORD_COMPONENTS = 2;

    // The sizes of the components in bytes
    public static final int SIZE_OF_VERTEX   = Float.BYTES * NUM_VERTEX_COMPONENTS;
    public static final int SIZE_OF_NORMAL   = Float.BYTES * NUM_NORMAL_COMPONENTS;
    public static final int SIZE_OF_COLOR    = Float.BYTES * NUM_COLOR_COMPONENTS;
    public static final int SIZE_OF_TEXCOORD = Float.BYTES * NUM_TEXCOORD_COMPONENTS;

    // The maximum size of the batch, by default is 1024^2 = 10,48,576 vertices
    private int maxBatchSize = 1024 * 1024;

    // The current size of the batch, starts at 4096 vertices
    private int batchSize = 4096;

    // Active state of this batcher
    private boolean active = false;

    // The original buffer addresses (These exist because JEMalloc cannot recognise
    // the buffers once they are mapped with glMapBuffer)
    private long vBufferAddress;
    private long cBufferAddress;
    private long tBufferAddress;
    private long nBufferAddress;

    // The mapped buffers to store the collected data
    private ByteBuffer vBuffer;
    private ByteBuffer cBuffer;
    private ByteBuffer tBuffer;
    private ByteBuffer nBuffer;

    // VAO and VBOs
    private VertexArray  vao;
    private BufferObject vboVert;
    private BufferObject vboCol;
    private BufferObject vboTex;
    private BufferObject vboNorm;

    // VBO index locations in shader
    private int vertexLocation;
    private int colorLocation;
    private int texCoordLocation;
    private int normalLocation;

    // The no. of vertices in the current batch
    private int vertexCount;
    private int colorCount;
    private int texCoordCount;
    private int normalCount;

    // The rendering mode
    private Primitive beginMode;

    // The transform, and projection and view matrices
    private Transform transform;

    /**
     * Creates the Batcher, and initialises OpenGL
     */
    public Batcher()
    {
        // Create the buffers
        vBuffer = BufferUtils.createByteBuffer(batchSize * SIZE_OF_VERTEX);
        nBuffer = BufferUtils.createByteBuffer(batchSize * SIZE_OF_NORMAL);
        cBuffer = BufferUtils.createByteBuffer(batchSize * SIZE_OF_COLOR);
        tBuffer = BufferUtils.createByteBuffer(batchSize * SIZE_OF_TEXCOORD);

        // Get the buffer addresses
        vBufferAddress = BufferUtils.memAddress(vBuffer);
        nBufferAddress = BufferUtils.memAddress(nBuffer);
        cBufferAddress = BufferUtils.memAddress(cBuffer);
        tBufferAddress = BufferUtils.memAddress(tBuffer);

        // Create the transformations
        transform = new Transform();

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

        if (isActive())
            unmapBuffers();

        // Don't increase past the max batch size
        batchSize = Math.min(batchSize, maxBatchSize);

        // Resize the buffers
        vBuffer = BufferUtils.resizeBuffer(vBufferAddress, batchSize * SIZE_OF_VERTEX);
        nBuffer = BufferUtils.resizeBuffer(nBufferAddress, batchSize * SIZE_OF_NORMAL);
        cBuffer = BufferUtils.resizeBuffer(cBufferAddress, batchSize * SIZE_OF_COLOR);
        tBuffer = BufferUtils.resizeBuffer(tBufferAddress, batchSize * SIZE_OF_TEXCOORD);

        // Regather the addresses of the buffers
        vBufferAddress = BufferUtils.memAddress(vBuffer);
        nBufferAddress = BufferUtils.memAddress(nBuffer);
        cBufferAddress = BufferUtils.memAddress(cBuffer);
        tBufferAddress = BufferUtils.memAddress(tBuffer);

        if (isActive())
            mapBuffers();

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

        mapBuffers();
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
        unmapBuffers();

        transform.reset();
    }

    /**
     * Applies a Transform to the batcher data
     *
     * @param t The transform to use
     */
    public void applyTransform(Transform t)
    {
        // Flush the data first
        flush();

        // Apply transform
        transform.applySelf(t);
    }

    /**
     * Flushes the data collected to the GPU.
     */
    public void flush()
    {
        // Avoid doing unnecessary flushes
        if (vertexCount == 0)
            return;

        // Fill the buffers
        fillBuffers();

        // Buffer unmapping
        unmapBuffers();

        Program.CURRENT.prepareFrame();

        // Bind the VAO
        vao.bind();
        vao.enableAttributeArray(vertexLocation);
        vao.enableAttributeArray(colorLocation);
        vao.enableAttributeArray(texCoordLocation);
        vao.enableAttributeArray(normalLocation);

        // Do a rendering
        GL3Context.drawArrays(vao, beginMode, 0, vertexCount);

        // Unbind the VAO
        vao.disableAttributeArray(vertexLocation);
        vao.disableAttributeArray(colorLocation);
        vao.disableAttributeArray(texCoordLocation);
        vao.disableAttributeArray(normalLocation);

        GL3Context.bindVertexArray(null);

        // Clear the vertex count
        vertexCount = 0;
        colorCount = 0;
        texCoordCount = 0;
        normalCount = 0;

        // Map the buffers again
        mapBuffers();
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

    /**
     * Unmaps the buffers and invalidates the pointer to their data store.
     */
    private void unmapBuffers()
    {
        vboVert.unMap();
        vboCol.unMap();
        vboTex.unMap();
        vboNorm.unMap();
    }

    /**
     * Maps the buffers to get their data storage pointers.
     */
    private void mapBuffers()
    {
        if (!vao.isValid())
            vao = new VertexArray();

        vao.bind(true);

        vBuffer = vboVert.map(BufferObject.MapAccess.WRITE_ONLY, vBuffer);
        vao.pointAttribute(vertexLocation, NUM_VERTEX_COMPONENTS, GL_FLOAT, vboVert);

        cBuffer = vboCol.map(BufferObject.MapAccess.WRITE_ONLY, cBuffer);
        vao.pointAttribute(colorLocation, NUM_COLOR_COMPONENTS, GL_FLOAT, vboCol);

        tBuffer = vboTex.map(BufferObject.MapAccess.WRITE_ONLY, tBuffer);
        vao.pointAttribute(texCoordLocation, NUM_TEXCOORD_COMPONENTS, GL_FLOAT, vboTex);

        nBuffer = vboNorm.map(BufferObject.MapAccess.WRITE_ONLY, nBuffer);
        vao.pointAttribute(normalLocation, NUM_NORMAL_COMPONENTS, GL_FLOAT, vboNorm);
    }

    public void applyTransform(Matrix4 m)
    {
        flush();

        transform.applySelf(m);
    }

    public void setTransform(Matrix4 m)
    {
        flush();
        transform.reset().applySelf(m);
    }

    public void vertex(float x, float y)
    {
        vertex(x, y, 0, 1);
    }

    public void vertex(float x, float y, float z, float w)
    {
        if (vertexCount * SIZE_OF_VERTEX >= batchSize)
        {
            if (batchSize >= maxBatchSize)
                // Don't resize more than the max batch size
                flush();
            else
                // Resize the batch by adding a block of vertices at the end
                setBatchSize(batchSize + Math.min(4096, maxBatchSize - batchSize));
        }

        fillBuffers();

        vBuffer.putFloat(vertexCount * NUM_VERTEX_COMPONENTS * Float.BYTES, x)
                .putFloat(vertexCount * NUM_VERTEX_COMPONENTS * Float.BYTES + 4, y)
                .putFloat(vertexCount * NUM_VERTEX_COMPONENTS * Float.BYTES + 8, z)
                .putFloat(vertexCount * NUM_VERTEX_COMPONENTS * Float.BYTES + 12, w);

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
        vertex(v.getX(), v.getY(), 0, 1);
    }

    public void vertex(Vector3 v)
    {
        vertex(v.getX(), v.getY(), v.getZ(), 1);
    }

    public void vertex(Vector4 v)
    {
        vertex(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public void color(Color c)
    {
        color(c.getR(), c.getG(), c.getB(), c.getA());
    }

    public void color(Vector4 c)
    {
        color(c.getR(), c.getG(), c.getB(), c.getA());
    }

    public void color(float r, float g, float b, float a)
    {
        // Add the specified color
        cBuffer.putFloat(colorCount * NUM_COLOR_COMPONENTS * Float.BYTES, r)
                .putFloat(colorCount * NUM_COLOR_COMPONENTS * Float.BYTES + 4, g)
                .putFloat(colorCount * NUM_COLOR_COMPONENTS * Float.BYTES + 8, b)
                .putFloat(colorCount * NUM_COLOR_COMPONENTS * Float.BYTES + 12, a);

        colorCount++;
    }

    public void texCoord(Vector2 v)
    {
        texCoord(v.getX(), v.getY());
    }

    public void texCoord(float u, float v)
    {
        // Add the specified texcoord
        tBuffer.putFloat(texCoordCount * NUM_TEXCOORD_COMPONENTS * Float.BYTES, u)
                .putFloat(texCoordCount * NUM_TEXCOORD_COMPONENTS * Float.BYTES + 4, v);

        texCoordCount++;
    }

    public void normal(float x, float y, float z)
    {
        normal(x, y, z, 0);
    }

    public void normal(float x, float y, float z, float w)
    {
        nBuffer.putFloat(normalCount * NUM_NORMAL_COMPONENTS * Float.BYTES, x)
                .putFloat(normalCount * NUM_NORMAL_COMPONENTS * Float.BYTES + 4, y)
                .putFloat(normalCount * NUM_NORMAL_COMPONENTS * Float.BYTES + 8, z)
                .putFloat(normalCount * NUM_NORMAL_COMPONENTS * Float.BYTES + 12, w);

        normalCount++;
    }

    public void normal(Vector3 n)
    {
        normal(n.getX(), n.getY(), n.getZ(), 0);
    }

    public void drawTexture2d(Texture texture, Vector2 p)
    {
        drawTexture2d(texture, p, Color.TRANSPARENT);
    }

    /* Draw Texture */

    public void drawTexture2d(Texture texture, Vector2 p, Color color)
    {
        Texture current = Texture.CURRENT;

        texture.bind();
        begin();
        // First triangle
        {
            vertex(p.getX(), p.getY(), 0, 1);
            color(color);
            texCoord(texture.getMinU(), texture.getMinV());

            vertex(p.getX() + texture.getWidth(), p.getY(), 0, 1);
            color(color);
            texCoord(texture.getMaxU(), texture.getMinV());

            vertex(p.getX(), p.getY() + texture.getHeight(), 0, 1);
            color(color);
            texCoord(texture.getMinU(), texture.getMaxV());
        }
        // Second triangle
        {
            vertex(p.getX() + texture.getWidth(), p.getY(), 0, 1);
            color(color);
            texCoord(texture.getMaxU(), texture.getMinV());

            vertex(p.getX(), p.getY() + texture.getHeight(), 0, 1);
            color(color);
            texCoord(texture.getMinU(), texture.getMaxV());

            vertex(p.getX() + texture.getWidth(), p.getY() + texture.getHeight(), 0, 1);
            color(color);
            texCoord(texture.getMaxU(), texture.getMaxV());
        }

        end();
        current.bind();
    }

    public Transform getTransform()
    {
        return transform;
    }

    public void setTransform(Transform t)
    {
        flush();
        transform.set(t);
    }

    public void dispose()
    {
        if (isActive())
            unmapBuffers();

        GL3Context.bindVertexArray(null);
        vao.dispose();
        GL3Context.bindVertexBuffer(null);
        vboVert.dispose();
        vboCol.dispose();
        vboTex.dispose();
        vboNorm.dispose();

        BufferUtils.freeBuffer(vBufferAddress);
        BufferUtils.freeBuffer(nBufferAddress);
        BufferUtils.freeBuffer(cBufferAddress);
        BufferUtils.freeBuffer(tBufferAddress);
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
