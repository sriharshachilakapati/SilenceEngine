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
    private Color color;

    public PointLight()
    {
        this(Vector3.ZERO, Color.WHITE);
    }

    public PointLight(Vector3 position, Color color)
    {
        this.position = position.copy();
        this.color = color.copy();
    }

    public void use()
    {
        Program program = PointLightProgram.getInstance();
        program.use();

        program.setUniform("light.position", position);
        program.setUniform("light.color", color);
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
