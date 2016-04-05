package com.shc.silenceengine.core.gameloops;

import com.shc.silenceengine.core.IGameLoop;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.utils.TimeUtils;

import static com.shc.silenceengine.graphics.IGraphicsDevice.Constants.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class VariableTimeSteppedLoop implements IGameLoop
{
    private float prevTime;

    @Override
    public void performLoopFrame()
    {
        if (prevTime == 0)
            prevTime = (int) TimeUtils.currentTime();

        float currTime = (float) TimeUtils.currentTime();
        float elapsedTime = currTime - prevTime;

        prevTime = currTime;

        SilenceEngine.eventManager.raiseUpdateEvent(elapsedTime);

        GLContext.clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        SilenceEngine.eventManager.raiseRenderEvent(elapsedTime);
    }
}
