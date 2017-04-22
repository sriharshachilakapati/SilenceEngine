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

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.components.BoundsRenderComponent2D;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.components.PolygonRenderComponent;
import com.shc.silenceengine.scene.components.SpriteComponent;
import com.shc.silenceengine.utils.functional.BiCallback;

import static com.shc.silenceengine.graphics.opengl.Primitive.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class SceneRenderSystem implements BiCallback<Scene, Float>
{
    private final SpriteBatch batch;

    public SceneRenderSystem()
    {
        batch = new SpriteBatch(IGraphicsDevice.Renderers.sprite);
    }

    @Override
    public void invoke(Scene scene, Float elapsedTime)
    {
        Texture.EMPTY.bind();
        drawSprites(scene);
        drawPolygons(scene);
        drawBounds(scene);
    }

    private void drawSprites(Scene scene)
    {
        batch.begin();
        IGraphicsDevice.Programs.dynamic.use();

        scene.forEachEntityWithComponent(SpriteComponent.class, e ->
                e.forEachComponentOfType(SpriteComponent.class, c ->
                {
                    final Sprite sprite = c.sprite;
                    final Color tint = c.tint;
                    final float opacity = c.opacity;
                    final int layer = c.layer;
                    final Transform transform = e.transformComponent.getWorldTransform();

                    batch.render(sprite, transform, tint, opacity, layer);
                }));

        batch.end();
    }

    private void drawPolygons(Scene scene)
    {
        Texture.EMPTY.bind();
        DynamicRenderer renderer = IGraphicsDevice.Renderers.dynamic;
        IGraphicsDevice.Programs.dynamic.use();

        final PolygonRenderComponent.RenderType[] renderType = { PolygonRenderComponent.RenderType.OUTLINE };
        renderer.begin(LINES);

        Vector2 temp = Vector2.REUSABLE_STACK.pop();

        scene.forEachEntityWithComponent(PolygonRenderComponent.class, e ->
                e.forEachComponentOfType(PolygonRenderComponent.class, c ->
                {
                    Polygon polygon = c.polygon;

                    if (polygon == null)
                    {
                        CollisionComponent2D c2 = e.getComponent(CollisionComponent2D.class);
                        polygon = c2.polygon;
                    }

                    if (polygon != null)
                    {
                        if (renderType[0] != c.renderType)
                        {
                            renderer.end();
                            renderType[0] = c.renderType;
                            renderer.begin(renderType[0] == PolygonRenderComponent.RenderType.OUTLINE ? LINES : TRIANGLES);
                        }

                        final int factor = c.renderType == PolygonRenderComponent.RenderType.OUTLINE ? 1 : 3;

                        for (int i = 0; i < polygon.vertexCount(); i += factor)
                        {
                            if (c.renderType == PolygonRenderComponent.RenderType.OUTLINE)
                            {
                                Vector2 v1 = polygon.getVertex(i);
                                Vector2 v2 = polygon.getVertex((i + 1) % polygon.vertexCount());

                                renderer.flushOnOverflow(2);

                                renderer.vertex(temp.set(v1).add(polygon.getPosition()));
                                renderer.color(c.color);

                                renderer.vertex(temp.set(v2).add(polygon.getPosition()));
                                renderer.color(c.color);
                            }
                            else
                            {
                                Vector2 v1 = polygon.getVertex(i);
                                Vector2 v2 = polygon.getVertex((i + 1) % polygon.vertexCount());
                                Vector2 v3 = polygon.getVertex((i + 2) % polygon.vertexCount());

                                renderer.flushOnOverflow(3);

                                renderer.vertex(temp.set(v1).add(polygon.getPosition()));
                                renderer.color(c.color);

                                renderer.vertex(temp.set(v2).add(polygon.getPosition()));
                                renderer.color(c.color);

                                renderer.vertex(temp.set(v3).add(polygon.getPosition()));
                                renderer.color(c.color);
                            }
                        }
                    }
                }));
        renderer.end();
    }

    private void drawBounds(Scene scene)
    {
        Texture.EMPTY.bind();
        DynamicRenderer renderer = IGraphicsDevice.Renderers.dynamic;
        IGraphicsDevice.Programs.dynamic.use();

        final BoundsRenderComponent2D.RenderType[] renderType = { BoundsRenderComponent2D.RenderType.OUTLINE };
        renderer.begin(LINES);

        Vector2 temp = Vector2.REUSABLE_STACK.pop();

        scene.forEachEntityWithComponent(BoundsRenderComponent2D.class, e ->
                e.forEachComponentOfType(BoundsRenderComponent2D.class, c ->
                {
                    Rectangle bounds = c.bounds;

                    if (bounds == null)
                    {
                        CollisionComponent2D c2 = e.getComponent(CollisionComponent2D.class);
                        bounds = c2.polygon.getBounds();
                    }

                    if (bounds != null)
                    {
                        if (renderType[0] != c.renderType)
                        {
                            renderer.end();
                            renderType[0] = c.renderType;
                            renderer.begin(renderType[0] == BoundsRenderComponent2D.RenderType.OUTLINE ? LINES : TRIANGLES);
                        }

                        if (renderType[0] == BoundsRenderComponent2D.RenderType.OUTLINE)
                        {
                            renderer.flushOnOverflow(2);

                            renderer.vertex(temp.set(bounds.x, bounds.y));
                            renderer.color(c.color);

                            renderer.vertex(temp.set(bounds.x + bounds.width, bounds.y));
                            renderer.color(c.color);

                            renderer.flushOnOverflow(2);

                            renderer.vertex(temp.set(bounds.x + bounds.width, bounds.y));
                            renderer.color(c.color);

                            renderer.vertex(temp.set(bounds.x + bounds.width, bounds.y + bounds.height));
                            renderer.color(c.color);

                            renderer.flushOnOverflow(2);

                            renderer.vertex(temp.set(bounds.x + bounds.width, bounds.y + bounds.height));
                            renderer.color(c.color);

                            renderer.vertex(temp.set(bounds.x, bounds.y + bounds.height));
                            renderer.color(c.color);

                            renderer.flushOnOverflow(2);

                            renderer.vertex(temp.set(bounds.x, bounds.y + bounds.height));
                            renderer.color(c.color);

                            renderer.vertex(temp.set(bounds.x, bounds.y));
                            renderer.color(c.color);
                        }
                        else
                        {
                            renderer.flushOnOverflow(3);

                            renderer.vertex(temp.set(bounds.x, bounds.y));
                            renderer.color(c.color);

                            renderer.vertex(temp.set(bounds.x + bounds.width, bounds.y));
                            renderer.color(c.color);

                            renderer.vertex(temp.set(bounds.x + bounds.width, bounds.y + bounds.height));
                            renderer.color(c.color);

                            renderer.flushOnOverflow(3);

                            renderer.vertex(temp.set(bounds.x + bounds.width, bounds.y + bounds.height));
                            renderer.color(c.color);

                            renderer.vertex(temp.set(bounds.x, bounds.y + bounds.height));
                            renderer.color(c.color);

                            renderer.vertex(temp.set(bounds.x, bounds.y));
                            renderer.color(c.color);
                        }
                    }
                }));
        renderer.end();
    }
}
