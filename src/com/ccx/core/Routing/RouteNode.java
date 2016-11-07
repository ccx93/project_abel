package com.ccx.core.Routing;

import com.ccx.core.TransportObject.TransportableObject;
import com.ccx.core.Transportation.TransportVehicle;

public class RouteNode {
    private Point mPoint;
    private TransportableObject mObject;
    private TransportVehicle mAssignedVehicle;
    private boolean isPickup;

    public RouteNode(Point aPoint, TransportableObject aObject, boolean aIsPickup) {
        mPoint = aPoint;
        mObject = aObject;
        isPickup = aIsPickup;
        mAssignedVehicle = null;
    }

    public TransportVehicle getAssignedVehicle() {
        return mAssignedVehicle;
    }

    public void setAssignedVehicle(TransportVehicle aAssignedVehicle) {
        mAssignedVehicle = aAssignedVehicle;
    }

    public RouteNode getClone() {
        return new RouteNode(mPoint, mObject, isPickup);

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
            return String.format("Vehicle %s (%s) - %s (%s) pick at %s", mAssignedVehicle.getId(), mAssignedVehicle.getAssignedVehicle(), mObject.getName(), mObject.getType(), mPoint.toString());
        } else {
            return String.format("Vehicle %s (%s) - %s (%s) drop at %s", mAssignedVehicle.getId(), mAssignedVehicle.getAssignedVehicle(),mObject.getName(), mObject.getType(),mPoint.toString());
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
