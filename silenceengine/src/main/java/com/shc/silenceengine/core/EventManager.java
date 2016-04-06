package com.shc.silenceengine.core;

import com.shc.silenceengine.core.events.IDisposeEventHandler;
import com.shc.silenceengine.core.events.IRenderEventHandler;
import com.shc.silenceengine.core.events.IResizeEventHandler;
import com.shc.silenceengine.core.events.IUpdateEventHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Sri Harsha Chilakapati
 */
public final class EventManager
{
    private List<IUpdateEventHandler>  updateEventHandlers  = new ArrayList<>();
    private List<IRenderEventHandler>  renderEventHandlers  = new ArrayList<>();
    private List<IResizeEventHandler>  resizeEventHandlers  = new ArrayList<>();
    private List<IDisposeEventHandler> disposeEventHandlers = new ArrayList<>();

    private Queue<EventHandlerQueueObject> eventHandlersQueue = new LinkedList<>();

    /**
     * Prevent instantiation by the users.
     */
    EventManager()
    {
    }

    public void addUpdateHandler(IUpdateEventHandler handler)
    {
        EventHandlerQueueObject<IUpdateEventHandler> eventHandlersQueueObject = new EventHandlerQueueObject<>();
        eventHandlersQueueObject.handler = handler;
        eventHandlersQueueObject.action = EventQueueAction.ADD_HANDLER;
        eventHandlersQueueObject.eventType = EventType.UPDATE;

        eventHandlersQueue.add(eventHandlersQueueObject);
    }

    public void addRenderHandler(IRenderEventHandler handler)
    {
        EventHandlerQueueObject<IRenderEventHandler> eventHandlersQueueObject = new EventHandlerQueueObject<>();
        eventHandlersQueueObject.handler = handler;
        eventHandlersQueueObject.action = EventQueueAction.ADD_HANDLER;
        eventHandlersQueueObject.eventType = EventType.RENDER;

        eventHandlersQueue.add(eventHandlersQueueObject);
    }

    public void addResizeHandler(IResizeEventHandler handler)
    {
        EventHandlerQueueObject<IResizeEventHandler> eventHandlersQueueObject = new EventHandlerQueueObject<>();
        eventHandlersQueueObject.handler = handler;
        eventHandlersQueueObject.action = EventQueueAction.ADD_HANDLER;
        eventHandlersQueueObject.eventType = EventType.RESIZE;

        eventHandlersQueue.add(eventHandlersQueueObject);
    }

    public void addDisposeHandler(IDisposeEventHandler handler)
    {
        EventHandlerQueueObject<IDisposeEventHandler> eventHandlersQueueObject = new EventHandlerQueueObject<>();
        eventHandlersQueueObject.handler = handler;
        eventHandlersQueueObject.action = EventQueueAction.ADD_HANDLER;
        eventHandlersQueueObject.eventType = EventType.DISPOSE;

        eventHandlersQueue.add(eventHandlersQueueObject);
    }

    public void removeUpdateHandler(IUpdateEventHandler handler)
    {
        EventHandlerQueueObject<IUpdateEventHandler> eventHandlersQueueObject = new EventHandlerQueueObject<>();
        eventHandlersQueueObject.handler = handler;
        eventHandlersQueueObject.action = EventQueueAction.REMOVE_HANDLER;
        eventHandlersQueueObject.eventType = EventType.UPDATE;

        eventHandlersQueue.add(eventHandlersQueueObject);
    }

    public void removeRenderHandler(IRenderEventHandler handler)
    {
        EventHandlerQueueObject<IRenderEventHandler> eventHandlersQueueObject = new EventHandlerQueueObject<>();
        eventHandlersQueueObject.handler = handler;
        eventHandlersQueueObject.action = EventQueueAction.REMOVE_HANDLER;
        eventHandlersQueueObject.eventType = EventType.RENDER;

        eventHandlersQueue.add(eventHandlersQueueObject);
    }

    public void removeResizeHandler(IResizeEventHandler handler)
    {
        EventHandlerQueueObject<IResizeEventHandler> eventHandlersQueueObject = new EventHandlerQueueObject<>();
        eventHandlersQueueObject.handler = handler;
        eventHandlersQueueObject.action = EventQueueAction.REMOVE_HANDLER;
        eventHandlersQueueObject.eventType = EventType.RESIZE;

        eventHandlersQueue.add(eventHandlersQueueObject);
    }

    public void removeDisposeHandler(IDisposeEventHandler handler)
    {
        EventHandlerQueueObject<IDisposeEventHandler> eventHandlersQueueObject = new EventHandlerQueueObject<>();
        eventHandlersQueueObject.handler = handler;
        eventHandlersQueueObject.action = EventQueueAction.REMOVE_HANDLER;
        eventHandlersQueueObject.eventType = EventType.DISPOSE;

        eventHandlersQueue.add(eventHandlersQueueObject);
    }

    public void raiseUpdateEvent(float deltaTime)
    {
        processHandlers();

        for (IUpdateEventHandler handler : updateEventHandlers)
            handler.update(deltaTime);
    }

    public void raiseRenderEvent(float delta)
    {
        processHandlers();

        for (IRenderEventHandler handler : renderEventHandlers)
            handler.render(delta);
    }

    public void raiseResizeEvent()
    {
        processHandlers();

        for (IResizeEventHandler handler : resizeEventHandlers)
            handler.resized();
    }

    public void raiseDisposeEvent()
    {
        processHandlers();

        for (IDisposeEventHandler handler : disposeEventHandlers)
            handler.dispose();

        // No updates happen after dispose, it's the end of the game
        updateEventHandlers.clear();
        renderEventHandlers.clear();
        resizeEventHandlers.clear();
        disposeEventHandlers.clear();
    }

    @SuppressWarnings({ "suspicious", "SuspiciousMethodCalls" })
    private void processHandlers()
    {
        while (!eventHandlersQueue.isEmpty())
        {
            EventHandlerQueueObject object = eventHandlersQueue.remove();

            switch (object.eventType)
            {
                case UPDATE:
                    if (object.action == EventQueueAction.ADD_HANDLER)
                        updateEventHandlers.add((IUpdateEventHandler) object.handler);
                    else
                        updateEventHandlers.remove(object.handler);
                    break;

                case RENDER:
                    if (object.action == EventQueueAction.ADD_HANDLER)
                        renderEventHandlers.add((IRenderEventHandler) object.handler);
                    else
                        renderEventHandlers.remove(object.handler);
                    break;

                case RESIZE:
                    if (object.action == EventQueueAction.ADD_HANDLER)
                        resizeEventHandlers.add((IResizeEventHandler) object.handler);
                    else
                        resizeEventHandlers.remove(object.handler);
                    break;

                case DISPOSE:
                    if (object.action == EventQueueAction.ADD_HANDLER)
                        disposeEventHandlers.add((IDisposeEventHandler) object.handler);
                    else
                        disposeEventHandlers.remove(object.handler);
                    break;
            }
        }
    }

    private enum EventType
    {
        UPDATE,
        RENDER,
        RESIZE,
        DISPOSE
    }

    private enum EventQueueAction
    {
        ADD_HANDLER,
        REMOVE_HANDLER
    }

    private static class EventHandlerQueueObject<T>
    {
        EventType        eventType;
        EventQueueAction action;

        T handler;
    }
}
