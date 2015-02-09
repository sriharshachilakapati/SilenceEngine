package com.shc.silenceengine.utils;

import com.shc.silenceengine.core.SilenceException;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 *
 * @param <T> Any typed parameter.
 *
 * @author Sri Harsha Chilakapati
 */
public class ReusableStack<T>
{
    private Deque<T> stack;
    private Class<T> tClass;

    public ReusableStack(Class<T> tClass)
    {
        stack = new ArrayDeque<>();
        this.tClass = tClass;
    }

    public T pop()
    {
        if (stack.size() == 0)
            try
            {
                stack.push(tClass.newInstance());
            }
            catch (Exception e)
            {
                SilenceException.reThrow(e);
            }

        return stack.pop();
    }

    public void push(T value)
    {
        stack.push(value);
    }
}
