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

import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.TaskManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Scene2D
{
    private List<Entity2D> entities = new ArrayList<>();

    public void update(float deltaTime)
    {
        for (Entity2D entity : entities)
        {
            if (!entity.isDestroyed())
                entity.update(deltaTime);
            else
                removeEntity(entity);
        }
    }

    public void render(float deltaTime)
    {
        for (Entity2D entity : entities) entity.render(deltaTime);
    }

    public int numEntities()
    {
        int count = entities.size();

        for (Entity2D entity : entities)
            count += entity.getChildren().size();

        return count;
    }

    public void addEntity(Entity2D entity)
    {
        TaskManager.runOnRender(() -> entities.add(entity));
    }

    public void removeEntity(Entity2D entity)
    {
        TaskManager.runOnUpdate(() ->
        {
            if (!entity.isDestroyed())
                entity.destroy();

            entities.remove(entity);
        });
    }

    public List<Entity2D> getEntities()
    {
        return entities;
    }
}
