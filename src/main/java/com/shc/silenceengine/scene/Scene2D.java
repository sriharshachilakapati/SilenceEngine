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
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.graphics.cameras.BaseCamera;
import com.shc.silenceengine.math.Frustum;
import com.shc.silenceengine.scene.entity.Entity2D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class Scene2D implements IUpdatable
{
    private List<Entity2D> entities;

    private boolean frustumCulling;

    public Scene2D()
    {
        entities = new ArrayList<>();
        setFrustumCulling(true);
    }

    public void addChild(Entity2D e)
    {
        entities.add(e);
    }

    public void removeChild(Entity2D e)
    {
        entities.remove(e);
    }

    public void update(float delta)
    {
        for (int i = 0; i < entities.size(); i++)
        {
            Entity2D entity = entities.get(i);
            entity.preUpdate(delta);

            if (entity.isDestroyed())
            {
                removeChild(entity);
                i--;
            }
        }
    }

    public void render(float delta)
    {
        // Quit early if there are no children
        if (entities.size() == 0)
            return;

        // Sort the entities based on depth
        entities.sort((e1, e2) -> Integer.compare(e2.getDepth(), e1.getDepth()));

        // Render the entities in batches of depths
        int depth = entities.get(0).getDepth();

        // Get the Frustum once to prevent unnecessary calculations
        Frustum frustum = BaseCamera.CURRENT.getFrustum();

        SpriteBatch batch = SilenceEngine.graphics.getSpriteBatch();
        batch.begin();
        {
            for (Entity2D entity : entities)
            {
                if (frustumCulling && !frustum.intersects(entity.getPolygon()))
                    continue;

                if (entity.getDepth() != depth)
                {
                    batch.end();
                    depth = entity.getDepth();
                    batch.begin();
                }

                entity.render(delta, batch);
            }
        }
        batch.end();
    }

    public void destroy()
    {
        entities.forEach(Entity2D::destroy);
        entities.clear();
    }

    public List<Entity2D> getEntities()
    {
        return entities;
    }

    public boolean isFrustumCullingEnabled()
    {
        return frustumCulling;
    }

    public void setFrustumCulling(boolean frustumCulling)
    {
        this.frustumCulling = frustumCulling;
    }
}
