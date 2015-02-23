/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shc.silenceengine.math.geom3d;

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

    public float getRadius()
    {
        return getWidth() / 2;
    }
}
