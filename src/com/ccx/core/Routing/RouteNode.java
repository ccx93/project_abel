package com.ccx.core.Routing;

import com.ccx.core.TransportObject.TransportableObject;

public class RouteNode {
    private Point mPoint;
    private TransportableObject mObject;
    private boolean isPickup;

    public RouteNode(Point aPoint, TransportableObject aObject, boolean aIsPickup) {
        mPoint = aPoint;
        mObject = aObject;
        isPickup = aIsPickup;
    }

    public Point getPoint() {
        return mPoint;
    }

    public TransportableObject getObject() {
        return mObject;
    }

    public boolean isPickup() {
        return isPickup;
    }

    @Override
    public String toString() {
        if (isPickup()) {
            return String.format("%s (%s) pick at %s", mObject.getName(), mObject.getType(), mPoint.toString());
        } else {
            return String.format("%s (%s) drop at %s", mObject.getName(), mObject.getType(),mPoint.toString());
        }
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) return true;
        if (aO == null || getClass() != aO.getClass()) return false;

        RouteNode routeNode = (RouteNode) aO;

        if (isPickup != routeNode.isPickup) return false;
        if (!mPoint.equals(routeNode.mPoint)) return false;
        return mObject.equals(routeNode.mObject);

    }

    @Override
    public int hashCode() {
        int result = mPoint.hashCode();
        result = 31 * result + mObject.hashCode();
        result = 31 * result + (isPickup ? 1 : 0);
        return result;
    }
}
