/**
* Supplied by the COMP3506/7505 teaching team, Semester 2, 2026.
*/

import uq.comp3506.a2.Problems;
import uq.comp3506.a2.structures.Heap;
import uq.comp3506.a2.structures.UnorderedMap;
import uq.comp3506.a2.structures.Vertex;
import uq.comp3506.a2.structures.Edge;
import uq.comp3506.a2.structures.Entry;
import uq.comp3506.a2.structures.TopologyType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uq.comp3506.a2.Problems;

public class TestProblems {

    // The series of tests that need to be implemented
    public static void testCantEven() {
        System.out.println("Testing 'I Literally Can't Even'");

        assert Problems.cantEven(new Long[]{}) == -1
                : "Empty array";

        assert Problems.cantEven(new Long[]{0L}) == 0
                : "Single zero";

        assert Problems.cantEven(new Long[]{5L, 5L}) == -1
                : "All frequencies even";

        assert Problems.cantEven(
                new Long[]{1L, 5L, 2L, 4L, 6L, 5L, 1L, 5L, 5L, 2L, 5L}) == 6
                : "Assignment example";

        assert Problems.cantEven(new Long[]{9L, 1L, 5L, 1L, 9L, 1L, 9L}) == 9
                : "Several odd frequencies";

        assert Problems.cantEven(new Long[]{100L, 100L, 3L}) == 3
                : "Largest input has even frequency";

        assert Problems.cantEven(new Long[]{4294967295L, 2L, 2L}) == 4294967295L
                : "Maximum permitted value";

        System.out.println("cantEven: all basic tests passed");
    }

    public static void testDoominos() {
        System.out.println("Testing 'Doominos Pizza Delivery'");

        assert Problems.firstVisited("") == -1
                : "Empty commands";

        assert Problems.firstVisited("U") == -1
                : "Single move";

        assert Problems.firstVisited("URDL") == 3
                : "Return to starting cell";

        assert Problems.firstVisited("UD") == 1
                : "Immediate reversal";

        assert Problems.firstVisited("LR") == 1
                : "Negative coordinate and return";

        assert Problems.firstVisited("UURD") == -1
                : "No repeated cell";

        assert Problems.firstVisited("URDU") == 3
                : "Revisit a cell other than the start";

        assert Problems.firstVisited("LLDDRRUU") == 7
                : "Loop through negative coordinates";

        assert Problems.firstVisited("UDRR") == 1
                : "Return the first revisit index";

        System.out.println("firstVisited: all basic tests passed");
    }

    public static void testCurst() {
        System.out.println("Testing 'Curst Pizza's Robot Blocker'");

        uq.comp3506.a2.Pathfinder single = Problems.pathfinder(1);

        assert !single.doesPathExist() : "Initially blocked single cell";

        single.makeCellAccessible(0, 0);
        assert single.doesPathExist() : "Open single cell connects both edges";

        single.makeCellAccessible(0, 0);
        assert single.doesPathExist() : "Repeated opening";

        uq.comp3506.a2.Pathfinder grid = Problems.pathfinder(4);

        assert !grid.doesPathExist() : "Initially blocked grid";

        grid.makeCellAccessible(0, 1);
        grid.makeCellAccessible(1, 1);
        grid.makeCellAccessible(0, 3);
        assert !grid.doesPathExist() : "Left side only";

        grid.makeCellAccessible(2, 2);
        assert !grid.doesPathExist() : "Diagonal cells must not connect";

        grid.makeCellAccessible(3, 2);
        assert !grid.doesPathExist() : "Two separate components";

        grid.makeCellAccessible(1, 2);
        assert grid.doesPathExist() : "Bridge connects left and right";

        grid.makeCellAccessible(1, 2);
        assert grid.doesPathExist() : "Repeated bridge opening";

        uq.comp3506.a2.Pathfinder vertical = Problems.pathfinder(3);

        for (int y = 0; y < 3; y++) {
            vertical.makeCellAccessible(1, y);
        }

        assert !vertical.doesPathExist()
                : "Top-to-bottom path is not a left-to-right path";

        vertical.makeCellAccessible(0, 0);
        assert !vertical.doesPathExist() : "Right edge still inaccessible";

        vertical.makeCellAccessible(2, 2);
        assert vertical.doesPathExist() : "Bent left-to-right path";

        System.out.println("Pathfinder: all basic tests passed");
    }

