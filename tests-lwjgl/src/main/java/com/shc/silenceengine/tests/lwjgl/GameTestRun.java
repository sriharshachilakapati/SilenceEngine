package com.shc.silenceengine.tests.lwjgl;

import com.shc.silenceengine.backend.lwjgl.LwjglRuntime;
import com.shc.silenceengine.tests.GameTest;
import com.shc.silenceengine.tests.TestRunner;

/**
 * @author Sri Harsha Chilakapati
 */
public class GameTestRun
{
    public static void main(String[] args)
    {
        LwjglRuntime.start(new TestRunner(new GameTest()));
    }
}
