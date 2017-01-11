/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
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

package com.shc.silenceengine.backend.android;

import android.os.AsyncTask;
import com.shc.silenceengine.utils.functional.Provider;
import com.shc.silenceengine.utils.functional.SimpleCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
class AsyncRunner extends AsyncTask<Provider<SimpleCallback>, Void, SimpleCallback>
{
    private static List<AsyncRunner> runners = Collections.synchronizedList(new ArrayList<>());

    @SuppressWarnings({"unchecked"})
    static void runAsync(Provider<SimpleCallback> callback)
    {
        runners.add((AsyncRunner) new AsyncRunner().execute(callback));
    }

    static void cancelAll()
    {
        for (AsyncRunner runner : runners)
            runner.cancel(true);

        runners.clear();
    }

    @SafeVarargs
    @Override
    protected final SimpleCallback doInBackground(Provider<SimpleCallback>... params)
    {
        return params[0].provide();
    }

    @Override
    protected void onPostExecute(SimpleCallback result)
    {
        if (!isCancelled())
            result.invoke();
    }
}
