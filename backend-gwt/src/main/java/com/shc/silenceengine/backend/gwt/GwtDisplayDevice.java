/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.backend.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.shc.silenceengine.core.IDisplayDevice;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.functional.SimpleCallback;
import com.shc.silenceengine.utils.functional.UniCallback;
import com.shc.webgl4j.client.OES_vertex_array_object;
import com.shc.webgl4j.client.TimeUtil;
import com.shc.webgl4j.client.WebGL10;
import com.shc.webgl4j.client.WebGL20;
import com.shc.webgl4j.client.WebGLContext;

/**
 * A Display implementation for GWT platform.
 *
 * @author Sri Harsha Chilakapati
 */
public class GwtDisplayDevice implements IDisplayDevice
{
    Canvas canvas;

    private WebGLContext context;

    private String title;

    private boolean fullScreenRequested;
    private boolean focus = true;

    GwtDisplayDevice()
    {
        // Create the canvas
        canvas = Canvas.createIfSupported();

        if (canvas == null)
            throw new UnsupportedOperationException("Use a HTML5 supported browser to play this game.");

        // Set the canvas size
        canvas.setCoordinateSpaceWidth(800);
        canvas.setCoordinateSpaceHeight(600);

        // Add the canvas to the page
        RootPanel.get().add(canvas);
        canvas.setFocus(true);

        boolean webgl2 = WebGL20.isSupported();

        if (!webgl2 && !WebGL10.isSupported())
            throw new UnsupportedOperationException("Use a WebGL enabled browser to play this game.");

        // Create a WebGL context. (Prefer WebGL 2.0 over 1.0, but offer fallback)
        context = webgl2 ? WebGL20.createContext(canvas) : WebGL10.createContext(canvas);

        if (!webgl2)
            if (!OES_vertex_array_object.isSupported())
                throw new UnsupportedOperationException("This browser lacks required OES_vertex_array_object extension.");

        // Clear the screen to black and set the viewport
        WebGL10.glViewport(0, 0, 800, 600);
        WebGL10.glClearColor(0, 0, 0, 1);
        WebGL10.glClear(WebGL10.GL_COLOR_BUFFER_BIT);

        // Set some CSS to control fullscreen mode
        StyleElement style = Document.get().createStyleElement();

        style.setInnerHTML(
                // Style as per the specification
                "canvas:fullscreen                          \n" +
                "{                                          \n" +
                "    position: absolute;                    \n" +
                "    top: 0; left: 0; right: 0; bottom: 0;  \n" +
                "    margin: auto;                          \n" +
                "    width: 100%   !important;              \n" +
                "    height: 100%  !important;              \n" +
                "}" +

                // Style for webkit browsers (Chrome & Safari)
                "canvas:-webkit-full-screen                 \n" +
                "{                                          \n" +
                "    position: absolute;                    \n" +
                "    top: 0; left: 0; right: 0; bottom: 0;  \n" +
                "    margin: auto;                          \n" +
                "    width: 100%   !important;              \n" +
                "    height: 100%  !important;              \n" +
                "}" +

                // Style for mozilla (Firefox)
                "canvas:-moz-full-screen                    \n" +
                "{                                          \n" +
                "    position: absolute;                    \n" +
                "    top: 0; left: 0; right: 0; bottom: 0;  \n" +
                "    margin: auto;                          \n" +
                "    width: 100%   !important;              \n" +
                "    height: 100%  !important;              \n" +
                "}" +

                // Style for MS browsers (IE and Edge)
                "canvas:-ms-fullscreen                      \n" +
                "{                                          \n" +
                "    position: absolute;                    \n" +
                "    top: 0; left: 0; right: 0; bottom: 0;  \n" +
                "    margin: auto;                          \n" +
                "    width: 100%   !important;              \n" +
                "    height: 100%  !important;              \n" +
                "}"
        );

        // Insert the CSS into head of the page
        Document.get().getHead().appendChild(style);

        // Fullscreen should be requested from an event handler, so if there is a request, we delay it till an event
        // happens (this is usually fast enough, since happens just the next frame, or in the same frame if any user
        // interaction event has happened).
        canvas.addKeyDownHandler(event -> checkRequestFullscreen());
        canvas.addKeyUpHandler(event -> checkRequestFullscreen());
        canvas.addKeyPressHandler(event -> checkRequestFullscreen());
        canvas.addMouseWheelHandler(event -> checkRequestFullscreen());
        canvas.addMouseMoveHandler(event -> checkRequestFullscreen());
        canvas.addMouseDownHandler(event -> checkRequestFullscreen());
        canvas.addMouseUpHandler(event -> checkRequestFullscreen());
        canvas.addTouchStartHandler(event -> checkRequestFullscreen());
        canvas.addTouchEndHandler(event -> checkRequestFullscreen());
        canvas.addTouchMoveHandler(event -> checkRequestFullscreen());
        canvas.addTouchCancelHandler(event -> checkRequestFullscreen());

        canvas.addFocusHandler(event -> focus = true);
        canvas.addBlurHandler(event -> focus = false);

        hookFocusCallbacks(this);
    }

