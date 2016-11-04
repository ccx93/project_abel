package com.ccx.core.TransportObject;

import com.ccx.core.Routing.Point;

public class TransportableObject {
    private String mName;
    private Point mPickupPoint, mDropPoint;
    private TransportableObjectType mType;
    private TimingWindow mPickTiming, mDropTiming;

    public TransportableObject(String aName, Point aPickupPoint, Point aDropPoint, TransportableObjectType aType) {
        mName = aName;
        mPickupPoint = aPickupPoint;
        mDropPoint = aDropPoint;
        mType = aType;
        mPickTiming = null;
        mDropTiming = null;
    }

    public TransportableObject(String aName, Point aPickupPoint, Point aDropPoint, TransportableObjectType aType,
                               TimingWindow aPickTiming, TimingWindow aDropTiming) {
        mName = aName;
        mPickupPoint = aPickupPoint;
        mDropPoint = aDropPoint;
        mType = aType;
        mPickTiming = aPickTiming;
        mDropTiming = aDropTiming;
    }

    public boolean isWithinTimingWindow(double currentTime, boolean isPickup) {
        if (isPickup) {
            return (mPickTiming == null) || (currentTime >= mPickTiming.mTimingMin && currentTime <= mPickTiming.mTimingMax);
        } else {
            return (mDropTiming == null) || (currentTime >= mDropTiming.mTimingMin && currentTime <= mDropTiming.mTimingMax);
        }
    }

    public double getTransportDistance() {
        return Point.getDistanceBetween(mDropPoint, mPickupPoint);
    }

    public Point getPickupPoint() {
        return mPickupPoint;
    }

    public Point getDropPoint() {
        return mDropPoint;
    }

    public TransportableObjectType getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) return true;
        if (aO == null || getClass() != aO.getClass()) return false;

        TransportableObject that = (TransportableObject) aO;

        if (!mName.equals(that.mName)) return false;
        if (!mPickupPoint.equals(that.mPickupPoint)) return false;
        if (!mDropPoint.equals(that.mDropPoint)) return false;
        return mType == that.mType;

    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + mPickupPoint.hashCode();
        result = 31 * result + mDropPoint.hashCode();
        result = 31 * result + mType.hashCode();
        return result;
    }

    public static class TimingWindow {
        double mTimingMin;
        double mTimingMax;

        public TimingWindow(double aTimingMin, double aTimingMax) {
            mTimingMin = aTimingMin;
            mTimingMax = aTimingMax;
        }
    }
}
