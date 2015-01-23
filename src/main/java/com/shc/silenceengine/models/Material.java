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

    private float dissolve;
    private float specularPower;
    private float illumination;

    private String name;

    public Material()
    {
        ambient = new Color(1, 1, 1, 1);
        diffuse = new Color(1, 1, 1, 1);
        specular = new Color(1, 1, 1, 1);

        dissolve = 1;
        specularPower = 100;
        illumination = 2;

        this.name = "Default";
    }

    public Material(Material m)
    {
        this.ambient = m.ambient;
        this.diffuse = m.diffuse;
        this.specular = m.specular;

        this.diffuseMap = m.diffuseMap;
        this.normalMap = m.normalMap;
        this.specularMap = m.specularMap;

        this.dissolve = m.dissolve;
        this.specularPower = m.specularPower;
        this.illumination = m.illumination;

        this.diffuseMap = Texture.EMPTY;
        this.normalMap = Texture.EMPTY;
        this.specularMap = Texture.EMPTY;
    }

    public Color getAmbient()
    {
        return ambient;
    }

    public void setAmbient(Color ambient)
    {
        this.ambient = ambient;
    }

    public Color getDiffuse()
    {
        return diffuse;
    }

    public void setDiffuse(Color diffuse)
    {
        this.diffuse = diffuse;
    }

    public Color getSpecular()
    {
        return specular;
    }

    public void setSpecular(Color specular)
    {
        this.specular = specular;
    }

    public Texture getDiffuseMap()
    {
        return diffuseMap;
    }

    public void setDiffuseMap(Texture diffuseMap)
    {
        this.diffuseMap = diffuseMap;
    }

    public Texture getNormalMap()
    {
        return normalMap;
    }

    public void setNormalMap(Texture normalMap)
    {
        this.normalMap = normalMap;
    }

    public Texture getSpecularMap()
    {
        return specularMap;
    }

    public void setSpecularMap(Texture specularMap)
    {
        this.specularMap = specularMap;
    }

    public float getDissolve()
    {
        return dissolve;
    }

    public void setDissolve(float dissolve)
    {
        this.dissolve = dissolve;
    }

    public float getSpecularPower()
    {
        return specularPower;
    }

    public void setSpecularPower(float specularPower)
    {
        this.specularPower = specularPower;
    }

    public float getIllumination()
    {
        return illumination;
    }

    public void setIllumination(float illumination)
    {
        this.illumination = illumination;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
