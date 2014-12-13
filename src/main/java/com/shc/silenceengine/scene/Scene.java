package com.shc.silenceengine.scene;

import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.opengl.Program;
import com.shc.silenceengine.graphics.opengl.Texture;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Scene extends SceneNode
{
    public Scene()
    {
//        Program.CURRENT = null;
//        Texture.CURRENT = null;
//
//        Program.DEFAULT.use();
//        Texture.EMPTY.bind();
    }

    public void preUpdate(double delta)
    {
    }

    public void preRender(double delta, Batcher batcher)
    {
    }

    public void update(double delta)
    {
        updateChildren(delta);
    }

    public void render(double delta, Batcher batcher)
    {
        renderChildren(delta, batcher);
    }
}