    private native void hookFocusCallbacks(GwtDisplayDevice self) /*-{
        $wnd.onfocus = function ()
        {
            self.@com.shc.silenceengine.backend.gwt.GwtDisplayDevice::focus = true;

            var loop = @com.shc.silenceengine.core.SilenceEngine::gameLoop;
            loop.@com.shc.silenceengine.core.IGameLoop::onFocusGain()();
        };

        $wnd.onblur = function ()
        {
            self.@com.shc.silenceengine.backend.gwt.GwtDisplayDevice::focus = false;

            var loop = @com.shc.silenceengine.core.SilenceEngine::gameLoop;
            loop.@com.shc.silenceengine.core.IGameLoop::onFocusLost()();
        };
    }-*/;

    private void checkRequestFullscreen()
    {
        if (fullScreenRequested)
        {
            // If the display requested a fullscreen change,
            context.requestFullscreen();

            if (WebGLContext.isFullscreen())
            {
                fullScreenRequested = false;
                SilenceEngine.eventManager.raiseResizeEvent();

                canvas.setFocus(true);
            }
        }
    }

    @Override
    public SilenceEngine.Platform getPlatform()
    {
        return SilenceEngine.Platform.HTML5;
    }

    @Override
    public void setSize(int width, int height)
    {
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);

        SilenceEngine.eventManager.raiseResizeEvent();
    }

    @Override
    public boolean isFullscreen()
    {
        return WebGLContext.isFullscreen();
    }

    @Override
    public void setFullscreen(boolean fullscreen)
    {
        if (fullscreen)
        {
            // We need to make sure that fullscreen is only requested from within event handler.
            // Otherwise browsers will not accept our request.
            fullScreenRequested = true;
        }
        else
        {
            WebGLContext.exitFullscreen();
            fullScreenRequested = false;
        }

        SilenceEngine.eventManager.raiseResizeEvent();
        canvas.setFocus(true);
    }

    @Override
    public void centerOnScreen()
    {
    }

    @Override
    public void setPosition(int x, int y)
    {
    }

    @Override
    public int getWidth()
    {
        return canvas.getCoordinateSpaceWidth();
    }

    @Override
    public int getHeight()
    {
        return canvas.getCoordinateSpaceHeight();
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public void setTitle(String title)
    {
        this.title = title;
        Window.setTitle(title);
    }

    @Override
    public void setIcon(FilePath filePath, SimpleCallback callback, UniCallback<Throwable> error)
    {
        setIcon(filePath.getPath());
        callback.invoke();
    }

    @Override
    public void close()
    {
    }

    @Override
    public double nanoTime()
    {
        return TimeUtil.currentNanos();
    }

    @Override
    public void setVSync(boolean vSync)
    {
    }

    @Override
    public boolean hasFocus()
    {
        return focus;
    }

    @Override
    public void setGrabMouse(boolean grabMouse)
    {
        grabMouseImpl(grabMouse, canvas.getCanvasElement());
    }

    @Override
    public String prompt(String message, String defaultValue)
    {
        return Window.prompt(message, defaultValue);
    }

    @Override
    public boolean confirm(String message)
    {
        return Window.confirm(message);
    }

    @Override
    public void alert(String message)
    {
        Window.alert(message);
    }

    private native void grabMouseImpl(boolean grabMouse, CanvasElement canvas) /*-{
        if (grabMouse)
        {
            canvas.requestPointerLock = canvas.requestPointerLock ||
                canvas.mozRequestPointerLock;

            canvas.requestPointerLock();
        }
        else
        {
            $doc.exitPointerLock = $doc.exitPointerLock || $doc.mozExitPointerLock;
            $doc.exitPointerLock();
        }
    }-*/;

    private native void setIcon(String url) /*-{
        var head = $doc.getElementsByTagName("head")[0];

        // Remove existing favicons
        var links = head.getElementsByTagName("link");

        for (var i = 0; i < links.length; i++)
        {
            if (/\bicon\b/i.test(links[i].getAttribute("rel")))
                head.removeChild(links[i]);
        }

        // Create a new link element
        var link = $doc.createElement("link");

        link.type = "image/x-icon";
        link.rel = "icon";
        link.href = url;

        head.appendChild(link);
    }-*/;
}
