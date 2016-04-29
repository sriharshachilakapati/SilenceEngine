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

package com.shc.silenceengine.tests.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.shc.silenceengine.backend.gwt.GwtRuntime;
import com.shc.silenceengine.tests.DynamicRendererTest;
import com.shc.silenceengine.tests.EntityCollisionTest2D;
import com.shc.silenceengine.tests.GameTest;
import com.shc.silenceengine.tests.KeyboardTest;
import com.shc.silenceengine.tests.OpenGLTest;
import com.shc.silenceengine.tests.ResourceLoaderTest;
import com.shc.silenceengine.tests.SilenceTest;
import com.shc.silenceengine.tests.SoundTest;
import com.shc.silenceengine.tests.TestRunner;
import com.shc.silenceengine.tests.TouchTest;

import java.util.HashMap;
import java.util.Map;

public class TestLauncher implements EntryPoint
{
    private Map<String, TestProvider> tests = new HashMap<>();

    private TestRunner game;

    @Override
    public void onModuleLoad()
    {
        registerTests();

        ListBox selectionList = new ListBox();

        for (String testName : tests.keySet())
            selectionList.addItem(testName);

        selectionList.addChangeHandler(event ->
        {
            String test = selectionList.getSelectedItemText();
            Window.Location.replace(
                    Window.Location.getPath() + Window.Location.getQueryString() +"#" + test);

            game.changeTest(tests.get(test).provide());
        });

        RootPanel.get().add(new HTML("<h1>SilenceEngine Tests</h1>"));
        RootPanel.get().add(selectionList);
        RootPanel.get().add(new HTML("<br>"));

        String test = Window.Location.getHref();
        test = test == null ? "" : test.substring(test.lastIndexOf('#') + 1);

        if (!tests.keySet().contains(test))
            test = selectionList.getItemText(0);

        int index = 0;

        for (String testName : tests.keySet())
            if (testName.equals(test))
            {
                selectionList.setSelectedIndex(index);
                break;
            }
            else
                index++;

        game = new TestRunner(tests.get(test).provide());

        GwtRuntime.start(game);
    }

    private void registerTests()
    {
        tests.put("OpenGLTest", OpenGLTest::new);
        tests.put("GameTest", GameTest::new);
        tests.put("KeyboardTest", KeyboardTest::new);
        tests.put("TouchTest", TouchTest::new);
        tests.put("DynamicRendererTest", DynamicRendererTest::new);
        tests.put("EntityCollisionTest2D", EntityCollisionTest2D::new);
        tests.put("SoundTest", SoundTest::new);
        tests.put("ResourceLoaderTest", ResourceLoaderTest::new);
    }

    @FunctionalInterface
    private interface TestProvider
    {
        SilenceTest provide();
    }
}
