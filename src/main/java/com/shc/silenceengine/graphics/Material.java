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

package com.shc.silenceengine.graphics;

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

        this.diffuseMap = Texture.EMPTY;
        this.normalMap = Texture.EMPTY;
        this.specularMap = Texture.EMPTY;

        this.name = "Default";
    }

    public Material(String name)
    {
        this();
        setName(name);
    }

    public Material(Material m)
    {
        this.ambient = m.ambient.copy();
        this.diffuse = m.diffuse.copy();
        this.specular = m.specular.copy();

        this.diffuseMap = m.diffuseMap;
        this.normalMap = m.normalMap;
        this.specularMap = m.specularMap;

        this.dissolve = m.dissolve;
        this.specularPower = m.specularPower;
        this.illumination = m.illumination;
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

    @Override
    public int hashCode()
    {
        int result = getAmbient().hashCode();
        result = 31 * result + getDiffuse().hashCode();
        result = 31 * result + getSpecular().hashCode();
        result = 31 * result + getDiffuseMap().hashCode();
        result = 31 * result + getNormalMap().hashCode();
        result = 31 * result + getSpecularMap().hashCode();
        result = 31 * result + (getDissolve() != +0.0f ? Float.floatToIntBits(getDissolve()) : 0);
        result = 31 * result + (getSpecularPower() != +0.0f ? Float.floatToIntBits(getSpecularPower()) : 0);
        result = 31 * result + (getIllumination() != +0.0f ? Float.floatToIntBits(getIllumination()) : 0);
        result = 31 * result + getName().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Material material = (Material) o;

        return Float.compare(material.getDissolve(), getDissolve()) == 0 &&
               Float.compare(material.getSpecularPower(), getSpecularPower()) == 0 &&
               Float.compare(material.getIllumination(), getIllumination()) == 0 &&
               getAmbient().equals(material.getAmbient()) &&
               getDiffuse().equals(material.getDiffuse()) &&
               getSpecular().equals(material.getSpecular()) &&
               getDiffuseMap().equals(material.getDiffuseMap()) &&
               getNormalMap().equals(material.getNormalMap()) &&
               getSpecularMap().equals(material.getSpecularMap()) &&
               getName().equals(material.getName());
    }
}
