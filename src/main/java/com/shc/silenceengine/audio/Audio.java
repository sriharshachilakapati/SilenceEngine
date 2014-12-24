package com.shc.silenceengine.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.Util.*;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Audio
{
    private static Audio instance;

    public static Audio getInstance()
    {
        if (instance == null)
            instance = new Audio();

        return instance;
    }



    private Audio()
    {

    }
}
