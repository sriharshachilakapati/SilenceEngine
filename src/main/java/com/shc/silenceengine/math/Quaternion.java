package com.shc.silenceengine.math;

import com.shc.silenceengine.utils.ReusableStack;

/**
 * @author Sri Harsha Chilakapati
 */
public class Quaternion
{
    public static final ReusableStack<Quaternion> REUSABLE_STACK = new ReusableStack<>(Quaternion.class);

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
        set(axis, angle);
    }

    public Quaternion(float pitch, float yaw, float roll)
    {
        set(pitch, yaw, roll);
    }

    public Quaternion(float x, float y, float z, float w)
    {
        set(x, y, z, w);
    }

    public Quaternion add(float x, float y, float z, float w)
    {
        return new Quaternion(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    public Quaternion addSelf(float x, float y, float z, float w)
    {
        return set(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    public Quaternion add(Quaternion q)
    {
        return add(q.x, q.y, q.z, q.w);
    }

    public Quaternion addSelf(Quaternion q)
    {
        return addSelf(q.x, q.y, q.z, q.w);
    }

    public Quaternion subtract(float x, float y, float z, float w)
    {
        return add(-x, -y, -z, -w);
    }

    public Quaternion subtractSelf(float x, float y, float z, float w)
    {
        return addSelf(-x, -y, -z, -w);
    }

    public Quaternion subtract(Quaternion q)
    {
        return subtract(q.x, q.y, q.z, q.w);
    }

    public Quaternion subtractSelf(Quaternion q)
    {
        return subtractSelf(q.x, q.y, q.z, q.w);
    }

    public Quaternion normalize()
    {
        float length = length();

        if (length == 0 || length == 1)
            return copy();

        return new Quaternion(x / length, y / length, z / length, w / length);
    }

    public Quaternion normalizeSelf()
    {
        float length = length();

        if (length == 0 || length == 1)
            return this;

        return set(x / length, y / length, z / length, w / length);
    }

    public Quaternion conjugate()
    {
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion conjugateSelf()
    {
        return set(-x, -y, -z, w);
    }

    public Quaternion multiply(Quaternion q)
    {
        float nx = w * q.x + x * q.w + y * q.z - z * q.y;
        float ny = w * q.y + y * q.w + z * q.x - x * q.z;
        float nz = w * q.z + z * q.w + x * q.y - y * q.x;
        float nw = w * q.w - x * q.x - y * q.y - z * q.z;

        return new Quaternion(nx, ny, nz, nw).normalizeSelf();
    }

    public Quaternion multiplySelf(Quaternion q)
    {
        float nx = w * q.x + x * q.w + y * q.z - z * q.y;
        float ny = w * q.y + y * q.w + z * q.x - x * q.z;
        float nz = w * q.z + z * q.w + x * q.y - y * q.x;
        float nw = w * q.w - x * q.x - y * q.y - z * q.z;

        return set(nx, ny, nz, nw).normalizeSelf();
    }

    public Vector3 multiply(Vector3 v)
    {
        Vector3 tempVec3 = Vector3.REUSABLE_STACK.pop();

        Quaternion temp1 = Quaternion.REUSABLE_STACK.pop();
        Quaternion temp2 = Quaternion.REUSABLE_STACK.pop();
        Quaternion temp3 = Quaternion.REUSABLE_STACK.pop();

        Vector3 vn = tempVec3.set(v).normalizeSelf();

        Quaternion q1 = temp1.set(this).conjugateSelf();
        Quaternion qv = temp2.set(vn.x, vn.y, vn.z, 1);

        qv = temp3.set(this).multiplySelf(qv);
        qv.multiplySelf(q1);

        Vector3 result = new Vector3(qv.x, qv.y, qv.z).normalizeSelf().scaleSelf(v.length());

        Vector3.REUSABLE_STACK.push(tempVec3);

        Quaternion.REUSABLE_STACK.push(temp1);
        Quaternion.REUSABLE_STACK.push(temp2);
        Quaternion.REUSABLE_STACK.push(temp3);

        return result;
    }

    public Vector3 multiply(Vector3 v, Vector3 dest)
    {
        Vector3 tempVec3 = Vector3.REUSABLE_STACK.pop();

        Quaternion temp1 = Quaternion.REUSABLE_STACK.pop();
        Quaternion temp2 = Quaternion.REUSABLE_STACK.pop();
        Quaternion temp3 = Quaternion.REUSABLE_STACK.pop();

        Vector3 vn = tempVec3.set(v).normalizeSelf();

        Quaternion q1 = temp1.set(this).conjugateSelf();
        Quaternion qv = temp2.set(vn.x, vn.y, vn.z, 1);

        qv = temp3.set(this).multiplySelf(qv);
        qv.multiplySelf(q1);

        dest.set(qv.x, qv.y, qv.z).normalizeSelf().scaleSelf(v.length());

        Vector3.REUSABLE_STACK.push(tempVec3);

        Quaternion.REUSABLE_STACK.push(temp1);
        Quaternion.REUSABLE_STACK.push(temp2);
        Quaternion.REUSABLE_STACK.push(temp3);

        return dest;
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

    public Quaternion set(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

        return this;
    }

    public Quaternion set(Vector3 axis, float angle)
    {
        angle = (float) Math.toRadians(angle) * 0.5f;
        axis = axis.normalize();

        float sinAngle = (float) Math.sin(angle);
        float cosAngle = (float) Math.cos(angle);

        x = axis.x * sinAngle;
        y = axis.y * sinAngle;
        z = axis.z * sinAngle;
        w = cosAngle;

        return this;
    }

    public Quaternion set(float pitch, float yaw, float roll)
    {
        pitch = (float) Math.toRadians(pitch) * 0.5f;
        yaw = (float) Math.toRadians(yaw) * 0.5f;
        roll = (float) Math.toRadians(roll) * 0.5f;

        float sinP = (float) Math.sin(pitch);
        float sinY = (float) Math.sin(yaw);
        float sinR = (float) Math.sin(roll);
        float cosP = (float) Math.cos(pitch);
        float cosY = (float) Math.cos(yaw);
        float cosR = (float) Math.cos(roll);

        x = sinP * cosY * cosR - cosP * sinY * sinR;
        y = cosP * sinY * cosR + sinP * cosY * sinR;
        z = cosP * cosY * sinR - sinP * sinY * cosR;
        w = cosP * cosY * cosR + sinP * sinY * sinR;

        return this;
    }

    public Quaternion set()
    {
        return set(0, 0, 0, 1);
    }

    public Quaternion set(Quaternion q)
    {
        return set(q.x, q.y, q.z, q.w);
    }
}
