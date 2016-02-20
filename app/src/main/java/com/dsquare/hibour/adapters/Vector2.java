package com.dsquare.hibour.adapters;

/**
 * Created by deepthi on 2/19/16.
 */
public class Vector2
{
    // Members
    public float x;
    public float y;

    // Constructors
    public Vector2() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Compare two vectors
    public boolean equals(Vector2 other) {
        return (this.x == other.x && this.y == other.y);
    }

    public static double distance(Vector2 a, Vector2 b) {
        float v0 = b.x - a.x;
        float v1 = b.y - a.y;
        return Math.sqrt(v0*v0 + v1*v1);
    }

    public static Vector2 distanceInVector(Vector2 a, Vector2 b) {
        float v0 = b.x - a.x;
        float v1 = b.y - a.y;
        return new Vector2(v0, v1);
    }

    public static double lengthSquared(Vector2 v) {
        return Math.sqrt(v.x*v.x + v.y*v.y);
    }

    public static Vector2 add(Vector2 a, Vector2 b) {
        float v0 = b.x + a.x;
        float v1 = b.y + a.y;
        return new Vector2(v0, v1);
    }

    public static Vector2 multiplyWithConstant(Vector2 a, float b) {
        float v0 = b * a.x;
        float v1 = b * a.y;
        return new Vector2(v0, v1);
    }

    public static Vector2 normalize(Vector2 A) {
        float distance = (float)Math.sqrt(A.x * A.x + A.y * A.y);
        return new Vector2(A.x / distance, A.y / distance);
    }
}