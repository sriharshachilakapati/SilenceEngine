package com.shc.silenceengine.geom2d;

import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.MathUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Ellipse extends Polygon
{
    public Ellipse(Vector2 position, float rx, float ry)
    {
        setPosition(position);

        updateVertices(rx, ry);
    }

    private void updateVertices(float rx, float ry)
    {
        clearVertices();

        float x = getPosition().x;
        float y = getPosition().y;

        for (int i = 0; i < 360; i++)
        {
            addVertex(new Vector2(x + MathUtils.cos(i) * rx,
                    y + MathUtils.sin(i) * ry));
        }
    }

    public float getRadiusX()
    {
        return getBounds().getWidth() / 2;
    }

    public void setRadiusX(float rx)
    {
        float rotation = getRotation();
        updateVertices(rx, getRadiusY());
        setRotation(rotation);
    }

    public float getRadiusY()
    {
        return getBounds().getHeight() / 2;
    }

    public void setRadiusY(float ry)
    {
        float rotation = getRotation();
        updateVertices(getRadiusX(), ry);
        setRotation(rotation);
    }
}
