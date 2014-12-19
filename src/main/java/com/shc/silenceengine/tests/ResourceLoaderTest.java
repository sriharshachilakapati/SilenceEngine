package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.OrthoCam;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;

/**
 * @author Sri Harsha Chilakapati
 */
public class ResourceLoaderTest extends Game
{
    private Texture texture;

    private TrueTypeFont font1;
    private TrueTypeFont font2;

    private OrthoCam cam;

    public void init()
    {
        ResourceLoader loader = ResourceLoader.getInstance();

        int fontID1 = loader.defineFont("Times New Roman", TrueTypeFont.STYLE_NORMAL, 24);
        int fontID2 = loader.defineFont("resources/Cursif.ttf", TrueTypeFont.STYLE_ITALIC | TrueTypeFont.STYLE_BOLD, 24);
        int textureID = loader.defineTexture("resources/texture2.png");

        loader.startLoading();

        texture = loader.getTexture(textureID);
        font1   = loader.getFont(fontID1);
        font2   = loader.getFont(fontID2);

        cam = new OrthoCam().initProjection(Display.getWidth(), Display.getHeight());
    }

    public void update(float delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();
    }

    public void render(float delta, Batcher batcher)
    {
        cam.apply();

        batcher.begin();
        {
            batcher.drawTexture2d(texture, new Vector2(100, 100));
        }
        batcher.end();

        font1.drawString(batcher, "Times New Roman!!", 10, 10);
        font2.drawString(batcher, "Cursifi -- A Custom Font!", 10, 40, Color.GREEN);
    }

    public void resize()
    {
        cam.initProjection(Display.getWidth(), Display.getHeight());
    }

    public void dispose()
    {
        ResourceLoader.getInstance().dispose();
    }

    public static void main(String[] args)
    {
        new ResourceLoaderTest().start();
    }
}
