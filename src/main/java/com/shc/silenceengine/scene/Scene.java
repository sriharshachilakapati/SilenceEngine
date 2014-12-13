package com.shc.silenceengine.scene;

import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Transform;

import java.util.ArrayList;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Scene
{
    private static Transform            transform;
    private static ArrayList<SceneNode> children;

    private Scene()
    {
    }

    public static void init()
    {
        transform = new Transform();
        children = new ArrayList<>();
    }

    public static void update(double delta)
    {
        for (int i = 0; i < children.size(); i++)
        {
            SceneNode child = children.get(i);
            child.preUpdate(delta);

            if (child.isDestroyed())
            {
                removeChild(child);
                i--;
            }
        }
    }

    public static void render(double delta, Batcher batcher)
    {
        for (int i = 0; i < children.size(); i++)
        {
            SceneNode child = children.get(i);
            child.preRender(delta, batcher);

            if (child.isDestroyed())
            {
                removeChild(child);
                i--;
            }
        }
    }

    public static void addChild(SceneNode child)
    {
        children.add(child);
        child.init();
    }

    public static void removeChild(SceneNode child)
    {
        child.destroy();
        children.remove(child);
    }

    public static void removeChildren()
    {
        for (int i = 0; i < children.size(); i++)
        {
            removeChild(children.get(i));
            i--;
        }
    }

    public static Transform getTransform()
    {
        return transform;
    }
}
