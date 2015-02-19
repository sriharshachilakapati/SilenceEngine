package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.utils.Logger;
import org.lwjgl.openal.ALC10;
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
    // The Device and the context
    private ALDevice device;
    private org.lwjgl.openal.ALContext context;
    // Prevent instantiation
    private ALContext()
    {
    }

    /**
     * @return The only one instance of the ALContext.
     */
    public static ALContext getInstance()
    {
        if (instance == null)
            instance = new ALContext();

        return instance;
    }

    /**
     * Initializes this context. This is not meant to be called by the user,
     * as this is called by the Engine internally.
     *
     * @throws ALCException.InvalidDevice  If there is no available OpenAL device.
     * @throws ALCException.InvalidContext If the context created is invalid.
     * @throws ALCException                If the context doesn't support OpenAL 1.1
     */
    public void init()
    {
        // Create the context and the device
        context = org.lwjgl.openal.ALContext.create(null, 48000, 60, false);
        device = context.getDevice();

        // Check for the context errors
        ALCError.check();

        // Make the context current and check for errors
        context.makeCurrent();
        ALCError.check();
        ALCCapabilities capabilities = device.getCapabilities();

        // We use OpenAL 1.1 in this engine, if it is not present,
        // The context creation is failed.
        if (!capabilities.OpenALC11)
            throw new ALCException("OpenAL Context Creation failed");

        Logger.log("Using device: " + ALC10.alcGetString(device.getPointer(), ALC10.ALC_DEVICE_SPECIFIER));
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
