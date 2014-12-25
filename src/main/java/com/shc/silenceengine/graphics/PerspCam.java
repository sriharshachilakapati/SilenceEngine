package com.shc.silenceengine.graphics;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Quaternion;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.TransformUtils;
import org.lwjgl.opengl.GL11;

/**
 * @author Sri Harsha Chilakapati
 */
public class PerspCam extends BaseCamera
{
    private Matrix4 mProj;
    private Matrix4 mView;

    private Vector3    position;
    private Quaternion rotation;

    public PerspCam()
    {
        this(70, Display.getAspectRatio(), 0.01f, 100);
    }

    public PerspCam(float fovy, float aspect, float zNear, float zFar)
    {
        mProj = TransformUtils.createPerspective(fovy, aspect, zNear, zFar).copy();
        mView = new Matrix4();

        position = new Vector3(0, 0, 1);
        rotation = new Quaternion();
    }

    public PerspCam lookAt(Vector3 point)
    {
        Vector3 forward = point.subtract(position);
        Vector3 up = Vector3.AXIS_Y;

        float dot = Vector3.AXIS_Z.negate().dot(forward);

        if (Math.abs(dot + 1) < 0.000001f)
        {
            rotation = new Quaternion(up.x, up.y, up.z, (float) Math.PI);
            return this;
        }

        if (Math.abs(dot - 1) < 0.000001f)
        {
            rotation = new Quaternion();
            return this;
        }

        float rotAngle = (float) Math.acos(dot);
        Vector3 rotAxis = Vector3.AXIS_Z.negate().cross(forward).normalize();

        rotation = new Quaternion(rotAxis, rotAngle);

        return this;
    }

    public PerspCam moveForward(float amount)
    {
        return move(getForward(), amount);
    }

    public PerspCam moveBackward(float amount)
    {
        return move(getForward().negate(), amount);
    }

    public PerspCam moveLeft(float amount)
    {
        return move(getRight().negate(), amount);
    }

    public PerspCam moveRight(float amount)
    {
        return move(getRight(), amount);
    }

    public PerspCam moveUp(float amount)
    {
        return move(getUp(), amount);
    }

    public PerspCam moveDown(float amount)
    {
        return move(getUp().negate(), amount);
    }

    public PerspCam move(Vector3 dir, float amount)
    {
        position = position.add(dir.normalize().scale(amount));
        return this;
    }

    public PerspCam rotateX(float angle)
    {
        Quaternion xRot = new Quaternion(Vector3.AXIS_X, angle);
        rotation = rotation.multiply(xRot);

        return this;
    }

    public PerspCam rotateY(float angle)
    {
        Quaternion yRot = new Quaternion(Vector3.AXIS_Y, angle);
        rotation = yRot.multiply(rotation);

        return this;
    }

    public Vector3 getUp()
    {
        return rotation.multiply(Vector3.AXIS_Y);
    }

    public Vector3 getForward()
    {
        return rotation.multiply(Vector3.AXIS_Z.negate());
    }

    public Vector3 getRight()
    {
        return rotation.multiply(Vector3.AXIS_X);
    }

    public PerspCam initProjection(float fovy, float aspect, float zNear, float zFar)
    {
        mProj = TransformUtils.createPerspective(fovy, aspect, zNear, zFar).copy();
        return this;
    }

    public PerspCam initProjection(float width, float height)
    {
        return initProjection(0, width, height, 0, 0.01f, 100f);
    }

    public PerspCam initProjection(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        mProj  = TransformUtils.createFrustum(left, right, bottom, top, zNear, zFar).copy();
        return this;
    }

    public void apply()
    {
        mView.initIdentity()
             .multiply(TransformUtils.createTranslation(position.negate()))
             .multiply(TransformUtils.createRotation(rotation));

        BaseCamera.projection = mProj;
        BaseCamera.view       = mView;

        // Enable Depth Testing
        GL3Context.enable(GL11.GL_DEPTH_TEST);
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setPosition(Vector3 position)
    {
        this.position = position;
    }
}
