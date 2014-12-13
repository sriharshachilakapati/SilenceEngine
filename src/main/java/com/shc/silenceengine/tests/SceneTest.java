package com.shc.silenceengine.tests;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.PerspCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.SceneNode;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class SceneTest extends Game
{
    private PerspCam cam;

    public void init()
    {
        cam = new PerspCam().initProjection(70, Display.getAspectRatio(), 0.01f, 100f);

        Scene.init();
        SceneObject root = new SceneObject(new Vector2(0, 0), Color.RED);
        {
            root.addChild(new SceneObject(new Vector2(-0.5f, 0.5f), Color.GREEN));
            root.addChild(new SceneObject(new Vector2(0.5f, 0.5f), Color.BLUE));
            root.addChild(new SceneObject(new Vector2(0, -0.5f), Color.SILVER));
        }
        Scene.addChild(root);
    }

    public void update(double delta)
    {
        if (Keyboard.isPressed(Keyboard.KEY_ESCAPE))
            end();

        Scene.update(delta);
    }

    public void render(double delta, Batcher batcher)
    {
        cam.apply();
        Scene.render(delta, batcher);
    }

    public void dispose()
    {
        Scene.removeChildren();
    }

    public static void main(String[] args)
    {
        new SceneTest().start();
    }

    public static class SceneObject extends SceneNode
    {
        private Color color;
        private float rotation;
        private Vector2 position;

        public SceneObject(Vector2 position, Color color)
        {
            this.color = color;
            this.position = position;
            getLocalTransform().translate(position);
        }

        public void update(double delta)
        {
            rotation += (getParent() == null) ? (float) delta : (float) -delta * 2;

            float z = -Math.abs((float) Math.sin(TimeUtils.currentSeconds()));

            getLocalTransform().reset().rotate(Vector3.AXIS_Z, rotation)
                                       .translate(new Vector3(position.getX(), position.getY(), z));
        }

        public void render(double delta, Batcher batcher)
        {
            batcher.applyTransform(getTransform());
            batcher.begin();
            {
                batcher.vertex(0, 0.1f);
                batcher.color(color);

                batcher.vertex(-0.1f, -0.1f);
                batcher.color(color);

                batcher.vertex(0.1f, -0.1f);
                batcher.color(color);
            }
            batcher.end();
        }
    }
}
