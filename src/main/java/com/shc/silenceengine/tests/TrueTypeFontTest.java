package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class TrueTypeFontTest extends Game
{
    private TrueTypeFont font;
    private OrthoCam cam;
    private Texture logo;

    public static void main(String[] args)
    {
        new TrueTypeFontTest().start();
    }

    public void dispose()
    {
        font.dispose();
        logo.dispose();
    }

    public void init()
    {
        cam = new OrthoCam();

        logo = Texture.fromResource("resources/logo.png");
        font = new TrueTypeFont("Comic Sans MS", TrueTypeFont.STYLE_ITALIC | TrueTypeFont.STYLE_BOLD, 36);
    }

    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();
    }

    public void render(float delta, Batcher batcher)
    {
        // Draw some text: Warning, requires OrthoCam
        cam.apply();

        // Fonts require texture changes, so pass the batcher in inactive state
        font.drawString(batcher, "Hello World!!", 10, 10);
        font.drawString(batcher, "Colored Text!!", 10, 10 + font.getHeight(), Color.RED);
        font.drawString(batcher, "Multi line\nText!!", 10, 10 + 2 * font.getHeight(), Color.GOLD);

        String fpsString = "FPS: " + getFPS();
        font.drawString(batcher, fpsString, Display.getWidth() - font.getWidth(fpsString) - 10, 10, Color.CORN_FLOWER_BLUE);

        batcher.drawTexture2d(logo, new Vector2(Display.getWidth() - logo.getWidth(),
                Display.getHeight() - logo.getHeight()));
    }

    public void resize()
    {
        cam.initProjection(Display.getWidth(), Display.getHeight());
    }
}
