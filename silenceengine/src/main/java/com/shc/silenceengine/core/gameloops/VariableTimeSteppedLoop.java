package com.shc.silenceengine.core.gameloops;

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.IGameLoop;
import com.shc.silenceengine.core.SilenceEngine;

/**
 * @author Sri Harsha Chilakapati
 */
public class VariableTimeSteppedLoop implements IGameLoop
{
    @Override
    public void performLoopFrame()
    {
        SilenceEngine.input.update();
        Game.INSTANCE.update(0);
        Game.INSTANCE.render(0);
    }
}
