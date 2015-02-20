package com.shc.silenceengine.scene.entity;

import com.shc.silenceengine.math.geom3d.Polyhedron;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.models.Model;

/**
 * @author Sri Harsha Chilakapati
 */
public class ModelEntity extends Entity3D
{
    private Model model;

    public ModelEntity(Model model, Polyhedron polyhedron)
    {
        super(polyhedron);

        this.model = model;
    }

    public void preUpdate(float delta)
    {
        super.preUpdate(delta);
        model.update(delta);
    }

    public void preRender(float delta, Batcher batcher)
    {
        super.preRender(delta, batcher);
        model.render(delta, batcher, getTransform());
    }
}
