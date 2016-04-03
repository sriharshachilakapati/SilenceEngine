package com.shc.silenceengine.graphics;

import com.shc.silenceengine.io.DirectBuffer;
import com.shc.silenceengine.io.DirectFloatBuffer;

/**
 * Describes a graphics device which is used to provide OpenGL functions on a target platform.
 *
 * @author Sri Harsha Chilakapati
 */
public interface IGraphicsDevice
{
    int glGenBuffers();

    boolean glIsBuffer(int buffer);

    void glBufferData(int value, DirectBuffer data, int usage);

    void glBindBuffer(int target, int buffer);

    void glBufferData(int target, int capacity, int usage);

    void glBufferSubData(int target, int offset, DirectBuffer data);

    void glDeleteBuffers(int... buffer);

    int glGenFramebuffers();

    boolean glIsFramebuffer(int framebuffer);

    void glFramebufferTexture2D(int target, int attachment, int textureTarget, int texture, int level);

    void glBindFramebuffer(int target, int framebuffer);

    void glViewport(int x, int y, int width, int height);

    void glClear(int flags);

    int glCheckFramebufferStatus(int target);

    void glDeleteFramebuffers(int... framebuffer);

    void glDrawArrays(int primitive, int offset, int vertexCount);

    void glDrawElements(int primitive, int vertexCount, int type, int offset);

    void glEnable(int capability);

    void glBlendFunc(int src, int dst);

    void glDisable(int capability);

    void glClearColor(float r, float g, float b, float a);

    void glBindVertexArray(int vaoID);

    void glDepthMask(boolean value);

    void glDepthFunc(int func);

    void glCullFace(int mode);

    int glGetError();

    int glCreateProgram();

    void glAttachShader(int program, int shader);

    void glLinkProgram(int program);

    int glGetProgrami(int program, int param);

    String glGetProgramInfoLog(int program);

    int glGetAttribLocation(int program, String name);

    void glUseProgram(int program);

    int glGetUniformLocation(int program, String name);

    void glUniform1i(int location, int value);

    void glUniform2i(int location, int v1, int v2);

    void glUniform3i(int location, int v1, int v2, int v3);

    void glUniform4i(int location, int v1, int v2, int v3, int v4);

    void glUniform1f(int location, float value);

    void glUniform2f(int location, float v1, float v2);

    void glUniform3f(int location, float v1, float v2, float v3);

    void glUniform4f(int location, float v1, float v2, float v3, float v4);

    void glUniformMatrix3fv(int location, boolean transpose, DirectFloatBuffer matrix);

    void glUniformMatrix4fv(int location, boolean transpose, DirectFloatBuffer matrix);

    void glDeleteProgram(int... id);

    int glCreateShader(int type);

    void glShaderSource(int shader, String... source);

    void glCompileShader(int shader);

    int glGetShaderi(int shader, int param);

    String glGetShaderInfoLog(int shader);

    void glDeleteShader(int... shader);

    int glGenTextures();

    void glActiveTexture(int unit);

    void glBindTexture(int target, int texture);

    void glTexParameteri(int target, int param, int value);

    void glTexImage2D(int target, int level, int internalFormat, int width, int height, int border, int format,
                      int type, DirectBuffer pixels);

    void glGenerateMipmap(int target);

    void glDeleteTextures(int... texture);

    int glGenVertexArrays();

    boolean glIsVertexArray(int vertexArray);

    void glEnableVertexAttribArray(int index);

    void glDisableVertexAttribArray(int index);

    void glVertexAttribPointer(int index, int count, int type, boolean normalized, int stride, long offset);

    void glDeleteVertexArrays(int... vertexArray);

    final class Constants
    {
        public static final int GL_ARRAY_BUFFER         = 0x8892;
        public static final int GL_ELEMENT_ARRAY_BUFFER = 0x8893;

        public static final int GL_STREAM_DRAW  = 0x88E0;
        public static final int GL_STATIC_DRAW  = 0x88E4;
        public static final int GL_DYNAMIC_DRAW = 0x88E8;

        public static final int GL_COLOR_BUFFER_BIT   = 0x4000;
        public static final int GL_DEPTH_BUFFER_BIT   = 0x0100;
        public static final int GL_STENCIL_BUFFER_BIT = 0x0400;

        public static final int GL_TEXTURE_2D  = 0x0DE1;
        public static final int GL_FRAMEBUFFER = 0x8D40;

        public static final int GL_FRAMEBUFFER_COMPLETE = 0x8CD5;

        public static final int GL_COLOR_ATTACHMENT0        = 0x8CE0;
        public static final int GL_DEPTH_ATTACHMENT         = 0x8D00;
        public static final int GL_STENCIL_ATTACHMENT       = 0x8D20;
        public static final int GL_DEPTH_STENCIL_ATTACHMENT = 0x821A;

        public static final int GL_NO_ERROR                      = 0x0000;
        public static final int GL_INVALID_ENUM                  = 0x0500;
        public static final int GL_INVALID_VALUE                 = 0x0501;
        public static final int GL_INVALID_OPERATION             = 0x0502;
        public static final int GL_INVALID_FRAMEBUFFER_OPERATION = 0x0506;
        public static final int GL_OUT_OF_MEMORY                 = 0x0505;

