package com.shc.silenceengine.models.md2;

import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.models.Model;
import com.shc.silenceengine.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class MD2Model extends Model
{
    // The Model File Constants
    public final static int NUM_VERTEX_NORMALS = 162;
    public final static int SHADEDOT_QUANT = 16;

    private List<Vector3> vertices;

    private MD2Header header;

    public MD2Model(String filename)
    {
        parseMD2(filename);
    }

    private void parseMD2(String filename)
    {
        // MD2 Format Constants
        final int MD2_IDENT = (('2' << 24) + ('P' << 16) + ('D' << 8) + 'I');
        final int MD2_VERSION = 8;

        InputStream is = FileUtils.getResource(filename);
        BufferedInputStream bis = new BufferedInputStream(is);
        DataInputStream dis = new DataInputStream(bis);

        try
        {
            // Create the MD2 file header
            header = new MD2Header();

            header.ident = dis.readInt();

            // Check the ident
            if (header.ident != MD2_IDENT)
                throw new SilenceException("The file " + filename + " is not a valid MD2 model.");

            header.version = dis.readInt();

            // Check the version
            if (header.version != MD2_VERSION)
                throw new SilenceException("The version of the MD2 Header is invalid.");

            // Read the rest of the MD2 header
            header.skinWidth = dis.readInt();
            header.skinHeight = dis.readInt();
            header.frameSize = dis.readInt();
            header.numSkins = dis.readInt();
            header.numXYZ = dis.readInt();
            header.numST = dis.readInt();
            header.numTris = dis.readInt();
            header.numGLCommands = dis.readInt();
            header.numFrames = dis.readInt();
            header.ofsSkins = dis.readInt();
            header.ofsST = dis.readInt();
            header.ofsTris = dis.readInt();
            header.ofsFrames = dis.readInt();
            header.ofsGLCommands = dis.readInt();
            header.ofsEOF = dis.readInt();


            // Close the streams
            dis.close();
            bis.close();
            is.close();
        }
        catch (Exception e)
        {
            SilenceException.reThrow(e);
        }
    }

    @Override
    public void update(float delta)
    {

    }

    @Override
    public void render(float delta, Batcher batcher, Transform transform)
    {

    }

    @Override
    public void dispose()
    {

    }
}
