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
 *
 */

package com.shc.silenceengine.scene.entity;

import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.components.IComponent3D;
import com.shc.silenceengine.scene.components.TransformComponent3D;
import com.shc.silenceengine.utils.IDGenerator;
import com.shc.silenceengine.utils.ReflectionUtils;

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

    private final List<IComponent3D> components = new ArrayList<>();
    private final List<Entity3D>     children   = new ArrayList<>();

    public Entity3D parent;

    public Entity3D()
    {
        addComponent(transformComponent = new TransformComponent3D());
    }

    public final void update(float deltaTime)
    {
        onUpdate(deltaTime);

        for (IComponent3D component : components)
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

        for (IComponent3D component : components)
            component.render(deltaTime);

        for (int i = 0; i < children.size(); i++)
            children.get(i).render(deltaTime);
    }

    protected void onRender(float deltaTime)
    {
    }

    public void addChild(Entity3D entity)
    {
        entity.parent = this;
        children.add(entity);
    }

    public void removeChild(Entity3D entity)
    {
        entity.parent = null;
        children.remove(entity);
    }

    public void addComponent(IComponent3D component)
    {
        components.add(component);
        component.init(this);
    }

    public void removeComponent(IComponent3D component)
    {
        components.remove(component);
        component.dispose();
    }

    @SuppressWarnings("unchecked")
    public <T extends IComponent3D> T getComponent(Class<T> type)
    {
        for (IComponent3D component : components)
            if (ReflectionUtils.isInstanceOf(type, component))
                return (T) component;

        return null;
    }

    public List<Entity3D> getChildren()
    {
        return children;
    }

    public List<IComponent3D> getComponents()
    {
        return components;
    }
}
