package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.*;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class CameraTest extends Game
{
    private Texture texture;

    private OrthoCam orthoCam;
    private PerspCam perspCam;

    private Transform transform;

    public void init()
    {
        transform = new Transform();

        orthoCam = new OrthoCam();
        perspCam = new PerspCam();

        texture = Texture.fromResource("resources/texture2.png");
    }

    public void update(double delta)
    {
        transform.rotate(Vector3.AXIS_Y, (float) (4 * delta));

        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();
    }

    public void render(double delta, Batcher batcher)
    {
        texture.bind();

        orthoCam.apply();

        // 2D triangle in the left-top
        batcher.begin();
        {
            batcher.vertex(50, 0);
            batcher.texCoord(0.5f, 0);

            batcher.vertex(0, 100);
            batcher.texCoord(0, 1);

            batcher.vertex(100, 100);
            batcher.texCoord(1, 1);
        }
        batcher.end();

        perspCam.apply();

        // 3D triangle in the center
        batcher.applyTransform(transform);
        batcher.begin();
        {
            batcher.vertex(0, 0.5f);
            batcher.texCoord(0.5f, 0);

            batcher.vertex(-0.5f, -0.5f);
            batcher.texCoord(0, 1);

            batcher.vertex(0.5f, -0.5f);
            batcher.texCoord(1, 1);
        }
        batcher.end();
    }

    public void resize()
    {
        orthoCam.initProjection(Display.getWidth(), Display.getHeight());
        perspCam.initProjection(70, Display.getAspectRatio(), 0.1f, 100);
    }

    public void dispose()
    {
        texture.dispose();
    }

    public static void main(String[] args)
    {
        new CameraTest().start();
    }
}
