package com.shc.silenceengine.core;

import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.models.Model;
import com.shc.silenceengine.utils.FileUtils;
import com.shc.silenceengine.utils.MathUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Sri Harsha Chilakapati
 */
public final class ResourceLoader
{
    private Map<Integer, Texture>      textures;
    private Map<Integer, TrueTypeFont> fonts;
    private Map<Integer, Sound>        sounds;
    private Map<Integer, Model> models;

    private Map<String, Integer> texturesToLoad;
    private Map<String, Integer> fontsToLoad;
    private Map<String, Integer> soundsToLoad;
    private Map<String, Integer> modelsToLoad;

    private int     numLoaded;
    private Texture logo;

    private static ResourceLoader instance;

    public static ResourceLoader getInstance()
    {
        if (instance == null)
            instance = new ResourceLoader();

        return instance;
    }

    private ResourceLoader()
    {
        textures = new HashMap<>();
        fonts = new HashMap<>();
        sounds = new HashMap<>();
        models = new HashMap<>();

        texturesToLoad = new HashMap<>();
        fontsToLoad = new HashMap<>();
        soundsToLoad = new HashMap<>();
        modelsToLoad = new HashMap<>();

        numLoaded = 0;

        logo = Texture.fromResource("resources/logo.png");
    }

    public void setLogo(String logoName)
    {
        logo.dispose();
        logo = Texture.fromResource(logoName);
    }

    public int defineTexture(String name)
    {
        int id = texturesToLoad.size();
        texturesToLoad.put(name, id);
        return id;
    }

    public int defineSound(String name)
    {
        int id = soundsToLoad.size();
        soundsToLoad.put(name, id);
        return id;
    }

    public int defineModel(String name)
    {
        int id = modelsToLoad.size();
        modelsToLoad.put(name, id);
        return id;
    }

    public int defineFont(String name, int style, int size)
    {
        int id = fontsToLoad.size();
        fontsToLoad.put(fontToString(name, style, size), id);
        return id;
    }

    public void startLoading()
    {
        Display.setResizable(false);
        renderProgress();

        for (String texName : texturesToLoad.keySet())
        {
            textures.put(texturesToLoad.get(texName), Texture.fromResource(texName));
            numLoaded++;

            renderProgress();
        }

        for (String fontName : fontsToLoad.keySet())
        {
            String[] parts = fontName.split(",");
            TrueTypeFont font;
            int style = Integer.parseInt(parts[1]);
            int size = Integer.parseInt(parts[2]);

            if (parts[0].endsWith(".ttf"))
            {
                InputStream ttfStream = FileUtils.getResource(parts[0]);

                font = new TrueTypeFont(ttfStream, style, size, true);
            }
            else
            {
                font = new TrueTypeFont(parts[0], style, size);
            }
            fonts.put(fontsToLoad.get(fontName), font);

            numLoaded++;

            renderProgress();
        }

        for (String soundName : soundsToLoad.keySet())
        {
            sounds.put(soundsToLoad.get(soundName), new Sound(soundName));
            numLoaded++;

            renderProgress();
        }

        for (String modelName : modelsToLoad.keySet())
        {
            models.put(modelsToLoad.get(modelName), Model.load(modelName));
            numLoaded++;

            renderProgress();
        }

        Display.setResizable(true);
    }

    public Texture getTexture(int id)
    {
        return textures.get(id);
    }

    public TrueTypeFont getFont(int id)
    {
        return fonts.get(id);
    }

    public Sound getSound(int id)
    {
        return sounds.get(id);
    }

    public Model getModel(int id)
    {
        return models.get(id);
    }

    public void dispose()
    {
        for (int id : textures.keySet())
            textures.get(id).dispose();

        for (int id : fonts.keySet())
            fonts.get(id).dispose();

        for (int id : sounds.keySet())
            sounds.get(id).dispose();

        for (int id : models.keySet())
            models.get(id).dispose();
    }

    public void clear(boolean dispose)
    {
        if (dispose)
            dispose();

        fonts.clear();
        fontsToLoad.clear();
        textures.clear();
        texturesToLoad.clear();
        sounds.clear();
        soundsToLoad.clear();
        models.clear();
        modelsToLoad.clear();
    }

    public void clear()
    {
        clear(false);
    }

    private String fontToString(String name, int style, int size)
    {
        return name + "," + style + "," + size;
    }

    // How much progress that is rendered, used to smooth the
    // transition in the progressbar.
    private float renderedProgress;

    private void renderProgress()
    {
        float percentage = 100 * numLoaded / (fontsToLoad.size() + texturesToLoad.size() + soundsToLoad.size() + modelsToLoad.size());

        while (renderedProgress < percentage)
        {
            logo.bind();
            renderedProgress = MathUtils.clamp(++renderedProgress, 0, 100);

            // Bring percentage to a scale (-0.8f to +0.8f)
            float actualPercentage = ((renderedProgress * 1.6f) / 100) - 0.8f;

            GL3Context.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            GL3Context.viewport(0, 0, Display.getWidth(), Display.getHeight());

            Batcher batcher = Game.getBatcher();

            // Draw the logo
            batcher.begin(Primitive.TRIANGLE_STRIP);
            {
                batcher.vertex(-0.5f, +0.5f);
                batcher.texCoord(0, 0);

                batcher.vertex(+0.5f, +0.5f);
                batcher.texCoord(1, 0);

                batcher.vertex(-0.5f, -0.5f);
                batcher.texCoord(0, 1);

                batcher.vertex(+0.5f, -0.5f);
                batcher.texCoord(1, 1);
            }
            batcher.end();

            Texture.EMPTY.bind();

            // Draw the progressbar
            batcher.begin(Primitive.LINE_LOOP);
            {
                batcher.vertex(-0.8f, -0.7f);
                batcher.color(Color.GREEN);

                batcher.vertex(+0.8f, -0.7f);
                batcher.color(Color.GREEN);

                batcher.vertex(+0.8f, -0.7f);
                batcher.color(Color.GREEN);

                batcher.vertex(-0.8f, -0.7f);
                batcher.color(Color.GREEN);
            }
            batcher.end();

            batcher.begin(Primitive.TRIANGLE_STRIP);
            {
                batcher.vertex(-0.8f, -0.6f);
                batcher.color(Color.BLUE);

                batcher.vertex(actualPercentage, -0.6f);
                batcher.color(Color.GRAY);

                batcher.vertex(-0.8f, -0.7f);
                batcher.color(Color.BLUE);

                batcher.vertex(actualPercentage, -0.7f);
                batcher.color(Color.GRAY);
            }
            batcher.end();

            Display.update();
            try
            {
                Thread.sleep(1 / Game.getTargetUPS());
            }
            catch (Exception e)
            {
                throw new SilenceException(e.getMessage());
            }
        }
    }
}
