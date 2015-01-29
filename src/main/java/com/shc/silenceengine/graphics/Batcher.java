package com.shc.silenceengine.graphics;

import com.shc.silenceengine.graphics.opengl.BufferObject;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.opengl.VertexArray;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.Vector4;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * A simple class which eases the rendering of Graphics by batching
 * the vertices, colors, textures to the shaders using VAOs and VBOs.
 * The Batcher is where, all the rendering takes place in SilenceEngine.
 * <p>
 * The batcher is just instantiated like an object, usually only once
 * in a game. Then, you can use the methods defined to add various
 * shapes, or even raw vertices. They get rendered whenever the end
 * method is called.
 * <p>
 * A batcher can also take transforms, and Cameras and render using
 * them. To use them, either pass them to the begin method, or apply
 * them manually using the apply[Camera/Transform] methods.
 * <p>
 * After the game completes, before the termination, the dispose method
 * should be called by the user. Better place it in the dispose method
 * of the game.
 *
 * @author Sri Harsha Chilakapati
 */
public class Batcher
{
    // Active state of this batcher
    private boolean active = false;

    // The sizes (no. of components) in vertex, color, texcoord
    private static final int SIZE_OF_VERTEX = 4;
    private static final int SIZE_OF_NORMAL = 4;
    private static final int SIZE_OF_COLOR = 4;
    private static final int SIZE_OF_TEXCOORD = 2;

    // The maximum number of vertices in a batch (number of vertices that fits in  4 MB)
    private static final int MAX_VERTICES_IN_BATCH = 1024 * 1024;

    // The buffers to store the collected data
    private FloatBuffer vBuffer;
    private FloatBuffer cBuffer;
    private FloatBuffer tBuffer;
    private FloatBuffer nBuffer;

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
        vBuffer = BufferUtils.createFloatBuffer(SIZE_OF_VERTEX * MAX_VERTICES_IN_BATCH);
        cBuffer = BufferUtils.createFloatBuffer(SIZE_OF_COLOR * MAX_VERTICES_IN_BATCH);
        tBuffer = BufferUtils.createFloatBuffer(SIZE_OF_TEXCOORD * MAX_VERTICES_IN_BATCH);
        nBuffer = BufferUtils.createFloatBuffer(SIZE_OF_NORMAL * MAX_VERTICES_IN_BATCH);

        // Create the transformations
        transform = new Transform();

