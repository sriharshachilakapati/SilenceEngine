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

    public Batcher()
    {
        vBuffer = BufferUtils.createFloatBuffer(SIZE_OF_VERTEX   * MAX_VERTICES_IN_BATCH);
        cBuffer = BufferUtils.createFloatBuffer(SIZE_OF_COLOR    * MAX_VERTICES_IN_BATCH);
        tBuffer = BufferUtils.createFloatBuffer(SIZE_OF_TEXCOORD * MAX_VERTICES_IN_BATCH);

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
    }

    public void flush()
    {
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

        vertexCount = 0;
    }

    public void addVertex(Vector2 v)
    {
        flushOnOverflow(1);

        vertexCount++;
        vBuffer.put(v.getX()).put(v.getY()).put(0).put(1);
        cBuffer.put(1).put(1).put(1).put(1);
    }

    public void addVertex(Vector3 v)
    {
        flushOnOverflow(1);

        vertexCount++;
        vBuffer.put(v.getX()).put(v.getY()).put(v.getZ()).put(1);
        cBuffer.put(1).put(1).put(1).put(1);
    }

    public void addVertex(Vector4 v)
    {
        flushOnOverflow(1);

        vertexCount++;
        vBuffer.put(v.getX()).put(v.getY()).put(v.getZ()).put(v.getW());
        cBuffer.put(1).put(1).put(1).put(1);
    }

    public void addVertex(float x, float y, float z, float w)
    {
        flushOnOverflow(1);

        vertexCount++;
        vBuffer.put(x).put(y).put(z).put(w);
        cBuffer.put(1).put(1).put(1).put(1);
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
