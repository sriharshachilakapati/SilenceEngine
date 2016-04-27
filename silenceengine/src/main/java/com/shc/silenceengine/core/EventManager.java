/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
            EventHandlerQueueObject object = eventHandlersQueue.poll();

            if (object == null)
                break;

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
