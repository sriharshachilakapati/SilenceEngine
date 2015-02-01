#SilenceEngine

![logo](http://silenceengine.goharsha.com/img/logo.png)

A 2D/3D Game Engine written on top of LWJGL3

##What is SilenceEngine?

SilenceEngine is a 2D/3D game engine, that it is meant to take care of all the low level things like Graphics, Input, Asset Loading, Collision Detection for you, meaning that you only need to make your game. It lets you focus on the game play and game design, by doing most of the hard work itself.

##Is it only for Games?

Perhaps not, you can create any form of OpenGL application using SilenceEngine. It provides you with OpenGL classes in the `com.shc.silenceengine.graphics.opengl` package, which cleanly wraps the OpenGL functions into Java classes, making them more easy to use. All you have to take care of is that you must call the `dispose()` on those objects when you no longer need them.

It is also easier to use in Development mode as we will check for OpenGL errors after every call to the OpenGL functions and report you the errors, if any exist, in the form of `GLException` allowing you to get rid of the errors quickly and easily. By the way, everything in SilenceEngine is modern, and there is no deprecated stuff. I'm proud to say that this engine only uses OpenGL 3.3 (no deprecated OpenGL).

##Licence

 - This is licenced under MIT licence, which you can find [here](http://choosealicense.com/licenses/mit/).
 - This game engine uses JOrbis library from JCraft which are licenced under [LGPL](http://choosealicense.com/licenses/lgpl-3.0/).
 - This game engine uses J-Ogg library from [j-ogg.de](http://www.j-ogg.de/) on a free permissive licence.
 - This game engine is written on top of [LWJGL 3](http://lwjgl.org). You can find it's licence [here](https://github.com/LWJGL/lwjgl3/blob/master/doc/LICENSE.txt).
 
##Links

 - SilenceEngine website ([http://silenceengine.goharsha.com/](http://silenceengine.goharsha.com))
