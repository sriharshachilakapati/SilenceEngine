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

package com.shc.silenceengine.scene.entity;

import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.components.Component2D;
import com.shc.silenceengine.scene.components.TransformComponent2D;
import com.shc.silenceengine.utils.IDGenerator;
import com.shc.silenceengine.utils.ReflectionUtils;
import com.shc.silenceengine.utils.TaskManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Entity2D
{
    public final long id = IDGenerator.generate();

    public final Vector2 position = new Vector2();
    public final Vector2 scale    = new Vector2(1, 1);

    public final TransformComponent2D transformComponent;

    private final List<Entity2D>    children   = new ArrayList<>();
    private final List<Component2D> components = new ArrayList<>();

    public float rotation;

    public Entity2D parent;

    private boolean initializedComponents = false;
    private boolean destroyed             = false;

    public Entity2D()
    {
        addComponent(transformComponent = new TransformComponent2D());
    }

    public final void update(float deltaTime)
    {
        if (destroyed)
            return;

        onUpdate(deltaTime);

        if (!initializedComponents)
        {
            for (Component2D component : components)
                component.init();

            initializedComponents = true;
        }

        for (Component2D component : components)
            component.update(deltaTime);

        for (Entity2D child : children)
        {
            if (child.destroyed)
                removeChild(child);
            else
                child.update(deltaTime);
        }
    }

    protected void onUpdate(float deltaTime)
    {
    }

    public final void render(float deltaTime)
    {
        if (destroyed)
            return;

        onRender(deltaTime);

        for (Component2D component : components)
            component.render(deltaTime);

        for (Entity2D child : children)
            child.render(deltaTime);
    }

    protected void onRender(float deltaTime)
    {
    }

    protected void onDestroy()
    {
    }

    public void destroy()
    {
        if (destroyed)
            return;

        onDestroy();

        for (Component2D component : components)
            component.dispose();

        for (Entity2D entity : children)
            entity.destroy();

        components.clear();
        children.clear();

        destroyed = true;
    }

    public void addChild(Entity2D entity)
    {
        TaskManager.runOnUpdate(() ->
        {
            entity.parent = this;
            children.add(entity);
        });
    }

    public void addComponent(Component2D component)
    {
        if (!initializedComponents)
        {
            components.add(component);
            component.create(this);
            return;
        }

        TaskManager.runOnUpdate(() ->
        {
            components.add(component);
            component.create(this);
            component.init();
        });
    }

    public void removeComponent(Component2D component)
    {
        TaskManager.runOnUpdate(() ->
        {
            components.remove(component);
            component.dispose();
        });
    }

    public void removeChild(Entity2D entity)
    {
        TaskManager.runOnUpdate(() ->
        {
            entity.parent = null;
            children.remove(entity);
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends Component2D> T getComponent(Class<T> type)
    {
        for (Component2D component : components)
            if (ReflectionUtils.isInstanceOf(type, component))
                return (T) component;

        return null;
    }

    public List<Entity2D> getChildren()
    {
        return children;
    }

    public List<Component2D> getComponents()
    {
        return components;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }
}
