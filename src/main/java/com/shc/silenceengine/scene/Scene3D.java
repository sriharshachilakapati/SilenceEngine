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

package com.shc.silenceengine.scene;

import com.shc.silenceengine.core.IUpdatable;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.ModelBatch;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.scene.entity.Entity3D;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Scene3D implements IUpdatable
{
    private List<SceneComponent> components;
    private List<Entity3D> entities;

    private Transform transform;

    public Scene3D()
    {
        components = new ArrayList<>();
        entities = new ArrayList<>();
        transform = new Transform();
    }

    public void update(float delta)
    {
        for (int i = 0; i < entities.size(); i++)
        {
            Entity3D entity = entities.get(i);

            if (entity.isDestroyed())
            {
                entities.remove(entity);
                i--;
                continue;
            }

            entity.preUpdate(delta);
        }

        for (SceneComponent component : components)
            component.update(delta);
    }

    public void render(float delta)
    {
        // Quit early if there are no children
        if (entities.size() == 0)
            return;

        doRender(delta);

        for (SceneComponent component : components)
        {
            GL3Context.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL3Context.depthMask(false);
            GL3Context.depthFunc(GL11.GL_EQUAL);

            component.use();
            doRender(delta);
            component.release();

            GL3Context.depthFunc(GL11.GL_LESS);
            GL3Context.depthMask(true);
            GL3Context.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    private void doRender(float delta)
    {
        ModelBatch batch = SilenceEngine.graphics.getModelBatch();

        batch.begin(transform);
        {
            entities.forEach(e -> e.render(delta, batch));
        }
        batch.end();
    }

    public void addChild(Entity3D e)
    {
        entities.add(e);
    }

    public void addComponent(SceneComponent c)
    {
        components.add(c);
    }

    public void removeChild(Entity3D e)
    {
        entities.remove(e);
    }

    public void removeComponent(SceneComponent component)
    {
        components.remove(component);
    }

    public void destroy()
    {
        entities.forEach(Entity3D::destroy);
        components.forEach(SceneComponent::dispose);

        entities.clear();
        components.clear();
    }

    public Transform getTransform()
    {
        return transform;
    }

    public List<Entity3D> getEntities()
    {
        return entities;
    }

    public List<SceneComponent> getComponents()
    {
        return components;
    }
}
