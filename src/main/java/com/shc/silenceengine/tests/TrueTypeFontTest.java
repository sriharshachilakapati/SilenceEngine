package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.OrthoCam;
import com.shc.silenceengine.graphics.Texture;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class TrueTypeFontTest extends Game
{
    private TrueTypeFont font;
    private Batcher      batcher;
    private OrthoCam     cam;
    private Texture      logo;

    public void init()
    {
        batcher = new Batcher();
        cam = new OrthoCam();

        logo = Texture.fromResource("resources/logo.png");

        font = new TrueTypeFont("Comic Sans MS", TrueTypeFont.STYLE_ITALIC | TrueTypeFont.STYLE_BOLD, 36);
    }

    public void update(long delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();
    }

    public void render(long delta)
    {
        // Draw some text: Warning, requires OrthoCam
        batcher.begin(cam);
        {
            font.drawString(batcher, "Hello World!!", 10, 10);
            font.drawString(batcher, "Colored Text!!", 10, 10 + font.getHeight(), Color.RED);
            font.drawString(batcher, "Multi line\nText!!", 10, 10 + 2 * font.getHeight(), Color.GOLD);

            batcher.drawTexture2d(logo, new Vector2(Display.getWidth() - logo.getWidth(),
                                                    Display.getHeight() - logo.getHeight()));
        }
        batcher.end();
    }

    public void resize()
    {
        cam.initProjection(Display.getWidth(), Display.getHeight());
    }

    public void dispose()
    {
        batcher.dispose();
        font.dispose();
        logo.dispose();
    }

    public static void main(String[] args)
    {
        new TrueTypeFontTest().start();
    }
}
