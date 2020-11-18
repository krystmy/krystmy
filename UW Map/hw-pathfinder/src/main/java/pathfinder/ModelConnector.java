/*
 * Copyright ©2019 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Summer Quarter 2019 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder;

import graph.Edge;
import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.*;

/*
In the pathfinder homework, the text user interface calls these methods to talk
to your model. In the campuspaths homework, your graphical user interface
will ultimately make class to these methods (through a web server) to
talk to your model the same way.

This is the power of the Model-View-Controller pattern, two completely different
user interfaces can use the same model to display and interact with data in
different ways, without requiring a lot of work to change things over.
*/

/**
 * This class represents the connection between the view and controller and the model
 * for the pathfinder and campus paths applications.
 */
public class ModelConnector {

    private List<CampusBuilding> buildings;//list of campus buildings
    private Graph<Point,Double> graph;
    private Map<String, String> shortLong;

    /**
     * Creates a new {@link ModelConnector} and initializes it to contain data about
     * pathways and buildings or locations of interest on the campus of the University
     * of Washington, Seattle. When this constructor completes, the dataset is loaded
     * and prepared, and any method may be called on this object to query the data.
     */
    public ModelConnector() {
        // TODO: You'll want to do things like read in the campus data and assemble your graph.
        // Remember the tenets of design that you've learned. You shouldn't necessarily do everything
        // you need for the model in this one constructor, factor code out to helper methods or
        // classes to work with your design best. The only thing that needs to remain the
        // same is the name of this class and the four method signatures below, because the
        // Pathfinder application calls these methods in order to talk to your model.
        // Change and add anything else as you'd like.
        buildings = CampusPathsParser.parseCampusBuildings();
        List<CampusPath> path = CampusPathsParser.parseCampusPaths();
        List<Point> points = buildingsToPoint(buildings);
        graph = loadGraph(points, path);
        shortLong = buildingNames();
    }

    private List<Point> buildingsToPoint(List<CampusBuilding> buildings ) {
        List<Point> list = new ArrayList<>();
        for (CampusBuilding b: buildings) {
            list.add(new Point(b.getX(), b.getY()));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private Graph<Point, Double> loadGraph(List<Point> points, List<CampusPath> paths) {
        Graph<Point, Double> graph = new Graph<>();
        for(Point p: points) {
            graph.addNode(p);
        }
        for(CampusPath p: paths) {
            Point fromNode = new Point(p.getX1(), p.getY1());
            Point toNode = new Point(p.getX2(), p.getY2());
            if (!graph.nodeExist(fromNode)) {
                graph.addNode(fromNode);
            }
            if ((!graph.nodeExist(toNode))) {
                graph.addNode(toNode);
            }
            graph.addEdge(new Edge(fromNode, toNode, p.getDistance()));
        }
        return graph;
    }

    /**
     * @param shortName The short name of a building to query.
     * @return {@literal true} iff the short name provided exists in this campus map.
     */
    public boolean shortNameExists(String shortName) {
        if(shortName == null) {
            throw new IllegalArgumentException();
        }
        return shortLong.containsKey(shortName);
    }

    /**
     * @param shortName The short name of a building to look up.
     * @return The long name of the building corresponding to the provided short name.
     * @throws IllegalArgumentException if the short name provided does not exist.
     */
    public String longNameForShort(String shortName) {
        if(!shortNameExists(shortName)) {
            throw new IllegalArgumentException();
        }
        return shortLong.get(shortName);
    }

    /**
     * @return The mapping from all the buildings' short names to their long names in this campus map.
     */
    public Map<String, String> buildingNames() {
        Map<String, String> shortToLong = new HashMap<>();
        for(CampusBuilding b: buildings) {
            shortToLong.put(b.getShortName(), b.getLongName());
        }
        return shortToLong;
    }

    /**
     * Finds the shortest path, by distance, between the two provided buildings.
     *
     * @param startShortName The short name of the building at the beginning of this path.
     * @param endShortName   The short name of the building at the end of this path.
     * @return A path between {@code startBuilding} and {@code endBuilding}, or {@literal null}
     * if none exists.
     * @throws IllegalArgumentException if {@code startBuilding} or {@code endBuilding} are
     *                                  {@literal null}, or not valid short names of buildings in
     *                                  this campus map.
     */
    @SuppressWarnings("unchecked")
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        if(startShortName == null || endShortName == null || !shortNameExists(startShortName)
                || !shortNameExists(endShortName)){
            throw new IllegalArgumentException();
        }
        Dijkstra<Point> dijkstra = new Dijkstra<>();
        return dijkstra.dijsktra(nameToPoint(startShortName), nameToPoint(endShortName), graph);
    }

    private Point nameToPoint(String name) {
        for(CampusBuilding b: buildings) {
            if (b.getShortName().equals(name)) {
                return new Point(b.getX(), b.getY());
            }
        }
        return null;
    }


}
