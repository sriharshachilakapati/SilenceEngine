package com.shc.silenceengine.backend.android;

import android.app.Activity;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;

/**
 * @author Sri Harsha Chilakapati
 */
public final class AndroidRuntime
{
    private AndroidRuntime()
    {
    }

    public static void start(Activity activity, Game game)
    {
        SilenceEngine.display = new AndroidDisplayDevice(activity);
        SilenceEngine.graphics = new AndroidGraphicsDevice();

        game.init();
    }
}
