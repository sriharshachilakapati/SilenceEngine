package com.shc.silenceengine.scene;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Transform;

import java.util.ArrayList;

/**
 * @author Sri Harsha Chilakapati
 */
public class SceneNode
{
    private ArrayList<SceneNode> children;
    private SceneNode            parent;
    private Transform            transform;

    public SceneNode()
    {
        children = new ArrayList<>();
        transform = new Transform();
        parent = null;
    }

    public void init()
    {
    }

    public void initChildren()
    {
        for (SceneNode child : children)
            child.init();
    }

    public void addChild(SceneNode child)
    {
        if (child.getParent() != null)
            throw new SilenceException("A WorldComponent can be a child of a single parent!");

        children.add(child);
        child.setParent(this);
        child.init();
    }

    public void update(double delta)
    {
        updateChildren(delta);
    }

    public void updateChildren(double delta)
    {
        for (SceneNode child : children)
            child.update(delta);
    }

    public void render(double delta, Batcher batcher)
    {
        renderChildren(delta, batcher);
    }

    public void renderChildren(double delta, Batcher batcher)
    {
        for (SceneNode child : children)
            child.render(delta, batcher);
    }

    public void removeChild(SceneNode child)
    {
        if (child.getParent() != this)
            throw new SilenceException("Cannot remove non-existing Child!");

        child.destroy();
        children.remove(child);
        child.setParent(null);
    }

    public void removeChildren()
    {
        for (SceneNode child : children)
            removeChild(child);
    }

    public void destroy()
    {
        destroyChildren();
    }

    public void destroyChildren()
    {
        for (SceneNode child : children)
            child.destroy();
    }

    public SceneNode getParent()
    {
        return parent;
    }

    private void setParent(SceneNode parent)
    {
        this.parent = parent;
    }

    public Transform getTransform()
    {
        if (getParent() != null)
            return getParent().getTransform().copy().apply(transform);
        else
            return Scene.getTransform().copy().apply(transform);
    }

    public Transform getLocalTransform()
    {
        return transform;
    }
}
