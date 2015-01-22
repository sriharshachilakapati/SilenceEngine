package com.shc.silenceengine.models;

import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.Texture;

/**
 * @author Sri Harsha Chilakapati
 */
public class Material
{
    private Color ambient;
    private Color diffuse;
    private Color specular;

    private Texture diffuseMap;
    private Texture normalMap;
    private Texture specularMap;
}
