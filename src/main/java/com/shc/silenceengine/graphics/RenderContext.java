package com.shc.silenceengine.graphics;

import com.shc.silenceengine.models.Material;

/**
 * @author Sri Harsha Chilakapati
 */
public final class RenderContext
{
    public static final Material DEFAULT_MATERIAL;
    public static Material CURRENT_MATERIAL;

    static
    {
        DEFAULT_MATERIAL = new Material();
        DEFAULT_MATERIAL.setAmbient(Color.WHITE);
        DEFAULT_MATERIAL.setSpecular(Color.TRANSPARENT);

        CURRENT_MATERIAL = DEFAULT_MATERIAL;
    }

    private RenderContext()
    {
    }

    public static void useMaterial(Material m)
    {
        CURRENT_MATERIAL = m;
    }
}
