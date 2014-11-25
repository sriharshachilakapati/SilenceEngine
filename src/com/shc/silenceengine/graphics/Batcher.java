package com.shc.silenceengine.graphics;

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
 * @author Sri Harsha Chilakapati
 */
public class Batcher
{
    private boolean active = false;

    public static final int SIZE_OF_VERTEX = 4;
    public static final int SIZE_OF_COLOR  = 4;
    public static final int SIZE_OF_TEXCOORD = 2;

    public static final int MAX_VERTICES_IN_BATCH = 4096;

    private FloatBuffer vBuffer;
    private FloatBuffer cBuffer;
    private FloatBuffer tBuffer;

    private int vaoID;
    private int vboVertID;
    private int vboColID;
    private int vboTexID;
    private int vertexCount;

    private Transform transform;

    public Batcher()
    {
        vBuffer = BufferUtils.createFloatBuffer(SIZE_OF_VERTEX   * MAX_VERTICES_IN_BATCH);
        cBuffer = BufferUtils.createFloatBuffer(SIZE_OF_COLOR    * MAX_VERTICES_IN_BATCH);
        tBuffer = BufferUtils.createFloatBuffer(SIZE_OF_TEXCOORD * MAX_VERTICES_IN_BATCH);

        transform = new Transform();

        initGLHandles();
    }

    private void initGLHandles()
    {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboVertID = glGenBuffers();
        vboColID = glGenBuffers();
        vboTexID = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
        glBufferData(GL_ARRAY_BUFFER, SIZE_OF_VERTEX * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, vboColID);
        glBufferData(GL_ARRAY_BUFFER, SIZE_OF_COLOR * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, vboTexID);
        glBufferData(GL_ARRAY_BUFFER, SIZE_OF_TEXCOORD * MAX_VERTICES_IN_BATCH, GL_STREAM_DRAW);
    }

    private void uploadData()
    {
        glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vBuffer);

        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboColID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, cBuffer);

        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, vboTexID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, tBuffer);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
    }

    public void begin()
    {
        if (active)
            throw new IllegalStateException("Batcher Already Active!");

        active = true;
        vertexCount = 0;
    }

    public void end()
    {
        if (!active)
            throw new IllegalStateException("Batcher not Active!");

        active = false;
        flush();

        transform.reset();
    }

    public void flush()
    {
        // Shader uniforms
        Shader.CURRENT.setUniform("tex", Texture.getActiveUnit());
        Shader.CURRENT.setUniform("mTransform", transform.getMatrix());

        vBuffer.flip();
        cBuffer.flip();
        tBuffer.flip();

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        uploadData();

        glDrawArrays(GL_TRIANGLES, 0, vertexCount);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        vBuffer.clear();
        cBuffer.clear();
        tBuffer.clear();

        vertexCount = 0;
    }

    public void applyTransform(Transform t)
    {
        // Flush the data first
        flush();

        // Apply transform
        transform.apply(t);
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
