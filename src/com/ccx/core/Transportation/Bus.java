package com.ccx.core.Transportation;

import com.ccx.core.TransportObject.TransportableObjectType;

public class Bus extends TransportVehicle {
    @Override
    public int getObjectCapacity(TransportableObjectType aCategory) {
        switch (aCategory) {
            case OBJECT:
                return 1;
            case PEOPLE:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public double getSpeed() {
        return 5;
    }
}