        // Initialise OpenGL handles
        initGLHandles();
    }

    /**
     * Initialises VAOs and VBOs and creates the data store to
     * store the entire batch.
     */
    private void initGLHandles()
    {
        // Create a VAO
        vao = new VertexArray();
        vao.bind();

        // Create VBOs
        vboVert = new BufferObject(GL_ARRAY_BUFFER);
        vboCol = new BufferObject(GL_ARRAY_BUFFER);
        vboTex = new BufferObject(GL_ARRAY_BUFFER);
        vboNorm = new BufferObject(GL_ARRAY_BUFFER);

        // Initialize vertex-buffer
        vboVert.bind();
        vboVert.uploadData(SIZE_OF_VERTEX * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);

        // Initialize color-buffer
        vboCol.bind();
        vboCol.uploadData(SIZE_OF_COLOR * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);

        // Initialize texcoord-buffer
        vboTex.bind();
        vboTex.uploadData(SIZE_OF_TEXCOORD * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);

        // Initialize normal-buffer
        vboNorm.bind();
        vboNorm.uploadData(SIZE_OF_NORMAL * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);
    }

    /**
     * Uploads the data collected to the OpenGL buffers.
     */
    private void uploadData()
    {
        // Upload vertices
        vboVert.bind();
        vboVert.uploadSubData(vBuffer, 0);
        vao.pointAttribute(vertexLocation, 4, GL_FLOAT, vboVert);

        vboCol.bind();
        vboCol.uploadSubData(cBuffer, 0);
        vao.pointAttribute(colorLocation, 4, GL_FLOAT, vboCol);

        vboTex.bind();
        vboTex.uploadSubData(tBuffer, 0);
        vao.pointAttribute(texCoordLocation, 2, GL_FLOAT, vboTex);

        vboNorm.bind();
        vboNorm.uploadSubData(nBuffer, 0);
        vao.pointAttribute(normalLocation, 4, GL_FLOAT, vboNorm);
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

        vertexCount   = 0;
        colorCount    = 0;
        texCoordCount = 0;
        normalCount   = 0;

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

        // Reset Transform and camera matrices
        transform.reset();
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

        Program.CURRENT.prepareFrame();

        // Flip the buffers
        vBuffer.flip();
        cBuffer.flip();
        tBuffer.flip();
        nBuffer.flip();

        // Bind the VAO
        vao.bind();
        vao.enableAttributeArray(vertexLocation);
        vao.enableAttributeArray(colorLocation);
        vao.enableAttributeArray(texCoordLocation);
        vao.enableAttributeArray(normalLocation);

        // Upload the data
        uploadData();

        // Do a rendering
        GL3Context.drawArrays(vao, beginMode, 0, vertexCount);

        // Unbind the VAO
        vao.disableAttributeArray(vertexLocation);
        vao.disableAttributeArray(colorLocation);
        vao.disableAttributeArray(texCoordLocation);
        vao.disableAttributeArray(normalLocation);

        GL3Context.bindVertexArray(null);

        // Clear the buffers
        vBuffer.clear();
        cBuffer.clear();
        tBuffer.clear();
        nBuffer.clear();

        // Clear the vertex count
        vertexCount   = 0;
        colorCount    = 0;
        texCoordCount = 0;
        normalCount   = 0;
    }

    /**
     * Applies a Transform to the batcher data
     * @param t The transform to use
     */
    public void applyTransform(Transform t)
    {
        // Flush the data first
        flush();

        // Apply transform
        transform.reset().apply(t);
    }

    private void fillBuffers()
    {
        // Determine the fill color
        Color col = texCoordCount == vertexCount ? Color.TRANSPARENT : Color.WHITE;

        // Fill the color buffers
        while (colorCount < vertexCount)
        {
            cBuffer.put(col.getR()).put(col.getG()).put(col.getB()).put(col.getA());
            colorCount++;
        }

        // Fill the texcoord buffers
        while (texCoordCount < vertexCount)
        {
            tBuffer.put(0).put(0);
            texCoordCount++;
        }

        // Fill the normal buffers
        while (normalCount < vertexCount)
        {
            // Junk normal
            nBuffer.put(0).put(0).put(0).put(0);
            normalCount++;
        }
    }

    public void vertex(float x, float y)
    {
        vertex(x, y, 0, 1);
    }

    public void vertex(float x, float y, float z)
    {
        vertex(x, y, z, 1);
    }

    public void vertex(float x, float y, float z, float w)
    {
        flushOnOverflow(1);
        fillBuffers();

        vBuffer.put(x).put(y).put(z).put(w);
        vertexCount++;
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

    public void color(float r, float g, float b, float a)
    {
        // Add the specified color
        cBuffer.put(r).put(g).put(b).put(a);
        colorCount++;
    }

    public void color(Color c)
    {
        color(c.getR(), c.getG(), c.getB(), c.getA());
    }

    public void color(Vector4 c)
    {
        color(c.getR(), c.getG(), c.getB(), c.getA());
    }

    public void texCoord(float u, float v)
    {
        // Add the specified texcoord
        tBuffer.put(u).put(v);
        texCoordCount++;
    }

    public void texCoord(Vector2 v)
    {
        texCoord(v.getX(), v.getY());
    }

    public void normal(float x, float y, float z, float w)
    {
        nBuffer.put(x).put(y).put(z).put(w);
        normalCount++;
    }

    public void normal(float x, float y, float z)
    {
        normal(x, y, z, 1);
    }

    public void normal(Vector3 n)
    {
        normal(n.getX(), n.getY(), n.getZ(), 1);
    }

    /* Draw Texture */

    public void drawTexture2d(Texture texture, Vector2 p)
    {
        drawTexture2d(texture, p, Color.TRANSPARENT);
    }

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

    public void flushOnOverflow(int capacity)
    {
        if (vertexCount + capacity >= MAX_VERTICES_IN_BATCH)
            flush();
    }

    public Transform getTransform()
    {
        return transform;
    }

    public void dispose()
    {
        GL3Context.bindVertexArray(null);
        vao.dispose();
        GL3Context.bindVertexBuffer(null);
        vboVert.dispose();
        vboCol.dispose();
        vboTex.dispose();
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
}
