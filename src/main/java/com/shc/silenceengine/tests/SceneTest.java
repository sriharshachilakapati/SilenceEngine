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
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.cameras.PerspCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.SceneNode;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class SceneTest extends Game
{
    private PerspCam     cam;
    private OrthoCam     fontCam;
    private TrueTypeFont font;
    private Scene        scene;

    public static void main(String[] args)
    {
        new SceneTest().start();
    }

    public void dispose()
    {
        scene.destroy();
        font.dispose();
    }

    public void init()
    {
        cam = new PerspCam().initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
        fontCam = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());

        font = new TrueTypeFont("Arial", TrueTypeFont.STYLE_NORMAL, 18);

        scene = new Scene();
        SceneObject root = new SceneObject(new Vector2(0, 0), Color.RED);
        {
            root.addChild(new SceneObject(new Vector2(-0.5f, 0.5f), Color.GREEN));
            root.addChild(new SceneObject(new Vector2(0.5f, 0.5f), Color.BLUE));
            root.addChild(new SceneObject(new Vector2(0, -0.5f), Color.SILVER));
        }
        scene.addChild(root);

        scene.init();
    }

    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        scene.update(delta);

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
    }

    public void render(float delta, Batcher batcher)
    {
        cam.apply();
        scene.render(delta, batcher);

        fontCam.apply();
        font.drawString(batcher, "FPS: " + getFPS(), 10, 10);
        font.drawString(batcher, "\nUPS: " + getUPS(), 10, 10);
        font.drawString(batcher, "\n\nDelta: " + delta, 10, 10);
    }

    public void resize()
    {
        cam.initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
        fontCam.initProjection(Display.getWidth(), Display.getHeight());
    }

    public static class SceneObject extends SceneNode
    {
        private Color   color;
        private float   rotation;
        private Vector2 position;

        public SceneObject(Vector2 position, Color color)
        {
            this.color = color;
            this.position = position;
        }

        public void update(float delta)
        {
            rotation += 60 * delta;

            float z = -Math.abs((float) Math.sin(TimeUtils.currentSeconds()));

            getLocalTransform().reset().rotateSelf(Vector3.AXIS_Z, rotation)
                    .translateSelf(new Vector3(position.getX(), position.getY(), z));
        }

        public void render(float delta, Batcher batcher)
        {
            batcher.applyTransform(getTransform());
            batcher.begin();
            {
                batcher.vertex(0, 0.1f);
                batcher.color(color);

                batcher.vertex(-0.1f, -0.1f);
                batcher.color(color);

                batcher.vertex(0.1f, -0.1f);
                batcher.color(color);
            }
            batcher.end();
        }
    }
}
