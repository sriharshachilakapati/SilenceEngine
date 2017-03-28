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

    public Scene()
    {
        registerUpdateSystem(Scene::updateSystem);
    }

    private static void updateSystem(Scene scene, float elapsedTime)
    {
        scene.forEachEntity(entity ->
        {
            entity.forEachComponent(c -> c.onUpdate(elapsedTime));

            if (entity.isDestroyed())
                TaskManager.runOnUpdate(() -> scene.removeEntity(entity));
        });
    }

    public void addEntity(Entity entity)
    {
        entities.add(entity);
    }

    public void removeEntity(Entity entity)
    {
        if (!entity.isDestroyed())
            entity.destroy();

        entities.remove(entity);
    }

    public void init()
    {
        forEachEntity(e -> e.forEachComponent(Component::onInit));
    }

    public void update(float elapsedTime)
    {
        for (BiCallback<Scene, Float> system : updateSystems)
            system.invoke(this, elapsedTime);
    }

    public void render(float elapsedTime)
    {
        for (BiCallback<Scene, Float> system : renderSystems)
            system.invoke(this, elapsedTime);
    }

    public void registerUpdateSystem(BiCallback<Scene, Float> system)
    {
        updateSystems.add(system);
    }

    public void registerRenderSystem(BiCallback<Scene, Float> system)
    {
        renderSystems.add(system);
    }

    public void forEachEntity(UniCallback<Entity> callback)
    {
        for (Entity e : entities)
            callback.invoke(e);
    }
}
