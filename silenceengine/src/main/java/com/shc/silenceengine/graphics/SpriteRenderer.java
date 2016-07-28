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

package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.programs.DynamicProgram;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.functional.UniCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpriteRenderer
{
    private Vector3 tempVec = new Vector3();
    private Color   tempCol = new Color();

    private static DynamicProgram program;
    private static int            instances;

    private DynamicRenderer renderer;
    private Texture         currentTexture;

    private boolean disposed;

    // Instantiation should happen only via static method
    private SpriteRenderer()
    {
        renderer = new DynamicRenderer(500 * 2 * 3);
        program.applyToRenderer(renderer);

        currentTexture = Texture.CURRENT != null ? Texture.CURRENT
                                                 : Texture.fromColor(Color.BLACK, 16, 16);
    }

    public static void create(UniCallback<SpriteRenderer> onComplete)
    {
        instances++;

        if (program != null)
        {
            onComplete.invoke(new SpriteRenderer());
            return;
        }

        DynamicProgram.create(program ->
        {
            SpriteRenderer.program = program;
            onComplete.invoke(new SpriteRenderer());
        });
    }

    public void begin()
    {
        renderer.begin(Primitive.TRIANGLES);
    }

    public void render(Sprite sprite, Transform transform)
    {
        render(sprite, transform, Color.BLACK);
    }

    public void render(Sprite sprite, Transform transform, Color tint)
    {
        render(sprite, transform, tint, 1);
    }

    public void render(Sprite sprite, Transform transform, Color tint, float opacity)
    {
        Texture texture = sprite.getCurrentFrame();

        if (currentTexture == null || texture.getID() != currentTexture.getID())
        {
            flush();
            texture.bind(0);
            program.setUniform("tex", 0);
            currentTexture = texture;
        }

        tempCol.set(tint).a *= opacity;

        final float tw = texture.getWidth() / 2;
        final float th = texture.getHeight() / 2;

        renderer.vertex(tempVec.set(-1, -1, 0).scale(tw, th, 0).multiply(transform.matrix));
        renderer.texCoord(currentTexture.getMinU(), currentTexture.getMinV());
        renderer.color(tempCol);

        renderer.vertex(tempVec.set(1, -1, 0).scale(tw, th, 0).multiply(transform.matrix));
        renderer.texCoord(currentTexture.getMaxU(), currentTexture.getMinV());
        renderer.color(tempCol);

        renderer.vertex(tempVec.set(-1, 1, 0).scale(tw, th, 0).multiply(transform.matrix));
        renderer.texCoord(currentTexture.getMinU(), currentTexture.getMaxV());
        renderer.color(tempCol);

        renderer.vertex(tempVec.set(1, -1, 0).scale(tw, th, 0).multiply(transform.matrix));
        renderer.texCoord(currentTexture.getMaxU(), currentTexture.getMinV());
        renderer.color(tempCol);

        renderer.vertex(tempVec.set(1, 1, 0).scale(tw, th, 0).multiply(transform.matrix));
        renderer.texCoord(currentTexture.getMaxU(), currentTexture.getMaxV());
        renderer.color(tempCol);

        renderer.vertex(tempVec.set(-1, 1, 0).scale(tw, th, 0).multiply(transform.matrix));
        renderer.texCoord(currentTexture.getMinU(), currentTexture.getMaxV());
        renderer.color(tempCol);
    }

    public void flush()
    {
        renderer.flush();
    }

    public void end()
    {
        renderer.end();
    }

    public boolean isActive()
    {
        return renderer.isActive();
    }

    public void dispose()
    {
        if (disposed)
            throw new SilenceException("Cannot dispose an already disposed object");

        instances--;
        renderer.dispose();

        if (instances == 0)
        {
            program.dispose();
            program = null;
        }

        disposed = true;
    }
}
