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

package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.fonts.BitmapFont;
import com.shc.silenceengine.graphics.fonts.BitmapFontRenderer;
import com.shc.silenceengine.graphics.opengl.GLContext;
import com.shc.silenceengine.input.Controller;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;

import static com.shc.silenceengine.input.InputState.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class ControllerTest extends SilenceTest
{
    private OrthoCam camera;

    private BitmapFont         font;
    private BitmapFontRenderer renderer;

    private int     controllerId;
    private boolean useMappings;

    @Override
    public void init()
    {
        camera = new OrthoCam(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());

        BitmapFont.load(FilePath.getResourceFile("engine_resources/fonts/roboto32px.fnt"), font ->
        {
            this.font = font;
            BitmapFontRenderer.create(renderer -> this.renderer = renderer);
        });

        controllerId = 0;
        useMappings = false;

        SilenceEngine.input.addTextEventHandler(chars ->
        {
            if (Character.isDigit(chars[0]))
                controllerId = chars[0] - '0';
        });

        SilenceEngine.display.setTitle("Controller Test - Mapping Utility");
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();

        if (Keyboard.isKeyTapped(Keyboard.KEY_SPACE))
            useMappings = !useMappings;

        if (Keyboard.isKeyTapped(Keyboard.KEY_PLUS) || Keyboard.isKeyTapped(Keyboard.KEY_KP_PLUS))
            controllerId++;

        if (Keyboard.isKeyTapped(Keyboard.KEY_MINUS) || Keyboard.isKeyTapped(Keyboard.KEY_KP_MINUS))
            controllerId--;

        SilenceEngine.display.setTitle("Controller Test - Mapping Utility | FPS: " + SilenceEngine.gameLoop.getFPS() +
                                       " | UPS: " + SilenceEngine.gameLoop.getUPS() +
                                       " | RC: " + IGraphicsDevice.Data.renderCallsThisFrame);
    }

    @Override
    public void render(float delta)
    {
        camera.apply();

        if (renderer == null)
            return;

        if (controllerId < Controller.CONTROLLER_0 || controllerId > Controller.CONTROLLER_15)
        {
            controllerId = 0;
            return;
        }

        renderer.begin();
        {
            Controller.State state = Controller.states[controllerId];

            renderer.render(font, "Controller " + controllerId + ": " + state.connected, 10, 10);
            renderer.render(font, "\nName: " + state.name, 10, 10);
            renderer.render(font, "\n\nUsing Mappings: " + useMappings, 10, 10);

            String message = "Buttons: " + state.numButtons;
            renderer.render(font, message, SilenceEngine.display.getWidth() - font.getWidth(message) - 10, 10);
            message = "Axes: " + state.numAxes;
            renderer.render(font, "\n" + message, SilenceEngine.display.getWidth() - font.getWidth(message) - 10, 10);
            message = "Ideal: " + state.ideal;
            renderer.render(font, "\n\n" + message, SilenceEngine.display.getWidth() - font.getWidth(message) - 10, 10);

            float x = 10;
            float y = font.getHeight() * 4 + 10;

            for (int i = 0; i < Math.min(state.buttons.length, state.numButtons); i++)
            {
                if (y + font.getHeight() >= SilenceEngine.display.getHeight())
                {
                    y = font.getHeight() * 4 + 10;
                    x += 150;
                }

                final int button = useMappings ? state.buttonMapping.getIdealCode(i) : i;

                renderer.render(font, "B" + i + ": " + (state.buttons[button].state != RELEASED), x, y);
                y += font.getHeight();
            }

            x += 150;
            y = font.getHeight() * 4 + 10;

            for (int i = 0; i < Math.min(state.axes.length, state.numAxes); i++)
            {
                if (y + font.getHeight() >= SilenceEngine.display.getHeight())
                {
                    y = font.getHeight() * 4 + 10;
                    x += 150;
                }

                final int axis = useMappings ? state.axisMapping.getIdealCode(i) : i;

                renderer.render(font, "A" + i + ": " + state.axes[axis].amount, x, y);
                y += font.getHeight();
            }
        }
        renderer.end();
    }

    @Override
    public void resized()
    {
        GLContext.viewport(0, 0, SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
        camera.initProjection(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    @Override
    public void dispose()
    {
        if (renderer != null)
            renderer.dispose();

        if (font != null)
            font.dispose();
    }
}
