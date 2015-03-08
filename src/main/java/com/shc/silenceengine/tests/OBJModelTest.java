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

package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.cameras.PerspCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.models.Model;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.entity.ModelEntity;
import com.shc.silenceengine.scene.lights.PointLight;

/**
 * @author Sri Harsha Chilakapati
 */
public class OBJModelTest extends Game
{
    private PerspCam    cam;
    private Model       model;
    private ModelEntity entity;

    private PointLight cameraLight;

    private Scene scene;

    public static void main(String[] args)
    {
        new OBJModelTest().start();
    }

    public void dispose()
    {
        model.dispose();
    }

    public void init()
    {
        cam = new PerspCam().initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
        cam.setPosition(new Vector3(0, 0, 2.5f));

        model = Model.load("resources/monkey.obj");

        scene = new Scene();
        {
            // Add the ModelEntity to the scene
            scene.addChild(entity = new ModelEntity(model, new Cuboid(Vector3.ZERO, 1, 1, 1)));

            // Add some lights, so that we can see the model
            scene.addComponent(new PointLight(new Vector3(-1, -1, 1), Color.BLUE));
            scene.addComponent(new PointLight(new Vector3(+1, -1, 1), Color.RED));
            scene.addComponent(new PointLight(new Vector3(+1, +1, 1), Color.GREEN));
            scene.addComponent(cameraLight = new PointLight(cam.getPosition(), Color.DARK_KHAKI));
        }
        scene.init();
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

        entity.rotate(90 * delta, 90 * delta, 90 * delta);
        cameraLight.setPosition(cam.getPosition());

        scene.update(delta);

        Display.setTitle("Total Memory: " + (getTotalMemory() / 1048576) + "MB / Free Memory: " + (getFreeMemory() / 1048576) + "MB / Used Memory: " + (getUsedMemory() / 1048576) + "MB");
    }

    public void render(float delta, Batcher batcher)
    {
        cam.apply();
        scene.render(delta, batcher);
    }

    public void resize()
    {
        cam.initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
    }
}
