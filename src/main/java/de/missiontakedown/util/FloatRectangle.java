package de.missiontakedown.util;

public class FloatRectangle {

    private float x;
    private float y;
    private float width;
    private float height;

    public FloatRectangle() {

    }

    public FloatRectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(float px, float py) {
        return px >= this.x && px <= this.x + this.width &&
                py >= this.y && py <= this.y + this.height;
    }

    public boolean contains(FloatPoint floatPoint) {
        return contains(floatPoint.getX(), floatPoint.getY());
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

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
