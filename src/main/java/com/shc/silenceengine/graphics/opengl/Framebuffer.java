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

    public boolean isComplete()
    {
        bind();
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        GLError.check();

        return status == GL_FRAMEBUFFER_COMPLETE;
    }

    public void release()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        GLError.check();
    }

    public void dispose()
    {
        release();

        glDeleteFramebuffers(id);
        GLError.check();

        disposed = true;
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
