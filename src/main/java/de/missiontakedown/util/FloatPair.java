package de.missiontakedown.util;

/**
 * @author Jan
 */
public class FloatPair {
    private float x;
    private float y;

    public FloatPair(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    @Override
    public String toString() {
        return "FloatPair{" + "x=" + x + ", y=" + y + '}';
    }
}