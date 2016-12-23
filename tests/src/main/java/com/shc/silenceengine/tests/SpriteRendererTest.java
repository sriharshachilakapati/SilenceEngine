/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
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
import com.shc.silenceengine.collision.colliders.SceneCollider2D;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.graphics.SpriteRenderer;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.programs.DynamicProgram;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Touch;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.Scene2D;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.components.IComponent2D;
import com.shc.silenceengine.scene.components.SpriteComponent;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteRendererTest extends SilenceTest
{
    private static SpriteRenderer  renderer;
    private static DynamicRenderer dynamicRenderer;
    private static DynamicProgram  dynamicProgram;
    private static SpriteBatch     batch;

    private static CollisionTag heroTag    = new CollisionTag();
    private static CollisionTag subHeroTag = new CollisionTag();
    private static CollisionTag wallsTag   = new CollisionTag();

    private static Sprite sprite;

    private OrthoCam        camera;
    private Scene2D         scene;
    private SceneCollider2D collider;

    @Override
    public void init()
    {
        // Needed because static variables persist between launches
        renderer = null;
        batch = null;

        // Normal initialization
        camera = new OrthoCam(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());

        SilenceEngine.io.getImageReader().readImage(FilePath.getResourceFile("test_resources/test_texture.png"), image ->
        {
            sprite = new Sprite(Texture.fromImage(image).getSubTexture(0, 0, 1, 1, 48, 48));
            image.dispose();
            DynamicProgram.create(dynamicProgram ->
            {
                dynamicRenderer = new DynamicRenderer();

                SpriteRendererTest.dynamicProgram = dynamicProgram;
                dynamicProgram.applyToRenderer(dynamicRenderer);
                SpriteRenderer.create(this::init);
            });
        });
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        if (renderer == null || dynamicProgram == null)
            return;

        scene.update(deltaTime);
        collider.checkCollisions();

        SilenceEngine.display.setTitle("UPS: " + SilenceEngine.gameLoop.getUPS()
                                       + " | FPS: " + SilenceEngine.gameLoop.getFPS()
                                       + " | RC: " + IGraphicsDevice.Data.renderCallsThisFrame
                                       + " | SpriteRendererTest");
    }

    @Override
    public void render(float deltaTime)
    {
        if (renderer == null || dynamicProgram == null)
            return;

        camera.apply();
        batch.begin();
        dynamicRenderer.begin(Primitive.LINES);
        scene.render(deltaTime);
        batch.end();
        dynamicProgram.use();
        dynamicRenderer.end();
    }

    @Override
    public void resized()
    {
        if (renderer == null || dynamicProgram == null)
            return;

        camera.initProjection(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
        camera.apply();
        GLContext.viewport(0, 0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    @Override
    public void dispose()
    {
        if (renderer != null)
            renderer.dispose();

        if (dynamicProgram != null)
            dynamicProgram.dispose();

        if (dynamicRenderer != null)
            dynamicRenderer.dispose();
    }

    private void init(SpriteRenderer renderer)
    {
        batch = new SpriteBatch(renderer);

        if (SilenceEngine.display.getPlatform() != SilenceEngine.Platform.ANDROID)
            SilenceEngine.input.setSimulateTouch(true);

        scene = new Scene2D();
        collider = new SceneCollider2D(new DynamicTree2D());
        collider.setScene(scene);

        collider.register(heroTag, wallsTag);
        collider.register(subHeroTag, wallsTag);

        Entity2D wall = new Entity2D();
        wall.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
        wall.addComponent(new SpriteComponent(sprite, batch));
        wall.addComponent(new BoundsRenderComponent());
        wall.position.set(SilenceEngine.display.getWidth() / 2, SilenceEngine.display.getHeight() / 2);
        scene.entities.add(wall);

        for (int i = 0; i < SilenceEngine.display.getWidth(); i += 48)
        {
            wall = new Entity2D();
            wall.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall.addComponent(new SpriteComponent(sprite, batch));
            wall.addComponent(new BoundsRenderComponent());
            wall.position.set(i + 24, 24);

            Entity2D wall2 = new Entity2D();
            wall2.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall2.addComponent(new SpriteComponent(sprite, batch));
            wall2.addComponent(new BoundsRenderComponent());
            wall2.position.set(i + 24, SilenceEngine.display.getHeight() - 24);

            scene.entities.add(wall);
            scene.entities.add(wall2);
        }

        for (int i = 48; i < SilenceEngine.display.getHeight() - 48; i += 48)
        {
            wall = new Entity2D();
            wall.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall.addComponent(new SpriteComponent(sprite, batch));
            wall.addComponent(new BoundsRenderComponent());
            wall.position.set(24, i + 24);

            Entity2D wall2 = new Entity2D();
            wall2.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48).createPolygon()));
            wall2.addComponent(new SpriteComponent(sprite, batch));
            wall2.addComponent(new BoundsRenderComponent());
            wall2.position.set(SilenceEngine.display.getWidth() - 24, i + 24);

            scene.entities.add(wall);
            scene.entities.add(wall2);
        }

        scene.entities.add(new Hero());
        SpriteRendererTest.renderer = renderer;
    }

    private static class Hero extends Entity2D
    {
        private SpriteComponent renderComponent;
        private SpriteComponent subRenderComponent;

        private Entity2D subHero;

        Hero()
        {
            position.set(SilenceEngine.display.getWidth() / 2 - 24, SilenceEngine.display.getHeight() / 2 - 24);

            addComponent(new CollisionComponent2D(heroTag, new Rectangle(48, 48).createPolygon(), this::onHeroCollision));
            addComponent(renderComponent = new SpriteComponent(sprite, batch));
            addComponent(new BoundsRenderComponent());

            subHero = new Entity2D();
            subHero.position.set(100, 30);

            subHero.addComponent(new CollisionComponent2D(subHeroTag, new Rectangle(48, 48).createPolygon(), this::onSubHeroCollision));
            subHero.addComponent(subRenderComponent = new SpriteComponent(sprite, batch));
            subHero.addComponent(new BoundsRenderComponent());

            addChild(subHero);
        }

        private void onHeroCollision(Entity2D other, CollisionComponent2D component)
        {
            renderComponent.tint.set(Color.YELLOW_GREEN);
        }

        private void onSubHeroCollision(Entity2D other, CollisionComponent2D component)
        {
            subRenderComponent.tint.set(Color.RED);
        }

        @Override
        public void onUpdate(float deltaTime)
        {
            rotation += 45 * deltaTime;

            if (Touch.isFingerDown(Touch.FINGER_0))
                position.set(Touch.getFingerPosition(Touch.FINGER_0));

            subHero.rotation += 45 * deltaTime;

            renderComponent.tint.set(Color.AQUA);
            subRenderComponent.tint.set(Color.GREEN);
        }
    }

    private static class BoundsRenderComponent implements IComponent2D
    {
        private Polygon polygon;

        @Override
        public void init(Entity2D entity)
        {
            CollisionComponent2D collision = entity.getComponent(CollisionComponent2D.class);
            polygon = collision.polygon;
        }

        @Override
        public void update(float deltaTime)
        {
        }

        @Override
        public void render(float deltaTime)
        {
            Rectangle rect = polygon.getBounds();

            dynamicRenderer.vertex(rect.x, rect.y);
            dynamicRenderer.color(Color.RED);

            dynamicRenderer.vertex(rect.x + rect.width, rect.y);
            dynamicRenderer.color(Color.RED);

            dynamicRenderer.vertex(rect.x + rect.width, rect.y);
            dynamicRenderer.color(Color.RED);

            dynamicRenderer.vertex(rect.x + rect.width, rect.y + rect.height);
            dynamicRenderer.color(Color.RED);

            dynamicRenderer.vertex(rect.x + rect.width, rect.y + rect.height);
            dynamicRenderer.color(Color.RED);

            dynamicRenderer.vertex(rect.x, rect.y + rect.height);
            dynamicRenderer.color(Color.RED);

            dynamicRenderer.vertex(rect.x, rect.y + rect.height);
            dynamicRenderer.color(Color.RED);

            dynamicRenderer.vertex(rect.x, rect.y);
            dynamicRenderer.color(Color.RED);
        }

        @Override
        public void dispose()
        {
        }
    }
}
