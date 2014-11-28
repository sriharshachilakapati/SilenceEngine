package com.shc.silenceengine.graphics;

import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.Vector4;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

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

    // VAO and VBO handles
    private int vaoID;
    private int vboVertID;
    private int vboColID;
    private int vboTexID;

    // The no. of vertices in the current batch
    private int vertexCount;

    // The transform, and projection and view matrices
    private Transform transform;
    private Matrix4   camProj;
    private Matrix4   camView;

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
        camProj   = new Matrix4();
        camView   = new Matrix4();

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
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create VBOs
        vboVertID = glGenBuffers();
        vboColID = glGenBuffers();
        vboTexID = glGenBuffers();

        // Create vertex-buffer
        glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
        glBufferData(GL_ARRAY_BUFFER, SIZE_OF_VERTEX * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);

        // Create color-buffer
        glBindBuffer(GL_ARRAY_BUFFER, vboColID);
        glBufferData(GL_ARRAY_BUFFER, SIZE_OF_COLOR * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);

        // Create texcoord-buffer
        glBindBuffer(GL_ARRAY_BUFFER, vboTexID);
        glBufferData(GL_ARRAY_BUFFER, SIZE_OF_TEXCOORD * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);
    }

    /**
     * Uploads the data collected to the OpenGL buffers.
     */
    private void uploadData()
    {
        // Upload vertices
        glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vBuffer);

        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);

        // Upload colors
        glBindBuffer(GL_ARRAY_BUFFER, vboColID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, cBuffer);

        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

        // Upload tex-coords
        glBindBuffer(GL_ARRAY_BUFFER, vboTexID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, tBuffer);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
    }

    /**
     * Begins the batcher with a Transform. All the vertices
     * collected will get applied with that Transform
     *
     * @param t The transform object to do transformation
     */
    public void begin(Transform t)
    {
        applyTransform(t);
        begin();
    }

    /**
     * Begins the batcher with a Camera.
     * @param c The camera to use.
     */
    public void begin(ICamera c)
    {
        applyCamera(c);
        begin();
    }

    /**
     * Begins the batcher with a Camera and a Transform
     * @param c The camera to use.
     * @param t The transform object to do transformation
     */
    public void begin(ICamera c, Transform t)
    {
        applyCamera(c);
        applyTransform(t);
        begin();
    }

    /**
     * Begins the batcher and marks it active.
     */
    public void begin()
    {
        if (active)
            throw new IllegalStateException("Batcher Already Active!");

        active = true;
        vertexCount = 0;
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
        camProj.initIdentity();
        camView.initIdentity();
    }

    /**
     * Flushes the data collected to the GPU.
     */
    public void flush()
    {
        // Avoid doing unnecessary flushes
        if (vertexCount == 0)
            return;

        // Shader uniforms
        Shader.CURRENT.setUniform("tex", Texture.getActiveUnit());
        Shader.CURRENT.setUniform("mTransform", transform.getMatrix());
        Shader.CURRENT.setUniform("camProj", camProj);
        Shader.CURRENT.setUniform("camView", camView);

        // Flip the buffers
        vBuffer.flip();
        cBuffer.flip();
        tBuffer.flip();

        // Bind the VAO
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        // Upload the data
        uploadData();

        // Do a rendering
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);

        // Unbind the VAO
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        // Clear the buffers
        vBuffer.clear();
        cBuffer.clear();
        tBuffer.clear();

        // Clear the vertex count
        vertexCount = 0;
    }

    /**
     *
     * @param t
     */
    public void applyTransform(Transform t)
    {
        // Flush the data first
        flush();

        // Apply transform
        transform.apply(t);
    }

    public void applyCamera(ICamera c)
    {
        // Flush the data first
        flush();

        camProj.initIdentity().multiply(c.getProjection());
        camView.initIdentity().multiply(c.getView());
    }

    /* addVertex variations */

    public void addVertex(Vector2 v)
    {
        addVertex(v.getX(), v.getY(), 0, 1);
    }

    public void addVertex(Vector2 v, Vector2 t)
    {
        addVertex(v.getX(), v.getY(), 0, 1, 0, 0, 0, 1, t.getX(), t.getY());
    }

    public void addVertex(Vector2 v, Color c)
    {
        addVertex(v.getX(), v.getY(), 0, 1, c.getR(), c.getG(), c.getB(), c.getA());
    }

    public void addVertex(Vector2 v, Color c, Vector2 t)
    {
        addVertex(v.getX(), v.getY(), 0, 1, c.getR(), c.getG(), c.getB(), c.getA(), t.getX(), t.getY());
    }

    public void addVertex(Vector3 v)
    {
        addVertex(v.getX(), v.getY(), v.getZ(), 1);
    }

    public void addVertex(Vector3 v, Vector2 t)
    {
        addVertex(v.getX(), v.getY(), v.getZ(), 1, 0, 0, 0, 1, t.getX(), t.getY());
    }

    public void addVertex(Vector3 v, Color c)
    {
        addVertex(v.getX(), v.getY(), v.getZ(), 1, c.getR(), c.getG(), c.getB(), c.getA());
    }

    public void addVertex(Vector3 v, Color c, Vector2 t)
    {
        addVertex(v.getX(), v.getY(), v.getZ(), 1, c.getR(), c.getG(), c.getB(), c.getA(), t.getX(), t.getY());
    }

    public void addVertex(Vector4 v)
    {
        addVertex(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    public void addVertex(Vector4 v, Vector2 t)
    {
        addVertex(v.getX(), v.getY(), v.getZ(), v.getW(), 0, 0, 0, 1, t.getX(), t.getY());
    }

    public void addVertex(Vector4 v, Color c)
    {
        addVertex(v.getX(), v.getY(), v.getZ(), v.getW(), c.getR(), c.getG(), c.getB(), c.getA());
    }

    public void addVertex(Vector4 v, Color c, Vector2 t)
    {
        addVertex(v.getX(), v.getY(), v.getZ(), v.getW(), c.getR(), c.getG(), c.getB(), c.getA(), t.getX(), t.getY());
    }

    public void addVertex(float x, float y, float z, float w)
    {
        addVertex(x, y, z, w, 1, 1, 1, 1, 0, 0);
    }

    public void addVertex(float x, float y, float z, float w, float u, float v)
    {
        addVertex(x, y, z, w, 0, 0, 0, 0, u, v);
    }

    public void addVertex(float x, float y, float z, float w, float r, float g, float b, float a)
    {
        addVertex(x, y, z, w, r, g, b, a, 0, 0);
    }

    public void addVertex(float x, float y, float z, float w, float r, float g, float b, float a, float u, float v)
    {
        flushOnOverflow(1);

        vertexCount++;
        vBuffer.put(x).put(y).put(z).put(w);
        cBuffer.put(r).put(g).put(b).put(a);
        tBuffer.put(u).put(v);
    }

    /* addTriangle variations */

    public void addTriangle(Vector2 v1, Vector2 v2, Vector2 v3,
                            Color   c1, Color   c2, Color   c3,
                            Vector2 t1, Vector2 t2, Vector2 t3)
    {
        addTriangle(v1.getX(), v1.getY(), 0, 1,
                    v2.getX(), v2.getY(), 0, 1,
                    v3.getX(), v3.getY(), 0, 1,
                    c1.getR(), c1.getG(), c1.getB(), c1.getA(),
                    c2.getR(), c2.getG(), c2.getB(), c2.getA(),
                    c3.getR(), c3.getG(), c3.getB(), c3.getA(),
                    t1.getX(), t1.getY(),
                    t2.getX(), t2.getY(),
                    t3.getX(), t3.getY());
    }

    public void addTriangle(Vector2 v1, Vector2 v2, Vector2 v3)
    {
        addTriangle(v1, v2, v3, Color.WHITE, Color.WHITE, Color.WHITE, Vector2.ZERO, Vector2.ZERO, Vector2.ZERO);
    }

    public void addTriangle(Vector2 v1, Vector2 v2, Vector2 v3,
                            Color   c1, Color   c2, Color   c3)
    {
        addTriangle(v1, v2, v3, c1, c2, c3, Vector2.ZERO, Vector2.ZERO, Vector2.ZERO);
    }

    public void addTriangle(Vector2 v1, Vector2 v2, Vector2 v3,
                            Vector2 t1, Vector2 t2, Vector2 t3)
    {
        addTriangle(v1, v2, v3, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, t1, t2, t3);
    }

    public void addTriangle(Vector3 v1, Vector3 v2, Vector3 v3,
                            Color   c1, Color   c2, Color   c3,
                            Vector2 t1, Vector2 t2, Vector2 t3)
    {
        addTriangle(v1.getX(), v1.getY(), v1.getZ(), 1,
                    v2.getX(), v2.getY(), v2.getZ(), 1,
                    v3.getX(), v3.getY(), v3.getZ(), 1,
                    c1.getR(), c1.getG(), c1.getB(), c1.getA(),
                    c2.getR(), c2.getG(), c2.getB(), c2.getA(),
                    c3.getR(), c3.getG(), c3.getB(), c3.getA(),
                    t1.getX(), t1.getY(), t2.getX(), t2.getY(), t3.getX(), t3.getY());
    }

    public void addTriangle(float x1, float y1, float z1, float w1,
                            float x2, float y2, float z2, float w2,
                            float x3, float y3, float z3, float w3,
                            float r1, float g1, float b1, float a1,
                            float r2, float g2, float b2, float a2,
                            float r3, float g3, float b3, float a3,
                            float u1, float v1,
                            float u2, float v2,
                            float u3, float v3)
    {
        flushOnOverflow(3);

        addVertex(x1, y1, z1, w1, r1, g1, b1, a1, u1, v1);
        addVertex(x2, y2, z2, w2, r2, g2, b2, a2, u2, v2);
        addVertex(x3, y3, z3, w3, r3, g3, b3, a3, u3, v3);
    }

    /* Draw Texture */

    public void drawTexture2d(Texture texture, Vector2 position)
    {
        Texture current = Texture.CURRENT;
        flush();

        texture.bind();
        addTriangle(new Vector2(0, 0).add(position),
                    new Vector2(texture.getWidth(), 0).add(position),
                    new Vector2(0, texture.getHeight()).add(position),

                    new Vector2(0, 0),
                    new Vector2(1, 0),
                    new Vector2(0, 1));

        addTriangle(new Vector2(texture.getWidth(), 0).add(position),
                new Vector2(texture.getWidth(), texture.getHeight()).add(position),
                new Vector2(0, texture.getHeight()).add(position),

                new Vector2(1, 0),
                new Vector2(1, 1),
                new Vector2(0, 1));

        flush();
        current.bind();
    }

    public void flushOnOverflow(int capacity)
    {
        if (vertexCount + capacity > MAX_VERTICES_IN_BATCH)
            flush();
    }

    public void dispose()
    {
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboVertID);
        glDeleteBuffers(vboColID);
    }
}
