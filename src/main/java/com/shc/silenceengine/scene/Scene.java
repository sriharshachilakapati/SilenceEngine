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
        for (SceneNode child : children)
            child.update(delta);
    }

    public static void render(double delta, Batcher batcher)
    {
        for (SceneNode child : children)
            child.render(delta, batcher);
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
        for (SceneNode child : children)
            removeChild(child);
    }

    public static Transform getTransform()
    {
        return transform;
    }
}
