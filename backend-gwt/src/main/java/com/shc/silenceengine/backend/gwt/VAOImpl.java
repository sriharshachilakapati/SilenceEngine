/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.backend.gwt;

import com.shc.webgl4j.client.OES_vertex_array_object;
import com.shc.webgl4j.client.WebGL10;
import com.shc.webgl4j.client.WebGL20;

/**
 * @author Sri Harsha Chilakapati
 */
public abstract class VAOImpl
{
    private static VAOImpl instance = null;

    public static VAOImpl get()
    {
        if (instance == null)
            instance = WebGL20.isContextCompatible() ? new WebGL2() : new Extension();

        return instance;
    }

    public abstract int glGenVertexArrays();

    public abstract void glBindVertexArray(int vertexArray);

    public abstract boolean glIsVertexArray(int vertexArray);

    public abstract void glEnableVertexAttribArray(int index);

    public abstract void glDisableVertexAttribArray(int index);

    public abstract void glVertexAttribPointer(int index, int count, int type, boolean normalized, int stride, long offset);

    public abstract void glDeleteVertexArray(int vertexArray);

    public static class Extension extends VAOImpl
    {
        public Extension()
        {
            OES_vertex_array_object.enableExtension();
        }

        @Override
        public int glGenVertexArrays()
        {
            return OES_vertex_array_object.glCreateVertexArrayOES();
        }

        @Override
        public void glBindVertexArray(int vertexArray)
        {
            OES_vertex_array_object.glBindVertexArrayOES(vertexArray);
        }

        @Override
        public boolean glIsVertexArray(int vertexArray)
        {
            return OES_vertex_array_object.glIsVertexArrayOES(vertexArray);
        }

        @Override
        public void glEnableVertexAttribArray(int index)
        {
            WebGL10.glEnableVertexAttribArray(index);
        }

        @Override
        public void glDisableVertexAttribArray(int index)
        {
            WebGL10.glDisableVertexAttribArray(index);
        }

        @Override
        public void glVertexAttribPointer(int index, int count, int type, boolean normalized, int stride, long offset)
        {
            WebGL10.glVertexAttribPointer(index, count, type, normalized, stride, offset);
        }

        @Override
        public void glDeleteVertexArray(int vertexArray)
        {
            OES_vertex_array_object.glDeleteVertexArrayOES(vertexArray);
        }
    }

    public static class WebGL2 extends VAOImpl
    {
        @Override
        public int glGenVertexArrays()
        {
            return WebGL20.glCreateVertexArray();
        }

        @Override
        public void glBindVertexArray(int vertexArray)
        {
            WebGL20.glBindVertexArray(vertexArray);
        }

        @Override
        public boolean glIsVertexArray(int vertexArray)
        {
            return WebGL20.glIsVertexArray(vertexArray);
        }

        @Override
        public void glEnableVertexAttribArray(int index)
        {
            WebGL10.glEnableVertexAttribArray(index);
        }

        @Override
        public void glDisableVertexAttribArray(int index)
        {
            WebGL10.glDisableVertexAttribArray(index);
        }

        @Override
        public void glVertexAttribPointer(int index, int count, int type, boolean normalized, int stride, long offset)
        {
            WebGL10.glVertexAttribPointer(index, count, type, normalized, stride, offset);
        }

        @Override
        public void glDeleteVertexArray(int vertexArray)
        {
            WebGL20.glDeleteVertexArray(vertexArray);
        }
    }
}
