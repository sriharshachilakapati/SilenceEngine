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

package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.backend.lwjgl3.glfw.Cursor;
import com.shc.silenceengine.backend.lwjgl3.glfw.GLFW3;
import com.shc.silenceengine.backend.lwjgl3.glfw.Monitor;
import com.shc.silenceengine.backend.lwjgl3.glfw.VideoMode;
import com.shc.silenceengine.backend.lwjgl3.glfw.Window;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.cameras.PerspCam;
import com.shc.silenceengine.backend.lwjgl3.opengl.GL3Context;
import com.shc.silenceengine.backend.lwjgl3.opengl.Program;
import com.shc.silenceengine.backend.lwjgl3.opengl.Shader;
import com.shc.silenceengine.backend.lwjgl3.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.Logger;
import com.shc.silenceengine.utils.NativesLoader;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * This is a minimal GLFW test that uses the GLFW wrapper classes and multi threading. This one is modified GLFW demo
 * presented in the LWJGL3 demos repository written by Kai Burjack. Original code can be found at <a
 * href="https://github.com/LWJGL/lwjgl3-demos/blob/master/src/org/lwjgl/demo/opengl/glfw/Multithreaded.java">
 * https://github.com/LWJGL/lwjgl3-demos/blob/master/src/org/lwjgl/demo/opengl/glfw/Multithreaded.java</a>
 *
 * @author Sri Harsha Chilakapati
 */
public class GLFWTest
{
    private final Object lock = new Object();

    private Window  window;
    private boolean running;

    private int width;
    private int height;

    private Program program;

    public static void main(String[] args)
    {
        new GLFWTest().run();
    }

    private void run()
    {
        NativesLoader.loadLWJGL();

        if (!GLFW3.init())
            throw new SilenceException("Failed to initialize GLFW3");

        // Log the available monitors
        for (Monitor monitor : Monitor.getMonitors())
        {
            Logger.info(monitor);

            // Log the available videomodes of that monitor
            monitor.getVideoModes().forEach(Logger::info);
        }

        // Log the gamma ramp of the monitor
        Logger.info(Monitor.getPrimaryMonitor().getGammaRamp());

        // Set the window hints
        Window.setDefaultHints();
        Window.setHint(GLFW.GLFW_RESIZABLE, true);
        Window.setHint(GLFW.GLFW_VISIBLE, false);
        Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        Window.setHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, SilenceEngine.getPlatform() == SilenceEngine.Platform.MACOSX ? 2 : 3);
        Window.setHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, true);
        Window.setHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

        GLFW3.setSwapInterval(1);

        // Create the Window
        window = new Window(width = 640, height = 480, "GLFW Test");
        window.setKeyCallback(this::glfwKeyCallback);
        window.setSizeCallback(this::glfwWindowSizeCallback);

        // Center the window on screen
        VideoMode videoMode = Monitor.getPrimaryMonitor().getVideoMode();
        window.setPosition((videoMode.getWidth() - 640) / 2, (videoMode.getHeight() - 480) / 2);
        window.show();

        // Start the rendering thread
        new Thread(this::renderLoop).start();

        // Start the event message loop
        running = true;
        while (!window.shouldClose())
        {
            GLFW3.waitEvents();
        }

        // Destroy the window once we are no longer running
        synchronized (lock)
        {
            running = false;
            window.destroy();
        }

        GLFW3.terminate();
    }

    private void renderLoop()
    {
        window.makeCurrent();
        window.setCursor(new Cursor(Texture.fromFilePath(FilePath.getResourceFile("resources/cursor.png"))));

        Batcher batcher = new Batcher();
        initShaders();

        batcher.setVertexLocation(0);
        batcher.setColorLocation(1);
        batcher.setTexCoordLocation(2);
        batcher.setNormalLocation(3);

        PerspCam camera = new PerspCam();
        Transform transform = new Transform();

        while (running)
        {
            // Apply the camera
            camera.initProjection(70, ((float) width) / ((float) height), 0.1f, 100f);
            camera.apply();

            // Clear the context and set the viewport
            GL3Context.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL3Context.viewport(0, 0, width, height);

            // Rotate the transform
            transform.rotateSelf(Vector3.AXIS_Y, 1);

            // Setup the uniforms for the batcher
            program.setUniform("projection", camera.getProjection());
            program.setUniform("view", camera.getView());
            program.setUniform("transform", transform.getMatrix());

            // Draw the triangle
            batcher.begin();
            {
                batcher.vertex(+0.0f, +0.5f);
                batcher.color(Color.RED);

                batcher.vertex(+0.5f, -0.5f);
                batcher.color(Color.GREEN);

                batcher.vertex(-0.5f, -0.5f);
                batcher.color(Color.BLUE);
            }
            batcher.end();

            synchronized (lock)
            {
                if (running) window.swapBuffers();
            }
        }

        batcher.dispose();
        program.dispose();
    }

    private void initShaders()
    {
        Shader vs = new Shader(GL20.GL_VERTEX_SHADER);
        vs.source(
                "#version 330 core                                           \n" +

                "layout (location = 0) in vec4 position;                     \n" +
                "layout (location = 1) in vec4 color;                        \n" +
                "layout (location = 2) in vec4 texCoords;                    \n" +
                "layout (location = 3) in vec4 normal;                       \n" +

                "out vec4 fsColor;" +

                "uniform mat4 projection;                                    \n" +
                "uniform mat4 view;                                          \n" +
                "uniform mat4 transform;                                     \n" +

                "void main()                                                 \n" +
                "{                                                           \n" +
                "    gl_Position = projection * view * transform * position; \n" +
                "    fsColor = color;                                        \n" +
                "}"
        );
        vs.compile();

        Shader fs = new Shader(GL20.GL_FRAGMENT_SHADER);
        fs.source(
                "#version 330 core                                           \n" +

                "in vec4 fsColor;                                            \n" +
                "out vec4 fragColor;                                         \n" +

                "void main()                                                 \n" +
                "{                                                           \n" +
                "    fragColor = fsColor;                                    \n" +
                "}"
        );
        fs.compile();

        program = new Program();
        program.attach(vs);
        program.attach(fs);
        program.link();

        vs.dispose();
        fs.dispose();
    }

    private void glfwKeyCallback(Window window, int key, int scanCode, int action, int mods)
    {
        if (key == Keyboard.KEY_ESCAPE && action == GLFW.GLFW_PRESS)
            window.setShouldClose(true);
    }

    private void glfwWindowSizeCallback(Window window, double width, double height)
    {
        this.width = (int) width;
        this.height = (int) height;
    }
}
