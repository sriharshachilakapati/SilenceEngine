package com.shc.silenceengine.graphics.opengl;

import com.shc.silenceengine.core.Game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Static Utility class to check for OpenGL errors. If you want to check
 * for errors anytime, just make a call to <code>GlError.check()</code>
 * and you are done.
 *
 * @author Sri Harsha Chilakapati
 */
public final class GLError
{
    /**
     * Prevent instantiation, this is just a utility class
     */
    private GLError(){}

    /**
     * Checks for OpenGL errors. If any error is found, it throws a GLException
     * which is a runtime exception. Use this if you are suspecting if there is
     * some error in your OpenGL code. This does not run if the game is not in
     * development mode.
     */
    public static void check()
    {
        check(false);
    }

    /**
     * Checks for OpenGL errors. If any error is found, it throws a GLException
     * which is a runtime exception. Use this if you are suspecting if there is
     * some error in your OpenGL code.
     *
     * @param force Forces the running of glGetError even in Deployed mode. By
     *              default, it is only executed in development mode
     */
    public static void check(boolean force)
    {
        // We don't want to run GL checks
        if (!force && !Game.development)
            return;

        switch (glGetError())
        {
            case GL_NO_ERROR: break;

            case GL_INVALID_ENUM:
                throw new GLException("GL_INVALID_ENUM: An unacceptable value is specified for an enumerated argument");

            case GL_INVALID_VALUE:
                throw new GLException("GL_INVALID_VALUE: A numeric argument is out of range");

            case GL_INVALID_OPERATION:
                throw new GLException("GL_INVALID_OPERATION: The specified operation is not allowed in current state");

            case GL_INVALID_FRAMEBUFFER_OPERATION:
                throw new GLException("GL_INVALID_FRAMEBUFFER_OPERATION: The FrameBuffer object is incomplete");

            case GL_OUT_OF_MEMORY:
                throw new GLException("GL_OUT_OF_MEMORY: There is not enough memory left to execute the command");

            case GL_STACK_UNDERFLOW:
                throw new GLException("GL_STACK_UNDERFLOW: An attempt has been made to perform an operation that would cause an internal stack to underflow.");

            case GL_STACK_OVERFLOW:
                throw new GLException("GL_STACK_OVERFLOW: An attempt has been made to perform an operation that would cause an internal stack to overflow.");
        }
    }

    public static void clear()
    {
        while (true)
        {
            if (glGetError() == GL_NO_ERROR) break;
        }
    }
}
