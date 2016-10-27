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

package com.shc.silenceengine.utils.functional;

/**
 * @author Sri Harsha Chilakapati
 */
public class Promise<T>
{
    public State state = State.PENDING;
    public T value;

    private Promise<T> next;
    private Throwable throwable;

    private UniCallback<T>         onFulfilled;
    private UniCallback<Throwable> onRejected;

    public Promise(BiCallback<UniCallback<T>, UniCallback<Throwable>> function)
    {
        try
        {
            function.invoke(this::resolve, this::reject);
        }
        catch (Throwable throwable)
        {
            reject(throwable);
        }
    }

    private Promise(UniCallback<T> onFulfilled, UniCallback<Throwable> onRejected)
    {
        this.onFulfilled = onFulfilled;
        this.onRejected = onRejected;
    }

    private void resolve(T value)
    {
        this.value = value;
        this.state = State.FULFILLED;

        if (onFulfilled != null)
            onFulfilled.invoke(value);

        if (next != null)
            next.resolve(value);
    }

    private void reject(Throwable throwable)
    {
        this.value = null;
        this.state = State.REJECTED;
        this.throwable = throwable;

        if (onRejected != null)
            onRejected.invoke(throwable);

        if (next != null)
            next.reject(throwable);
    }

    public Promise<T> then(UniCallback<T> onFulfilled, UniCallback<Throwable> onRejected)
    {
        next = new Promise<>(onFulfilled, onRejected);

        switch (state)
        {
            case REJECTED:
                next.reject(throwable);
                break;

            case FULFILLED:
                next.resolve(value);
                break;
        }

        return next;
    }

    public Promise<T> then(UniCallback<T> onFulfilled)
    {
        return then(onFulfilled, t ->
        {
        });
    }

    public Promise<T> whenThrown(UniCallback<Throwable> onThrown)
    {
        return then(v ->
        {
        }, onThrown);
    }

    public enum State
    {
        PENDING, FULFILLED, REJECTED
    }
}
