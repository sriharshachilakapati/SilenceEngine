package com.shc.silenceengine.graphics.opengl;

import com.shc.silenceengine.core.Game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Static Utility class to check for OpenGL errors. If you want to check for errors anytime, just make a call to
 * <code>GlError.check()</code> and you are done.
 *
 * @author Sri Harsha Chilakapati
 */
public final class GLError
{
    /**
     * Prevent instantiation, this is just a utility class
     */
    private GLError()
    {
    }

    /**
     * Checks for OpenGL errors. If any error is found, it throws a GLException which is a runtime exception. Use this
     * if you are suspecting if there is some error in your OpenGL code. This does not run if the game is not in
     * development mode.
     */
    public static void check()
    {
        check(false);
    }

    /**
     * Checks for OpenGL errors. If any error is found, it throws a GLException which is a runtime exception. Use this
     * if you are suspecting if there is some error in your OpenGL code.
     *
     * @param force Forces the running of glGetError even in Deployed mode. By default, it is only executed in
     *              development mode
     */
    public static void check(boolean force)
    {
        // We don't want to run GL checks
        if (!force && !Game.development)
            return;

        switch (glGetError())
        {
            case GL_NO_ERROR:
                break;
            case GL_INVALID_ENUM:
                throw new GLException.InvalidEnum();
            case GL_INVALID_VALUE:
                throw new GLException.InvalidValue();
            case GL_INVALID_OPERATION:
                throw new GLException.InvalidOperation();
            case GL_INVALID_FRAMEBUFFER_OPERATION:
                throw new GLException.InvalidFramebufferOperation();
            case GL_OUT_OF_MEMORY:
                throw new GLException.OutOfMemory();
            case GL_STACK_UNDERFLOW:
                throw new GLException.StackUnderflow();
            case GL_STACK_OVERFLOW:
                throw new GLException.StackOverflow();
        }
    }

    public static Value get()
    {
        switch (glGetError())
        {
            case GL_INVALID_ENUM:
                return Value.INVALID_ENUM;
            case GL_INVALID_VALUE:
                return Value.INVALID_VALUE;
            case GL_INVALID_OPERATION:
                return Value.INVALID_OPERATION;
            case GL_INVALID_FRAMEBUFFER_OPERATION:
                return Value.INVALID_FRAMEBUFFER_OPERATION;
            case GL_OUT_OF_MEMORY:
                return Value.OUT_OF_MEMORY;
            case GL_STACK_UNDERFLOW:
                return Value.STACK_UNDERFLOW;
            case GL_STACK_OVERFLOW:
                return Value.STACK_OVERFLOW;
        }

        return Value.NO_ERROR;
    }

    /**
     * Encapsulates the Glenum error.
     */
    public static enum Value
    {
        NO_ERROR,
        INVALID_ENUM,
        INVALID_VALUE,
        INVALID_OPERATION,
        INVALID_FRAMEBUFFER_OPERATION,
        OUT_OF_MEMORY,
        STACK_UNDERFLOW,
        STACK_OVERFLOW
    }
}
