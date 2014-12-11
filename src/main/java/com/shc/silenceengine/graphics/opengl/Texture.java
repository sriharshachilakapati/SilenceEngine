package com.shc.silenceengine.graphics.opengl;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.utils.FileUtils;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Texture
{
    private int id;

    private float width;
    private float height;

    private boolean disposed;

    public static Texture CURRENT;
    public static Texture EMPTY;

    private static int activeUnit;

    public Texture()
    {
        id = glGenTextures();
        GLError.check();
    }

    public Texture(int id)
    {
        this.id = id;
    }

    public void bind()
    {
        if (CURRENT == this)
            return;

        if (disposed)
            throw new GLException("Cannot bind a disposed texture!");

        glBindTexture(GL_TEXTURE_2D, id);
        GLError.check();

        CURRENT = this;
    }

    public void image2d(ByteBuffer data, int type, int format, int width, int height, int internalFormat)
    {
        bind();

        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, data);
        GLError.check();

        this.width = width;
        this.height = height;
    }

    public void setWrapping(int s)
    {
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, s);
        GLError.check();
    }

    public void setWrapping(int s, int t)
    {
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, s); GLError.check();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, t); GLError.check();
    }

    public void setWrapping(int s, int t, int r)
    {
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, s); GLError.check();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, t); GLError.check();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, r); GLError.check();
    }

    public void setFilter(int min, int mag)
    {
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, min); GLError.check();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mag); GLError.check();
    }

    public void generateMipMaps()
    {
        bind();
        glGenerateMipmap(GL_TEXTURE_2D);
        GLError.check();
    }

    public SubTexture getSubTexture(float minU, float minV, float maxU, float maxV)
    {
        return new SubTexture(this, minU, minV, maxU, maxV);
    }

    public SubTexture getSubTexture(Vector2 min, Vector2 max)
    {
        return new SubTexture(this, min, max);
    }

    public static int getActiveUnit()
    {
        return activeUnit;
    }

    public static void setActiveUnit(int unit)
    {
        if (unit == activeUnit)
            return;

        GLError.check();
        glActiveTexture(GL_TEXTURE0 + unit);
        GLError.check();

        activeUnit = unit;
    }

    public static Texture fromByteBuffer(ByteBuffer buffer, int width, int height)
    {
        Texture texture = new Texture();

        texture.bind();
        texture.setFilter(GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR);
        texture.image2d(buffer, GL_UNSIGNED_BYTE, GL_RGBA, width, height, GL_RGBA8);
        texture.generateMipMaps();

        return texture;
    }

    public static Texture fromColor(Color c, int width, int height)
    {
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int i=0; i<height; i++)
        {
            for (int j=0; j<width; j++)
            {
                buffer.put((byte) (c.getR() * 255f))
                      .put((byte) (c.getG() * 255f))
                      .put((byte) (c.getB() * 255f))
                      .put((byte) (c.getA() * 255f));
            }
        }

        buffer.flip();

        return fromByteBuffer(buffer, width, height);
    }

    public static Texture fromBufferedImage(BufferedImage img)
    {
        ByteBuffer buffer = BufferUtils.createByteBuffer(img.getWidth() * img.getHeight() * 4);

        for (int y = 0; y < img.getHeight(); y++)
        {
            for (int x = 0; x < img.getWidth(); x++)
            {
                // Select the pixel
                int pixel = img.getRGB(x, y);
                // Add the RED component
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                // Add the GREEN component
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                // Add the BLUE component
                buffer.put((byte) (pixel & 0xFF));
                // Add the ALPHA component
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.rewind();

        return fromByteBuffer(buffer, img.getWidth(), img.getHeight());
    }

    public static Texture fromInputStream(InputStream stream)
    {
        try
        {
            return fromBufferedImage(ImageIO.read(stream));
        }
        catch (Exception e)
        {
            throw new SilenceException(e.getMessage());
        }
    }

    public static Texture fromResource(String name)
    {
        return fromInputStream(FileUtils.getResource(name));
    }

    public static void loadNullTexture()
    {
        if (EMPTY == null)
            EMPTY = fromColor(Color.TRANSPARENT, 16, 16);
    }

    public void dispose()
    {
        EMPTY.bind();
        GLError.check();
        glDeleteTextures(id);
        GLError.check();
        disposed = true;
    }

    public int getId()
    {
        return id;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public float getMinU()
    {
        return 0;
    }

    public float getMaxU()
    {
        return 1;
    }

    public float getMinV()
    {
        return 0;
    }

    public float getMaxV()
    {
        return 1;
    }

    public boolean isDisposed()
    {
        return disposed;
    }
}
