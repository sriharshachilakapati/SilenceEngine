package com.shc.silenceengine.audio.openal;

import com.shc.silenceengine.core.SilenceException;

/**
 * An exception class that represents a OpenAL Context error. This
 * exception is a runtime exception.
 *
 * @author Sri Harsha Chilakapati
 */
public class ALCException extends SilenceException
{
    /**
     * Constructs the SilenceException with a message
     *
     * @param message The exception message
     */
    public ALCException(String message)
    {
        super(message);
    }
}
