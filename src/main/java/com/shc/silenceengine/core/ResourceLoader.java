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
import com.shc.silenceengine.core.glfw.Window;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.Paint;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.models.Model;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.IDGenerator;
import com.shc.silenceengine.utils.MathUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The ResourceLoader is used to load resources asynchronously in a separate thread, so you can display a progress bar
 * and let the player not get bored by showing him a un-responsive black window.
 *
 * @author Sri Harsha Chilakapati
 * @author Gamefreak0
 */
public class ResourceLoader
{
    private static Map<Class<?>, IResourceLoadHelper> loadHelpers;

    private BlockingQueue<ResourceLoadEvent> loadEvents;

    private Map<FilePath, Class<?>>  toBeLoaded;
    private Map<Integer, FilePath>   idMap;
    private Map<FilePath, IResource> loaded;
    private IProgressRenderCallback  progressRenderCallback;

    private Window  loaderWindow;
    private Texture logo;

    private Paint progressPaint;
    private Paint progressPaint2;

    public ResourceLoader()
    {
        toBeLoaded = new HashMap<>();
        idMap = new HashMap<>();
        loaded = new HashMap<>();
        setLogo(FilePath.getResourceFile("resources/logo.png"));

        progressRenderCallback = this::defaultRenderProgressCallback;
    }

    public static void setHelper(Class<?> clazz, IResourceLoadHelper loadHelper)
    {
        loadHelpers.put(clazz, loadHelper);
    }

    private static void textureLoadHelper(FilePath path, ResourceLoader loader)
    {
        try
        {
            ResourceLoadEvent event = new ResourceLoadEvent();
            loader.loadEvents.put(event);

            event.info = "Loading texture: " + path.getPath();
            event.percentage = loader.getProgress();

            Texture texture = Texture.fromFilePath(path);
            loader.loaded.put(path, texture);
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }
    }

    private static void soundLoadHelper(FilePath path, ResourceLoader loader)
    {
        try
        {
            ResourceLoadEvent event = new ResourceLoadEvent();
            loader.loadEvents.put(event);

            event.info = "Loading sound: " + path.getPath();
            event.percentage = loader.getProgress();
            Sound sound = SilenceEngine.audio.getSound(path.getInputStream(), path.getExtension());
            loader.loaded.put(path, sound);
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }
    }

    private static void modelLoadHelper(FilePath path, ResourceLoader loader)
    {
        try
        {
            ResourceLoadEvent event = new ResourceLoadEvent();
            loader.loadEvents.put(event);

            event.info = "Loading model: " + path.getPath();
            event.percentage = loader.getProgress();

            Model model = Model.load(path);
            loader.loaded.put(path, model);
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }
    }

    private static void fontLoadHelper(FilePath path, ResourceLoader loader)
    {
        try
        {
            ResourceLoadEvent event = new ResourceLoadEvent();
            loader.loadEvents.put(event);

            event.info = "Loading font: " + path.getPath();
            event.percentage = loader.getProgress();

            TrueTypeFont font;

            if (!path.exists() && !path.getExtension().equalsIgnoreCase("ttf"))
                font = new TrueTypeFont(path.getName());
            else
                font = new TrueTypeFont(path.getInputStream());

            loader.loaded.put(path, font);
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }
    }

    public void defaultRenderProgressCallback(String info, float percentage)
    {
        if (progressPaint == null)
        {
            progressPaint = new Paint(Color.GRAY, Color.BLUE, Paint.Gradient.LINEAR_LEFT_TO_RIGHT);
            progressPaint2 = new Paint(Color.GREEN);
        }

        // Bring percentage to a scale of 0 - width - 100
        float actualPercentage = MathUtils.convertRange(percentage, 0, 100, 0, Display.getWidth() - 100);

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
        g2d.drawString(info, 50, Display.getHeight() - 80 - g2d.getFont().getHeight());

        g2d.setPaint(originalPaint);
    }

    public void startLoading()
    {
        if (toBeLoaded.keySet().size() == 0)
            return;

        float smoothedProgress = 0;

        loadEvents = new ArrayBlockingQueue<>(toBeLoaded.keySet().size());

        Display.setHints();
        loaderWindow = new Window(Display.getWindow());

        Thread loaderThread = new Thread(this::asyncLoadResources);
        loaderThread.start();

        float progress = 0;
        String info = "Initializing";

        while (smoothedProgress < 100)
        {
            try
            {
                if (loadEvents.peek() != null)
                {
                    ResourceLoadEvent event = loadEvents.peek();

                    if (smoothedProgress >= progress)
                    {
                        progress = event.percentage;
                        info = event.info;

                        loadEvents.take();
                    }
                }

                SilenceEngine.graphics.beginFrame();

                GL3Context.viewport(0, 0, Display.getWidth(), Display.getHeight());
                SilenceEngine.graphics.getGraphics2D().getCamera().initProjection(Display.getWidth(), Display.getHeight());

                smoothedProgress = MathUtils.clamp(++smoothedProgress, 0, progress);
                progressRenderCallback.invoke(info, smoothedProgress);

                // End an engine frame
                SilenceEngine.graphics.endFrame();

                Thread.sleep(1000 / Game.getTargetUPS());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        loaderWindow.destroy();
        Window.setDefaultHints();
    }

    private void asyncLoadResources()
    {
        loaderWindow.makeCurrent();

        for (FilePath path : toBeLoaded.keySet())
        {
            Class<?> clazz = toBeLoaded.get(path);
            loadHelpers.get(clazz).load(path, this);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getResource(int id)
    {
        FilePath path = idMap.get(id);

        if (loaded.containsKey(path))
            return (T) loaded.get(path);

        return null;
    }

    public int loadResource(Class<?> clazz, FilePath path)
    {
        toBeLoaded.put(path, clazz);
        int id = IDGenerator.generate();
        idMap.put(id, path);
        return id;
    }

    public int loadResource(Class<?> clazz, String path)
    {
        return loadResource(clazz, FilePath.getResourceFile(path));
    }

    private float getProgress()
    {
        return (((loaded.keySet().size() + 1) * 100) / toBeLoaded.keySet().size());
    }

    public void setLogo(FilePath logo)
    {
        this.logo = Texture.fromFilePath(logo);
    }

    public Texture getLogo()
    {
        return logo;
    }

    public void setProgressRenderCallback(IProgressRenderCallback progressRenderCallback)
    {
        this.progressRenderCallback = progressRenderCallback;
    }

    public void dispose()
    {
        loaded.values().forEach(IResource::dispose);

        idMap.clear();
        loaded.clear();
        toBeLoaded.clear();
        logo.dispose();
    }

    @FunctionalInterface
    public interface IProgressRenderCallback
    {
        void invoke(String info, float percentage);
    }

    @FunctionalInterface
    public interface IResourceLoadHelper
    {
        void load(FilePath path, ResourceLoader loader);
    }

    private static class ResourceLoadEvent
    {
        public String info;
        public float  percentage;
    }

    static
    {
        loadHelpers = new HashMap<>();

        setHelper(Texture.class, ResourceLoader::textureLoadHelper);
        setHelper(Sound.class, ResourceLoader::soundLoadHelper);
        setHelper(Model.class, ResourceLoader::modelLoadHelper);
        setHelper(TrueTypeFont.class, ResourceLoader::fontLoadHelper);
    }
}
