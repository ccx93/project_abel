package com.ccx.core.Routing;

import java.util.LinkedList;

public class Route {
    private LinkedList<RouteNode> mNodes;

    public Route() {
    }

    public Route(RouteNode aNode) {
        mNodes = new LinkedList<>();
        mNodes.add(aNode);
    }

    public LinkedList<RouteNode> getNodes() {
        return mNodes;
    }

    public void addRouteOnEnd(Route route) {
        mNodes.addAll(route.getNodes());
    }

    public void addNodeOnEnd(RouteNode aNode) {
        mNodes.addLast(aNode);
    }

    public void addNodeOnFirst(RouteNode aNode) {
        mNodes.addFirst(aNode);
    }

    @Override
    public String toString() {
        String out = "";
        int i = 1;
        for (RouteNode node : mNodes) {
            out = out + String.format("%2d - %s", i++ , node.toString()) + "\n";
        }
        return out;
    }

}
