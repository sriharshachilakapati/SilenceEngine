package com.shc.silenceengine.scene;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.math.Transform;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class SceneNode
{
    private static int idGenerator = 0;
    private List<SceneNode> children;
    private List<SceneComponent> components;
    private SceneNode parent;
    private Transform transform;
    private boolean destroyed;
    private int id;

    public SceneNode()
    {
        transform = new Transform();
        parent = null;

        id = ++idGenerator;
    }

    public void preInit()
    {
        init();
        initChildren();
    }

    public void init()
    {
    }

    private void initChildren()
    {
        if (children == null)
            return;

        children.forEach(SceneNode::preInit);
    }

    public void addChild(SceneNode child)
    {
        if (child.getParent() != null)
            throw new SilenceException("A WorldComponent can be a child of a single parent!");

        if (children == null)
            children = new ArrayList<>();

        children.add(child);
        child.setParent(this);
        child.init();

        if (child instanceof Entity2D)
        {
            // Sort the Entity2D's in children based on their depth
            Collections.sort(children, (SceneNode c1, SceneNode c2) ->
            {
                if (c1 instanceof Entity2D && c2 instanceof Entity2D)
                    return ((Integer) ((Entity2D) c2).getDepth()).compareTo(((Entity2D) c1).getDepth());

                return 0;
            });
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

    public void addComponent(SceneComponent component)
    {
        if (components == null)
            components = new ArrayList<>();

        components.add(component);
    }

    public void removeComponent(SceneComponent component)
    {
        if (components == null)
            return;

        if (components.contains(component))
        {
            components.remove(component);
            component.dispose();
        }
    }

    public void preUpdate(float delta)
    {
        update(delta);
        updateComponents(delta);
        updateChildren(delta);
    }

    public void update(float delta)
    {
    }

    protected void updateComponents(float delta)
    {
        if (components == null)
            return;

        for (SceneComponent component : components)
            component.update(delta);
    }

    protected void updateChildren(float delta)
    {
        if (children == null)
            return;

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

    public void preRender(float delta, Batcher batcher)
    {
        render(delta, batcher);
        renderChildren(delta, batcher);
    }

    protected void renderChildren(float delta, Batcher batcher)
    {
        if (children == null)
            return;

        doRenderChildren(delta, batcher);
        renderChildrenWithComponents(delta, batcher);
    }

    public void render(float delta, Batcher batcher)
    {
    }

    private void doRenderChildren(float delta, Batcher batcher)
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

    private void renderChildrenWithComponents(float delta, Batcher batcher)
    {
        if (components == null)
            return;

        for (SceneComponent component : components)
        {
            // Enable forward rendering
            GL3Context.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL3Context.depthMask(false);
            GL3Context.depthFunc(GL11.GL_EQUAL);

            component.use();
            doRenderChildren(delta, batcher);
            component.release();

            // Disable forward rendering
            GL3Context.depthFunc(GL11.GL_LESS);
            GL3Context.depthMask(true);
            GL3Context.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    public void removeChildren()
    {
        if (children == null)
            return;

        for (int i = 0; i < children.size(); i++)
        {
            removeChild(children.get(i));
            i--;
        }
    }

    public void removeChild(SceneNode child)
    {
        if (child.getParent() != this)
            throw new SilenceException("Cannot remove non-existing Child!");

        if (children == null)
            return;

        child.destroy();
        children.remove(child);
        child.setParent(null);
    }

    public void destroy()
    {
        destroyed = true;
        destroyChildren();
        destroyComponents();
    }

    public void destroyChildren()
    {
        if (children == null)
            return;

        children.forEach(SceneNode::destroy);
        children.clear();
    }

    public void destroyComponents()
    {
        if (components == null)
            return;

        components.forEach(SceneComponent::dispose);
        components.clear();
    }

    public List<SceneNode> getChildren()
    {
        return children;
    }

    public List<SceneComponent> getComponents()
    {
        return components;
    }

    public Transform getTransform()
    {
        if (getParent() != null)
            return transform.copy().apply(parent.getTransform());
        else
            return transform;
    }

    public Transform getLocalTransform()
    {
        return transform;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public int getID()
    {
        return id;
    }
}
