package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.utils.FileUtils;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class Texture
{
    private int width;
    private int height;
    private int id;

    public static Texture EMPTY;
    public static Texture CURRENT;

    private static int activeTextureUnit = 0;

    private Texture(int width, int height, int id)
    {
        this.width  = width;
        this.height = height;
        this.id     = id;
    }

    public void bind()
    {
        CURRENT = this;
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public static void unbind()
    {
        EMPTY.bind();
    }

    public static int getActiveUnit()
    {
        return activeTextureUnit;
    }

    public static void setActiveUnit(int unit)
    {
        activeTextureUnit = unit;
        glActiveTexture(GL_TEXTURE0 + unit);
    }

    public static Texture fromResource(String name)
    {
        return fromInputStream(FileUtils.getResource(name));
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

    public static Texture fromByteBuffer(ByteBuffer buffer, int width, int height)
    {
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        return new Texture(width, height, textureID);
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

    public static void loadNullTexture()
    {
        if (EMPTY == null)
            EMPTY = fromColor(Color.TRANSPARENT, 16, 16);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getId()
    {
        return id;
    }

    public void dispose()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
        glDeleteTextures(id);
    }
}
