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

package com.shc.silenceengine.collision.broadphase;

import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.math.geom3d.Polyhedron;
import com.shc.silenceengine.scene.components.CollisionComponent3D;

import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public interface IBroadphase3D
{
    void clear();

    void insert(CollisionComponent3D e);

    void remove(CollisionComponent3D e);

    default void update(CollisionComponent3D e)
    {
        remove(e);
        insert(e);
    }

    default List<CollisionComponent3D> retrieve(CollisionComponent3D e)
    {
        return retrieve(e.polyhedron.getBounds());
    }

    List<CollisionComponent3D> retrieve(Cuboid polyhedron);

    default List<CollisionComponent3D> retrieve(Polyhedron polyhedron)
    {
        return retrieve(polyhedron.getBounds());
    }
}
