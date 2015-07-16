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
    public static final Framebuffer SCREEN  = new Framebuffer(0);
    public static       Framebuffer CURRENT = SCREEN;

    private boolean disposed;
    private Target  target;

    private int id;

    public Framebuffer(int id)
    {
        this.id = id;
        this.target = Target.READ_WRITE;
    }

    public Framebuffer()
    {
        this(Target.READ_WRITE);
    }

    public Framebuffer(Target target)
    {
        id = glGenFramebuffers();
        GLError.check();
        this.target = target;
    }

    public static boolean isValid(int id)
    {
        if (id == 0)
            return true;

        boolean value = glIsFramebuffer(id);
        GLError.check();

        return value;
    }

    public void texture2d(Texture texture)
    {
        texture2d(texture, Attachment.COLOR0);
    }

    public void texture2d(Texture texture, Attachment attachment)
    {
        texture2d(texture, attachment, 0);
    }

    public void texture2d(Texture texture, Attachment attachment, int level)
    {
        bind();

        glFramebufferTexture2D(target.getValue(), attachment.getValue(), GL_TEXTURE_2D, texture.getId(), level);
        GLError.check();

        glDrawBuffer(attachment.getValue());
        GLError.check();
    }

    public void textureLayer(Texture texture, Attachment attachment, int level, int layer)
    {
        bind();

        glFramebufferTextureLayer(target.getValue(), attachment.getValue(), texture.getId(), level, layer);
        GLError.check();

        glDrawBuffer(attachment.getValue());
        GLError.check();
    }

    public void bind()
    {
        bind(false);
    }

    public void bind(boolean force)
    {
        if (disposed)
            throw new GLException("Cannot bind a disposed framebuffer");

        if (!force && CURRENT == this)
            return;

        glBindFramebuffer(target.getValue(), id);
        GLError.check();

        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        GLError.check();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        GLError.check();

        CURRENT = this;
    }

    public boolean isComplete()
    {
        bind();
        int status = glCheckFramebufferStatus(target.getValue());
        GLError.check();

        return status == GL_FRAMEBUFFER_COMPLETE;
    }

    public boolean isValid()
    {
        return isValid(id);
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
        SCREEN.bind();
    }

    public boolean isDisposed()
    {
        return disposed;
    }

    public int getId()
    {
        return id;
    }

    public Target getTarget()
    {
        return target;
    }

    public enum Target
    {
        WRITE(GL_DRAW_FRAMEBUFFER),
        READ(GL_READ_FRAMEBUFFER),
        READ_WRITE(GL_FRAMEBUFFER);

        int value;

        Target(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    public enum Attachment
    {
        COLOR0(GL_COLOR_ATTACHMENT0),
        COLOR1(GL_COLOR_ATTACHMENT1),
        COLOR2(GL_COLOR_ATTACHMENT2),
        COLOR3(GL_COLOR_ATTACHMENT3),
        COLOR4(GL_COLOR_ATTACHMENT4),
        COLOR5(GL_COLOR_ATTACHMENT5),
        COLOR6(GL_COLOR_ATTACHMENT6),
        COLOR7(GL_COLOR_ATTACHMENT7),
        COLOR8(GL_COLOR_ATTACHMENT8),
        COLOR9(GL_COLOR_ATTACHMENT9),
        COLOR10(GL_COLOR_ATTACHMENT10),
        COLOR11(GL_COLOR_ATTACHMENT11),
        COLOR12(GL_COLOR_ATTACHMENT12),
        COLOR13(GL_COLOR_ATTACHMENT13),
        COLOR14(GL_COLOR_ATTACHMENT14),
        COLOR15(GL_COLOR_ATTACHMENT15),

        DEPTH(GL_DEPTH_ATTACHMENT),
        STENCIL(GL_STENCIL_ATTACHMENT),
        DEPTH_STENCIL(GL_DEPTH_STENCIL_ATTACHMENT);

        int value;

        Attachment(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }
}
