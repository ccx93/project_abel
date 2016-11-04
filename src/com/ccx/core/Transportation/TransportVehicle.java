package com.ccx.core.Transportation;

import com.ccx.core.TransportObject.TransportableObject;
import com.ccx.core.TransportObject.TransportableObjectType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class TransportVehicle {
    private Map<TransportableObjectType, List<TransportableObject>> mLuggage;

    public TransportVehicle() {
        mLuggage = new HashMap<>(TransportableObjectType.values().length);
        for (TransportableObjectType category : TransportableObjectType.values()) {
            mLuggage.put(category, new LinkedList<>());
        }
    }

    public Map<TransportableObjectType, List<TransportableObject>> getCurrentLuggage() {
        return mLuggage;
    }

    public List<TransportableObject> getCurrentLuggage(TransportableObjectType aCategory) {
        return mLuggage.get(aCategory);
    }

    public abstract int getObjectCapacity(TransportableObjectType aCategory);
    public abstract double getSpeed();
}
