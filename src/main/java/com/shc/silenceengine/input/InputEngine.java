package com.shc.silenceengine.input;

import com.shc.silenceengine.IEngine;

/**
 * @author Sri Harsha Chilakapati
 */
public class InputEngine implements IEngine
{
    @Override
    public void init()
    {
        Controller.create();
    }

    @Override
    public void beginFrame()
    {
        // Poll the controllers
        Controller.poll();

        // Start the event frames
        Keyboard.startEventFrame();
        Mouse.startEventFrame();
        Controller.startEventFrame();
    }

    @Override
    public void endFrame()
    {
        // Clear the event frames
        Keyboard.clearEventFrame();
        Mouse.clearEventFrame();
        Controller.clearEventFrame();
    }

    @Override
    public void dispose()
    {
    }
}
