package pathfinder.implTest;

import graph.Edge;
import graph.Graph;
import org.junit.Test;
import pathfinder.Dijkstra;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class TestDijkstra {

    private void makeGraph(Graph graph) {
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
    }

    private Path<String> makePath(List<String> lst, List<Double> cost, Graph graph) {
        Path<String> path = new Path<>("A");
        for(int i = 1; i < lst.size(); i++) {
            path = path.extend(lst.get(i), cost.get(i));
        }
        return path;
    }

    @Test
    public void testEmpty() {
        Graph graph = new Graph();
        graph.addNode("A");
        Path<String> empty = new Path<>("A");
        Dijkstra<String> Dijkstra = new Dijkstra<>();
        assertEquals(empty, Dijkstra.dijsktra("A", "A", graph));
    }

    @Test
    public void testNodeDontExist() {
        Graph graph = new Graph();
        graph.addNode("A");
        Dijkstra<String> Dijkstra = new Dijkstra<>();
        assertNull(Dijkstra.dijsktra("A", "B", graph));
        assertNull(Dijkstra.dijsktra("B", "A", graph));
        assertNull(Dijkstra.dijsktra("B", "B", graph));
    }

    @Test
    public void testPathDontExist() {
        Graph graph = new Graph();
        makeGraph(graph);
        graph.addEdge(new Edge("A", "B", 2.0));
        Dijkstra<String> Dijkstra = new Dijkstra<>();
        assertNull(Dijkstra.dijsktra("B", "A", graph));
        assertNull(Dijkstra.dijsktra("B", "C", graph));
        assertNull(Dijkstra.dijsktra("A", "C", graph));
    }

    @Test
    public void testSmallPath() {
        Graph graph = new Graph();
        makeGraph(graph);
        graph.addEdge(new Edge("A", "B", 1.0));
        graph.addEdge(new Edge("B", "C", 1.0));
        graph.addEdge(new Edge("A", "C", 10.0));
        Path<String> path = new Path<>("A");
        path = path.extend("B", 1.0).extend("C", 1.0);
        Dijkstra<String> Dijkstra = new Dijkstra<>();
        assertEquals(path, Dijkstra.dijsktra("A", "C", graph));
    }

    @Test
    public void testLargePath() {
        Graph graph = new Graph();
        makeGraph(graph);
        graph.addEdge(new Edge("A", "B", 1.0));
        graph.addEdge(new Edge("B", "C", 1.0));
        graph.addEdge(new Edge("C", "D", 2.0));
        graph.addEdge(new Edge("A", "D", 10.0));
        Path<String> path = new Path<>("A");
        path = path.extend("B", 1.0)
                .extend("C", 1.0)
                .extend("D",2.0);
        Dijkstra<String> Dijkstra = new Dijkstra<>();

        assertEquals(path, Dijkstra.dijsktra("A", "D", graph));
    }

    @Test
    public void testMultiplePath() {
        Graph graph = new Graph();
        makeGraph(graph);
        graph.addEdge(new Edge("A", "B", 1.0));
        graph.addEdge(new Edge("B", "C", 1.0));
        graph.addEdge(new Edge("C", "D", 2.0));
        graph.addEdge(new Edge("A", "D", 4.0));
        Path<String> path = new Path<>("A");
        path = path.extend("D", 4.0);
        Dijkstra<String> Dijkstra = new Dijkstra<>();

        assertEquals(path, Dijkstra.dijsktra("A", "D", graph));
    }
}