        public static final int GL_POINTS         = 0x0000;
        public static final int GL_LINES          = 0x0001;
        public static final int GL_LINE_LOOP      = 0x0002;
        public static final int GL_LINE_STRIP     = 0x0003;
        public static final int GL_TRIANGLES      = 0x0004;
        public static final int GL_TRIANGLE_FAN   = 0x0006;
        public static final int GL_TRIANGLE_STRIP = 0x0005;

        public static final int GL_TRUE  = 0x0001;
        public static final int GL_FALSE = 0x0000;

        public static final int GL_LINK_STATUS    = 0x8B82;
        public static final int GL_COMPILE_STATUS = 0x8B81;

        public static final int GL_TEXTURE   = 0x1702;
        public static final int GL_TEXTURE0  = 0x84C0;
        public static final int GL_TEXTURE1  = 0x84C1;
        public static final int GL_TEXTURE2  = 0x84C2;
        public static final int GL_TEXTURE3  = 0x84C3;
        public static final int GL_TEXTURE4  = 0x84C4;
        public static final int GL_TEXTURE5  = 0x84C5;
        public static final int GL_TEXTURE6  = 0x84C6;
        public static final int GL_TEXTURE7  = 0x84C7;
        public static final int GL_TEXTURE8  = 0x84C8;
        public static final int GL_TEXTURE9  = 0x84C9;
        public static final int GL_TEXTURE10 = 0x84CA;
        public static final int GL_TEXTURE11 = 0x84CB;
        public static final int GL_TEXTURE12 = 0x84CC;
        public static final int GL_TEXTURE13 = 0x84CD;
        public static final int GL_TEXTURE14 = 0x84CE;
        public static final int GL_TEXTURE15 = 0x84CF;
        public static final int GL_TEXTURE16 = 0x84D0;
        public static final int GL_TEXTURE17 = 0x84D1;
        public static final int GL_TEXTURE18 = 0x84D2;
        public static final int GL_TEXTURE19 = 0x84D3;
        public static final int GL_TEXTURE20 = 0x84D4;
        public static final int GL_TEXTURE21 = 0x84D5;
        public static final int GL_TEXTURE22 = 0x84D6;
        public static final int GL_TEXTURE23 = 0x84D7;
        public static final int GL_TEXTURE24 = 0x84D8;
        public static final int GL_TEXTURE25 = 0x84D9;
        public static final int GL_TEXTURE26 = 0x84DA;
        public static final int GL_TEXTURE27 = 0x84DB;
        public static final int GL_TEXTURE28 = 0x84DC;
        public static final int GL_TEXTURE29 = 0x84DD;
        public static final int GL_TEXTURE30 = 0x84DE;
        public static final int GL_TEXTURE31 = 0x84DF;

        public static final int GL_LINEAR                = 0x2601;
        public static final int GL_LINEAR_MIPMAP_LINEAR  = 0x2703;
        public static final int GL_LINEAR_MIPMAP_NEAREST = 0x2701;

        public static final int GL_UNSIGNED_BYTE          = 0x1401;
        public static final int GL_UNSIGNED_INT           = 0x1405;
        public static final int GL_UNSIGNED_SHORT         = 0x1403;
        public static final int GL_UNSIGNED_SHORT_4_4_4_4 = 0x8033;
        public static final int GL_UNSIGNED_SHORT_5_5_5_1 = 0x8034;
        public static final int GL_UNSIGNED_SHORT_5_6_5   = 0x8363;

        public static final int GL_INT      = 0x1404;
        public static final int GL_INT_VEC2 = 0x8B53;
        public static final int GL_INT_VEC3 = 0x8B54;
        public static final int GL_INT_VEC4 = 0x8B55;

        public static final int GL_FLOAT      = 0x1406;
        public static final int GL_FLOAT_MAT2 = 0x8B5A;
        public static final int GL_FLOAT_MAT3 = 0x8B5B;
        public static final int GL_FLOAT_MAT4 = 0x8B5C;
        public static final int GL_FLOAT_VEC2 = 0x8B50;
        public static final int GL_FLOAT_VEC3 = 0x8B51;
        public static final int GL_FLOAT_VEC4 = 0x8B52;

        public static final int GL_RGB     = 0x1907;
        public static final int GL_RGB565  = 0x8D62;
        public static final int GL_RGB5_A1 = 0x8057;
        public static final int GL_RGBA    = 0x1908;
        public static final int GL_RGBA4   = 0x8056;

        public static final int GL_TEXTURE_MAG_FILTER = 0x2800;
        public static final int GL_TEXTURE_MIN_FILTER = 0x2801;
        public static final int GL_TEXTURE_WRAP_S     = 0x2802;
        public static final int GL_TEXTURE_WRAP_T     = 0x2803;

        public static final int GL_VERTEX_SHADER   = 0x8B31;
        public static final int GL_FRAGMENT_SHADER = 0x8B30;

        private Constants()
        {
        }
    }
}
