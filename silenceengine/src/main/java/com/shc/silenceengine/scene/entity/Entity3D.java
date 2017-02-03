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

import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.components.Component3D;
import com.shc.silenceengine.scene.components.TransformComponent3D;
import com.shc.silenceengine.utils.IDGenerator;
import com.shc.silenceengine.utils.ReflectionUtils;
import com.shc.silenceengine.utils.TaskManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Entity3D
{
    public final Vector3 position = new Vector3();
    public final Vector3 rotation = new Vector3();
    public final Vector3 scale    = new Vector3(1, 1, 1);

    public final long id = IDGenerator.generate();
    public final TransformComponent3D transformComponent;

    private final List<Component3D> components = new ArrayList<>();
    private final List<Entity3D>    children   = new ArrayList<>();

    public Entity3D parent;

    private boolean initializedComponents = false;
    private boolean destroyed             = false;

    public Entity3D()
    {
        addComponent(transformComponent = new TransformComponent3D());
    }

    public final void update(float deltaTime)
    {
        if (destroyed)
            return;

        onUpdate(deltaTime);

        if (!initializedComponents)
        {
            for (Component3D component : components)
                component.init();

            initializedComponents = true;
        }

        for (Component3D component : components)
            component.update(deltaTime);

        for (Entity3D child : children)
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

        for (Component3D component : components)
            component.render(deltaTime);

        for (Entity3D child : children)
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

        for (Component3D component : components)
            component.dispose();

        for (Entity3D entity : children)
            entity.destroy();

        components.clear();
        children.clear();

        destroyed = true;
    }

    public void addChild(Entity3D entity)
    {
        TaskManager.runOnUpdate(() ->
        {
            entity.parent = this;
            children.add(entity);
        });
    }

    public void removeChild(Entity3D entity)
    {
        TaskManager.runOnUpdate(() ->
        {
            entity.parent = null;
            children.remove(entity);
        });
    }

    public void addComponent(Component3D component)
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

    public void removeComponent(Component3D component)
    {
        TaskManager.runOnUpdate(() ->
        {
            components.remove(component);
            component.dispose();
        });
    }

    @SuppressWarnings("unchecked")
    public <T extends Component3D> T getComponent(Class<T> type)
    {
        for (Component3D component : components)
            if (ReflectionUtils.isInstanceOf(type, component))
                return (T) component;

        return null;
    }

    public List<Entity3D> getChildren()
    {
        return children;
    }

    public List<Component3D> getComponents()
    {
        return components;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }
}
