package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;

/**
 * @author Sri Harsha Chilakapati
 */
public class Graphics2DTest extends Game
{
    private Texture texture;

    public static void main(String[] args)
    {
        new Graphics2DTest().start();
    }

    public void dispose()
    {
        ResourceLoader.getInstance().dispose();
    }

    public void init()
    {
        ResourceLoader loader = ResourceLoader.getInstance();

        int fontID = loader.defineFont("Arial", TrueTypeFont.STYLE_NORMAL, 16);
        int texID = loader.defineTexture("resources/texture2.png");
        loader.startLoading();

        Graphics2D g2d = getGraphics2D();
        g2d.setFont(loader.getFont(fontID));

        texture = loader.getTexture(texID);
    }

    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            end();
    }

    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = getGraphics2D();

        g2d.setColor(Color.RED);
        g2d.drawRect(10, 50, 100, 100);

        g2d.setColor(Color.YELLOW_GREEN);
        g2d.fillRect(0, 100, 200, 150);

        g2d.setColor(Color.BLANCHED_ALMOND);
        g2d.fillOval(250, 100, 150, 50);

        g2d.drawTexture(texture, 100, 100, 254, 252);
        g2d.drawString("Hello World", 10, 10);
    }
}
