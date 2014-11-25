package com.shc.silenceengine.graphics;

import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.TransformUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Transform
{
    // The transformation matrix
    private Matrix4 tMatrix;

    public Transform()
    {
        tMatrix = new Matrix4();
    }

    public Transform translate(Vector2 v)
    {
        return translate(new Vector3(v, 0));
    }

    public Transform translate(Vector3 v)
    {
        tMatrix.multiply(TransformUtils.createTranslation(v));
        return this;
    }


}
