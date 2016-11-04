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

    public List<Route> generateRoute() {
        List<Route> tempRouteList = new ArrayList<>(mObjectList.size());
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
    private List<Route> generateRouteFromList(List<RouteNode> aRouteNodes, List<TransportVehicle> aTransportVehicles,
                                              boolean isFirst) {
        if (aRouteNodes.size() <= 0) {
            // WTF happened here
            return new LinkedList<>();
        } else {
            List<Route> finalGeneratedRoutes = null;
            for (int i = 0; i < aRouteNodes.size(); i++) {
                List<TransportVehicle> currentIterationVehicles = new ArrayList<>(aTransportVehicles.size());
                for (TransportVehicle vehicle : aTransportVehicles) {
                    currentIterationVehicles.add(vehicle.getClone());
                }

                RouteNode node = aRouteNodes.get(i);

                for (TransportVehicle availableVehicle : currentIterationVehicles) {
                    // Initialize variable for this iteration
                    if (isFirst) {
                        availableVehicle.setTime(0);
                        availableVehicle.setLastPoint(new Point(0, 0));
                    }

                    List<TransportableObject> currentLuggage = availableVehicle.getCurrentLuggage(node.getObject().getType());
                    boolean isValid = isNodeValid(node, currentLuggage, availableVehicle,
                            availableVehicle.getTime(), availableVehicle.getLastPoint());

                    if (isValid) {
                        // Update time and previous node
                        if (node.isPickup()) {
                            currentLuggage.add(node.getObject());
                        } else {
                            currentLuggage.remove(node.getObject());
                        }
                        availableVehicle.setTime(availableVehicle.getTime()
                                + availableVehicle.getLastPoint().getDistanceTo(node.getPoint())
                                / availableVehicle.getSpeed());
                        availableVehicle.setLastPoint(node.getPoint());

                        // Clone list of points, but remove current node
                        if (finalGeneratedRoutes == null) {
                            finalGeneratedRoutes = new LinkedList<>();
                        }
                        if (aRouteNodes.size() == 1) {
                            // Stop iteration
                            finalGeneratedRoutes.add(new Route(node));
                            return finalGeneratedRoutes;
                        } else {
                            List<RouteNode> tempPoints = new LinkedList<>(aRouteNodes);
                            tempPoints.remove(i);
                            List<Route> generatedRoutes = generateRouteFromList(tempPoints, currentIterationVehicles, false);
                            if (generatedRoutes != null) {
                                for (Route route : generatedRoutes) {
                                    route.addNodeOnFirst(node);
                                }
                                finalGeneratedRoutes.addAll(generatedRoutes);
                            }
                        }
                    }
                }
            }
            return finalGeneratedRoutes;
        }
    }

    private static boolean isNodeValid(RouteNode node, List<TransportableObject> aCurrentLuggage,
                                       TransportVehicle aTransportVehicle, double aCurrentTime, Point aPrevPoint) {
        // Filter out invalid paths
        TransportableObjectType currentType = node.getObject().getType();
        if (!aCurrentLuggage.contains(node.getObject())) {
            // This is a different object
            if (aCurrentLuggage.size() < aTransportVehicle.getObjectCapacity(currentType)) {
                // carry this new object, if this is the pickup point
                if (node.isPickup()) {
                    // Check timing window first
                    // update current time
                    double arrivalTime = aCurrentTime + aPrevPoint.getDistanceTo(node.getPoint()) / aTransportVehicle.getSpeed();
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
                double arrivalTime = aCurrentTime + aPrevPoint.getDistanceTo(node.getPoint()) / aTransportVehicle.getSpeed();
                return node.getObject().isWithinTimingWindow(arrivalTime, false);
            } else {
                // we encountered this node again? invalid node
                return false;
            }
        }
    }

    private static final BruteForceRouter ourInstance = new BruteForceRouter();
    public static BruteForceRouter getInstance() {
        return ourInstance;
    }
    private BruteForceRouter() {
        mObjectList = new ArrayList<>();
    }
}
