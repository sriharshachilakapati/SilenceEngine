package com.shc.silenceengine.audio.openal;

import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALDevice;

/**
 * @author Sri Harsha Chilakapati
 */
public final class ALContext
{
    private static ALContext instance;

    public static ALContext getInstance()
    {
        if (instance == null)
            instance = new ALContext();

        return instance;
    }

    private ALDevice                   device;
    private org.lwjgl.openal.ALContext context;

    private ALContext()
    {
    }

    public void init()
    {
        context = org.lwjgl.openal.ALContext.create();
        device = context.getDevice();

        ALCError.check();

        context.makeCurrent();
        ALCError.check();
        ALCCapabilities capabilities = device.getCapabilities();

        if (!capabilities.OpenALC10)
            throw new ALCException("OpenAL Context Creation failed");
    }

    public void dispose()
    {
        context.destroy();
        device.destroy();
    }

    public org.lwjgl.openal.ALContext getContext()
    {
        return context;
    }

    public ALDevice getDevice()
    {
        return device;
    }
}
