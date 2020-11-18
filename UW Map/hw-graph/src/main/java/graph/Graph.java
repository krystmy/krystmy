package graph;

import java.util.*;

/**
 * This class represents the concept of a direct graph, with nodes for vertices and edges
 * to connect them
 *
 * RI: size greater than 0, graph != null, each key and value pair in the graph are not null, each
 * Edge in the set is not null, and each edge points to/ points from an existing node in graph
 * 
 * AF: size of 0, means an empty graph, with no nodes or edges. A graph with a node and an edge
 * reflects a graph with 1 vertex with an edge pointing to itself. For a graph with n nodes and
 * m edges, it represents the graph with n vertices and m edges
 *
 *
 */

public class Graph<E extends Comparable<E>,T extends Comparable<T>> {
    private static final boolean CHECKREP = false;

    //size of graph in terms of number of nodes
    private int size;

    //map of all nodes and their outgoing edges
    private Map<E, Set<Edge<E,T>>> graph;

    /**
     * Creates an empty graph
     * @spec.requires graph != null
     */
    public Graph() {
        size = 0;
        graph = new HashMap<>();
        checkRep();
    }

    /**
     * Adds a node to the graph
     *
     * @param node node to insert in graph
     * @return true if node is inserted, false otherwise
     * @throws IllegalArgumentException if node is null
     */
    public boolean addNode(E node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        if (graph.put(node, new HashSet<>()) == null) {
            size++;
            checkRep();
            return true;
        }
        return false;
    }

    /**
     * Remove a node to that node
     * @param node node to remove from graph
     * @return true if node is removed, false otherwise
     * @throws IllegalArgumentException if node is null
     */
    public boolean removeNode(E node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        if (graph.remove(node) != null) {
            size--;
            checkRep();
            return true;
        }
        return false;
    }

    /**
     * Returns a set of nodes that are the children on given node
     * @param node parent node
     * @return a set of node that are the child of given node
     * @throws IllegalArgumentException if node is null
     */
    @SuppressWarnings("unchecked")
    public Set<E> getChildren(E node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        Set<Edge<E,T>> edges = graph.get(node);
        Set<E> children = new HashSet<>();
        for (Edge e : edges) {
            children.add((E) e.getChild());
        }
        checkRep();
        return children;
    }

    /**
     * remove an edge between nodefrom and to
     * @param edge the edge to remove
     * @return true if edge is removed false otherwise
     * @throws IllegalArgumentException if edge is null or graph is empty
     */
    @SuppressWarnings("unchecked")
    public boolean removeEdge(Edge edge) {
        if (edge == null || graph.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if(edgeExist((E) edge.getParent(), (E) edge.getChild())) {
            checkRep();
            return graph.get(edge.getParent()).remove((edge));
        }
        return false;
    }

    /**
     * A an edge from one node to another
     *
     * @param edge edge to add to graph
     * @return true if edge is inserted, false otherwise
     * @throws IllegalArgumentException if edge is null
     */
    @SuppressWarnings("unchecked")
    public boolean addEdge(Edge edge) {
        if (edge == null) {
            throw new IllegalArgumentException();
        }
        if(nodeExist((E) edge.getParent()) && nodeExist((E) edge.getChild())) {
            checkRep();
            return graph.get(edge.getParent()).add(edge);
        }
        return false;
    }

    /**
     * Get the edge going fromNode to ToNode
     *
     * @param toNode   node the edge is coming from
     * @param fromNode node the edge is pointing to
     * @return A set of edges that exist from one node to the other
     * @throws IllegalArgumentException if any node is null
     */
    public Set<Edge> getEdge(E fromNode, E toNode) {
        if (fromNode == null || toNode == null) {
            throw new IllegalArgumentException();
        }
        Set<Edge<E,T>> list = graph.get(fromNode);
        Set<Edge> ans = new HashSet<>();
        if (edgeExist(fromNode, toNode)) {
            for (Edge e : list) {
                if (e.getParent().equals(fromNode) && e.getChild().equals(toNode)) {
                    ans.add(e);
                }
            }
        }
        checkRep();
        return ans;
    }

    /**
     * Get the edge going fromNode to ToNode
     * @param parentNode node the edge is coming from
     * @return A set of outgoing edges from parentNode
     * @throws IllegalArgumentException if node is null
     */
    public Set<Edge<E,T>> getEdgeList(E parentNode) {
        if (parentNode == null) {
            throw new IllegalArgumentException();
        }
        return graph.get(parentNode);
    }

    /**
     * Returns whether or not an edge exist
     * @param fromNode the node the edge is coming from
     * @param toNode   the node the edge is coming from
     * @return true if an edge exist between those nodes, false otherwise
     * @throws IllegalArgumentException if any node is null
     */
    public boolean edgeExist(E fromNode, E toNode) {
        if (fromNode == null || toNode == null) {
            throw new IllegalArgumentException();
        }
        if (graph.get(fromNode) == null) {
            return false;
        }
        Set<Edge<E,T>> list = graph.get(fromNode);
        for (Edge e : list) {
            if (e.getParent().equals(fromNode) && e.getChild().equals(toNode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param node node of interest
     * @return true if node exist in graph, false otherwise
     * @throws IllegalArgumentException if any node is null
     */
    public boolean nodeExist(E node) {
        if (node == null) {
            throw new IllegalArgumentException();
        }
        return graph.get(node) != null;
    }

    /**
     * @return a list of nodes in the graph
     */
    public Set<E> nodeList() {
        return graph.keySet();
    }

    /**
     * Returns the number of nodes
     * @return number of nodes in graph
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns whether or not the graph is empty
     * @return true if graph is empty, false otherwise
     */
    public boolean isEmpty() {
        return graph.isEmpty();
    }

    /**
     * Returns a string representation of this graph
     * @return String of all the nodes and edges in this graph
     */
    public String toString() {
        String ans = "";
        for (E n : graph.keySet()) {
            ans += n.toString() + " " ;
        }
        return ans;
    }

    /**Check the representation of the graph
     */
    @SuppressWarnings("unchecked")
    private void checkRep() {
        if(CHECKREP) {
            assert size >= 0 : "size is less than 0";
            assert graph != null : "graph is null";
            for (E n : graph.keySet()) {
                assert n != null : "node is null";
                for (Edge e : graph.get(n)) {
                    assert e != null : "edge is null";
                    assert nodeExist((E) e.getParent()) : "parent node doesn't exist";
                    assert nodeExist((E) e.getChild()) : "child node doesn't exist";
                }
            }
        }
    }
}
