package graph;
/**
 * Represents a directed edge, that points from one node to another,
 * immutable edge
 * RI: parent, child, and data can't be null.
 *
 * AF: given an Edge, its parent is the parent node that the edge comes from. its
 * child is the node the edge points to, and data is the info of that edge
 */
public class

Edge<E extends Comparable<E>,T extends Comparable<T>> {
    private static final boolean CHECKREP = false;

    //node that edge is coming from
    private E parent;

    //node edge is pointing to
    private E child;

    //data/info of node
    private T data;

    /** Creates a new Edge
     * @param parent the node the edge is coming from
     * @param child the node the edge is point to
     * @param data the data of the edge
     * @throws IllegalArgumentException if child, parent, or data is null
     */
    public Edge(E parent, E child, T data) {
        if(parent == null || child == null || data == null) {
            throw new IllegalArgumentException();
        }
        this.parent = parent;
        this.child = child;
        this.data = data;
        checkRep();
    }

    /** returns the child node edge is pointing to
     * @return the child node
     */
    public E getChild() {
        return this.child;
    }

    /** return the data of the edge
     * @return the data of the edge
     */
    public T getData() {
        return this.data;
    }

    /** returns the parent node edge is pointing from
     * @return the parent node
     */
    public E getParent() { return this.parent;}

    /** return a E representation of the edge
     * @return return a E representation of the edge
     */
    public String toString() {
        return "Parent: " + this.parent.toString() + ", Child: " + this.child.toString()
                + ", Data: " + this.data.toString();
    }

    private void checkRep() {
        if(CHECKREP) {
            assert this.child != null;
            assert this.parent != null;
            assert this.data != null;
        }
    }

    @Override
    public boolean equals(Object other){
        if (! (other instanceof Edge)){
            return false;
        }
        Edge o = (Edge) other;
        return this.data.equals(o.data) && this.child.equals(o.child) && this.parent.equals(o.parent);
    }

    @Override
    public int hashCode() {
        return 31*data.hashCode() + 11*child.hashCode() + 17*parent.hashCode();
    }


}
