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
import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.BiCallback;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import com.shc.silenceengine.utils.functional.UniCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A scene is a collection of {@link Entity} instances. It can be registered with Systems, which are any methods that
 * accepts the scene and the elapsed time as the arguments, and the systems are run every time you work with the Scene,
 * may it be an update, a render or any event.
 *
 * @author Sri Harsha Chilakapati
 */
public class Scene
{
    /**
     * The list of all the entities in the scene.
     */
    private final List<Entity> entities = new ArrayList<>();

    /**
     * The list of all newly added entities.
     */
    private final List<Entity> newEntities = new ArrayList<>();

    /**
     * The list of systems that handle the updating of scene.
     */
    private final List<BiCallback<Scene, Float>> updateSystems = new ArrayList<>();

    /**
     * The list of systems that handle the rendering of scene.
     */
    private final List<BiCallback<Scene, Float>> renderSystems = new ArrayList<>();

    /**
     * A flag used to find whether to delay insertion of entities.
     */
    private boolean isOperationInProgress = false;

    /**
     * Construct a new Scene object which contains the default component update system and the component render system.
     */
    public Scene()
    {
        registerUpdateSystem(Scene::componentUpdateSystem);
        registerRenderSystem(Scene::componentRenderSystem);
    }

    /**
     * A default update system that updates all the components in all the entities in the Scene.
     *
     * @param scene       The scene whose entity components should be updated.
     * @param elapsedTime The time took in the last frame.
     */
    private static void componentUpdateSystem(Scene scene, float elapsedTime)
    {
        scene.forEachEntity(entity -> entity.forEachComponent(c ->
        {
            if (c.enabled)
                c.onUpdate(elapsedTime);
        }));
    }

    /**
     * A default render system that renders all the components in all the entities in the Scene.
     *
     * @param scene       The scene whose entity components should be updated.
     * @param elapsedTime The time took in the last frame.
     */
    private static void componentRenderSystem(Scene scene, float elapsedTime)
    {
        scene.forEachEntity(e -> e.forEachComponent(c ->
        {
            if (c.enabled)
                c.onRender(elapsedTime);
        }));
    }

    /**
     * Adds an entity to the scene. In case that this scene is currently updating or rendering, then this call will
     * automatically delay the instantiation of the entities.
     *
     * @param entity The new entity to be added to this scene.
     */
    public void addEntity(Entity entity)
    {
        entity.forEachComponent(Component::onInit);

        if (isOperationInProgress)
            newEntities.add(entity);
        else
            entities.add(entity);
    }

    /**
     * Updates the scene. This calls all the registered systems that are registered for update event. The order of
     * invoking is the same as the order of registration. The components will however be updated before the systems
     * start work.
     *
     * @param elapsedTime The time elapsed in the previous frame.
     */
    public void update(float elapsedTime)
    {
        boolean isOperationInProgressOrig = isOperationInProgress;
        isOperationInProgress = true;

        // Process newly added entities
        entities.addAll(newEntities);
        newEntities.clear();

        // Remove dead entities
        for (Iterator<Entity> it = entities.iterator(); it.hasNext(); )
        {
            if (it.next().isDestroyed())
                it.remove();
        }

        // Run the update systems
        for (BiCallback<Scene, Float> system : updateSystems)
            system.invoke(this, elapsedTime);

        isOperationInProgress = isOperationInProgressOrig;
    }

    /**
     * Renders the scene. This calls all the registered systems that are registered for render event. The order of
     * invoking is the same as the order of registration. The components will however be rendered before the systems
     * start work.
     *
     * @param elapsedTime The time elapsed in the previous frame.
     */
    public void render(float elapsedTime)
    {
        boolean isOperationInProgressOrig = isOperationInProgress;
        isOperationInProgress = true;

        for (BiCallback<Scene, Float> system : renderSystems)
            system.invoke(this, elapsedTime);

        isOperationInProgress = isOperationInProgressOrig;
    }

    /**
     * Registers a system to act on the update event. A system is nothing but a {@link BiCallback} that accepts a Scene
     * and the elapsed time as a float. In case you are calling this method while the scene is in use, wrap the call in
     * {@link TaskManager#runOnUpdate(SimpleCallback)} method.
     *
     * @param system The system that is going to be registered for the update event.
     */
    public void registerUpdateSystem(BiCallback<Scene, Float> system)
    {
        updateSystems.add(system);
    }

    /**
     * Registers a system to act on the render event. A system is nothing but a {@link BiCallback} that accepts a Scene
     * and the elapsed time as a float. In case you are calling this method while the scene is in use, wrap the call in
     * {@link TaskManager#runOnUpdate(SimpleCallback)} method.
     *
     * @param system The system that is going to be registered for the update event.
     */
    public void registerRenderSystem(BiCallback<Scene, Float> system)
    {
        renderSystems.add(system);
    }

    /**
     * Runs a callback for all the active entities in this scene.
     *
     * @param callback The callback to be called for each of the active entity.
     */
    public void forEachEntity(UniCallback<Entity> callback)
    {
        boolean isOperationInProgressOrig = isOperationInProgress;
        isOperationInProgress = true;

        for (Entity e : entities)
            callback.invoke(e);

        isOperationInProgress = isOperationInProgressOrig;
    }

    /**
     * Runs a callback for all the active entities which have no parent in this scene.
     *
     * @param callback The callback to be called for each of the active entity.
     */
    public void forEachRootEntity(UniCallback<Entity> callback)
    {
        forEachEntityWithComponent(TransformComponent.class, e ->
        {
            if (e.getComponent(TransformComponent.class).getParent() != null)
                callback.invoke(e);
        });
    }

    /**
     * Finds all the entities in this scene that has a component which is an instance of {@code klass}. The found
     * entities are then processed by a callback.
     *
     * @param klass    The class of the component.
     * @param callback The callback to be used to process each such entity.
     * @param <T>      The type of the component.
     */
    public <T extends Component> void forEachEntityWithComponent(Class<T> klass, UniCallback<Entity> callback)
    {
        forEachEntity(e ->
        {
            if (e.hasComponent(klass))
                callback.invoke(e);
        });
    }

    /**
     * Finds all the entities in this scene that has a component which is an instance of {@code klass}. The found
     * entities are then added to the passed in {@code list}.
     *
     * @param klass The class of the component.
     * @param list  The list to add the found the entities into.
     * @param <T>   The type of the component.
     *
     * @return The same list that is passed in. If {@code null} is passed, then a new list is created.
     */
    public <T extends Component> List<Entity> findAllWithComponent(Class<T> klass, List<Entity> list)
    {
        if (list == null)
            list = new ArrayList<>();

        List<Entity> elements = list;
        forEachEntityWithComponent(klass, elements::add);

        return list;
    }
}
