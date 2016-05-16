package com.shc.silenceengine.utils.functional;

/**
 * @author Sri Harsha Chilakapati
 */
@FunctionalInterface
public interface UniCallback<T>
{
    void invoke(T object);
}
