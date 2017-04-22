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

package com.shc.silenceengine.scene;

import com.shc.silenceengine.scene.components.TransformComponent;
import com.shc.silenceengine.utils.IDGenerator;
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import com.shc.silenceengine.utils.functional.UniCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An Entity is the actor in a {@link Scene} that has a set of behaviours which are composed by adding different {@link
 * Component}s to it. These will be used by Systems attached to the Scene to perform tasks like updating the scene and
 * rendering the scene.
 *
 * @author Sri Harsha Chilakapati
 */
public class Entity
{
    /**
     * The ID that is used to uniquely identify this entity.
     */
    public final long id = IDGenerator.generate();

    /**
     * The components that compose the behaviour of this Entity.
     */
    private final List<Component> components = new ArrayList<>();

    /**
     * A separated storage of components so that the components are easy to fetch and iterated.
     */
    private final Map<ComponentType, List<Component>> componentsByType = new HashMap<>();

    /**
     * Flag used by the entity to keep itself whether it is destroyed or not.
     */
    private boolean destroyed = false;

    /**
     * The {@link TransformComponent} of this Entity.
     */
    public TransformComponent transformComponent;

    public Entity()
    {
        addComponent(transformComponent = new TransformComponent());
    }

    /**
     * Adds a component to this entity. You cannot call this method while in Update or Render events. If you need to do
     * this from there, enclose this call using {@link TaskManager#runOnUpdate(SimpleCallback)} method.
     *
     * @param component The component to be added to this entity.
     */
    public void addComponent(Component component)
    {
        if (destroyed)
            return;

        components.add(component);

        ComponentType componentType = ComponentType.of(component.getClass());
        List<Component> typedComponents = componentsByType.get(componentType);

        if (typedComponents == null)
            componentsByType.put(componentType, typedComponents = new ArrayList<>());

        typedComponents.add(component);

        component.setup(this);
    }

    /**
     * Removes a component from this entity. You cannot call this method while in Update or Render events. If you need
     * to do this from there, enclose this call using {@link TaskManager#runOnUpdate(SimpleCallback)} method.
     *
     * @param component The component to be removed from this entity.
     */
    public void removeComponent(Component component)
    {
        if (destroyed)
            return;

        components.remove(component);

        ComponentType componentType = ComponentType.of(component.getClass());
        List<Component> typedComponents = componentsByType.get(componentType);

        if (typedComponents != null)
            typedComponents.remove(component);

        component.onDestroyed();
    }

    /**
     * Gets the first component in this entity that matches the given type. If there is none, it returns {@code null}.
     *
     * @param klass The class of the component
     * @param <T>   Any type that extends from {@link Component}.
     *
     * @return A component that matches the given type.
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> klass)
    {
        ComponentType componentType = ComponentType.of(klass);
        List<Component> componentsOfType = componentsByType.get(componentType);

        if (componentsOfType == null || componentsOfType.size() == 0)
            return null;

        return (T) componentsOfType.get(0);
    }

    /**
     * Gets all the components in this entity that matches the given type and adds them all into the list passed.
     *
     * @param klass The class of the component
     * @param list  The list to add all the matching components
     * @param <T>   Any type that extends from {@link Component}
     *
     * @return The same list that is passed into this method. If the passed list is {@code null} then a new list is
     * returned.
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> List<T> getComponents(Class<T> klass, List<T> list)
    {
        if (list == null)
            list = new ArrayList<>();

        ComponentType componentType = ComponentType.of(klass);
        List<Component> componentsOfType = componentsByType.get(componentType);

        if (componentsOfType != null)
            for (Component c : componentsOfType)
                list.add((T) c);

        return list;
    }

    /**
     * Gets all the components in this entity into the list passed.
     *
     * @param list The list that should be used to add the components into.
     *
     * @return The same list that is passed into this method. If the passed list is {@code null} then a new list is
     * returned.
     */
    public List<Component> getComponents(List<Component> list)
    {
        if (list == null)
            list = new ArrayList<>();

        list.addAll(components);
        return list;
    }

    /**
     * Checks if this entity has a component that is of {@code klass} type.
     *
     * @param klass The type of component to look for.
     * @param <T>   Any type that extends from the {@link Component} class.
     *
     * @return True if there is a component that matched or false.
     */
    public <T extends Component> boolean hasComponent(Class<T> klass)
    {
        ComponentType componentType = ComponentType.of(klass);
        List<Component> componentsOfType = componentsByType.get(componentType);

        return !(componentsOfType == null || componentsOfType.size() == 0);
    }

    /**
     * Runs a callback for each of the component in the Entity. Note that you cannot add components while iterating, you
     * must enclose it with {@link TaskManager#runOnUpdate(SimpleCallback)} method to add before the next iteration.
     *
     * @param callback A {@link UniCallback} that will accept a component.
     */
    public void forEachComponent(UniCallback<Component> callback)
    {
        for (Component c : components)
            callback.invoke(c);
    }

    /**
     * Runs a callback for each of the component of the specified type in the Entity. Note that you cannot add
     * components while iterating, you must enclose it with {@link TaskManager#runOnUpdate(SimpleCallback)} method to
     * add before the next iteration.
     *
     * @param klass    The class of the component to find.
     * @param callback A {@link UniCallback} that will accept a component.
     * @param <T>      The type of component.
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> void forEachComponentOfType(Class<T> klass, UniCallback<T> callback)
    {
        ComponentType componentType = ComponentType.of(klass);
        List<Component> componentsOfType = componentsByType.get(componentType);

        if (componentsOfType != null)
            for (Component c : componentsOfType)
                callback.invoke((T) c);
    }

    /**
     * Returns whether this entity is destroyed or not.
     *
     * @return True if this entity is destroyed, or false otherwise.
     */
    public boolean isDestroyed()
    {
        return destroyed;
    }

    /**
     * Destroys this entity, and also notifies all the components that this entity is destroyed.
     */
    public void destroy()
    {
        if (!destroyed)
            for (Component c : components)
                c.onDestroyed();

        components.clear();
        destroyed = true;
    }
}
