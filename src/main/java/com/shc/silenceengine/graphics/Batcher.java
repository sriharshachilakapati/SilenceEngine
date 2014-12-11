package com.shc.silenceengine.graphics;

import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.Vector4;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

import com.shc.silenceengine.graphics.opengl.GLError;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.opengl.VertexArrayObject;
import com.shc.silenceengine.graphics.opengl.VertexBufferObject;

/**
 * A simple class which eases the rendering of Graphics by batching
 * the vertices, colors, textures to the shaders using VAOs and VBOs.
 * The Batcher is where, all the rendering takes place in SilenceEngine.
 *
 * The batcher is just instantiated like an object, usually only once
 * in a game. Then, you can use the methods defined to add various
 * shapes, or even raw vertices. They get rendered whenever the end
 * method is called.
 *
 * A batcher can also take transforms, and Cameras and render using
 * them. To use them, either pass them to the begin method, or apply
 * them manually using the apply[Camera/Transform] methods.
 *
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
    public static final int SIZE_OF_VERTEX = 4;
    public static final int SIZE_OF_COLOR  = 4;
    public static final int SIZE_OF_TEXCOORD = 2;

    // The maximum number of vertices in a batch
    public static final int MAX_VERTICES_IN_BATCH = 4096;

    // The buffers to store the collected data
    private FloatBuffer vBuffer;
    private FloatBuffer cBuffer;
    private FloatBuffer tBuffer;

    // VAO and VBOs
    private VertexArrayObject  vao;
    private VertexBufferObject vboVert;
    private VertexBufferObject vboCol;
    private VertexBufferObject vboTex;

    // The no. of vertices in the current batch
    private int vertexCount;
    private int colorCount;
    private int texCoordCount;

    // The transform, and projection and view matrices
    private Transform transform;

    /**
     * Creates the Batcher, and initialises OpenGL
     */
    public Batcher()
    {
        // Create the buffers
        vBuffer = BufferUtils.createFloatBuffer(SIZE_OF_VERTEX   * MAX_VERTICES_IN_BATCH);
        cBuffer = BufferUtils.createFloatBuffer(SIZE_OF_COLOR    * MAX_VERTICES_IN_BATCH);
        tBuffer = BufferUtils.createFloatBuffer(SIZE_OF_TEXCOORD * MAX_VERTICES_IN_BATCH);

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
        vao = new VertexArrayObject();
        vao.bind();

        // Create VBOs
        vboVert = new VertexBufferObject(GL_ARRAY_BUFFER);
        vboCol  = new VertexBufferObject(GL_ARRAY_BUFFER);
        vboTex  = new VertexBufferObject(GL_ARRAY_BUFFER);

        // Initialize vertex-buffer
        vboVert.bind();
        vboVert.uploadData(SIZE_OF_VERTEX * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);

        // Initialize color-buffer
        vboCol.bind();
        vboCol.uploadData(SIZE_OF_COLOR * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);

        // Initialize texcoord-buffer
        vboTex.bind();
        vboTex.uploadData(SIZE_OF_TEXCOORD * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);
    }

    /**
     * Uploads the data collected to the OpenGL buffers.
     */
    private void uploadData()
    {
        // Upload vertices
        vboVert.bind();
        vboVert.uploadSubData(vBuffer, 0);
        vao.pointAttribute(0, 4, GL_FLOAT, vboVert);

        vboCol.bind();
        vboCol.uploadSubData(cBuffer, 0);
        vao.pointAttribute(1, 4, GL_FLOAT, vboCol);

        vboTex.bind();
        vboTex.uploadSubData(tBuffer, 0);
        vao.pointAttribute(2, 2, GL_FLOAT, vboTex);
    }

    /**
     * Begins the batcher and marks it active.
     */
    public void begin()
    {
        if (active)
            throw new IllegalStateException("Batcher Already Active!");

        active = true;

        vertexCount   = 0;
        colorCount    = 0;
        texCoordCount = 0;
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

        Program.CURRENT.setupUniforms();

        // Flip the buffers
        vBuffer.flip();
        cBuffer.flip();
        tBuffer.flip();

        // Bind the VAO
        vao.bind();
        vao.enableAttributeArray(0);
        vao.enableAttributeArray(1);
        vao.enableAttributeArray(2);

        // Upload the data
        uploadData();

        // Do a rendering
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        GLError.check();

        // Unbind the VAO
        vao.disableAttributeArray(0);
        vao.disableAttributeArray(1);
        vao.disableAttributeArray(2);
        glBindVertexArray(0);
        GLError.check();

        // Clear the buffers
        vBuffer.clear();
        cBuffer.clear();
        tBuffer.clear();

        // Clear the vertex count
        vertexCount   = 0;
        colorCount    = 0;
        texCoordCount = 0;
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
        transform.apply(t);
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

    /* Draw Texture */

    public void drawTexture2d(Texture texture, Vector2 p)
    {
        drawTexture2d(texture, p, Color.TRANSPARENT);
    }

    public void drawTexture2d(Texture texture, Vector2 p, Color color)
    {
        Texture current = Texture.CURRENT;
        flush();

        texture.bind();

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

        flush();
        current.bind();
    }

    public void flushOnOverflow(int capacity)
    {
        if (vertexCount + capacity > MAX_VERTICES_IN_BATCH)
            flush();
    }

    public Transform getTransform()
    {
        return transform;
    }

    public void dispose()
    {
        glBindVertexArray(0);
        vao.dispose();
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        vboVert.dispose();
        vboCol.dispose();
        vboTex.dispose();
    }
}
