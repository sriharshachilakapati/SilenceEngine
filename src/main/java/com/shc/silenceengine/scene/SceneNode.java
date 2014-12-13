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
    private boolean              destroyed;

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

    public void preUpdate(double delta)
    {
        update(delta);
        updateChildren(delta);
    }

    public void update(double delta)
    {
    }

    public void updateChildren(double delta)
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

    public void preRender(double delta, Batcher batcher)
    {
        render(delta, batcher);
        renderChildren(delta, batcher);
    }

    public void render(double delta, Batcher batcher)
    {
    }

    public void renderChildren(double delta, Batcher batcher)
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
        for (int i = 0; i < children.size(); i++)
        {
            removeChild(children.get(i));
            i--;
        }
    }

    public void destroy()
    {
        destroyed = true;
        destroyChildren();
    }

    public void destroyChildren()
    {
        for (int i = 0; i < children.size(); i++)
        {
            SceneNode child = children.get(i);
            child.destroy();
            removeChild(child);
            i--;
        }
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
            return transform.copy().apply(parent.getTransform());
        else
            return transform.copy().apply(Scene.getTransform());
    }

    public Transform getLocalTransform()
    {
        return transform;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }
}
