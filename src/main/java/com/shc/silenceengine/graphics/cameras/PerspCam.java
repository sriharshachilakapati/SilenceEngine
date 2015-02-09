package com.shc.silenceengine.graphics.cameras;

import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.graphics.opengl.GL3Context;
import com.shc.silenceengine.math.Matrix4;
import com.shc.silenceengine.math.Quaternion;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.utils.MathUtils;
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
    private Quaternion tempQuat;

    private Vector3 tempVec;

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
        tempQuat = new Quaternion();

        tempVec = new Vector3();
    }

    public PerspCam lookAt(Vector3 point)
    {
        Vector3 forward = point.subtract(position).normalizeSelf();
        Vector3 up = Vector3.AXIS_Y;

        Vector3 negativeZ = tempVec.set(Vector3.AXIS_Z).negateSelf();

        float dot = negativeZ.dot(forward);

        if (Math.abs(dot + 1) < 0.000001f)
        {
            rotation.set(up.x, up.y, up.z, (float) Math.PI);
            return this;
        }

        if (Math.abs(dot - 1) < 0.000001f)
        {
            rotation.set();
            return this;
        }

        float rotAngle = MathUtils.acos(dot);
        Vector3 rotAxis = negativeZ.crossSelf(forward).normalizeSelf();

        rotation.set(rotAxis, rotAngle);

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
        return move(getUp().negateSelf(), amount);
    }

    public PerspCam move(Vector3 dir, float amount)
    {
        position.addSelf(dir.normalize().scale(amount));
        return this;
    }

    public PerspCam rotateX(float angle)
    {
        Quaternion xRot = tempQuat.set(Vector3.AXIS_X, angle);
        rotation.multiplySelf(xRot);

        return this;
    }

    public PerspCam rotateY(float angle)
    {
        Quaternion yRot = tempQuat.set(Vector3.AXIS_Y, angle);
        rotation.set(yRot.multiplySelf(rotation));

        return this;
    }

    public Vector3 getUp()
    {
        return rotation.multiply(Vector3.AXIS_Y, tempVec);
    }

    public Vector3 getForward()
    {
        return rotation.multiply(Vector3.AXIS_Z.negate(), tempVec);
    }

    public Vector3 getRight()
    {
        return rotation.multiply(Vector3.AXIS_X, tempVec);
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

    private Vector3 tempVec3 = new Vector3();

    public void apply()
    {
        super.apply();

        mView.initIdentity()
             .multiply(TransformUtils.createTranslation(tempVec3.set(position).negateSelf()))
             .multiply(TransformUtils.createRotation(rotation));

        // Enable Depth Testing
        GL3Context.enable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public Matrix4 getProjection()
    {
        return mProj;
    }

    @Override
    public Matrix4 getView()
    {
        return mView;
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
