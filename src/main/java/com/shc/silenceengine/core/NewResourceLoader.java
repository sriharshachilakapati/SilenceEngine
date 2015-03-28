package com.shc.silenceengine.core;

import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.models.Model;
import com.shc.silenceengine.utils.FileUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gamefreak0
 */
public class NewResourceLoader
{
    private static NewResourceLoader instance;

    private Map<Integer, Texture> textures;
    private Map<Integer, TrueTypeFont> fonts;
    private Map<Integer, Sound> sounds;
    private Map<Integer, Model> models;
    private Map<String, Integer> texturesToLoad;
    private Map<String, Integer> fontsToLoad;
    private Map<String, Integer> soundsToLoad;
    private Map<String, Integer> modelsToLoad;

    private IRenderProgressCallback renderProgressCallback;

    private int numLoaded;
    private float progress;

    private Texture logo;

    private NewResourceLoader()
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

    public static NewResourceLoader getInstance()
    {
        if(instance == null) instance = new NewResourceLoader();

        return instance;
    }

    public NewResourceLoader setLogo(String logoName)
    {
        setLogo(Texture.fromResource(logoName));
        return instance;
    }

    public NewResourceLoader setLogo(Texture logo)
    {
        logo.dispose();
        this.logo = logo;
        return instance;
    }

    public void setRenderProgressCallback(IRenderProgressCallback
                                                  renderProgressCallback)
    {
        this.renderProgressCallback = renderProgressCallback;
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

    private String fontToString(String name, int style, int size)
    {
        return name + "," + style + "," + size;
    }

    public void startLoading()
    {
        boolean recreateDisplay = Display.isResizable();

        if(recreateDisplay) Display.setResizable(false);

        invoke("");

        for(String texName : texturesToLoad.keySet()) {
            textures.put(texturesToLoad.get(texName), Texture.fromResource
                    (texName));
            numLoaded++;

            invoke(texName);
        }

        for(String fontName : fontsToLoad.keySet()) {
            String[] parts = fontName.split(",");
            TrueTypeFont font;
            int style = Integer.parseInt(parts[1]);
            int size = Integer.parseInt(parts[2]);

            if(parts[0].endsWith(".ttf")) {
                InputStream ttfStream = FileUtils.getResource(parts[0]);

                font = new TrueTypeFont(ttfStream, style, size, true);
            } else {
                font = new TrueTypeFont(parts[0], style, size);
            }
            fonts.put(fontsToLoad.get(fontName), font);

            numLoaded++;

            invoke(fontName);
        }

        for(String soundName : soundsToLoad.keySet()) {
            sounds.put(soundsToLoad.get(soundName), new Sound(soundName));
            numLoaded++;

            invoke(soundName);
        }

        for(String modelName : modelsToLoad.keySet()) {
            models.put(modelsToLoad.get(modelName), Model.load(modelName));
            numLoaded++;

            invoke(modelName);
        }

        invoke("DONE!");

        if(recreateDisplay) Display.setResizable(true);
    }

    /**
     * Invoke the callback with the Game's Batcher and progress using the String
     * provided.
     */
    private void invoke(String info)
    {
        renderProgressCallback.invoke(Game.getBatcher(), updateProgress(),
                info);
    }

    /**
     * Updates the current progress for resource loading and returns the value
     *
     * @return Progress on a scale of 0 (inclusive) to  1 (inclusive)
     */
    private float updateProgress()
    {
        progress = numLoaded / (fontsToLoad.size() + texturesToLoad.size() +
                soundsToLoad.size() + modelsToLoad.size());
        return progress;
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

    public void clear()
    {
        clear(false);
    }

    public void clear(boolean dispose)
    {
        if(dispose) dispose();

        fonts.clear();
        fontsToLoad.clear();
        textures.clear();
        texturesToLoad.clear();
        sounds.clear();
        soundsToLoad.clear();
        models.clear();
        modelsToLoad.clear();
    }

    public void dispose()
    {
        for(int id : textures.keySet())
            textures.get(id).dispose();

        for(int id : fonts.keySet())
            fonts.get(id).dispose();

        for(int id : sounds.keySet())
            sounds.get(id).dispose();

        for(int id : models.keySet())
            models.get(id).dispose();
    }

    @FunctionalInterface
    public interface IRenderProgressCallback
    {
        void invoke(Batcher batcher, float percentage, String file);
    }
}
