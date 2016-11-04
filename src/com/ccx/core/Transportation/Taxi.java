package com.ccx.core.Transportation;

import com.ccx.core.TransportObject.TransportableObjectType;

public class Taxi extends TransportVehicle {
    @Override
    public int getObjectCapacity(TransportableObjectType aCategory) {
        switch (aCategory) {
            case OBJECT:
                return 1;
            case PEOPLE:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public double getSpeed() {
        return 10;
    }
}
