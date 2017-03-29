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

package com.shc.silenceengine.scene.wip;

import com.shc.silenceengine.utils.TaskManager;
import com.shc.silenceengine.utils.functional.BiCallback;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import com.shc.silenceengine.utils.functional.UniCallback;

import java.util.ArrayList;
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
     * The list of systems that handle the updating of scene.
     */
    private final List<BiCallback<Scene, Float>> updateSystems = new ArrayList<>();

    /**
     * The list of systems that handle the rendering of scene.
     */
    private final List<BiCallback<Scene, Float>> renderSystems = new ArrayList<>();

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
        scene.forEachEntity(entity ->
        {
            entity.forEachComponent(c -> c.onUpdate(elapsedTime));

            if (entity.isDestroyed())
                TaskManager.runOnUpdate(() -> scene.removeEntity(entity));
        });
    }

    /**
     * A default render system that renders all the components in all the entities in the Scene.
     *
     * @param scene       The scene whose entity components should be updated.
     * @param elapsedTime The time took in the last frame.
     */
    private static void componentRenderSystem(Scene scene, float elapsedTime)
    {
        scene.forEachEntity(e ->
        {
            if (!e.isDestroyed())
                e.forEachComponent(c -> c.onRender(elapsedTime));
        });
    }

    /**
     * Adds an entity to the scene. In case you want to add entities while the Scene is in use, wrap this call with the
     * {@link TaskManager#runOnUpdate(SimpleCallback)} method.
     *
     * @param entity The new entity to be added to this scene.
     */
    public void addEntity(Entity entity)
    {
        entities.add(entity);
    }

    /**
     * Removes an entity from the scene. In case you want to remove the entity while the Scene is in use, wrap this call
     * with the {@link TaskManager#runOnUpdate(SimpleCallback)} method. This method does call {@link Entity#destroy()}
     * if the entity is alive.
     *
     * @param entity The entity to be removed from this scene.
     */
    public void removeEntity(Entity entity)
    {
        if (!entity.isDestroyed())
            entity.destroy();

        entities.remove(entity);
    }

    /**
     * Initializes the scene. It loops over all the entities and calls init on all the components in them.
     */
    public void init()
    {
        forEachEntity(e -> e.forEachComponent(Component::onInit));
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
        for (BiCallback<Scene, Float> system : updateSystems)
            system.invoke(this, elapsedTime);
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
        for (BiCallback<Scene, Float> system : renderSystems)
            system.invoke(this, elapsedTime);
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
        for (Entity e : entities)
            if (!e.isDestroyed())
                callback.invoke(e);
    }

    /**
     * Finds all the entities in this scene that has a component which is an instance of {@code klass}. The found entities
     * are then added to the passed in {@code list}.
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

        forEachEntity(e ->
        {
            if (e.hasComponent(klass))
                elements.add(e);
        });

        return list;
    }
}
