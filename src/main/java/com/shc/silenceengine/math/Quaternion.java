package com.shc.silenceengine.math;

/**
 * @author Sri Harsha Chilakapati
 */
public class Quaternion
{
    public float x;
    public float y;
    public float z;
    public float w;

    public Quaternion()
    {
        this(0, 0, 0, 1);
    }

    public Quaternion(Vector3 axis, float angle)
    {
        angle = (float) Math.toRadians(angle) * 0.5f;
        axis = axis.normalize();

        float sinAngle = (float) Math.sin(angle);
        float cosAngle = (float) Math.cos(angle);

        x = axis.x * sinAngle;
        y = axis.y * sinAngle;
        z = axis.z * sinAngle;
        w = cosAngle;
    }

    public Quaternion(float pitch, float yaw, float roll)
    {
        pitch = (float) Math.toRadians(pitch) * 0.5f;
        yaw   = (float) Math.toRadians(yaw)   * 0.5f;
        roll  = (float) Math.toRadians(roll)  * 0.5f;

        float sinP = (float) Math.sin(pitch);
        float sinY = (float) Math.sin(yaw);
        float sinR = (float) Math.sin(roll);
        float cosP = (float) Math.cos(pitch);
        float cosY = (float) Math.cos(yaw);
        float cosR = (float) Math.cos(roll);

        x = sinR * cosP * cosY - cosR * sinP * sinY;
        y = cosR * sinP * cosY + sinR * cosP * sinY;
        z = cosR * cosP * sinY - sinR * sinP * cosY;
        w = cosR * cosP * cosY + sinR * sinP * sinY;
    }

    public Quaternion(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion add(float x, float y, float z, float w)
    {
        return new Quaternion(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    public Quaternion add(Quaternion q)
    {
        return add(q.x, q.y, q.z, q.w);
    }

    public Quaternion subtract(float x, float y, float z, float w)
    {
        return add(-x, -y, -z, -w);
    }

    public Quaternion subtract(Quaternion q)
    {
        return subtract(q.x, q.y, q.z, q.w);
    }

    public Quaternion normalize()
    {
        float length = length();

        return new Quaternion(x/length, y/length, z/length, w/length);
    }

    public Quaternion conjugate()
    {
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion multiply(Quaternion q)
    {
        float nx = w * q.x + x * q.w + y * q.z - z * q.y;
        float ny = w * q.y + y * q.w + z * q.x - x * q.z;
        float nz = w * q.z + z * q.w + x * q.y - y * q.x;
        float nw = w * q.w - x * q.x - y * q.y - z * q.z;

        return new Quaternion(nx, ny, nz, nw).normalize();
    }

    public Vector3 multiply(Vector3 v)
    {
        Vector3 vn = v.normalize();

        Quaternion q1 = conjugate();
        Quaternion qv = new Quaternion(vn.x, vn.y, vn.z, 1);

        qv = this.multiply(qv);
        qv = qv.multiply(q1);

        return new Vector3(qv.x, qv.y, qv.z);
    }

    public Quaternion copy()
    {
        return new Quaternion(x, y, z, w);
    }

    public float lengthSquared()
    {
        return x * x + y * y + z * z + w * w;
    }

    public float length()
    {
        return (float) Math.sqrt(lengthSquared());
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getZ()
    {
        return z;
    }

    public void setZ(float z)
    {
        this.z = z;
    }

    public float getW()
    {
        return w;
    }

    public void setW(float w)
    {
        this.w = w;
    }
}
