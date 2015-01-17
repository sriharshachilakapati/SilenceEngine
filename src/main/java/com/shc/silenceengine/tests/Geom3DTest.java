package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.geom3d.Cuboid;
import com.shc.silenceengine.geom3d.Sphere;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.PerspCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.RenderUtils;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Geom3DTest extends Game
{
    private PerspCam camera;

    private Cuboid cube;
    private Sphere sphere;

    public void init()
    {
        camera = new PerspCam().initProjection(70, Display.getAspectRatio(), 0.01f, 1000f);
        camera.setPosition(new Vector3(0, 0, 2));

        cube = new Cuboid(new Vector3(), 1, 1, 1);
        sphere = new Sphere(new Vector3(), 1);
    }

    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            end();

        if (!Keyboard.isPressed(Keyboard.KEY_SPACE))
        {
            float movement = (float) Math.sin(TimeUtils.currentSeconds()) * 2;

            cube.getPosition().x = movement;
            cube.getPosition().y = movement;
            cube.getPosition().z = movement;

            sphere.getPosition().x = -movement;
            sphere.getPosition().y = -movement;
            sphere.getPosition().z = -movement;
        }

        if (Keyboard.isPressed(Keyboard.KEY_W))
            camera.moveForward(delta);

        if (Keyboard.isPressed(Keyboard.KEY_S))
            camera.moveBackward(delta);

        if (Keyboard.isPressed(Keyboard.KEY_A))
            camera.moveLeft(delta);

        if (Keyboard.isPressed(Keyboard.KEY_D))
            camera.moveRight(delta);

        if (Keyboard.isPressed(Keyboard.KEY_Q))
            camera.moveUp(delta);

        if (Keyboard.isPressed(Keyboard.KEY_E))
            camera.moveDown(delta);

        if (Keyboard.isPressed(Keyboard.KEY_UP))
            camera.rotateX(1);

        if (Keyboard.isPressed(Keyboard.KEY_DOWN))
            camera.rotateX(-1);

        if (Keyboard.isPressed(Keyboard.KEY_LEFT))
            camera.rotateY(1);

        if (Keyboard.isPressed(Keyboard.KEY_RIGHT))
            camera.rotateY(-1);
    }

    public void render(float delta, Batcher batcher)
    {
        camera.apply();

        if (!sphere.intersects(cube))
        {
            RenderUtils.fillPolyhedron(batcher, cube, Color.BLUE);
            RenderUtils.fillPolyhedron(batcher, sphere, Color.DARK_RED);
        }

        RenderUtils.tracePolyhedron(batcher, cube, Color.GREEN);
        RenderUtils.tracePolyhedron(batcher, sphere, Color.GREEN);
    }

    public void resize()
    {
        camera.initProjection(70, Display.getAspectRatio(), 0.01f, 1000f);
    }

    public static void main(String[] args)
    {
        new Geom3DTest().start();
    }
}
