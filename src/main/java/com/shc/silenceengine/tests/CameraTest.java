package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.*;
import com.shc.silenceengine.graphics.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class CameraTest extends Game
{
    private Texture texture;
    private Batcher batcher;

    private OrthoCam orthoCam;
    private PerspCam perspCam;

    private Transform transform;

    public void init()
    {
        batcher = new Batcher();
        transform = new Transform();

        orthoCam = new OrthoCam();
        perspCam = new PerspCam();

        texture = Texture.fromResource("resources/texture2.png");
    }

    public void update(long delta)
    {
        transform.rotate(Vector3.AXIS_Y, 4 * delta / 1000.0f);

        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();
    }

    public void render(long delta)
    {
        texture.bind();

        // 2D triangle in the left-top
        batcher.begin(orthoCam);
        {
            batcher.addTriangle(new Vector2(50, 0),   new Vector2(0, 100), new Vector2(100, 100),
                                new Vector2(0.5f, 0), new Vector2(0, 1),   new Vector2(1, 1));
        }
        batcher.end();

        // 3D triangle in the center
        batcher.begin(perspCam, transform);
        {
            batcher.addTriangle(new Vector2(+0.0f, +0.5f), new Vector2(-0.5f, -0.5f), new Vector2(+0.5f, -0.5f),
                                new Vector2(0.5f, 0),      new Vector2(0, 1),         new Vector2(1, 1));
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
        batcher.dispose();
    }

    public static void main(String[] args)
    {
        new CameraTest().start();
    }
}
