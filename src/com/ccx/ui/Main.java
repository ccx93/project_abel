package com.ccx.ui;

import com.ccx.core.Routing.Point;
import com.ccx.core.Routing.Route;
import com.ccx.core.Routing.BruteForceRouter;
import com.ccx.core.TransportObject.TransportableObject;
import com.ccx.core.TransportObject.TransportableObjectType;
import com.ccx.core.Transportation.TransportVehicle;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<TransportableObject> dummyPoints = new LinkedList<>();
        Date startTime = new Date();
//        dummyPoints.add(new TransportableObject("A", new Point(0, 0)  , new Point(100, 0), TransportableObjectType.PEOPLE,
//                new TransportableObject.TimingWindow(0, 1d), new TransportableObject.TimingWindow(9, 15d)));
        dummyPoints.add(new TransportableObject("B", new Point(100, 0), new Point(200, 0), TransportableObjectType.PEOPLE));
        dummyPoints.add(new TransportableObject("C", new Point(50, 0) , new Point(150, 1), TransportableObjectType.OBJECT));
//        dummyPoints.add(new TransportableObject("D", new Point(150, 1), new Point(250, 1), TransportableObjectType.OBJECT));
//        dummyPoints.add(new TransportableObject("E", new Point(100, 0), new Point(200, 0), TransportableObjectType.PEOPLE));
//        dummyPoints.add(new TransportableObject("F", new Point(50, 0) , new Point(150, 1), TransportableObjectType.OBJECT));
//        dummyPoints.add(new TransportableObject("G", new Point(150, 1), new Point(250, 1), TransportableObjectType.OBJECT));
//        dummyPoints.add(new TransportableObject("H", new Point(100, 0), new Point(200, 0), TransportableObjectType.PEOPLE));
//        dummyPoints.add(new TransportableObject("I", new Point(50, 0) , new Point(150, 1), TransportableObjectType.OBJECT));
//        dummyPoints.add(new TransportableObject("J", new Point(150, 1), new Point(250, 1), TransportableObjectType.OBJECT));
//        dummyPoints.add(new TransportableObject("K", new Point(50, 0) , new Point(150, 1), TransportableObjectType.OBJECT));
//        dummyPoints.add(new TransportableObject("L", new Point(150, 1), new Point(250, 1), TransportableObjectType.OBJECT));
//        dummyPoints.add(new TransportableObject("M", new Point(100, 0), new Point(200, 0), TransportableObjectType.PEOPLE));
//        dummyPoints.add(new TransportableObject("N", new Point(50, 0) , new Point(150, 1), TransportableObjectType.OBJECT));
//        dummyPoints.add(new TransportableObject("O", new Point(150, 1), new Point(250, 1), TransportableObjectType.OBJECT));

        BruteForceRouter router = BruteForceRouter.getInstance();
        router.setObjectList(dummyPoints);
        List<TransportVehicle> vehicleStock = new ArrayList<>();
        vehicleStock.add(new TransportVehicle(TransportVehicle.VehiclesEnum.TAXI));
        router.setTransportVehicles(vehicleStock);
        List<Route> result = router.generateRoute();

        Date endTime = new Date();
        for (Route route : result) {
            System.out.println(route.toString());
        }

        System.out.println(result.size() + " iteration founds");
        System.out.println("Elapsed time : " + (endTime.getTime() - startTime.getTime()) + " ms");
    }
}
