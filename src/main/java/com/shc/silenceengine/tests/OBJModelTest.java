package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.PerspCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.models.Model;

/**
 * @author Sri Harsha Chilakapati
 */
public class OBJModelTest extends Game
{
    private PerspCam cam;
    private Model model;
    private Transform modelTransform;

    public void init()
    {
        cam = new PerspCam().initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
        cam.setPosition(new Vector3(0, 0, 2.5f));

        modelTransform = new Transform();

        model = Model.load("resources/monkey.obj");
    }

    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            end();

        if (Keyboard.isPressed(Keyboard.KEY_W))
            cam.moveForward(delta);

        if (Keyboard.isPressed(Keyboard.KEY_S))
            cam.moveBackward(delta);

        if (Keyboard.isPressed(Keyboard.KEY_A))
            cam.moveLeft(delta);

        if (Keyboard.isPressed(Keyboard.KEY_D))
            cam.moveRight(delta);

        if (Keyboard.isPressed(Keyboard.KEY_Q))
            cam.moveUp(delta);

        if (Keyboard.isPressed(Keyboard.KEY_E))
            cam.moveDown(delta);

        if (Keyboard.isPressed(Keyboard.KEY_UP))
            cam.rotateX(1);

        if (Keyboard.isPressed(Keyboard.KEY_DOWN))
            cam.rotateX(-1);

        if (Keyboard.isPressed(Keyboard.KEY_LEFT))
            cam.rotateY(1);

        if (Keyboard.isPressed(Keyboard.KEY_RIGHT))
            cam.rotateY(-1);

        modelTransform.rotate(Vector3.AXIS_Y, 90 * delta);
    }

    public void render(float delta, Batcher batcher)
    {
        cam.apply();

        model.render(delta, batcher, modelTransform);
    }

    public void resize()
    {
        cam.initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
    }

    public void dispose()
    {
        model.dispose();
    }

    public static void main(String[] args)
    {
        new OBJModelTest().start();
    }
}
