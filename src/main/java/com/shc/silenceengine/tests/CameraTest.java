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
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.cameras.PerspCam;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Mouse;
import com.shc.silenceengine.math.Transform;
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

    public static void main(String[] args)
    {
        new CameraTest().start();
    }

    public void dispose()
    {
        texture.dispose();
    }

    public void init()
    {
        transform = new Transform();

        orthoCam = new OrthoCam();
        perspCam = new PerspCam();

        texture = Texture.fromResource("resources/texture2.png");

        Display.hideCursor();
    }

    public void update(float delta)
    {
        transform.rotateSelf(Vector3.AXIS_Y, 60 * delta);

        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        float speed = 2 * delta;

        if (Keyboard.isPressed(Keyboard.KEY_W))
            perspCam.moveForward(speed);

        if (Keyboard.isPressed(Keyboard.KEY_S))
            perspCam.moveBackward(speed);

        if (Keyboard.isPressed(Keyboard.KEY_A))
            perspCam.moveLeft(speed);

        if (Keyboard.isPressed(Keyboard.KEY_D))
            perspCam.moveRight(speed);

        if (Keyboard.isPressed(Keyboard.KEY_Q))
            perspCam.moveUp(speed);

        if (Keyboard.isPressed(Keyboard.KEY_E))
            perspCam.moveDown(speed);

        if (Keyboard.isPressed(Keyboard.KEY_UP))
            perspCam.rotateX(1);

        if (Keyboard.isPressed(Keyboard.KEY_DOWN))
            perspCam.rotateX(-1);

        if (Keyboard.isPressed(Keyboard.KEY_LEFT))
            perspCam.rotateY(1);

        if (Keyboard.isPressed(Keyboard.KEY_RIGHT))
            perspCam.rotateY(-1);

        // Mouse look!
        if (Keyboard.isPressed(Keyboard.KEY_SPACE))
        {
            perspCam.rotateX(-Mouse.getDY() * speed);
            perspCam.rotateY(-Mouse.getDX() * speed);
        }
    }

    public void render(float delta, Batcher batcher)
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
}
