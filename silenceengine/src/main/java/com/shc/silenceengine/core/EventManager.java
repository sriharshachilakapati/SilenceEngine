package com.shc.silenceengine.core;

import com.shc.silenceengine.core.events.IDisposeEventHandler;
import com.shc.silenceengine.core.events.IRenderEventHandler;
import com.shc.silenceengine.core.events.IResizeEventHandler;
import com.shc.silenceengine.core.events.IUpdateEventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public final class EventManager
{
    private List<IUpdateEventHandler>  updateEventHandlers  = new ArrayList<>();
    private List<IRenderEventHandler>  renderEventHandlers  = new ArrayList<>();
    private List<IResizeEventHandler>  resizeEventHandlers  = new ArrayList<>();
    private List<IDisposeEventHandler> disposeEventHandlers = new ArrayList<>();

    /**
     * Prevent instantiation by the users.
     */
    EventManager()
    {
    }

    public void addUpdateHandler(IUpdateEventHandler handler)
    {
        updateEventHandlers.add(handler);
    }

    public void addRenderHandler(IRenderEventHandler handler)
    {
        renderEventHandlers.add(handler);
    }

    public void addResizeHandler(IResizeEventHandler handler)
    {
        resizeEventHandlers.add(handler);
    }

    public void addDisposeHandler(IDisposeEventHandler handler)
    {
        disposeEventHandlers.add(handler);
    }

    public void removeDisposeHandler(IDisposeEventHandler handler)
    {
        disposeEventHandlers.remove(handler);
    }

    public void removeResizeHandler(IResizeEventHandler handler)
    {
        resizeEventHandlers.remove(handler);
    }

    public void removeUpdateHandler(IUpdateEventHandler handler)
    {
        updateEventHandlers.remove(handler);
    }

    public void removeRenderHandler(IRenderEventHandler handler)
    {
        renderEventHandlers.remove(handler);
    }

    public void raiseUpdateEvent(float deltaTime)
    {
        for (IUpdateEventHandler handler : updateEventHandlers)
            handler.update(deltaTime);
    }

    public void raiseRenderEvent(float delta)
    {
        for (IRenderEventHandler handler : renderEventHandlers)
            handler.render(delta);
    }

    public void raiseResizeEvent()
    {
        for (IResizeEventHandler handler : resizeEventHandlers)
            handler.resized();
    }

    public void raiseDisposeEvent()
    {
        for (IDisposeEventHandler handler : disposeEventHandlers)
            handler.dispose();

        // No updates happen after dispose, it's the end of the game
        updateEventHandlers.clear();
        renderEventHandlers.clear();
        resizeEventHandlers.clear();
        disposeEventHandlers.clear();
    }
}
