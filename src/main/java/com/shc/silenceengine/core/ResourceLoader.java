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

package com.shc.silenceengine.core;

import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.Paint;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.models.Model;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.utils.FileUtils;
import com.shc.silenceengine.utils.MathUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 * @author Gamefreak0
 */
public final class ResourceLoader
{
    private static ResourceLoader instance;

    private Map<Integer, Texture>      textures;
    private Map<Integer, TrueTypeFont> fonts;
    private Map<Integer, Sound>        sounds;
    private Map<Integer, Model>        models;
    private Map<String, Integer>       texturesToLoad;
    private Map<String, Integer>       fontsToLoad;
    private Map<String, Integer>       soundsToLoad;
    private Map<String, Integer>       modelsToLoad;

    private IRenderProgressCallback renderProgressCallback;

    private int numLoaded;

    private float renderedProgress;

    private Texture logo;
    private Paint   progressPaint;
    private Paint   progressPaint2;

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

        setLogo("resources/logo.png");
        setRenderProgressCallback(this::defaultRenderProgressCallback);
    }

    public static ResourceLoader getInstance()
    {
        if (instance == null)
            instance = new ResourceLoader();

        return instance;
    }

    public void defaultRenderProgressCallback(Batcher batcher, float percentage, String file)
    {
        if (progressPaint == null)
        {
            progressPaint = new Paint(Color.GRAY, Color.BLUE, Paint.Gradient.LINEAR_LEFT_TO_RIGHT);
            progressPaint2 = new Paint(Color.GREEN);
        }

        while (renderedProgress < percentage * 100)
        {
            // Begin an engine frame
            SilenceEngine.graphics.beginFrame();

            renderedProgress = MathUtils.clamp(++renderedProgress, 0, 100);

            // Bring percentage to a scale of 100 - width - 100
            float actualPercentage = MathUtils.convertRange(renderedProgress, 0, 100, 100, Display.getWidth() - 100);

            // Draw using Graphics2D
            Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();

            // Draw the logo in the center
            float logoX = Display.getWidth() / 2 - logo.getWidth() / 2;
            float logoY = Display.getHeight() / 2 - logo.getHeight() / 2;
            float logoW = logo.getWidth();
            float logoH = logo.getHeight();

            // Check if the logo fits in the display. Otherwise, make it fit.
            if (logoW > Display.getWidth())
            {
                logoX = 0;
                logoW = Display.getWidth();
            }

            if (logoH > Display.getHeight())
            {
                logoY = 0;
                logoH = Display.getHeight();
            }

            // Draw the logo finally
            g2d.drawTexture(logo, logoX, logoY, logoW, logoH);

            Paint originalPaint = g2d.getPaint();
            g2d.setPaint(progressPaint);

            // Draw the progress bar
            g2d.fillRect(50, Display.getHeight() - 75, actualPercentage, 25);
            g2d.setPaint(progressPaint2);
            g2d.drawLine(50, Display.getHeight() - 50, Display.getWidth() - 50, Display.getHeight() - 50);

            g2d.setPaint(originalPaint);

            // End the frame, updating the screen
            SilenceEngine.graphics.endFrame();

            try
            {
                Thread.sleep(1);
            }
            catch (Exception e)
            {
                SilenceException.reThrow(e);
            }
        }
    }

    public ResourceLoader setLogo(String logoName)
    {
        setLogo(Texture.fromResource(logoName));
        return instance;
    }

    public ResourceLoader setLogo(Texture logo)
    {
        if (this.logo != null)
            this.logo.dispose();

        this.logo = logo;
        return instance;
    }

    public void setRenderProgressCallback(IRenderProgressCallback renderProgressCallback)
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
        // No loading if there are no resources to load
        if (texturesToLoad.size() + soundsToLoad.size() + fontsToLoad.size() + modelsToLoad.size() == 0)
            return;

        // Reset the variables to zero
        renderedProgress = 0;
        numLoaded = 0;

        boolean recreateDisplay = Display.isResizable() && !Display.isFullScreen();

        if (recreateDisplay) Display.setResizable(false);

        invokeRenderProgressCallback("");

        for (String texName : texturesToLoad.keySet())
        {
            textures.put(texturesToLoad.get(texName), Texture.fromResource(texName));
            numLoaded++;

            invokeRenderProgressCallback(texName);
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

            invokeRenderProgressCallback(fontName);
        }

        for (String soundName : soundsToLoad.keySet())
        {
            sounds.put(soundsToLoad.get(soundName), new Sound(soundName));
            numLoaded++;

            invokeRenderProgressCallback(soundName);
        }

        for (String modelName : modelsToLoad.keySet())
        {
            models.put(modelsToLoad.get(modelName), Model.load(modelName));
            numLoaded++;

            invokeRenderProgressCallback(modelName);
        }

        invokeRenderProgressCallback("DONE!");

        if (recreateDisplay) Display.setResizable(true);
    }

    /**
     * Invoke the callback with the Game's Batcher and progress using the String provided.
     */
    private void invokeRenderProgressCallback(String info)
    {
        renderProgressCallback.invoke(SilenceEngine.graphics.getBatcher(), updateProgress(), info);
    }

    /**
     * Updates the current progress for resource loading and returns the value
     *
     * @return Progress on a scale of 0 (inclusive) to  1 (inclusive)
     */
    private float updateProgress()
    {
        return numLoaded / (fontsToLoad.size() + texturesToLoad.size() + soundsToLoad.size() + modelsToLoad.size());
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
        if (dispose) dispose();

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
        for (int id : textures.keySet())
            textures.get(id).dispose();

        for (int id : fonts.keySet())
            fonts.get(id).dispose();

        for (int id : sounds.keySet())
            sounds.get(id).dispose();

        for (int id : models.keySet())
            models.get(id).dispose();
    }

    @FunctionalInterface
    public interface IRenderProgressCallback
    {
        void invoke(Batcher batcher, float percentage, String file);
    }
}
