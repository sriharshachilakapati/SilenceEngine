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
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Touch;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.Component;
import com.shc.silenceengine.scene.Entity;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.components.PolygonRenderComponent;

/**
 * @author Sri Harsha Chilakapati
 */
public class EntityCollisionTest2D extends SilenceTest
{
    private static CollisionTag heroTag    = new CollisionTag();
    private static CollisionTag subHeroTag = new CollisionTag();
    private static CollisionTag wallsTag   = new CollisionTag();

    private OrthoCam camera;
    private Scene    scene;

    @Override
    public void init()
    {
        if (SilenceEngine.display.getPlatform() != SilenceEngine.Platform.ANDROID)
            SilenceEngine.input.setSimulateTouch(true);

        camera = new OrthoCam(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());

        IGraphicsDevice.Programs.dynamic.applyToRenderer(IGraphicsDevice.Renderers.dynamic);

        scene = new Scene();
        CollisionSystem2D collider = new CollisionSystem2D(new DynamicTree2D());

        collider.register(heroTag, wallsTag);
        collider.register(subHeroTag, wallsTag);

        scene.registerUpdateSystem(collider);
        scene.registerRenderSystem(new SceneRenderSystem());

        for (int i = 0; i < SilenceEngine.display.getWidth(); i += 48)
        {
            Entity wall = new Entity();
            wall.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall.addComponent(new PolygonRenderComponent());
            wall.transformComponent.setPosition(i + 24, 24);

            Entity wall2 = new Entity();
            wall2.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall2.addComponent(new PolygonRenderComponent());
            wall2.transformComponent.setPosition(i + 24, SilenceEngine.display.getHeight() - 24);

            scene.addEntity(wall);
            scene.addEntity(wall2);
        }

        for (int i = 48; i < SilenceEngine.display.getHeight() - 48; i += 48)
        {
            Entity wall = new Entity();
            wall.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall.addComponent(new PolygonRenderComponent());
            wall.transformComponent.setPosition(24, i + 24);

            Entity wall2 = new Entity();
            wall2.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall2.addComponent(new PolygonRenderComponent());
            wall2.transformComponent.setPosition(SilenceEngine.display.getWidth() - 24, i + 24);

            scene.addEntity(wall);
            scene.addEntity(wall2);
        }

        scene.addEntity(new Hero(scene));
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        scene.update(deltaTime);

        SilenceEngine.display.setTitle("UPS: " + SilenceEngine.gameLoop.getUPS()
                                       + " | FPS: " + SilenceEngine.gameLoop.getFPS()
                                       + " | RC: " + IGraphicsDevice.Data.renderCallsThisFrame
                                       + " | EntityCollisionTest2D");
    }

    @Override
    public void render(float deltaTime)
    {
        camera.apply();
        scene.render(deltaTime);
    }

    @Override
    public void resized()
    {
        camera.initProjection(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
        GLContext.viewport(0, 0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    private static class Hero extends Entity
    {
        private static PolygonRenderComponent renderComponent;
        private static PolygonRenderComponent subRenderComponent;

        private static Entity subHero;

        Hero(Scene scene)
        {
            transformComponent.setPosition(SilenceEngine.display.getWidth() / 2 - 24, SilenceEngine.display.getHeight() / 2 - 24);

            addComponent(new Behaviour());
            addComponent(new CollisionComponent2D(heroTag, new Rectangle(48, 48).createPolygon(), this::onHeroCollision));
            addComponent(renderComponent = new PolygonRenderComponent(Color.AQUA));

            subHero = new Entity();
            subHero.transformComponent.setPosition(100, 30);

            subHero.addComponent(new CollisionComponent2D(subHeroTag, new Rectangle(48, 48).createPolygon(), this::onSubHeroCollision));
            subHero.addComponent(subRenderComponent = new PolygonRenderComponent(Color.GREEN));

            subHero.transformComponent.setParent(transformComponent);
            scene.addEntity(subHero);
        }

        private void onHeroCollision(CollisionComponent2D other)
        {
            renderComponent.color.set(Color.YELLOW_GREEN);
        }

        private void onSubHeroCollision(CollisionComponent2D other)
        {
            subRenderComponent.color.set(Color.WHITE);
        }

        private static class Behaviour extends Component
        {
            @Override
            protected void onUpdate(float deltaTime)
            {
                transformComponent.rotate(45 * deltaTime);

                if (Touch.isFingerDown(Touch.FINGER_0))
                    transformComponent.setPosition(Touch.getFingerPosition(Touch.FINGER_0));

                subHero.transformComponent.rotate(45 * deltaTime);

                renderComponent.color.set(Color.AQUA);
                subRenderComponent.color.set(Color.GREEN);
            }
        }
    }
}
