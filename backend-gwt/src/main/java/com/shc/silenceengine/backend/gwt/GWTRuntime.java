package com.shc.silenceengine.backend.gwt;

import com.shc.silenceengine.core.SilenceEngine;

/**
 * The GwtRuntime class creates the GWT configuration, creates the display. It starts the native event loop and emits
 * the events required for SilenceEngine to start the game loop.
 *
 * @author Sri Harsha Chilakapati
 */
public final class GwtRuntime
{
    private GwtRuntime()
    {
    }

    public static void start()
    {
        SilenceEngine.log = new GwtLogDevice();
        SilenceEngine.display = new GwtDisplayDevice();
        SilenceEngine.io = new GwtIODevice();
        SilenceEngine.graphics = new GwtGraphicsDevice();
    }
}
