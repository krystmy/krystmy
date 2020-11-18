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

package graph.specTest;

import graph.Edge;
import graph.Graph;

import java.io.*;
import java.util.*;

/**
 * This class implements a testing driver which reads test scripts
 * from files for testing Graph.
 **/
public class GraphTestDriver {

    public static void main(String[] args) {
        try {
            if(args.length > 1) {
                printUsage();
                return;
            }

            GraphTestDriver td;

            if(args.length == 0) {
                td = new GraphTestDriver(new InputStreamReader(System.in),
                                         new OutputStreamWriter(System.out));
            } else {

                String fileName = args[0];
                File tests = new File(fileName);

                if(tests.exists() || tests.canRead()) {
                    td = new GraphTestDriver(new FileReader(tests),
                                             new OutputStreamWriter(System.out));
                } else {
                    System.err.println("Cannot read from " + tests.toString());
                    printUsage();
                    return;
                }
            }

            td.runTests();

        } catch(IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("to read from a file: java graph.specTest.GraphTestDriver <name of input script>");
        System.err.println("to read from standard in: java graph.specTest.GraphTestDriver");
    }

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    private final Map<String, Graph> graphs = new HashMap<String, Graph>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @requires r != null && w != null
     * @effects Creates a new GraphTestDriver which reads command from
     * <tt>r</tt> and writes results to <tt>w</tt>.
     **/
    public GraphTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @effects Executes the commands read from the input and writes results to the output
     **/
    public void runTests()
            throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
               (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            if(command.equals("CreateGraph")) {
                createGraph(arguments);
            } else if(command.equals("AddNode")) {
                addNode(arguments);
            } else if(command.equals("AddEdge")) {
                addEdge(arguments);
            } else if(command.equals("ListNodes")) {
                listNodes(arguments);
            } else if(command.equals("ListChildren")) {
                listChildren(arguments);
            } else {
                output.println("Unrecognized command: " + command);
            }
        } catch(Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        Graph graph = new Graph();
        graphs.put(graphName, graph);
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to addNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    @SuppressWarnings("unchecked")
    private void addNode(String graphName, String nodeName) {
        Graph graph1 = graphs.get(graphName);
        graph1.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to addEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    @SuppressWarnings("unchecked")
    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {
        Graph graph1 = graphs.get(graphName);
        Edge edge = new Edge(parentName, childName, edgeLabel);
        graph1.addEdge(edge);
        output.println("added edge " + edgeLabel + " from " + parentName + " to " + childName +
                " in " + graphName);

    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to listNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph graph1 = graphs.get(graphName);
        output.println(graphName + " contains: " + graph1.toString());
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to listChildren: " + arguments);
        }
        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    @SuppressWarnings("unchecked")
    private void listChildren(String graphName, String parentName) {
        //the children of n1 in graph1 are: n2(e1) n3(e2)
        Graph graph1 = graphs.get(graphName);
        String ans = "the children of " + parentName + " in " + graphName + " are:";
        List<Edge<String, String>> sorted = new ArrayList<Edge<String, String>>(graph1.getEdgeList(parentName));
        Collections.sort(sorted, new Comparator<Edge<String, String>>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                if(e1.equals(e2)) {
                    return e1.getData().compareTo(e2.getData());
                }
                return e1.getChild().compareTo(e2.getChild());
            }
        });
        for(Edge e: sorted) {
            ans += " " + e.getChild().toString() + "(" + e.getData().toString() + ")";
        }
        output.println(ans);
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
