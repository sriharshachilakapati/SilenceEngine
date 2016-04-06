package com.shc.silenceengine.tests.lwjgl;

import com.shc.silenceengine.backend.lwjgl.LwjglRuntime;
import com.shc.silenceengine.tests.TestRunner;
import com.shc.silenceengine.tests.TouchTest;

/**
 * @author Sri Harsha Chilakapati
 */
public class TouchTestRun
{
    public static void main(String[] args)
    {
        LwjglRuntime.start(new TestRunner(new TouchTest()));
    }
}
