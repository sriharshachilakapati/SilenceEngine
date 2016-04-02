package com.shc.silenceengine.backend.gwt;

import com.shc.silenceengine.logging.Logger;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtLogger extends Logger
{
    public GwtLogger(String name)
    {
        super(name);
    }

    @Override
    public void info(Object... messages)
    {
        for (Object message : messages)
            nInfo(name, message.toString());
    }

    @Override
    public void warn(Object... messages)
    {
        for (Object message : messages)
            nWarn(name, message.toString());
    }

    @Override
    public void error(Object... messages)
    {
        for (Object message : messages)
            nError(name, message.toString());
    }

    private static native void nInfo(String name, String message) /*-{
        $wnd.console.log("[" + name + "] INFO: " + message);
    }-*/;

    private static native void nWarn(String name, String message) /*-{
        $wnd.console.warn("[" + name + "] WARN: " + + message);
    }-*/;

    private static native void nError(String name, String message) /*-{
        $wnd.console.error("[" + name + "] ERROR: " + + message);
    }-*/;
}
