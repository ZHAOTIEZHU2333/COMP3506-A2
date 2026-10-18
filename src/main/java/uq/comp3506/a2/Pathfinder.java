
// @edu:student-assignment

package uq.comp3506.a2;


// This is part of COMP3506 Assignment 2. Students must implement their own solutions.

/**
 * Tracks accessible cells in a square grid and detects a path from its left edge to its
 * right edge.
 */
public class Pathfinder {

    private final int dimension;

    /** Flag indicating that a component touches the left edge. */
    private static final int LEFT_EDGE = 1;

    /** Flag indicating that a component touches the right edge. */
    private static final int RIGHT_EDGE = 2;

    /** Flags indicating that a component touches both edges. */
    private static final int BOTH_EDGES = LEFT_EDGE | RIGHT_EDGE;

    /**
     * Stores the state of each cell, allocating rows when first needed.
     * Zero means inaccessible.
     * A positive value is the parent cell identifier plus one.
     * A negative value identifies a root and encodes:
     * -(componentSize * 4 + boundaryFlags).
     */
    private final int[][] cells;

    /** Whether a path between the left and right edges exists. */
    private boolean connected;

    /**
     * Creates a pathfinder whose cells are initially inaccessible.
     *
     * @param dimension the width and height of the grid
     */
    public Pathfinder(int dimension) {
        this.dimension = dimension;

        // Implement the initialisation logic
        this.cells = new int[dimension][];
        this.connected = false;
    }

    /**
     * Marks a cell as accessible.
     *
     * @param x the cell's horizontal coordinate, in the range {@code [0, dimension)}
     * @param y the cell's vertical coordinate, in the range {@code [0, dimension)}
     */
    public void makeCellAccessible(int x, int y) {
        if (cells[y] == null) {
            cells[y] = new int[dimension];
        }

        if (cells[y][x] != 0) {
            return;
        }

        int flags = 0;

        if (x == 0) {
            flags |= LEFT_EDGE;
        }

        if (x == dimension - 1) {
            flags |= RIGHT_EDGE;
        }

        cells[y][x] = -(4 + flags);

        if (flags == BOTH_EDGES) {
            connected = true;
        }

        int id = y * dimension + x;

        if (x > 0 && cells[y][x - 1] != 0) {
            union(id, id - 1);
        }

        if (x + 1 < dimension && cells[y][x + 1] != 0) {
            union(id, id + 1);
        }

        if (y > 0 && cells[y - 1] != null
                && cells[y - 1][x] != 0) {
            union(id, id - dimension);
        }

        if (y + 1 < dimension && cells[y + 1] != null
                && cells[y + 1][x] != 0) {
            union(id, id + dimension);
        }
    }

    /**
     * Reports whether adjacent accessible cells form a path from the left edge of the grid
     * to the right edge.
     *
     * @return {@code true} if such a path exists; otherwise {@code false}
     */
    public boolean doesPathExist() {
        return connected;
    }

    /**
     * Reads a cell's stored state.
     *
     * @param id the cell identifier
     * @return the cell state
     */
    private int state(int id) {
        return cells[id / dimension][id % dimension];
    }

    /**
     * Updates a cell's stored state.
     *
     * @param id the cell identifier
     * @param value the new state
     */
    private void setState(int id, int value) {
        cells[id / dimension][id % dimension] = value;
    }

    /**
     * Finds a component root and compresses the traversed path.
     *
     * @param id an accessible cell identifier
     * @return the component root identifier
     */
    private int find(int id) {
        int root = id;
        int rootState = state(root);

        while (rootState > 0) {
            root = rootState - 1;
            rootState = state(root);
        }

        int current = id;

        while (current != root) {
            int next = state(current) - 1;
            setState(current, root + 1);
            current = next;
        }

        return root;
    }

    /**
     * Merges two components by attaching the smaller to the larger.
     *
     * @param first the first accessible cell
     * @param second the second accessible cell
     */
    private void union(int first, int second) {
        int rootA = find(first);
        int rootB = find(second);

        if (rootA == rootB) {
            return;
        }

        int informationA = -state(rootA);
        int informationB = -state(rootB);

        int sizeA = informationA >>> 2;
        int sizeB = informationB >>> 2;

        int flags = (informationA | informationB) & BOTH_EDGES;
        int combinedInformation = ((sizeA + sizeB) << 2) | flags;

        if (sizeA < sizeB) {
            setState(rootA, rootB + 1);
            setState(rootB, -combinedInformation);
        } else {
            setState(rootB, rootA + 1);
            setState(rootA, -combinedInformation);
        }

        if (flags == BOTH_EDGES) {
            connected = true;
        }
    }
}