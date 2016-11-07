package com.ccx.core.Routing;

import com.ccx.core.TransportObject.TransportableObject;
import com.ccx.core.TransportObject.TransportableObjectType;
import com.ccx.core.Transportation.TransportVehicle;
import com.sun.istack.internal.NotNull;

import java.util.*;

public class BruteForceRouter {
    private final List<TransportableObject> mObjectList;
    private List<TransportVehicle> mTransportVehicles;

    public List<TransportVehicle> getTransportVehicles() {
        return mTransportVehicles;
    }

    public void setTransportVehicles(List<TransportVehicle> aTransportVehicles) {
        mTransportVehicles = aTransportVehicles;
    }

    public void clearObjectList() {
        mObjectList.clear();
    }

    public void setObjectList(Collection<TransportableObject> aObjectList) {
        clearObjectList();
        mObjectList.addAll(aObjectList);
    }

    public Map<Long, List<Route>> generateRoute() {
        Map<Long, List<Route>> tempRouteList = new HashMap<>(mTransportVehicles.size());
        if (mObjectList.isEmpty() || mTransportVehicles == null) {
            return tempRouteList;
        }

        // First we create a global list containing all the points in given objectList
        List<RouteNode> allAvailableNodes = new LinkedList<>();
        for (TransportableObject o : mObjectList) {
            allAvailableNodes.add(new RouteNode(o.getPickupPoint(), o, true));
            allAvailableNodes.add(new RouteNode(o.getDropPoint(), o, false));
        }

        // loop until all object element is satisfied
        for (TransportVehicle vehicle : mTransportVehicles) {
            vehicle.reset();
        }
        tempRouteList = generateRouteFromList(allAvailableNodes, mTransportVehicles, true);

        return tempRouteList;
    }

    @NotNull
    private Map<Long, List<Route>> generateRouteFromList(List<RouteNode> aRouteNodes, List<TransportVehicle> aTransportVehicles,
                                              boolean isFirst) {
        if (aRouteNodes.size() <= 0) {
            // WTF happened here
            return null;
        } else {
            Map<Long, List<Route>> finalGeneratedRoutes = new HashMap<>(aTransportVehicles.size());
            for (TransportVehicle vehicle : aTransportVehicles) {
                finalGeneratedRoutes.put(vehicle.getId(), new LinkedList<>());
            }
            if (isFirst) {
                // Initialize vehicle property for first iteration (array is empty)
                for (TransportVehicle thisVehicle : aTransportVehicles) {
                    thisVehicle.setTime(0);
                    thisVehicle.setLastPoint(new Point(0, 0));
                }
            }
            for (int i = 0; i < aRouteNodes.size(); i++) {
                for (int vehicleIndex = 0; vehicleIndex < aTransportVehicles.size(); vehicleIndex++) {
                    // First, clone the nodes
                    RouteNode node = aRouteNodes.get(i).getClone();
                    // Copy Vehicle array
                    List<TransportVehicle> currentIterationVehicles = copyVehicleArray(aTransportVehicles);
                    TransportVehicle thisVehicle = currentIterationVehicles.get(vehicleIndex);
                    List<TransportableObject> currentLuggage = thisVehicle.getCurrentLuggage(node.getObject().getType());

                    // Make sure current node is valid, based on several properties
                    boolean isValid = isNodeValid(node, currentLuggage, thisVehicle.getTime(),
                            thisVehicle.getLastPoint(), currentIterationVehicles, vehicleIndex);

                    if (isValid) {
                        // Update time and previous node
                        if (node.isPickup()) {
                            currentLuggage.add(node.getObject());
                        } else {
                            currentLuggage.remove(node.getObject());
                        }
                        node.setAssignedVehicle(thisVehicle);
                        thisVehicle.setTime(thisVehicle.getTime()
                                + thisVehicle.getLastPoint().getDistanceTo(node.getPoint())
                                / thisVehicle.getSpeed());
                        thisVehicle.setLastPoint(node.getPoint());

                        if (aRouteNodes.size() == 1) {
                            // Stop iteration
                            finalGeneratedRoutes.get(thisVehicle.getId()).add(new Route(node));
                            return finalGeneratedRoutes;
                        } else {
                            // Clone list of points, but remove current node
                            List<RouteNode> clonedPoints = new LinkedList<>(aRouteNodes);
                            clonedPoints.remove(i);
                            Map<Long, List<Route>> generatedRoutes = generateRouteFromList(clonedPoints, currentIterationVehicles, false);
                            if (generatedRoutes != null) {
                                for (Long id : generatedRoutes.keySet()) {
                                    for (Route route : generatedRoutes.get(id)) {
                                        route.addNodeOnFirst(node);
                                    }
                                    finalGeneratedRoutes.get(id).addAll(generatedRoutes.get(id));
                                }
                            }
                        }
                    }
                }
            }
            return finalGeneratedRoutes;
        }
    }

    private static boolean isNodeValid(RouteNode node, List<TransportableObject> aCurrentLuggage, double aCurrentTime,
                                       Point aPrevPoint, List<TransportVehicle> aAvailableVehicles, int aAssignedVehicleIndex) {
        // Filter out invalid paths
        TransportableObjectType currentType = node.getObject().getType();
        boolean isCarriedByOtherVehicle = false;
        boolean isCarriedByCurrentVehicle = false;
        for (int i = 0; i < aAvailableVehicles.size(); i++) {
            TransportVehicle vehicle = aAvailableVehicles.get(i);
            if (vehicle.getCurrentLuggage(currentType).contains(node.getObject())) {
                if (i == aAssignedVehicleIndex) {
                    isCarriedByCurrentVehicle = true;
                } else {
                    isCarriedByOtherVehicle = true;
                }
                break;
            }
        }

        // This is other vehicle's object
        if (isCarriedByOtherVehicle) {
            return false;
        }

        TransportVehicle transportVehicle = aAvailableVehicles.get(aAssignedVehicleIndex);
        if (!isCarriedByCurrentVehicle) {
            // This is a different object
            if (aCurrentLuggage.size() < transportVehicle.getObjectCapacity(currentType)) {
                // carry this new object, if this is the pickup point
                if (node.isPickup()) {
                    // Check timing window first
                    // update current time
                    double arrivalTime = aCurrentTime + aPrevPoint.getDistanceTo(node.getPoint()) / transportVehicle.getSpeed();
                    return node.getObject().isWithinTimingWindow(arrivalTime, true);
                } else {
                    // not pickup, invalid point
                    return false;
                }
            } else {
                // We cannot carry this object anymore
                return false;
            }
        } else {
            // we are currently carrying this node, check if this is a dropoff one
            if (!node.isPickup()) {
                // This is a dropoff, find the object and remove it
                // Check timing window first
                // update current time
                double arrivalTime = aCurrentTime + aPrevPoint.getDistanceTo(node.getPoint()) / transportVehicle.getSpeed();
                return node.getObject().isWithinTimingWindow(arrivalTime, false);
            } else {
                // we encountered this node again? invalid node
                return false;
            }
        }
    }

    private static List<TransportVehicle> copyVehicleArray (List<TransportVehicle> aTransportVehicles) {
        // Copy Vehicle array
        List<TransportVehicle> clonedList = new ArrayList<>(aTransportVehicles.size());
        for (TransportVehicle vehicle : aTransportVehicles) {
            clonedList.add(vehicle.getClone());
        }

        return clonedList;
    }

    private static final BruteForceRouter ourInstance = new BruteForceRouter();
    public static BruteForceRouter getInstance() {
        return ourInstance;
    }
    private BruteForceRouter() {
        mObjectList = new ArrayList<>();
    }
}
