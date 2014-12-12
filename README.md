#SilenceEngine

![logo](http://goharsha.com/images/silenceengine.png)

A 2D/3D Game Engine written on top of LWJGL3

##What is SilenceEngine?

SilenceEngine is a 2D/3D game engine, that it is meant to take care of all the low level things like Graphics, Input, Asset Loading, Collision Detection for you, meaning that you only need to make your game. It lets you focus on the game play and game design, by doing most of the hard work itself.

##Is it only for Games?

Perhaps not, you can create any form of OpenGL application using SilenceEngine. It provides you with OpenGL classes in the `com.shc.silenceengine.graphics.opengl` package, which cleanly wraps the OpenGL functions into Java classes, making them more easy to use. All you have to take care of is that you must call the `dispose()` on those objects when you no longer need them.

It is also easier to use in Development mode as we will check for OpenGL errors after every call to the OpenGL functions and report you the errors, if any exist, in the form of `GLException` allowing you to get rid of the errors quickly and easily. By the way, everything in SilenceEngine is modern, and there is no deprecated stuff. I'm proud to say that this engine only uses OpenGL 3.3 (no deprecated OpenGL).

##I only know the glBegin()/glEnd() way!

I agree that the old fixed function rendering, or say the immediate mode rendering is easy to use and most of the tutorials on the internet uses them. So I provide a `Batcher` class. This class provides a `begin()`/`end()` way similar to the Immediate mode, but uses the VAOs and VBOs under the hood. Here is an example of rendering two triangles, one colored and one white using the batcher.

```java
public void render(double delta, Batcher batcher)
{
    batcher.begin();
    {
        // Vertex one of colored triangle
        batcher.vertex(0, 0.5f);
        batcher.color(Color.CORN_FLOWER_BLUE);

        // Vertex two of colored triangle
        batcher.vertex(-0.5f, -0.5f);
        batcher.color(Color.INDIAN_RED);

        // Vertex three of the colored triangle
        batcher.vertex(0.5f, -0.5f);
        batcher.color(Color.OLIVE_DRAB);

        // White triangle (just omit the color calls)
        batcher.vertex(1, 1, 0);
        batcher.vertex(1, -1, 0);
        batcher.vertex(0.5f, 0, 1);
    }
    batcher.end();
}
```

Simple, easy isn't it? This is still in heavy development, so more features are about to come! Thanks for checking it out!