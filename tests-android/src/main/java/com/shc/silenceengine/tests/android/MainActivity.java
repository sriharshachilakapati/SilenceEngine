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

package com.shc.silenceengine.tests.android;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.annimon.stream.Stream;
import com.shc.silenceengine.backend.android.AndroidLauncher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity
{
    private Map<String, Class<? extends AndroidLauncher>> tests;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.argb(255, 50, 3, 3));

        setTheme(R.style.MyTheme);

        setContentView(R.layout.main);

        List<String> items = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);

        addTestsToList(adapter);

        adapter.notifyDataSetChanged();

        ListView listView = getListView();
        listView.setOnItemClickListener((parent, view, position, id) ->
        {
            String key = adapter.getItem(position);
            Intent intent = new Intent(this, tests.get(key));
            startActivity(intent);
        });
    }

    @SuppressWarnings("unchecked")
    private void addTestsToList(ArrayAdapter<String> adapter)
    {
        tests = new HashMap<>();

        PackageManager packageManager = getPackageManager();
        try
        {
            ActivityInfo[] list = packageManager.getPackageInfo(getApplicationContext().getPackageName(),
                    PackageManager.GET_ACTIVITIES).activities;

            for (ActivityInfo activityInfo : list)
            {
                String name = activityInfo.name;
                Class<?> klass = Class.forName(name);

                if (AndroidLauncher.class.isAssignableFrom(klass))
                {
                    String simpleName = klass.getSimpleName().replaceAll("Activity", "");
                    tests.put(simpleName, (Class<? extends AndroidLauncher>) klass);
                }
            }
        }
        catch (NameNotFoundException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        Stream.of(tests)
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .forEach(e -> adapter.add(e.getKey()));
    }
}
