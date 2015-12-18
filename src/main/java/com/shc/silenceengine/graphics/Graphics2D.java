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

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.cameras.BaseCamera;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.backend.lwjgl3.opengl.Primitive;
import com.shc.silenceengine.backend.lwjgl3.opengl.Texture;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.utils.MathUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Graphics2D
{
    private static Graphics2D instance;

    private OrthoCam  camera;
    private Paint     paint;
    private IFont     font;
    private Transform transform;

    /* Utility Methods */
    private BaseCamera originalCamera;
    private Texture    originalTexture;

    private Color color = new Color();

    protected Graphics2D()
    {
        camera = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());

        paint = new Paint();
        transform = new Transform();
        font = TrueTypeFont.DEFAULT;
    }

    public static Graphics2D getInstance()
    {
        if (instance == null)
            instance = new Graphics2D();

        return instance;
    }

    public void drawRect(Vector2 pos, float w, float h)
    {
        drawRect(pos.x, pos.y, w, h);
    }

    public void drawRect(float x, float y, float w, float h)
    {
        rect(x, y, w, h, Primitive.LINE_LOOP);
    }

    private void rect(float x, float y, float w, float h, Primitive primitive)
    {
        Batcher batcher = SilenceEngine.graphics.getBatcher();
        batcher.applyTransform(transform);

        startPainting();
        {
            batcher.begin(primitive);
            {
                batcher.vertex(x, y);
                batcher.color(paint.getTopLeftColor());
                batcher.normal(Vector3.AXIS_Z);

                batcher.vertex(x + w, y);
                batcher.color(paint.getTopRightColor());
                batcher.normal(Vector3.AXIS_Z);

                batcher.vertex(x + w, y + h);
                batcher.color(paint.getBottomRightColor());
                batcher.normal(Vector3.AXIS_Z);

                batcher.vertex(x, y + h);
                batcher.color(paint.getBottomLeftColor());
                batcher.normal(Vector3.AXIS_Z);
            }
            batcher.end();
        }
        endPainting();
    }

    protected void startPainting()
    {
        originalCamera = BaseCamera.CURRENT;
        camera.apply();

        originalTexture = Texture.CURRENT;
    }

    protected void endPainting()
    {
        originalTexture.bind();

        if (originalCamera != null)
            originalCamera.apply();
    }

    public void drawRect(Vector2 min, Vector2 max)
    {
        Vector2 size = max.subtract(min);
        drawRect(min.x, min.y, size.x, size.y);
    }

    public void fillRect(Vector2 pos, float w, float h)
    {
        fillRect(pos.x, pos.y, w, h);
    }

    public void fillRect(float x, float y, float w, float h)
    {
        rect(x, y, w, h, Primitive.TRIANGLE_FAN);
    }

    public void fillRect(Vector2 min, Vector2 max)
    {
        Vector2 size = max.subtract(min);
        fillRect(min.x, min.y, size.x, size.y);
    }

    public void drawOval(Vector2 pos, float rx, float ry)
    {
        drawOval(pos.x, pos.y, rx, ry);
    }

    public void drawOval(float x, float y, float rx, float ry)
    {
        oval(x, y, rx, ry, Primitive.LINE_LOOP);
    }

    private void oval(float x, float y, float rx, float ry, Primitive primitive)
    {
        Batcher batcher = SilenceEngine.graphics.getBatcher();
        batcher.applyTransform(transform);

        Vector2 vertex = Vector2.REUSABLE_STACK.pop();
        Color color = Color.REUSABLE_STACK.pop();

        startPainting();
        {
            float width = 2 * rx;
            float height = 2 * ry;

            batcher.begin(primitive);
            {
                for (int i = 0; i < 360; i++)
                {
                    vertex.set(MathUtils.cos(i) * rx, MathUtils.sin(i) * ry);
                    batcher.vertex(x + vertex.x, y + vertex.y);

                    batcher.color(paint.getColor((rx + vertex.x) / width, (ry + vertex.y) / height, color));
                    batcher.normal(Vector3.AXIS_Z);
                }
            }
            batcher.end();
        }
        endPainting();

        Vector2.REUSABLE_STACK.push(vertex);
        Color.REUSABLE_STACK.push(color);
    }

    public void fillOval(Vector2 pos, float rx, float ry)
    {
        fillOval(pos.x, pos.y, rx, ry);
    }

    public void fillOval(float x, float y, float rx, float ry)
    {
        oval(x, y, rx, ry, Primitive.TRIANGLE_FAN);
    }

    public void drawCircle(Vector2 pos, float r)
    {
        drawCircle(pos.x, pos.y, r);
    }

    public void drawCircle(float x, float y, float r)
    {
        drawOval(x, y, r, r);
    }

    public void fillCircle(Vector2 pos, float r)
    {
        fillCircle(pos.x, pos.y, r);
    }

    public void fillCircle(float x, float y, float r)
    {
        fillOval(x, y, r, r);
    }

    public void drawLine(float x1, float y1, float x2, float y2)
    {
        Batcher batcher = SilenceEngine.graphics.getBatcher();
        batcher.applyTransform(transform);

        startPainting();
        {
            batcher.begin(Primitive.LINES);
            {
                batcher.vertex(x1, y1);
                batcher.color(paint.getTopLeftColor());
                batcher.normal(Vector3.AXIS_Z);

                batcher.vertex(x2, y2);
                batcher.color(paint.getTopRightColor());
                batcher.normal(Vector3.AXIS_Z);
            }
            batcher.end();
        }
        endPainting();
    }

    private void polygon(Polygon polygon, Primitive primitive)
    {
        Batcher batcher = SilenceEngine.graphics.getBatcher();
        batcher.applyTransform(transform);

        startPainting();
        {
            Vector2 tempVec2 = Vector2.REUSABLE_STACK.pop();
            Color color = Color.REUSABLE_STACK.pop();

            float width = polygon.getBounds().getWidth();
            float height = polygon.getBounds().getHeight();

            batcher.begin(primitive);
            {
                for (Vector2 vertex : polygon.getVertices())
                {
                    batcher.vertex(tempVec2.set(vertex).addSelf(polygon.getPosition()));
                    tempVec2.subtractSelf(polygon.getPosition());
                    batcher.color(paint.getColor(tempVec2.x / width, tempVec2.y / height, color));
                    batcher.normal(Vector3.AXIS_Z);
                }
            }
            batcher.end();

            Vector2.REUSABLE_STACK.push(tempVec2);
            Color.REUSABLE_STACK.push(color);
        }
        endPainting();
    }

    public void drawPolygon(Polygon polygon)
    {
        polygon(polygon, Primitive.LINE_LOOP);
    }

    public void fillPolygon(Polygon polygon)
    {
        polygon(polygon, Primitive.TRIANGLE_FAN);
    }

    public void drawTexturedPolygon(Texture texture, Polygon polygon)
    {
        Batcher batcher = SilenceEngine.graphics.getBatcher();
        batcher.applyTransform(transform);

        startPainting();
        {
            Vector2 vertex = Vector2.REUSABLE_STACK.pop();
            Vector2 texCoord = Vector2.REUSABLE_STACK.pop();

            texture.bind();

            batcher.begin(Primitive.TRIANGLE_FAN);
            {
                float polygonWidth = polygon.getMaxX() - polygon.getMinX();
                float polygonHeight = polygon.getMaxY() - polygon.getMinY();

                polygon.getVertices().forEach(v ->
                {
                    vertex.set(v).addSelf(polygon.getPosition());
                    batcher.vertex(vertex);

                    // Unrotate the original vertex to calculate the correct texture coordinates
                    texCoord.set(v)
                            .subtractSelf(polygonWidth / 2, polygonHeight / 2)
                            .rotateSelf(-polygon.getRotation())
                            .addSelf(polygonWidth / 2, polygonHeight / 2);

                    texCoord.scaleSelf(texture.getMaxU() / polygonWidth, texture.getMaxV() / polygonHeight);

                    batcher.texCoord(texCoord);
                });
            }
            batcher.end();

            Vector2.REUSABLE_STACK.push(vertex);
            Vector2.REUSABLE_STACK.push(texCoord);
        }
        endPainting();
    }

    public void drawTexture(Texture texture, float x, float y)
    {
        drawTexture(texture, x, y, texture.getWidth(), texture.getHeight(), false, false);
    }

    public void drawTexture(Texture texture, float x, float y, float w, float h, boolean flipX, boolean flipY)
    {
        drawTexture(texture, x, y, w, h, flipX, flipY, Color.TRANSPARENT);
    }

    public void drawTexture(Texture texture, float x, float y, float w, float h, boolean flipX, boolean flipY, Color tint)
    {
        Batcher batcher = SilenceEngine.graphics.getBatcher();
        batcher.applyTransform(transform);

        startPainting();
        {
            texture.bind();

            float minU = texture.getMinU();
            float minV = texture.getMinV();
            float maxU = texture.getMaxU();
            float maxV = texture.getMaxV();

            batcher.begin(Primitive.TRIANGLE_FAN);
            {
                batcher.vertex(x, y);
                batcher.color(tint);
                batcher.normal(Vector3.AXIS_Z);
                batcher.texCoord(flipX ? maxU : minU, flipY ? maxV : minV);

                batcher.vertex(x + w, y);
                batcher.color(tint);
                batcher.normal(Vector3.AXIS_Z);
                batcher.texCoord(flipX ? minU : maxU, flipY ? maxV : minV);

                batcher.vertex(x + w, y + h);
                batcher.color(tint);
                batcher.normal(Vector3.AXIS_Z);
                batcher.texCoord(flipX ? minU : maxU, flipY ? minV : maxV);

                batcher.vertex(x, y + h);
                batcher.color(tint);
                batcher.normal(Vector3.AXIS_Z);
                batcher.texCoord(flipX ? maxU : minU, flipY ? minV : maxV);
            }
            batcher.end();
        }
        endPainting();
    }

    public void drawTexture(Texture texture, float x, float y, float w, float h)
    {
        drawTexture(texture, x, y, w, h, false, false);
    }

    public void drawTexture(Texture texture, Vector2 pos)
    {
        drawTexture(texture, pos.x, pos.y, texture.getWidth(), texture.getHeight(), false, false);
    }

    public void drawTexture(Texture texture, Vector2 pos, float w, float h)
    {
        drawTexture(texture, pos.x, pos.y, w, h, false, false);
    }

    public void drawTexture(Texture texture, Vector2 min, Vector2 max)
    {
        Vector2 temp = Vector2.REUSABLE_STACK.pop();
        Vector2 size = temp.set(max).subtractSelf(min);

        drawTexture(texture, min.x, min.y, size.x, size.y, false, false);

        Vector2.REUSABLE_STACK.push(temp);
    }

    public void drawString(String string, Vector2 pos)
    {
        drawString(string, pos.x, pos.y);
    }

    public void drawString(String string, float x, float y)
    {
        Batcher batcher = SilenceEngine.graphics.getBatcher();
        batcher.applyTransform(transform);

        Color color = Color.REUSABLE_STACK.pop();

        startPainting();
        {
            font.drawString(batcher, string, x, y, paint.getColor(0.5f, 0.5f, color));
        }
        endPainting();

        Color.REUSABLE_STACK.push(color);
    }

    public void rotate(float angle)
    {
        transform.rotateSelf(Vector3.AXIS_Z, angle);
    }

    public void translate(float x, float y)
    {
        transform.translateSelf(new Vector2(x, y));
    }

    public void translate(Vector2 pos)
    {
        transform.translateSelf(pos);
    }

    public void scale(float x, float y)
    {
        transform.scaleSelf(new Vector2(x, y));
    }

    public void scale(Vector2 scale)
    {
        transform.scaleSelf(scale);
    }

    public void transform(Transform transform)
    {
        this.transform.applySelf(transform);
    }

    public void transform(Matrix4 transform)
    {
        this.transform.applySelf(transform);
    }

    public void resetTransform()
    {
        transform.reset();
    }

    public Color getColor()
    {
        return paint.getColor(0.5f, 0.5f, color);
    }

    public void setColor(Color color)
    {
        paint.setColor(color);
    }

    public Paint getPaint()
    {
        return paint;
    }

    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }

    public IFont getFont()
    {
        return font;
    }

    public void setFont(IFont font)
    {
        this.font = font;
    }

    public Transform getTransform()
    {
        return transform;
    }

    public void setTransform(Transform transform)
    {
        this.transform = transform;
    }

    public OrthoCam getCamera()
    {
        return camera;
    }

    public void setCamera(OrthoCam cam)
    {
        this.camera = cam;
    }
}
