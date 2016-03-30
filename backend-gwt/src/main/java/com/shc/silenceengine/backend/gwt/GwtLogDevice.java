package com.shc.silenceengine.backend.gwt;

import com.shc.silenceengine.logging.ILogDevice;
import com.shc.silenceengine.logging.Logger;

/**
 * @author Sri Harsha Chilakapati
 */
public class GwtLogDevice implements ILogDevice
{
    private Logger rootLogger = new GwtLogger("SilenceEngine");

    @Override
    public Logger getLogger(String name)
    {
        return new GwtLogger(name);
    }

    @Override
    public Logger getRootLogger()
    {
        return rootLogger;
    }
}
