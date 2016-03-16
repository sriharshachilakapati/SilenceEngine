package com.shc.silenceengine.backend.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.shc.silenceengine.core.IDisplayDevice;
import com.shc.silenceengine.io.FilePath;
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
    private Canvas       canvas;
    private WebGLContext context;

    private String title;

    public GwtDisplayDevice()
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

        boolean webgl2 = WebGL20.isSupported();

        if (!webgl2 && !WebGL10.isSupported())
            throw new UnsupportedOperationException("Use a WebGL enabled browser to play this game.");

        // Create a WebGL context. (Prefer WebGL 2.0 over 1.0, but offer fallback)
        context = webgl2 ? WebGL20.createContext(canvas) : WebGL10.createContext(canvas);

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
    }

    @Override
    public void setSize(int width, int height)
    {
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
    }

    @Override
    public void setFullscreen(boolean fullscreen)
    {
        if (fullscreen)
            context.requestFullscreen();
        else
            WebGLContext.exitFullscreen();
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

    private native void setIcon(String url) /*-{
        var head = $doc.getElementsByTagName("head")[0];

        // Remove existing favicons
        var links = head.getElementsByName("link");

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

    @Override
    public void setTitle(String title)
    {
        this.title = title;
        Window.setTitle(title);
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public void setIcon(FilePath filePath)
    {
        setIcon(filePath.getPath());
    }
}
