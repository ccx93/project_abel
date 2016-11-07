package com.ccx.core.Transportation;

import com.ccx.core.Routing.Point;
import com.ccx.core.TransportObject.TransportableObject;
import com.ccx.core.TransportObject.TransportableObjectType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransportVehicle {
    private long mId;
    private VehiclesEnum mAssignedVehicle;
    private Map<TransportableObjectType, List<TransportableObject>> mLuggage;
    private double mTime;
    private Point mLastPoint;

    public VehiclesEnum getAssignedVehicle() {
        return mAssignedVehicle;
    }

    public TransportVehicle(long id, VehiclesEnum aAssignedVehicle) {
        mId = id;
        mAssignedVehicle = aAssignedVehicle;
        mLuggage = new HashMap<>(TransportableObjectType.values().length);
        for (TransportableObjectType category : TransportableObjectType.values()) {
            mLuggage.put(category, new LinkedList<>());
        }
        mLastPoint = new Point(0, 0);
        mTime = 0;
    }

    public long getId() {
        return mId;
    }

    public TransportVehicle getClone() {
        TransportVehicle newVehicle = new TransportVehicle(mId, mAssignedVehicle);
        for (TransportableObjectType type : this.getCurrentLuggage().keySet()) {
            newVehicle.getCurrentLuggage().get(type).addAll(this.getCurrentLuggage(type));
        }
        return newVehicle;
    }

    public void reset() {
        for (List<TransportableObject> to : mLuggage.values()) {
            to.clear();
            ;
        }
        mTime = 0;
        mLastPoint.setX(0);
        mLastPoint.setY(0);
    }

    public Map<TransportableObjectType, List<TransportableObject>> getCurrentLuggage() {
        return mLuggage;
    }

    public List<TransportableObject> getCurrentLuggage(TransportableObjectType aType) {
        return mLuggage.get(aType);
    }

    public double getTime() {
        return mTime;
    }

    public void setTime(double aTime) {
        mTime = aTime;
    }

    public Point getLastPoint() {
        return mLastPoint;
    }

    public void setLastPoint(Point aLastPoint) {
        mLastPoint = aLastPoint;
    }

    public int getObjectCapacity(TransportableObjectType aCategory) {
        switch (mAssignedVehicle) {
            case TAXI:
                switch (aCategory) {
                    case PEOPLE:
                        return 1;
                    case OBJECT:
                        return 1;
                }
        }
        return 0;
    }

    public double getSpeed() {
        switch (mAssignedVehicle) {
            case TAXI:
                return 10;
        }
        return 0;
    }

    @Override
    public boolean equals(Object aO) {
        if (this == aO) return true;
        if (aO == null || getClass() != aO.getClass()) return false;

        TransportVehicle vehicle = (TransportVehicle) aO;

        if (Double.compare(vehicle.mTime, mTime) != 0) return false;
        if (mAssignedVehicle != vehicle.mAssignedVehicle) return false;
        if (!mLuggage.equals(vehicle.mLuggage)) return false;
        return mLastPoint.equals(vehicle.mLastPoint);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mAssignedVehicle.hashCode();
        result = 31 * result + mLuggage.hashCode();
        temp = Double.doubleToLongBits(mTime);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + mLastPoint.hashCode();
        return result;
    }

    public enum VehiclesEnum {
        TAXI
    }
}
