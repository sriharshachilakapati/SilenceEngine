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
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.ModelBatch;
import com.shc.silenceengine.graphics.cameras.PerspCam;
import com.shc.silenceengine.graphics.models.Model;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.lights.PointLight;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class ModelBatchTest extends Game
{
    private Model           model;
    private PerspCam        camera;
    private List<Transform> transforms;
    private Transform       transform;
    private PointLight camLight;

    public static void main(String[] args)
    {
        new ModelBatchTest().start();
    }

    @Override
    public void init()
    {
        camera = new PerspCam().initProjection(70, Display.getAspectRatio(), 0.1f, 1000f);

        model = Model.load("resources/cube.obj");

        transforms = new ArrayList<>();
        for (float i = -11; i < 10; i++)
        {
            for (float j = -11; j < 10; j++)
            {
                transforms.add(new Transform().translateSelf(new Vector3(i, 0, j)));
                j += 0.1f;
            }
            i += 0.1f;
        }

        transform = new Transform();

        camLight = new PointLight(new Vector3(0, 0, 0), Color.RED);

        camera.setPosition(new Vector3(0, 1, 0)).lookAt(new Vector3(0, 0.5f, -1));
    }

    @Override
    public void resize()
    {
        camera.initProjection(70, Display.getAspectRatio(), 0.1f, 1000f);
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            Game.end();

        Display.setTitle("UPS: " + Game.getUPS() + " | FPS: " + Game.getFPS() + " | RC: " + SilenceEngine.graphics.renderCallsPerFrame);

        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

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

        camLight.setPosition(camera.getPosition());

        transform.rotateSelf(Vector3.AXIS_Y, 45 * delta);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        camera.apply();
        renderScene();

        GL3Context.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GL3Context.depthMask(false);
        GL3Context.depthFunc(GL11.GL_EQUAL);

        camLight.use();
        {
            renderScene();
        }
        camLight.release();

        GL3Context.depthFunc(GL11.GL_LESS);
        GL3Context.depthMask(true);
        GL3Context.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void dispose()
    {
        model.dispose();
    }

    private void renderScene()
    {
        ModelBatch batch = SilenceEngine.graphics.getModelBatch();

        batch.begin(transform);
        {
            for (Transform transform : transforms)
            {
                batch.addModel(model, transform);
            }
        }
        batch.end();
    }
}
