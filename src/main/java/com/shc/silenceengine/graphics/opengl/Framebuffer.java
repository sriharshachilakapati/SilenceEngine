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

package com.shc.silenceengine.graphics.opengl;

import com.shc.silenceengine.core.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Framebuffer
{
    private boolean disposed;

    private int id;

    public Framebuffer()
    {
        id = glGenFramebuffers();
        GLError.check();
    }

    public void texture2d(Texture texture)
    {
        texture2d(texture, GL_COLOR_ATTACHMENT0);
    }

    public void texture2d(Texture texture, int attachment)
    {
        texture2d(texture, attachment, 0);
    }

    public void texture2d(Texture texture, int attachment, int level)
    {
        bind();

        glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, GL_TEXTURE_2D, texture.getId(), level);
        GLError.check();

        glDrawBuffer(attachment);
        GLError.check();
    }

    public void bind()
    {
        if (disposed)
            throw new GLException("Cannot bind a disposed framebuffer");

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, id);
        GLError.check();

        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        GLError.check();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        GLError.check();
    }

    public boolean isComplete()
    {
        bind();
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        GLError.check();

        return status == GL_FRAMEBUFFER_COMPLETE;
    }

    public void dispose()
    {
        release();

        glDeleteFramebuffers(id);
        GLError.check();

        disposed = true;
    }

    public void release()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        GLError.check();
    }

    public boolean isDisposed()
    {
        return disposed;
    }

    public int getId()
    {
        return id;
    }
}
