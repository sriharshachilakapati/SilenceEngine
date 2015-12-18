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
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteSheet;
import com.shc.silenceengine.graphics.cameras.PerspCam;
import com.shc.silenceengine.graphics.models.BillBoardModel;
import com.shc.silenceengine.graphics.models.Model;
import com.shc.silenceengine.backend.lwjgl3.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Mouse;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.scene.Scene3D;
import com.shc.silenceengine.scene.entity.Entity3D;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class BillBoardModelTest extends Game
{
    private PerspCam cam;

    private Model model;
    private Model model2;

    private SpriteSheet sheet;

    private Entity3D entity;

    private Scene3D scene;

    private long usedMemory = 0;

    public static void main(String[] args)
    {
        new BillBoardModelTest().start();
    }

    public void preInit()
    {
        Display.setTitle("BillBoard Model Test");
        Display.setSize(1280, 720);
    }

    public void init()
    {
        cam = new PerspCam().initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
        cam.setPosition(new Vector3(0, 0, 0.5f));

        sheet = new SpriteSheet(Texture.fromFilePath(FilePath.getResourceFile("resources/coin_sprinkle.png")), 32, 32);

        Sprite sprite = new Sprite();
        sprite.getAnimation().addFrame(sheet.getCell(0, 0), 0.25f, TimeUtils.Unit.SECONDS);
        sprite.getAnimation().addFrame(sheet.getCell(0, 1), 0.25f, TimeUtils.Unit.SECONDS);
        sprite.getAnimation().setEndCallback(sprite.getAnimation()::start);
        sprite.getAnimation().start();

        model = new BillBoardModel(sprite, 0.2f, 0.2f);
        model2 = new BillBoardModel(Texture.fromFilePath(FilePath.getResourceFile("resources/texture.png")), 0.8f, 0.8f);

        scene = new Scene3D();

        // Add the entity to the scene
        scene.addChild(entity = new Entity3D(model, new Cuboid(Vector3.ZERO, 1, 1, 1)));
        entity.setPosition(new Vector3(0, 0, 0.2f));
        scene.addChild(new Entity3D(model2, new Cuboid(Vector3.ZERO, 1, 1, 1)));
    }

    public void resize()
    {
        cam.initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
    }

    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            end();

        float speed = 2 * delta;

        if (Keyboard.isPressed(Keyboard.KEY_W))
            cam.moveForward(speed);

        if (Keyboard.isPressed(Keyboard.KEY_S))
            cam.moveBackward(speed);

        if (Keyboard.isPressed(Keyboard.KEY_A))
            cam.moveLeft(speed);

        if (Keyboard.isPressed(Keyboard.KEY_D))
            cam.moveRight(speed);

        if (Keyboard.isPressed(Keyboard.KEY_Q))
            cam.moveUp(speed);

        if (Keyboard.isPressed(Keyboard.KEY_E))
            cam.moveDown(speed);

        if (Keyboard.isPressed(Keyboard.KEY_UP))
            cam.rotateX(1);

        if (Keyboard.isPressed(Keyboard.KEY_DOWN))
            cam.rotateX(-1);

        if (Keyboard.isPressed(Keyboard.KEY_LEFT))
            cam.rotateY(1);

        if (Keyboard.isPressed(Keyboard.KEY_RIGHT))
            cam.rotateY(-1);

        // Mouse look!
        if (Keyboard.isPressed(Keyboard.KEY_SPACE))
        {
            if (!Display.isCursorGrabbed())
                Display.grabCursor();

            cam.rotateX(Mouse.getDY() * speed);
            cam.rotateY(Mouse.getDX() * speed);
        }
        else
            Display.showCursor();

        entity.rotate(0, 0, 90 * delta);
        scene.update(delta);

        // Calculate the new memory used again
        long newUsedMemory = (getUsedMemory() / 1048576);
        usedMemory = Math.max(usedMemory, newUsedMemory);

        Display.setTitle("Total Memory: " + (getTotalMemory() / 1048576) + "MB / Free Memory: " + (getFreeMemory() / 1048576) + "MB / Used Memory: " + newUsedMemory + "MB");
    }

    public void render(float delta, Batcher batcher)
    {
        cam.apply();
        scene.render(delta);
    }

    public void dispose()
    {
        model.dispose();
        model2.dispose();
        sheet.getTexture().dispose();
    }
}
