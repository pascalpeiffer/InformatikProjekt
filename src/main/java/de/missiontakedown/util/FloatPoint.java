package de.missiontakedown.util;

import java.util.concurrent.ThreadLocalRandom;

public class FloatPoint {

    private float x;
    private float y;

    public FloatPoint() {}

    public FloatPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "FloatPoint X: " + getX() + " Y: " + getY();
    }

    public static FloatPoint getRandomPointBetween(FloatPoint a, FloatPoint b) {
        float t = (float) ThreadLocalRandom.current().nextDouble();
        float x = a.x + t * (b.x - a.x);
        float y = a.y + t * (b.y - a.y);
        return new FloatPoint(x, y);
    }

    public static float distance(FloatPoint a, FloatPoint b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

}
