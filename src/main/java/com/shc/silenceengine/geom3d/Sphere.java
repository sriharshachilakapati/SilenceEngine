package com.shc.silenceengine.geom3d;

import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class Sphere extends Polyhedron
{
    private float radius;

    public Sphere(Vector3 position, float radius)
    {
        setPosition(position);

        this.radius = radius;

        updateVertices();
    }

    private void updateVertices()
    {
        clearVertices();

        // Algorithm copied from the article written by Polaris of hugi.scene.org available online at the url
        // http://hugi.scene.org/online/hugi27/hugi%2027%20-%20coding%20corner%20polaris%20sphere%20tessellation%20101.htm

        final int bandPower = 6;
        final int bandPoints = (int) Math.pow(2, bandPower); // 2^bandPower
        final int bandMask = bandPoints - 2;
        final int sectionsInBand = (bandPoints / 2) - 1;
        final int totalPoints = sectionsInBand * bandPoints;

        final float sectionArc = 6.28f / sectionsInBand;
        final float radius = -this.radius;

        float xAngle;
        float yAngle;

        for (int i = 0; i < totalPoints; i++)
        {
            xAngle = (float) (i & 1) + (i >> bandPower);
            yAngle = (float) ((i & bandMask) >> 1) + ((i >> bandPower) * sectionsInBand);

            xAngle *= sectionArc / 2f;
            yAngle *= sectionArc * -1;

            float x = (float) (radius * Math.sin(xAngle) * Math.sin(yAngle));
            float y = (float) (radius * Math.cos(xAngle));
            float z = (float) (radius * Math.sin(xAngle) * Math.cos(yAngle));

            addVertex(new Vector3(x, y, z));
        }
    }

    public boolean intersects(Polyhedron other)
    {
        if (other instanceof Sphere)
        {
            Sphere o = (Sphere) other;
            return getPosition().distanceSquared(o.getPosition()) <= (radius + o.radius) * (radius + o.radius);
        }

        return super.intersects(other);
    }

    public float getRadius()
    {
        return getWidth() / 2;
    }
}
