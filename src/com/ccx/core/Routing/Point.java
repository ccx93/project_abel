package com.ccx.core.Routing;

public class Point {
    private double mX, mY;

    public Point(double aX, double aY) {
        mX = aX;
        mY = aY;
    }

    double getDistanceTo(Point aPoint) {
        return getDistanceBetween(this, aPoint);
    }

    public double getX() {
        return mX;
    }

    public void setX(double aX) {
        mX = aX;
    }

    public double getY() {
        return mY;
    }

    public void setY(double aY) {
        mY = aY;
    }

    public static double getDistanceBetween(Point aPoint1, Point aPoint2) {
        double deltaX = Math.pow(aPoint1.getX() - aPoint2.getX(), 2);
        double deltaY = Math.pow(aPoint1.getY() - aPoint2.getY(), 2);
        return Math.abs(Math.sqrt(deltaX + deltaY));
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", mX, mY);
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        Point target = (Point) obj;
        return (target.getX() == getX()) && (target.getY() == getY());
    }
}
