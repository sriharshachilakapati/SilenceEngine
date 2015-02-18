package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.input.Controller;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.utils.Logger;

/**
 * @author Sri Harsha Chilakapati
 */
public class ControllerTest extends Game
{
    @Override
    public void init()
    {
        if (Controller.getConnectedControllers().length == 0)
        {
            Logger.warn("Error, no controller detected!");
            Game.end();
        }

        // Print information of all the controllers
        for (Controller controller : Controller.getConnectedControllers())
        {
            System.out.println("ID: " + controller.getId());
            System.out.println("Name: " + controller.getName());
            System.out.println("Type: " + controller.getType());
            System.out.println("Num axes: " + controller.getNumAxes());
            System.out.println("Num buttons: " + controller.getNumButtons());
        }
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.end();

        if (Controller.getConnectedControllers().length != 0)
            Controller.getConnectedControllers()[0].printValues();
    }

    public static void main(String[] args)
    {
        new ControllerTest().start();
    }
}
