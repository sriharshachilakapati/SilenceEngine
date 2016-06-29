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
import com.shc.silenceengine.collision.broadphase.Grid;
import com.shc.silenceengine.collision.colliders.SceneCollider2D;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Shader;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.input.Touch;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Transforms;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.Scene2D;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.components.IComponent2D;
import com.shc.silenceengine.scene.entity.Entity2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class EntityCollisionTest2D extends SilenceTest
{
    private static DynamicRenderer renderer;
    private static Program         program;

    private static CollisionTag heroTag    = new CollisionTag();
    private static CollisionTag subHeroTag = new CollisionTag();
    private static CollisionTag wallsTag   = new CollisionTag();

    private Matrix4         camera;
    private Scene2D         scene;
    private SceneCollider2D collider;

    @Override
    public void init()
    {
        if (SilenceEngine.display.getPlatform() != SilenceEngine.Platform.ANDROID)
            SilenceEngine.input.setSimulateTouch(true);

        renderer = new DynamicRenderer();
        camera = new Matrix4();

        // The vertex shader source
        String vsSource = "uniform mat4 camera;                 \n" +

                          "attribute vec4 position;             \n" +
                          "attribute vec4 color;                \n" +

                          "varying vec4 vColor;                 \n" +

                          "void main()                          \n" +
                          "{                                    \n" +
                          "    vColor = color;                  \n" +
                          "    gl_Position = camera * position; \n" +
                          "}";

        // The fragment shader source
        String fsSource = "varying vec4 vColor;       \n" +

                          "void main()                \n" +
                          "{                          \n" +
                          "    gl_FragColor = vColor; \n" +
                          "}";

        if (SilenceEngine.display.getPlatform() == SilenceEngine.Platform.HTML5)
        {
            // Shaders need a small change, we need to set float precision
            String precision = "precision mediump float;\n";

            vsSource = precision + vsSource;
            fsSource = precision + fsSource;
        }

        Shader vertexShader = new Shader(Shader.Type.VERTEX_SHADER);
        vertexShader.source(vsSource);
        vertexShader.compile();

        Shader fragmentShader = new Shader(Shader.Type.FRAGMENT_SHADER);
        fragmentShader.source(fsSource);
        fragmentShader.compile();

        program = new Program();

        program.attach(vertexShader);
        program.attach(fragmentShader);
        program.link();

        program.use();

        renderer.setVertexLocation(program.getAttribute("position"));
        renderer.setColorLocation(program.getAttribute("color"));

        Transforms.createOrtho2d(0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight(), 0, -1, 1, camera);
        program.setUniform("camera", camera);

        scene = new Scene2D();
        collider = new SceneCollider2D(new Grid(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight(), 48, 48));
        collider.setScene(scene);

        collider.register(heroTag, wallsTag);
        collider.register(subHeroTag, wallsTag);

        for (int i = 0; i < SilenceEngine.display.getWidth(); i += 48)
        {
            Entity2D wall = new Entity2D();
            wall.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48)));
            wall.addComponent(new PolygonRenderComponent());
            wall.position.set(i + 24, 24);

            Entity2D wall2 = new Entity2D();
            wall2.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48)));
            wall2.addComponent(new PolygonRenderComponent());
            wall2.position.set(i + 24, SilenceEngine.display.getHeight() - 24);

            scene.entities.add(wall);
            scene.entities.add(wall2);
        }

        for (int i = 48; i < SilenceEngine.display.getHeight() - 48; i += 48)
        {
            Entity2D wall = new Entity2D();
            wall.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48)));
            wall.addComponent(new PolygonRenderComponent());
            wall.position.set(24, i + 24);

            Entity2D wall2 = new Entity2D();
            wall2.addComponent(new CollisionComponent2D(wallsTag, new Rectangle(48, 48)));
            wall2.addComponent(new PolygonRenderComponent());
            wall2.position.set(SilenceEngine.display.getWidth() - 24, i + 24);

            scene.entities.add(wall);
            scene.entities.add(wall2);
        }

        scene.entities.add(new Hero());
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        scene.update(deltaTime);
        collider.checkCollisions();

        SilenceEngine.display.setTitle("UPS: " + SilenceEngine.gameLoop.getUPS()
                                       + " | FPS: " + SilenceEngine.gameLoop.getFPS()
                                       + " | RC: " + IGraphicsDevice.Data.renderCallsThisFrame
                                       + " | EntityCollisionTest2D");
    }

    @Override
    public void render(float deltaTime)
    {
        renderer.begin(Primitive.LINES);
        scene.render(deltaTime);
        renderer.end();
    }

    @Override
    public void resized()
    {
        Transforms.createOrtho2d(0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight(), 0, 1, -1, camera);
        program.setUniform("camera", camera);
        GLContext.viewport(0, 0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    @Override
    public void dispose()
    {
        renderer.dispose();
        program.dispose();
    }

    private static class Hero extends Entity2D
    {
        private PolygonRenderComponent renderComponent;
        private PolygonRenderComponent subRenderComponent;

        private Entity2D subHero;

        Hero()
        {
            position.set(SilenceEngine.display.getWidth() / 2 - 24, SilenceEngine.display.getHeight() / 2 - 24);

            addComponent(new CollisionComponent2D(heroTag, new Rectangle(48, 48), this::onHeroCollision));
            addComponent(renderComponent = new PolygonRenderComponent(Color.AQUA));

            subHero = new Entity2D();
            subHero.position.set(100, 30);

            subHero.addComponent(new CollisionComponent2D(subHeroTag, new Rectangle(48, 48), this::onSubHeroCollision));
            subHero.addComponent(subRenderComponent = new PolygonRenderComponent(Color.GREEN));

            addChild(subHero);
        }

        private void onHeroCollision(Entity2D other, CollisionComponent2D component)
        {
            renderComponent.color.set(Color.YELLOW_GREEN);
        }

        private void onSubHeroCollision(Entity2D other, CollisionComponent2D component)
        {
            subRenderComponent.color.set(Color.WHITE);
        }

        @Override
        public void onUpdate(float deltaTime)
        {
            rotation += 45 * deltaTime;

            if (Touch.isFingerDown(Touch.FINGER_0))
                position.set(Touch.getFingerPosition(Touch.FINGER_0));

            subHero.rotation += 45 * deltaTime;

            renderComponent.color.set(Color.AQUA);
            subRenderComponent.color.set(Color.GREEN);
        }
    }

    private static class PolygonRenderComponent implements IComponent2D
    {
        private CollisionComponent2D collisionComponent;

        private Color color;

        PolygonRenderComponent()
        {
            this(Color.RED);
        }

        PolygonRenderComponent(Color color)
        {
            this.color = color.copy();
        }

        @Override
        public void init(Entity2D entity)
        {
            collisionComponent = entity.getComponent(CollisionComponent2D.class);
        }

        @Override
        public void update(float deltaTime)
        {
        }

        @Override
        public void render(float deltaTime)
        {
            Vector2 temp = Vector2.REUSABLE_STACK.pop();

            for (int i = 0, n = collisionComponent.polygon.getVertices().size(); i < n; i++)
            {
                Vector2 v1 = collisionComponent.polygon.getVertex(i);
                Vector2 v2 = collisionComponent.polygon.getVertex((i + 1) % n);

                renderer.vertex(temp.set(v1).add(collisionComponent.polygon.getPosition()));
                renderer.color(color);

                renderer.vertex(temp.set(v2).add(collisionComponent.polygon.getPosition()));
                renderer.color(color);
            }

            Vector2.REUSABLE_STACK.push(temp);
        }

        @Override
        public void dispose()
        {
        }
    }
}
