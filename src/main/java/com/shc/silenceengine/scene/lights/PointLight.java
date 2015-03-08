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

package com.shc.silenceengine.scene.lights;

import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.programs.PointLightProgram;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.SceneComponent;

/**
 * @author Sri Harsha Chilakapati
 */
public class PointLight extends SceneComponent
{
    private Vector3 position;
    private Color   color;
    private float   intensity;
    private float   range;

    public PointLight()
    {
        this(Vector3.ZERO, Color.WHITE);
    }

    public PointLight(Vector3 position, Color color)
    {
        this(position, color, 1);
    }

    public PointLight(Vector3 position, Color color, float intensity)
    {
        this(position, color, intensity, 10);
    }

    public PointLight(Vector3 position, Color color, float intensity, float range)
    {
        this.position = position.copy();
        this.color = color.copy();
        this.intensity = intensity;
        this.range = range;
    }

    public void use()
    {
        Program program = PointLightProgram.getInstance();
        program.use();

        program.setUniform("light.position", position);
        program.setUniform("light.color", color);
        program.setUniform("light.intensity", intensity);
        program.setUniform("light.range", range);
    }

    public void release()
    {
        Program.DEFAULT.use();
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setPosition(Vector3 position)
    {
        this.position.set(position);
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color.set(color);
    }
}