    // Here is an example of how you can make your own tests
    public static void testTunnelVision() {
        System.out.println("Testing 'Tunnel Vision'");

        List<Vertex<String>> vertices = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            vertices.add(new Vertex<>(i, "yeet"));
        }
        ArrayList<Edge<String, Character>> connectedGraph = new ArrayList<>(Arrays.asList(
            new Edge<>(vertices.get(0), vertices.get(1), 'G'),
            new Edge<>(vertices.get(0), vertices.get(2), 'U'),
            new Edge<>(vertices.get(1), vertices.get(4), 'R'),
            new Edge<>(vertices.get(1), vertices.get(2), 'K'),
            new Edge<>(vertices.get(1), vertices.get(3), 'E'),
            new Edge<>(vertices.get(2), vertices.get(5), 'Y'),
            new Edge<>(vertices.get(2), vertices.get(6), 'T'),
            new Edge<>(vertices.get(3), vertices.get(5), 'I'),
            new Edge<>(vertices.get(3), vertices.get(6), 'M'),
            new Edge<>(vertices.get(5), vertices.get(6), 'E'))
        );
        assert Problems.topologyDetection(connectedGraph) == TopologyType.CONNECTED_CONFUSING;


        ArrayList<Edge<String, Character>> disconnectedTree = new ArrayList<>(Arrays.asList(
            new Edge<>(vertices.get(0), vertices.get(1), 'B'),
            new Edge<>(vertices.get(0), vertices.get(2), 'A'),
            new Edge<>(vertices.get(1), vertices.get(4), 'R'),
            new Edge<>(vertices.get(3), vertices.get(6), 'R'),
            new Edge<>(vertices.get(5), vertices.get(6), 'Y'))
        );
        assert Problems.topologyDetection(disconnectedTree) == TopologyType.DISCONNECTED_CONFIDENT;

        // you can use these ones if you prefer jUnit testing instead
        // assertEquals(TopologyType.CONNECTED_GRAPH, Problems.topologyDetection(connectedGraph));
        // assertEquals(TopologyType.FOREST, Problems.topologyDetection(disconnectedTree));
    }

    // This one is hard to make your own tests for. You might like to just sit
    // down, come up with a strategy to solve the problem, and then hit gradescope
    // with your solution
    public static void testLabyrinth() {
        System.out.println("Testing 'The UQ Nephilim Labyrinth'");
    }

    // Try to call the given test based on the input
    public static void dispatch(String str) {
        switch (str.toLowerCase()) {
            case "canteven": 
                testCantEven();
                return;
            case "doominos":
                testDoominos();
                return;
            case "curst":
                testCurst();
                return;
            case "tunnelvision":
                testTunnelVision();
                return;
            case "labyrinth":
                testLabyrinth();
                return;
            default:
                throw new IllegalArgumentException("Unknown command: " + str);
        }
    }

    // Does what it says on the tin 
    private static void usage() {
        System.out.println("Usage: java TestProblems <commands>");
        System.out.println("Commands:");
        System.out.println("  canteven");
        System.out.println("  doominos");
        System.out.println("  curst");
        System.out.println("  tunnelvision");
        System.out.println("  labyrinth");
    }

    public static void main(String[] args) {
        
        // Basic checking - make sure a command is provided
        if (args.length == 0) {
            usage();
            return;
        }

        // Walk the commands and try to dispatch them
        for (int i = 0; i < args.length; ++i) {
            dispatch(args[i]);
        }

        // profit??
    }

}
