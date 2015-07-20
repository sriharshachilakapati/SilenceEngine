package com.shc.silenceengine.scene;

import com.shc.silenceengine.core.IUpdatable;

/**
 * @author Schuster
 * @since 7/20/2015
 */
public interface IScene extends IUpdatable
{
    void update(float delta);

    void render(float delta);
}
