package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.geom2d.Polygon;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.RenderUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Graphics2D
{
    private static Graphics2D instance;

    public static Graphics2D getInstance()
    {
        if (instance == null)
            instance = new Graphics2D();

        return instance;
    }

    private OrthoCam camera;
    private Color color;
    private TrueTypeFont font;
    private Transform transform;

    protected Graphics2D()
    {
        camera = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());

        color = Color.WHITE;
        transform = new Transform();
    }

    public OrthoCam getCamera()
    {
        return camera;
    }

    /* SHAPE DRAWING METHODS */

    public void drawRect(float x, float y, float w, float h)
    {
        BaseCamera originalCam = BaseCamera.CURRENT;
        camera.apply();

        Batcher batcher = Game.getBatcher();

        batcher.applyTransform(transform);
        batcher.begin(Primitive.LINE_LOOP);
        {
            batcher.vertex(x, y);
            batcher.color(color);

            batcher.vertex(x + w, y);
            batcher.color(color);

            batcher.vertex(x + w, y + h);
            batcher.color(color);

            batcher.vertex(x, y + h);
            batcher.color(color);
        }
        batcher.end();

        if (originalCam != null)
            originalCam.apply();
    }

    public void drawRect(Vector2 pos, float w, float h)
    {
        drawRect(pos.x, pos.y, w, h);
    }

    public void drawRect(Vector2 min, Vector2 max)
    {
        Vector2 size = max.subtract(min);
        drawRect(min.x, max.x, size.x, size.y);
    }

    public void fillRect(float x, float y, float w, float h)
    {
        BaseCamera originalCam = BaseCamera.CURRENT;
        camera.apply();

        Batcher batcher = Game.getBatcher();

        batcher.applyTransform(transform);
        batcher.begin(Primitive.TRIANGLE_FAN);
        {
            batcher.vertex(x, y);
            batcher.color(color);

            batcher.vertex(x + w, y);
            batcher.color(color);

            batcher.vertex(x + w, y + h);
            batcher.color(color);

            batcher.vertex(x, y + h);
            batcher.color(color);
        }
        batcher.end();

        if (originalCam != null)
            originalCam.apply();
    }

    public void fillRect(Vector2 pos, float w, float h)
    {
        fillRect(pos.x, pos.y, w, h);
    }

    public void fillRect(Vector2 min, Vector2 max)
    {
        Vector2 size = max.subtract(min);
        fillRect(min.x, min.y, size.x, size.y);
    }

    public void drawOval(float x, float y, float rx, float ry)
    {
        BaseCamera originalCam = BaseCamera.CURRENT;
        camera.apply();

        Batcher batcher = Game.getBatcher();

        batcher.applyTransform(transform);
        batcher.begin(Primitive.LINE_LOOP);
        {
            for (int i = 0; i < 360; i++)
            {
                batcher.vertex(x + MathUtils.cos(i) * rx, y + MathUtils.sin(i) * ry);
                batcher.color(color);
            }
        }
        batcher.end();

        if (originalCam != null)
            originalCam.apply();
    }

    public void drawOval(Vector2 pos, float rx, float ry)
    {
        drawOval(pos.x, pos.y, rx, ry);
    }

    public void fillOval(float x, float y, float rx, float ry)
    {
        BaseCamera originalCam = BaseCamera.CURRENT;
        camera.apply();

        Batcher batcher = Game.getBatcher();

        batcher.applyTransform(transform);
        batcher.begin(Primitive.TRIANGLE_FAN);
        {
            for (int i = 0; i < 360; i++)
            {
                batcher.vertex(x + MathUtils.cos(i) * rx, y + MathUtils.sin(i) * ry);
                batcher.color(color);
            }
        }
        batcher.end();

        if (originalCam != null)
            originalCam.apply();
    }

    public void fillOval(Vector2 pos, float rx, float ry)
    {
        fillOval(pos.x, pos.y, rx, ry);
    }

    public void drawCircle(float x, float y, float r)
    {
        drawOval(x, y, r, r);
    }

    public void drawCircle(Vector2 pos, float r)
    {
        drawCircle(pos.x, pos.y, r);
    }

    public void fillCircle(float x, float y, float r)
    {
        fillOval(x, y, r, r);
    }

    public void fillCircle(Vector2 pos, float r)
    {
        fillCircle(pos.x, pos.y, r);
    }

    public void drawPolygon(Polygon polygon)
    {
        BaseCamera originalCam = BaseCamera.CURRENT;
        camera.apply();

        Batcher batcher = Game.getBatcher();
        batcher.applyTransform(transform);
        RenderUtils.tracePolygon(batcher, polygon, color);

        if (originalCam != null)
            originalCam.apply();
    }

    public void fillPolygon(Polygon polygon)
    {
        BaseCamera originalCam = BaseCamera.CURRENT;
        camera.apply();

        Batcher batcher = Game.getBatcher();
        batcher.applyTransform(transform);
        RenderUtils.fillPolygon(batcher, polygon, color);

        if (originalCam != null)
            originalCam.apply();
    }

    /* TEXTURE DRAWING METHODS */

    public void drawTexture(Texture texture, float x, float y, float w, float h, boolean flipX, boolean flipY)
    {
        BaseCamera originalCam = BaseCamera.CURRENT;
        camera.apply();

        Batcher batcher = Game.getBatcher();

        Texture originalTexture = Texture.CURRENT;
        texture.bind();

        float minU = texture.getMinU();
        float minV = texture.getMinV();
        float maxU = texture.getMaxU();
        float maxV = texture.getMaxV();

        batcher.applyTransform(transform);
        batcher.begin(Primitive.TRIANGLE_FAN);
        {
            batcher.vertex(x, y);
            batcher.texCoord(flipX ? maxU : minU, flipY ? maxV : minV);

            batcher.vertex(x + w, y);
            batcher.texCoord(flipX ? minU : maxU, flipY ? maxV : minV);

            batcher.vertex(x + w, y + h);
            batcher.texCoord(flipX ? minU : maxU, flipY ? minV : maxV);

            batcher.vertex(x, y + h);
            batcher.texCoord(flipX ? maxU : minU, flipY ? minV : maxV);
        }
        batcher.end();

        originalTexture.bind();
        if (originalCam != null)
            originalCam.apply();
    }

    public void drawTexture(Texture texture, float x, float y)
    {
        drawTexture(texture, x, y, texture.getWidth(), texture.getHeight(), false, false);
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
        Vector2 size = max.subtract(min);
        drawTexture(texture, min.x, min.y, size.x, size.y, false, false);
    }

    /**
     * String drawing functions
     */
    public void drawString(String string, float x, float y)
    {
        BaseCamera originalCamera = BaseCamera.CURRENT;
        camera.apply();

        Batcher batcher = Game.getBatcher();
        batcher.applyTransform(transform);

        font.drawString(batcher, string, x, y, color);
        if (originalCamera != null)
            originalCamera.apply();
    }

    public void drawString(String string, Vector2 pos)
    {
        drawString(string, pos.x, pos.y);
    }

    /**
     * Transform methods
     */
    public void rotate(float angle)
    {
        transform.rotate(Vector3.AXIS_Z, angle);
    }

    public void translate(float x, float y)
    {
        transform.translate(new Vector2(x, y));
    }

    public void translate(Vector2 pos)
    {
        transform.translate(pos);
    }

    public void scale(float x, float y)
    {
        transform.scale(new Vector2(x, y));
    }

    public void scale(Vector2 scale)
    {
        transform.scale(scale);
    }

    public void resetTransform()
    {
        transform.reset();
    }

    /**
     * Getters and Setters
     */

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public TrueTypeFont getFont()
    {
        return font;
    }

    public void setFont(TrueTypeFont font)
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
}
