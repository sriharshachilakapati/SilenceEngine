/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

import com.shc.silenceengine.collision.CollisionTag;
import com.shc.silenceengine.collision.broadphase.DynamicTree2D;
import com.shc.silenceengine.collision.colliders.CollisionSystem2D;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.graphics.SceneRenderSystem;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Touch;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.Component;
import com.shc.silenceengine.scene.Entity;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.components.BoundsRenderComponent2D;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.components.SpriteComponent;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteRendererTest extends SilenceTest
{
    private static CollisionTag heroTag    = new CollisionTag();
    private static CollisionTag subHeroTag = new CollisionTag();
    private static CollisionTag wallsTag   = new CollisionTag();

    private Scene    scene;
    private Sprite   sprite;
    private OrthoCam camera;

    @Override
    public void init()
    {
        IGraphicsDevice.Programs.dynamic.applyToRenderer(IGraphicsDevice.Renderers.dynamic);

        // Normal initialization
        camera = new OrthoCam(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());

        SilenceEngine.io.getImageReader().readImage(FilePath.getResourceFile("test_resources/test_texture.png"), image ->
        {
            sprite = new Sprite(Texture.fromImage(image).getSubTexture(0, 0, 1, 1, 48, 48));
            image.dispose();
            initTest();
        });
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        if (scene == null)
            return;

        scene.update(deltaTime);

        SilenceEngine.display.setTitle("UPS: " + SilenceEngine.gameLoop.getUPS()
                                       + " | FPS: " + SilenceEngine.gameLoop.getFPS()
                                       + " | RC: " + IGraphicsDevice.Data.renderCallsThisFrame
                                       + " | SpriteRendererTest");
    }

    @Override
    public void render(float deltaTime)
    {
        if (scene == null)
            return;

        camera.apply();
        scene.render(deltaTime);
    }

    @Override
    public void resized()
    {
        camera.initProjection(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
        camera.apply();
        GLContext.viewport(0, 0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    private void initTest()
    {
        if (SilenceEngine.display.getPlatform() != SilenceEngine.Platform.ANDROID)
            SilenceEngine.input.setSimulateTouch(true);

        scene = new Scene();
        CollisionSystem2D collider = new CollisionSystem2D(new DynamicTree2D());

        collider.register(heroTag, wallsTag);
        collider.register(subHeroTag, wallsTag);

        scene.registerUpdateSystem(collider);
        scene.registerRenderSystem(new SceneRenderSystem());

        Entity wall = new Entity();
        wall.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
        wall.addComponent(new SpriteComponent(sprite));
        wall.addComponent(new BoundsRenderComponent2D());
        wall.transformComponent.setPosition(SilenceEngine.display.getWidth() / 2, SilenceEngine.display.getHeight() / 2);
        scene.addEntity(wall);

        for (int i = 0; i < SilenceEngine.display.getWidth(); i += 48)
        {
            wall = new Entity();
            wall.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall.addComponent(new SpriteComponent(sprite));
            wall.addComponent(new BoundsRenderComponent2D());
            wall.transformComponent.setPosition(i + 24, 24);

            Entity wall2 = new Entity();
            wall2.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall2.addComponent(new SpriteComponent(sprite));
            wall2.addComponent(new BoundsRenderComponent2D());
            wall2.transformComponent.setPosition(i + 24, SilenceEngine.display.getHeight() - 24);

            scene.addEntity(wall);
            scene.addEntity(wall2);
        }

        for (int i = 48; i < SilenceEngine.display.getHeight() - 48; i += 48)
        {
            wall = new Entity();
            wall.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall.addComponent(new SpriteComponent(sprite));
            wall.addComponent(new BoundsRenderComponent2D());
            wall.transformComponent.setPosition(24, i + 24);

            Entity wall2 = new Entity();
            wall2.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall2.addComponent(new SpriteComponent(sprite));
            wall2.addComponent(new BoundsRenderComponent2D());
            wall2.transformComponent.setPosition(SilenceEngine.display.getWidth() - 24, i + 24);

            scene.addEntity(wall);
            scene.addEntity(wall2);
        }

        scene.addEntity(new Hero(sprite, scene));
    }

    private static class Hero extends Entity
    {
        private static SpriteComponent renderComponent;
        private static SpriteComponent subRenderComponent;

        private static Entity subHero;

        Hero(Sprite sprite, Scene scene)
        {
            transformComponent.setPosition(SilenceEngine.display.getWidth() / 2 - 24, SilenceEngine.display.getHeight() / 2 - 24);

            addComponent(new Behaviour());

            addComponent(new CollisionComponent2D(heroTag, new Rectangle(48, 48).createPolygon(), this::onHeroCollision));
            addComponent(renderComponent = new SpriteComponent(sprite));
            addComponent(new BoundsRenderComponent2D());

            subHero = new Entity();
            subHero.transformComponent.setPosition(100, 30);

            subHero.addComponent(new CollisionComponent2D(subHeroTag, new Rectangle(48, 48).createPolygon(), this::onSubHeroCollision));
            subHero.addComponent(subRenderComponent = new SpriteComponent(sprite));
            subHero.addComponent(new BoundsRenderComponent2D());

            subHero.transformComponent.setParent(transformComponent);
            scene.addEntity(subHero);
        }

        private void onHeroCollision(CollisionComponent2D other)
        {
            renderComponent.tint.set(Color.YELLOW_GREEN);
        }

        private void onSubHeroCollision(CollisionComponent2D other)
        {
            subRenderComponent.tint.set(Color.RED);
        }

        private static class Behaviour extends Component
        {
            @Override
            public void onUpdate(float deltaTime)
            {
                transformComponent.rotate(45 * deltaTime);

                if (Touch.isFingerDown(Touch.FINGER_0))
                    transformComponent.setPosition(Touch.getFingerPosition(Touch.FINGER_0));

                subHero.transformComponent.rotate(45 * deltaTime);

                renderComponent.tint.set(Color.AQUA);
                subRenderComponent.tint.set(Color.GREEN);
            }
        }
    }
}
