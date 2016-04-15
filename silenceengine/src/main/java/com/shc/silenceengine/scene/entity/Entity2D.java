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

package com.shc.silenceengine.scene.entity;

import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.components.IComponent2D;
import com.shc.silenceengine.scene.components.TransformComponent2D;
import com.shc.silenceengine.utils.IDGenerator;
import com.shc.silenceengine.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Entity2D
{
    public final Vector2 position = new Vector2();
    public final Vector2 scale    = new Vector2(1, 1);

    public final long id = IDGenerator.generate();
    public final TransformComponent2D transformComponent;

    private final List<Entity2D>     children   = new ArrayList<>();
    private final List<IComponent2D> components = new ArrayList<>();

    public float    rotation;
    public Entity2D parent;

    public Entity2D()
    {
        addComponent(transformComponent = new TransformComponent2D());
    }

    public final void update(float deltaTime)
    {
        onUpdate(deltaTime);

        for (IComponent2D component : components)
            component.update(deltaTime);

        for (int i = 0; i < children.size(); i++)
            children.get(i).update(deltaTime);
    }

    protected void onUpdate(float deltaTime)
    {
    }

    public final void render(float deltaTime)
    {
        onRender(deltaTime);

        for (IComponent2D component : components)
            component.render(deltaTime);

        for (int i = 0; i < children.size(); i++)
            children.get(i).render(deltaTime);
    }

    protected void onRender(float deltaTime)
    {
    }

    public void addChild(Entity2D entity)
    {
        entity.parent = this;
        children.add(entity);
    }

    public void addComponent(IComponent2D component)
    {
        components.add(component);
        component.init(this);
    }

    public void removeComponent(IComponent2D component)
    {
        components.remove(component);
        component.dispose();
    }

    public void removeChild(Entity2D entity)
    {
        entity.parent = null;
        children.remove(entity);
    }

    @SuppressWarnings("unchecked")
    public <T extends IComponent2D> T getComponent(Class<T> type)
    {
        for (IComponent2D component : components)
            if (ReflectionUtils.isInstanceOf(type, component))
                return (T) component;

        return null;
    }

    public List<Entity2D> getChildren()
    {
        return children;
    }

    public List<IComponent2D> getComponents()
    {
        return components;
    }
}
