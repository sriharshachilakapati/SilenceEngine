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
 *
 */

package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.logging.Logger;

/**
 * @author Sri Harsha Chilakapati
 */
public class KeyboardTest extends SilenceTest
{
    private Logger logger;

    @Override
    public void init()
    {
        logger = SilenceEngine.log.getLogger("KeyboardTest");

        SilenceEngine.display.setTitle("KeyboardTest");

        logger.info("Test initialized successfully");
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            logger.info("SPACE key down");

        if (Keyboard.isKeyTapped(Keyboard.KEY_ENTER))
            logger.info("ENTER key tapped");
    }

    @Override
    public void dispose()
    {
        logger.info("Dispose event!! Success.");
    }
}
