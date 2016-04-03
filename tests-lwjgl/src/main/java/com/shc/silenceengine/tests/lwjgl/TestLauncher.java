package com.shc.silenceengine.tests.lwjgl;

import com.shc.silenceengine.backend.lwjgl.LwjglRuntime;
import com.shc.silenceengine.tests.TestGame;

/**
 * @author Sri Harsha Chilakapati
 */
public class TestLauncher
{
    public static void main(String[] args)
    {
        LwjglRuntime.start(new TestGame());
    }
}