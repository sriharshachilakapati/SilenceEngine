package com.shc.silenceengine.audio.openal;

import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALDevice;

/**
 * Represents an OpenAL Context. This class is not meant to be used
 * directly by the user, as this is used internally in the engine.
 *
 * @author Sri Harsha Chilakapati
 */
public final class ALContext
{
    // Singleton pattern
    private static ALContext instance;

    /**
     * @return The only one instance of the ALContext.
     */
    public static ALContext getInstance()
    {
        if (instance == null)
            instance = new ALContext();

        return instance;
    }

    // The Device and the context
    private ALDevice                   device;
    private org.lwjgl.openal.ALContext context;

    // Prevent instantiation
    private ALContext()
    {
    }

    /**
     * Initializes this context. This is not meant to be called by the user,
     * as this is called by the Engine internally.
     */
    public void init()
    {
        // Create the context and the device
        context = org.lwjgl.openal.ALContext.create();
        device = context.getDevice();

        // Check for the context errors
        ALCError.check();

        // Make the context current and check for errors
        context.makeCurrent();
        ALCError.check();
        ALCCapabilities capabilities = device.getCapabilities();

        // We use OpenAL 1.0 in this engine, if it is not present,
        // The context creation is failed.
        if (!capabilities.OpenALC10)
            throw new ALCException("OpenAL Context Creation failed");
    }

    /**
     * Disposes this OpenAL context. This method is also called by the
     * engine automatically. Don't call it yourselves.
     */
    public void dispose()
    {
        context.destroy();
        device.destroy();
    }

    /**
     * @return The LWJGL's ALContext instance that is wrapped here.
     */
    public org.lwjgl.openal.ALContext getContext()
    {
        return context;
    }

    /**
     * @return The ALDevice that this context is using.
     */
    public ALDevice getDevice()
    {
        return device;
    }
}
