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
import com.shc.silenceengine.graphics.Material;
import com.shc.silenceengine.graphics.cameras.PerspCam;
import com.shc.silenceengine.graphics.models.Face;
import com.shc.silenceengine.graphics.models.Mesh;
import com.shc.silenceengine.graphics.models.Model;
import com.shc.silenceengine.graphics.models.ModelBuilder;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.scene.Scene3D;
import com.shc.silenceengine.scene.entity.Entity3D;
import com.shc.silenceengine.scene.lights.PointLight;

import java.io.BufferedWriter;

/**
 * @author Sri Harsha Chilakapati
 */
public class ModelBuilderTest extends Game
{
    private Model model;
    private PerspCam cam;
    private PointLight camLight;

    private Scene3D scene;

    public static void main(String[] args)
    {
        new ModelBuilderTest().start();
    }

    public void init()
    {
        cam = new PerspCam().initProjection(70, Display.getHeight(), 0.1f, 100f);
        camLight = new PointLight(cam.getPosition(), Color.FLORAL_WHITE);

        model = new ModelBuilder().createNewMesh()
                .setMaterial(new Material("Red")
                        .setDiffuse(new Color(1, 0, 0, 1))
                        .setAmbient(new Color(0.001f, 0.001f, 0.0011f))
                        .setDissolve(1).setIllumination(2).setSpecular(new Color(0.1f, 0.1f, 0.1f))
                        .setSpecularPower(96.07843f))
                .createBox(1, 1, 1).build();

        cam.setPosition(new Vector3(1)).lookAt(Vector3.ZERO);

        scene = new Scene3D();
        scene.addChild(new Entity3D(model, new Cuboid(Vector3.ZERO, 1, 1, 1)));
        scene.addComponent(camLight);
    }

    private void printMesh(Mesh mesh, FilePath path)
    {
        try (BufferedWriter writer = new BufferedWriter(path.getWriter()))
        {
            for (Face face : mesh.getFaces())
            {
                Vector3 v1 = mesh.getVertices().get((int) face.vertexIndex.x);
                Vector3 v2 = mesh.getVertices().get((int) face.vertexIndex.y);
                Vector3 v3 = mesh.getVertices().get((int) face.vertexIndex.z);

                Vector3 n1 = mesh.getNormals().get((int) face.normalIndex.x);
                Vector3 n2 = mesh.getNormals().get((int) face.normalIndex.y);
                Vector3 n3 = mesh.getNormals().get((int) face.normalIndex.z);

                writer.write(v1.toString() + " " + n1.toString());
                writer.newLine();
                writer.write(v2.toString() + " " + n2.toString());
                writer.newLine();
                writer.write(v3.toString() + " " + n3.toString());
                writer.newLine();
            }

            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void update(float delta)
    {
        Display.setTitle("UPS: " + Game.getUPS() + " | FPS: " + Game.getFPS() + " | RC: " + SilenceEngine.graphics.renderCallsPerFrame);

        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
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

        camLight.setPosition(cam.getPosition());
        scene.update(delta);
    }

    public void render(float delta, Batcher batcher)
    {
        cam.apply();
        scene.render(delta);
    }

    public void resize()
    {
        cam.initProjection(70, Display.getAspectRatio(), 0.1f, 100f);
    }

    public void dispose()
    {
        scene.destroy();
        model.dispose();
    }
}
