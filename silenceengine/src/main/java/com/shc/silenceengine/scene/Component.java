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

/**
 * A component defines the behaviour of an {@link Entity} in the game. Most of the time, components are only data
 * containers that contain the data, and they will be used by Systems while updating or rendering the game scene.
 *
 * @author Sri Harsha Chilakapati
 */
public abstract class Component
{
    /**
     * Whether this component is enabled or not. The systems will have to honour this flag and work only on the enabled
     * components.
     */
    public boolean enabled = true;

    /**
     * The entity that contains this component instance.
     */
    protected Entity entity;

    /**
     * The {@link TransformComponent} associated with this entity of the component.
     */
    protected TransformComponent transformComponent;

    /**
     * Package-private method called by the Entity class to register itself with the component. All it does is to store
     * the reference to the entity which can be accessed by the child classes.
     *
     * @param entity The {@link Entity} that owns this component.
     */
    void setup(Entity entity)
    {
        this.entity = entity;
        this.transformComponent = entity.getComponent(TransformComponent.class);
        onCreate();
    }

    /**
     * Called automatically after this component object is created. However at this point, other components might not
     * have been added to the entity.
     */
    protected void onCreate()
    {
    }

    /**
     * Called automatically before the first update operation. It is guaranteed that all the components have been
     * created and added to the entity at the point, unless you add custom components in the update event.
     */
    protected void onInit()
    {
    }

    /**
     * Called to update the state of the component. This is called each frame by the entity.
     *
     * @param elapsedTime The time elapsed in the previous frame.
     */
    protected void onUpdate(float elapsedTime)
    {
    }

    /**
     * Called by the rendering system in case there is anything left to render. However one might want to make use of
     * the Systems to control the rendering order. This is called at the end of the scene rendering event.
     *
     * @param elapsedTime The time elapsed in the previous frame.
     */
    protected void onRender(float elapsedTime)
    {
    }

    /**
     * Called automatically when the parent {@link Entity} is destroyed.
     */
    protected void onDestroyed()
    {
    }

    public Entity getEntity()
    {
        return entity;
    }
}
