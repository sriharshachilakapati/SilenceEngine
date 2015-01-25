package com.shc.silenceengine.models.md2;

/**
 * @author Sri Harsha Chilakapati
 */
public class MD2Header
{
    /**
     * The Magic number. Must equal "IDP2" for a valid header.
     */
    public int ident;

    /**
     * The MD2 file version. Must be equal to the number 8.
     */
    public int version;

    /**
     * The width of the texture.
     */
    public int skinWidth;

    /**
     * The height of the texture.
     */
    public int skinHeight;

    /**
     * The size of a single frame, in bytes
     */
    public int frameSize;

    /**
     * The number of textures in the model.
     */
    public int numSkins;

    /**
     * The number of vertices in the model.
     */
    public int numXYZ;

    /**
     * The number of texture coordinates in the model.
     */
    public int numST;

    /**
     * The number of triangles in the model
     */
    public int numTris;

    /**
     * The number of OpenGL commands.
     */
    public int numGLCommands;

    /**
     * The total number of animation frames in the model.
     */
    public int numFrames;

    /**
     * The offset to the skins.
     */
    public int ofsSkins;

    /**
     * The offset to the texture coordinates.
     */
    public int ofsST;

    /**
     * The offset to the triangles.
     */
    public int ofsTris;

    /**
     * The offset to the frames.
     */
    public int ofsFrames;

    /**
     * The offset to the OpenGL commands.
     */
    public int ofsGLCommands;

    /**
     * The offset to the end of file.
     */
    public int ofsEOF;
}
