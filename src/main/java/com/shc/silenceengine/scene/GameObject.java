package com.shc.silenceengine.scene;

/**
 * @author Sri Harsha Chilakapati
 */
public class GameObject extends SceneNode
{
    private boolean isAlive;

    public void init()
    {
        isAlive = true;
    }

    public void destroy()
    {
        destroyChildren();
        isAlive = false;
    }

    public boolean isAlive()
    {
        return isAlive;
    }
}
