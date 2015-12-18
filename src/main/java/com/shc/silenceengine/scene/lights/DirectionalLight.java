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
import com.shc.silenceengine.backend.lwjgl3.opengl.Program;
import com.shc.silenceengine.graphics.programs.DirectionalLightProgram;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.SceneComponent;

/**
 * @author Sri Harsha Chilakapati
 */
public class DirectionalLight extends SceneComponent
{
    private Vector3 direction;
    private Color   color;
    private float   intensity;

    public DirectionalLight(Vector3 direction, Color color)
    {
        this(direction, color, 1);
    }

    public DirectionalLight(Vector3 direction, Color color, float intensity)
    {
        this.direction = direction.copy();
        this.color = color.copy();
        this.intensity = intensity;
    }

    public void use()
    {
        Program program = DirectionalLightProgram.getInstance();
        program.use();

        program.setUniform("light.direction", direction);
        program.setUniform("light.color", color);
        program.setUniform("light.intensity", intensity);
    }

    public void release()
    {
        Program.DEFAULT.use();
    }

    public Vector3 getDirection()
    {
        return direction;
    }

    public void setDirection(Vector3 direction)
    {
        this.direction.set(direction);
    }

    public float getIntensity()
    {
        return intensity;
    }

    public void setIntensity(float intensity)
    {
        this.intensity = intensity;
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
