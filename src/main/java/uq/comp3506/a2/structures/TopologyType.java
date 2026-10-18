// @edu:student-assignment



package uq.comp3506.a2.structures;

/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2026.
 * Used to tag the topology of Mole's tunnels.
 */
public enum TopologyType {
    CONNECTED_CONFIDENT,     // One component, no cycles
    CONNECTED_CONFUSING,     // One component, cycles
    DISCONNECTED_CONFUSING,  // Two or more components, all with cycles 
    DISCONNECTED_CONFIDENT,  // Two or more components, none with cycles
    HYBRID,                  // Two or more components, some with and some without cycles
    UNKNOWN                  // Degenerate cases; an empty graph for example
}
