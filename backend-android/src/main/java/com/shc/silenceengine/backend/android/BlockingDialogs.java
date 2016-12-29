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

package com.shc.silenceengine.backend.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.widget.EditText;
import com.shc.silenceengine.core.SilenceException;

import java.util.concurrent.Semaphore;

/**
 * @author Sri Harsha Chilakapati
 */
final class BlockingDialogs
{
    private static boolean confirmResult;
    private static String  promptResult;

    private static final Semaphore semaphore = new Semaphore(0, true);

    private BlockingDialogs()
    {
    }

    static boolean confirm(String message, Activity activity)
    {
        confirmResult = false;

        activity.runOnUiThread(() ->
        {
            Dialog dialog = createAlertDialog(activity, message, true);
            dialog.show();
        });

        acquireSemaphore();

        return confirmResult;
    }

    private static void acquireSemaphore()
    {
        try
        {
            semaphore.acquire();
        }
        catch (InterruptedException e)
        {
            SilenceException.reThrow(e);
        }
    }

    private static Dialog createAlertDialog(Activity activity, String message, boolean cancelButton)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(message);

        builder.setPositiveButton("Okay", (dialog, id) ->
        {
            confirmResult = true;
            semaphore.release();
        });

        if (cancelButton)
        {
            builder.setNegativeButton("Cancel", (dialog, id) ->
            {
                confirmResult = false;
                semaphore.release();
            });
        }

        builder.setTitle(cancelButton ? "Confirm" : "Information");
        builder.setCancelable(false);

        return builder.create();
    }

    static String prompt(String message, String defaultValue, Activity activity)
    {
        promptResult = null;

        activity.runOnUiThread(() ->
        {
            Dialog dialog = createPromptDialog(message, defaultValue, activity);
            dialog.show();
        });

        acquireSemaphore();

        return promptResult;
    }

    private static Dialog createPromptDialog(String message, String defaultValue, Activity activity)
    {
        final AlertDialog.Builder inputAlert = new AlertDialog.Builder(activity);

        inputAlert.setMessage(message);

        final EditText userInput = new EditText(activity);
        inputAlert.setView(userInput);

        if (defaultValue != null)
            userInput.setText(defaultValue);

        inputAlert.setPositiveButton("Submit", (dialog, id) ->
        {
            promptResult = userInput.getText().toString();
            semaphore.release();
        });

        inputAlert.setNegativeButton("Cancel", (dialog, id) ->
        {
            promptResult = null;
            semaphore.release();
        });

        return inputAlert.create();
    }

    static void alert(String message, Activity activity)
    {
        activity.runOnUiThread(() ->
        {
            Dialog dialog = createAlertDialog(activity, message, false);
            dialog.show();
        });

        acquireSemaphore();
    }
}
