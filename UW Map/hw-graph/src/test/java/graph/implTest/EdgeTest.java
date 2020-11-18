package graph.implTest;

import org.junit.Test;
import graph.Edge;
import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class EdgeTest {

    //creates an edge
    private static Edge edge() {
        return new Edge("1", "2", "A");
    }

    @Test
    public void testToString() {
        assertEquals(edge().toString(), "Parent: 1, Child: 2, Data: A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEdge() {
        Edge edge = new Edge("1", "2", null);
    }

    @Test
    public void testGetParent() {
        Edge e = new Edge("1", "2", "A");
        assertEquals("1", e.getParent());
    }

    @Test
    public void testGetChild() {
        Edge e = new Edge("1", "2", "A");
        assertEquals("2", e.getChild());
    }

    @Test
    public void testGetData() {
        Edge e = new Edge("1", "2", "A");
        assertEquals("A", e.getData());
    }

}
