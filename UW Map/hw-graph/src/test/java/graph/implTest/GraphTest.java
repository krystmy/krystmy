package graph.implTest;

import graph.*;

import org.junit.Test;
import graph.Edge;

import java.util.*;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class GraphTest {

    //construct an empty graph
    private static Graph graph() { return new Graph();}

    //adds 5 nodes to a graph
    private static void addNodes(Graph graph){
        String[] nodes = {"1", "2", "3", "4", "5", "6"};
        for(String s: nodes) {
            graph.addNode(s);
        }
    }

    //creates an array of edges
    private static Edge[] edges() {
        Edge[] edge = new Edge[5];
        for(int i = 0; i < edge.length; i++) {
            edge[i] = new Edge("" + (i+1), "" +(i+2), "" + i);
        }
        return edge;
    }

    //adds edges to a graph
    private static void addEdges(Graph graph) {
        Edge[] list = edges();
        for(Edge e: list) {
            graph.addEdge(e);
        }
    }

    //adds many edges to a graph
    private static void addManyEdges(Graph graph) {
        Edge[] edge = new Edge[10];
        edge[0] = new Edge("2", "3", "A");
        edge[1] = new Edge("3", "4", "B");
        edge[2] = new Edge("3", "5", "C");
        edge[3] = new Edge("4", "5", "D");
        edge[4] = new Edge("4", "1", "E");
        edge[4] = new Edge("4", "1", "E");
        edge[5] = new Edge("4", "2", "F");
        edge[6] = new Edge("5", "1", "G");
        edge[7] = new Edge("5", "2", "H");
        edge[8] = new Edge("5", "3", "I");
        edge[9] = new Edge("5", "4", "K");
        for(Edge e: edge) {
            graph.addEdge(e);
        }
    }

    //creates hashSet of nodes
    private static Set<String> setNode(String[] nodes) {
        Set<String> set = new HashSet<>();
        Collections.addAll(set, nodes);
        return set;
    }

    //list of sets of children
    private static List<Set<String>> list() {
        List<Set<String>> list = new ArrayList<Set<String>>();
        String[] set1 = {""};
        String[] set2 = {"3"};
        String[] set3 = {"4", "5"};
        String[] set4 = {"5", "1", "2"};
        String[] set5 = {"1", "2", "3", "4"};
        list.add(setNode(set1));
        list.add(setNode(set2));
        list.add(setNode(set3));
        list.add(setNode(set4));
        list.add(setNode(set5));
        return list;
    }

    //set of Edges from node
    private static Set<Edge> edgeSet(Edge e) {
        Set<Edge> set = new HashSet<>();
        set.add(e);
        return set;
    }

    @Test
    public void testIsEmptyGraph() {
        assertTrue(graph().isEmpty());
    }

    @Test
    public void testIsNotEmptyGraph() {
        Graph graph = graph();
        graph.addNode("1");
        assertFalse(graph.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullNode() {
        Graph graph1 = graph();
        graph1.addNode(null);
    }

    @Test
    public void testAddingNode() {
        Graph graph1 = graph();
        assertTrue(graph1.addNode("1"));
    }

    @Test
    public void testAddingDuplicateNode() {
        Graph graph1 = graph();
        graph1.addNode("1");
        assertEquals(1, graph1.getSize());
        graph1.addNode("1");
        assertEquals(1, graph1.getSize());
    }

    @Test
    public void testAddingManyNodes() {
        Graph graph1 = graph();
        assertTrue(graph1.addNode("1"));
        assertTrue(graph1.addNode("2"));
        assertTrue(graph1.addNode("3"));
        assertTrue(graph1.addNode("4"));
        assertTrue(graph1.addNode("5"));
    }

    @Test
    public void testOneNodeExist() {
        Graph graph1 = graph();
        graph1.addNode("1");
        assertTrue(graph1.nodeExist("1"));
    }

    @Test
    public void testManyNodesExist() {
        Graph graph1 = graph();
        addNodes(graph1);
        assertTrue(graph1.nodeExist("1"));
        assertTrue(graph1.nodeExist("2"));
        assertTrue(graph1.nodeExist("3"));
        assertTrue(graph1.nodeExist("4"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNodeExist() {
        Graph graph1 = graph();
        graph1.nodeExist(null);
    }

    @Test
    public void testNodeDoesntExist() {
        Graph graph1 = graph();
        assertFalse(graph1.nodeExist("1"));
    }

    @Test
    public void testManyNodeDoesntExist() {
        Graph graph1 = graph();
        addNodes(graph1);
        assertFalse(graph1.nodeExist("7"));
        assertFalse(graph1.nodeExist("8"));
        assertFalse(graph1.nodeExist("9"));
        assertFalse(graph1.nodeExist("10"));
        assertFalse(graph1.nodeExist("11"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullNode() {
        Graph graph1 = graph();
        graph1.addNode(null);
    }

    @Test
    public void testRemoveNode() {
        Graph graph1 = graph();
        addNodes(graph1);
        assertTrue(graph1.removeNode("1"));
        assertTrue(graph1.removeNode("2"));
        assertTrue(graph1.removeNode("3"));
        assertTrue(graph1.removeNode("4"));
    }

    @Test
    public void testRemoveNodeDoesntExist() {
        Graph graph1 = graph();
        addNodes(graph1);
        assertFalse(graph1.removeNode("7"));
        assertFalse(graph1.removeNode("8"));
        assertFalse(graph1.removeNode("9"));
        assertFalse(graph1.removeNode("10"));
        assertFalse(graph1.removeNode("11"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullEdge() {
        Graph graph1 = graph();
        graph1.addEdge(null);
    }

    @Test
    public void testAddingReflexiveEdge() {
        Graph graph1 = graph();
        graph1.addNode("1");
        Edge edge = new Edge("1", "1", "1");
        assertTrue(graph1.addEdge(edge));
    }

    @Test
    public void testAddingEdge() {
        Graph graph1 = graph();
        graph1.addNode("1");
        graph1.addNode("2");
        Edge edge = new Edge("1", "2", "1");
        assertTrue(graph1.addEdge(edge));
    }

    @Test
    public void testAddingDuplicateEdge() {
        Graph graph1 = graph();
        graph1.addNode("1");
        graph1.addNode("2");
        Edge edge = new Edge("1", "2", "1");
        graph1.addEdge(edge);
        assertEquals(1, graph1.getEdgeList("1").size());
        Edge edge1 = new Edge("1", "2", "1");
        graph1.addEdge(edge1);
        assertEquals(1, graph1.getEdgeList("1").size());

    }

    @Test
    public void testAddingManyEdges() {
        Graph graph1 = graph();
        addNodes(graph1);
        Edge[] edge = edges();
        assertTrue(graph1.addEdge(edge[0]));
        assertTrue(graph1.addEdge(edge[1]));
        assertTrue(graph1.addEdge(edge[2]));
        assertTrue(graph1.addEdge(edge[3]));
        assertTrue(graph1.addEdge(edge[4]));
    }

    @Test
    public void testEdgeExist() {
        Graph graph1 = graph();
        addNodes(graph1);
        graph1.addEdge(new Edge("1", "2", "1"));
        assertTrue(graph1.edgeExist("1", "2"));
    }

    @Test
    public void testManyEdgeExist() {
        Graph graph1 = graph();
        addNodes(graph1);
        addEdges(graph1);
        assertTrue(graph1.edgeExist("1", "2"));
        assertTrue(graph1.edgeExist("2", "3"));
        assertTrue(graph1.edgeExist("3", "4"));
        assertTrue(graph1.edgeExist("4", "5"));
        assertTrue(graph1.edgeExist("5", "6"));
    }

    @Test
    public void testRemoveManyEdges() {
        Graph graph1 = graph();
        addNodes(graph1);
        addEdges(graph1);
        Edge[] list = edges();
        assertTrue(graph1.removeEdge(list[0]));
        assertTrue(graph1.removeEdge(list[1]));
        assertTrue(graph1.removeEdge(list[2]));
        assertTrue(graph1.removeEdge(list[3]));
        assertTrue(graph1.removeEdge(list[4]));

    }

    @Test
    public void testRemoveEdgesDontExist() {
        Graph graph1 = graph();
        addNodes(graph1);
        assertFalse(graph1.edgeExist("1", "2"));
        assertFalse(graph1.edgeExist("2", "3"));
        assertFalse(graph1.edgeExist("3", "4"));
        assertFalse(graph1.edgeExist("4", "5"));
        assertFalse(graph1.edgeExist("5", "6"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullEdge() {
        Graph graph1 = graph();
        graph1.removeEdge(null);
    }

    @Test
    public void testRemoveEdge() {
        Graph graph1 = graph();
        addNodes(graph1);
        Edge e = new Edge("1", "2", "A");
        graph1.addEdge(e);
        assertTrue(graph1.removeEdge(e));
        assertFalse(graph1.edgeExist("1", "2"));
    }

    @Test
    public void testGetEdges() {
        Graph graph1 = graph();
        Edge[] edge = edges();
        addNodes(graph1);
        addEdges(graph1);
        assertEquals(edgeSet(edge[0]), graph1.getEdge("1", "2"));
        assertEquals(edgeSet(edge[1]), graph1.getEdge("2", "3"));
        assertEquals(edgeSet(edge[2]), graph1.getEdge("3", "4"));
        assertEquals(edgeSet(edge[3]), graph1.getEdge("4", "5"));
        assertEquals(edgeSet(edge[4]), graph1.getEdge("5", "6"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullEdge() {
        Graph graph1 = graph();
        graph1.getEdge(null, null);
    }

    @Test
    public void testGetNonexistentEdges() {
        Graph graph1 = graph();
        addNodes(graph1);
        Set<Edge> e = new HashSet<>();
        assertEquals(e, graph1.getEdge("1", "2"));
        assertEquals(e, graph1.getEdge("2", "3"));
        assertEquals(e, graph1.getEdge("3", "4"));
        assertEquals(e, graph1.getEdge("4", "5"));
    }
    @Test
    public void testEmptySize() {
        Graph graph1 = graph();
        assertEquals( 0,graph1.getSize());
    }

    @Test
    public void testSize() {
        Graph graph1 = graph();
        graph1.addNode("1");
        assertEquals( 1,graph1.getSize());
        graph1.addNode("2");
        assertEquals( 2,graph1.getSize());
        graph1.addNode("3");
        assertEquals( 3,graph1.getSize());
        graph1.addNode("4");
        assertEquals( 4,graph1.getSize());
        graph1.addNode("5");
        assertEquals( 5,graph1.getSize());
    }

    @Test
    public void testToStringEmpty() {
        Graph graph1 = graph();
        assertEquals(graph1.toString(), "");
    }

    @Test
    public void testToStringSmall() {
        Graph graph1 = graph();
        graph1.addNode("1");
        graph1.addNode("2");
        assertEquals( "1 2 ", graph1.toString());
    }

    @Test
    public void testToString() {
        Graph graph1 = graph();
        addNodes(graph1);
        addEdges(graph1);
        assertEquals( "1 2 3 4 5 6 ", graph1.toString());
    }

    @Test
    public void testgetNoChild() {
        Graph graph = graph();
        graph.addNode("1");
        assertEquals(new HashSet<String>(),graph.getChildren("1"));
    }

    @Test
    public void testgetChildren() {
        Graph graph = graph();
        addNodes(graph);
        addManyEdges(graph);
        List<Set<String>> list = list();
        assertEquals(list.get(1), graph.getChildren("2"));
        assertEquals(list.get(2), graph.getChildren("3"));
        assertEquals(list.get(3), graph.getChildren("4"));
        assertEquals(list.get(4), graph.getChildren("5"));

    }

    @Test
    public void testGetEdgeList() {
        Graph graph = graph();
        graph.addNode("A");
        graph.addNode("B");
        Edge e = new Edge(  "A", "B", "1");
        graph.addEdge(e);
        Set<Edge> edge = new HashSet<>();
        edge.add(e);
        assertEquals(edge, graph.getEdgeList("A"));
    }

    @Test
    public void testGetEmptyEdgeList() {
        Graph graph = graph();
        graph.addNode("A");
        graph.addNode("B");
        Set<Edge> edge = new HashSet<>();
        assertEquals(edge, graph.getEdgeList("A"));
    }


}
