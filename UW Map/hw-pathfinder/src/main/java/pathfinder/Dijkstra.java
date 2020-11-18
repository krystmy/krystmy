package pathfinder;

import graph.Edge;
import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.*;

public class Dijkstra<E extends  Comparable<E>> {

    /**
     * finds the path with the least cost from start node to destination node
     * @spec.requires start and destination node must exist in graph, and edges are non negative numbers
     * @param start the place to start looking from
     * @param dest place to find path to
     * @param graph graph of the relationship nodes and their edges
     * @throws IllegalArgumentException if start, dest, or graph is null
     * @return returns the path with the least cost from one node to another,
     *         null if path does not exist
     */
    @SuppressWarnings("unchecked")
    public Path<E> dijsktra(E start, E dest, Graph graph) {
        if(start == null || dest == null || graph == null) {
            throw new IllegalArgumentException();
        }
        if(!graph.nodeExist(start) || !graph.nodeExist(dest)) {
            return null;
        }
        Queue<Path<E>> active = new PriorityQueue<>(new Comparator<Path<E>>() {
            @Override
            public int compare(Path<E> o1, Path<E> o2) {
                return (int) (o1.getCost() - o2.getCost());
            }
        });
        Set<E> finished = new HashSet<>();
        Path<E> empty = new Path<>(start);
        active.add(empty);
        while (!active.isEmpty()) {
            Path<E> minPath = active.remove();
            E minDest = minPath.getEnd();
            if(minDest.equals(dest)) { return minPath; }
            if(finished.contains(minDest)) { continue; }
            for(Edge e :(Set<Edge>) graph.getEdgeList(minDest)) {
                if(!finished.contains(e.getChild())) {
                    Path<E> newPath = minPath.extend((E) e.getChild(),(Double) e.getData());
                    active.add(newPath);
                }
            }
            finished.add(minDest);
        }
        return null;
    }
}
