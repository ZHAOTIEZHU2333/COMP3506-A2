// @edu:student-assignment

package uq.comp3506.a2;

// You may wish to import more/other structures too
import uq.comp3506.a2.structures.Edge;
import uq.comp3506.a2.structures.Vertex;
import uq.comp3506.a2.structures.Entry;
import uq.comp3506.a2.structures.TopologyType;
import uq.comp3506.a2.structures.Tunnel;
import uq.comp3506.a2.structures.Heap;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

// This is part of COMP3506 Assignment 2. Students must implement their own solutions.

/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2025.
 * No bounds are provided. You should maximize efficiency where possible.
 * Below we use `S` and `U` to represent the generic data types that a Vertex
 * and an Edge can have, respectively, to avoid confusion between V and E in
 * typical graph nomenclature. That is, Vertex objects store data of type `S`
 * and Edge objects store data of type `U`.
 */
public class Problems {

    /**
     * Return a double representing the minimum radius of illumination required
     * to light the entire tunnel. Your answer will be accepted if
     * |your_ans - true_ans| is less than or equal to 0.000001
     * @param tunnelLength The length of the tunnel in question
     * @param lightIntervals The list of light intervals in [0, tunnelLength];
     * that is, all light interval values are >= 0 and <= tunnelLength
     * @return The minimum radius value required to illuminate the tunnel
     * or -1 if no light fittings are provided
     * Note: We promise that the input List will be an ArrayList.
     */
    public static double tunnelLighting(int tunnelLength, List<Integer> lightIntervals) {
        if (lightIntervals == null || lightIntervals.isEmpty()) {
            return -1.0; 
        }
        
        ArrayList<Integer> a = new ArrayList<>(lightIntervals);
        Collections.sort(a);
    
        double r = Math.max(a.get(0), tunnelLength - a.get(a.size() - 1));
        
        for (int i = 0; i + 1 < a.size(); i++) {
            r = Math.max(r, (a.get(i + 1) - a.get(i)) / 2.0);
        }
        
        return r; 
    }

    /**
     * Compute the TopologyType of the graph as represented by the given edgeList.
     * @param edgeList The list of edges making up the graph G; each is of type
     *              Edge, which stores two vertices and a value. Vertex identifiers
     *              are NOT GUARANTEED to be contiguous or in a given range.
     * @return The corresponding TopologyType.
     * Note: We promise not to provide any self loops, double edges, or isolated
     * vertices.
     */
    public static <S, U> TopologyType topologyDetection(List<Edge<S, U>> edgeList) {
        if (edgeList == null || edgeList.isEmpty()) {
            return TopologyType.UNKNOWN;
        }
        
        
        ArrayList<Vertex<S>> verts = new ArrayList<>();
        for (Edge<S, U> e : edgeList) {
            indexOfOrAdd(verts, e.getVertex1());
            indexOfOrAdd(verts, e.getVertex2());
        }
        int n = verts.size();
        if (n == 0) return TopologyType.UNKNOWN;
        
        
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge<S, U> e : edgeList) {
            int u = indexOfOrAdd(verts, e.getVertex1());
            int v = indexOfOrAdd(verts, e.getVertex2());
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
        
        
        boolean[] vis = new boolean[n];
        int components = 0, treeComps = 0, cyclicComps = 0;
        ArrayList<Integer> stack = new ArrayList<>();
        
        for (int s = 0; s < n; s++) {
            if (vis[s]) continue;
            components++;
        
            stack.clear();
            stack.add(s);
            int nodes = 0;
            long degreeSum = 0;
        
            while (!stack.isEmpty()) {
                int i = stack.remove(stack.size() - 1);
                if (vis[i]) continue;
                vis[i] = true;
                nodes++;
                ArrayList<Integer> neigh = adj.get(i);
                degreeSum += neigh.size();
                for (int nb : neigh) {
                    if (!vis[nb]) stack.add(nb);
                }
            }
            long edges = degreeSum / 2;
            if (edges == nodes - 1) {
                treeComps++;
            } else {
                cyclicComps++;
            }
        }
        
       
        if (components == 1) {
           
            return (treeComps == 1) ? TopologyType.CONNECTED_TREE
                                    : TopologyType.CONNECTED_GRAPH;
        } else {
            
            if (cyclicComps == 0) return TopologyType.FOREST;
            if (treeComps == 0)   return TopologyType.DISCONNECTED_GRAPH;
            return TopologyType.HYBRID;
        }
    }

    private static <S> int indexOfOrAdd(ArrayList<Vertex<S>> verts, Vertex<S> x) {
        for (int i = 0; i < verts.size(); i++) {
            if (verts.get(i).equals(x)) return i;
        }
        verts.add(x);
        return verts.size() - 1;
        
    }
 
    /**
     * Compute the list of reachable destinations and their minimum costs.
     * @param edgeList The list of edges making up the graph G; each is of type
     *              Edge, which stores two vertices and a value. Vertex identifiers
     *              are NOT GUARANTEED to be contiguous or in a given range.
     * @param origin The origin vertex object.
     * @param threshold The total time the driver can drive before a break is required.
     * @return an ArrayList of Entry types, where the first element is the identifier
     *         of a reachable station (within the time threshold), and the second
     *         element is the minimum cost of reaching that given station. The
     *         order of the list is not important.
     * Note: We promise that S will be of Integer type.
     * Note: You should return the origin in your result with a cost of zero.
     */
    public static <S, U> List<Entry<Integer, Integer>> routeManagement(List<Edge<S, U>> edgeList,
                                                          Vertex<S> origin, int threshold) {

        ArrayList<Entry<Integer, Integer>> answers = new ArrayList<>();


        ArrayList<Vertex<S>> verts = new ArrayList<>();
        if (edgeList != null) {
            for (Edge<S, U> e : edgeList) {
                indexOfOrAdd(verts, e.getVertex1());
                indexOfOrAdd(verts, e.getVertex2());
            }
        }
 
        int src = indexOfOrAdd(verts, origin);
        int n = verts.size();
    
        ArrayList<ArrayList<Entry<Integer, Integer>>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    
        if (edgeList != null) {
            for (Edge<S, U> e : edgeList) {
                int u = indexOfOrAdd(verts, e.getVertex1());
                int v = indexOfOrAdd(verts, e.getVertex2());
    
              
                int w;
                Object val = e.getData();
                if (val instanceof Number) {
                    w = ((Number) val).intValue();
                } else {
                    throw new IllegalArgumentException("Edge weight must be numeric minutes.");
                }
    
                
                adj.get(u).add(new Entry<>(v, w));
                adj.get(v).add(new Entry<>(u, w));
            }
        }
    
        
        final int INF = Integer.MAX_VALUE / 4;
        int[] dist = new int[n];
        boolean[] vis = new boolean[n];
        for (int i = 0; i < n; i++) dist[i] = INF;
        dist[src] = 0;
    
        Heap<Integer, Integer> pq = new Heap<>();
        pq.insert(0, src);
    
        while (!pq.isEmpty()) {
            Entry<Integer, Integer> cur = pq.removeMin();
            int d = cur.getKey();
            int u = cur.getValue();
            if (vis[u]) continue;
            if (d > threshold) break;
            vis[u] = true;
                    
    
            for (Entry<Integer, Integer> e : adj.get(u)) {
                int v = e.getKey();
                int w = e.getValue();
                int nd = d + w;
                if (nd < dist[v] && nd <= threshold) {
                    dist[v] = nd;
                    pq.insert(nd, v);       
                }
            }
        }
    
        
        for (int i = 0; i < n; i++) {
            if (dist[i] != INF && dist[i] <= threshold && vis[i]) {
               
                int id = ((Integer) verts.get(i).getId());   
                answers.add(new Entry<>(id, dist[i]));
            }
        }
        return answers;
        
    }

    /**
     * Compute the tunnel that if flooded will cause the maximal flooding of 
     * the network
     * @param tunnels A list of the tunnels to consider; see Tunnel.java
     * @return The identifier of the Tunnel that would cause maximal flooding.
     * Note that for Tunnel A to drain into some other tunnel B, the distance
     * from A to B must be strictly less than the radius of A plus an epsilon
     * allowance of 0.000001. 
     * Note also that all identifiers in tunnels are GUARANTEED to be in the
     * range [0, n-1] for n unique tunnels.
     */
    public static int totallyFlooded(List<Tunnel> tunnels) {
        if (tunnels == null || tunnels.isEmpty()) return -1;
    
        final double EPS = 1e-6;
        final int n = tunnels.size();
    
        
        double[] X = new double[n];
        double[] Y = new double[n];
        double[] R = new double[n];
        for (Tunnel t : tunnels) {
            int id = t.getId();         
            X[id] = t.getX();            
            Y[id] = t.getY();
            R[id] = t.getRadius();       
        }
    
        
        java.util.ArrayList<java.util.ArrayList<Integer>> adj = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new java.util.ArrayList<>());
        for (int i = 0; i < n; i++) {
            double reach = R[i] + EPS;
            double reach2 = reach * reach;               
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                double dx = X[i] - X[j];
                double dy = Y[i] - Y[j];
                double d2 = dx * dx + dy * dy;
                if (d2 < reach2) adj.get(i).add(j);
            }
        }
    
       
        int bestId = 0, bestCnt = -1;
        boolean[] vis = new boolean[n];
        java.util.ArrayList<Integer> stack = new java.util.ArrayList<>();
    
        for (int s = 0; s < n; s++) {
           
            for (int k = 0; k < n; k++) vis[k] = false;
    
            stack.clear();
            stack.add(s);
            int cnt = 0;
    
            while (!stack.isEmpty()) {
                int u = stack.remove(stack.size() - 1);
                if (vis[u]) continue;
                vis[u] = true;
                if (u != s) cnt++;                    
                for (int v : adj.get(u)) {
                    if (!vis[v]) stack.add(v);
                }
            }
    
            if (cnt > bestCnt || (cnt == bestCnt && s < bestId)) {
                bestCnt = cnt;
                bestId = s;
            }
        }
    
        return bestId;
    }

    /**
     * Compute the number of sites that cannot be infiltrated from the given starting sites.
     * @param sites The list of unique site identifiers. A site identifier is GUARANTEED to be
     *              non-negative, starting from 0 and counting upwards to n-1.
     * @param rules The infiltration rule. The right-hand side of a rule is represented by a list
     *             of lists of site identifiers (as is done in the assignment specification). The
     *             left-hand side of a rule is given by the rule's index in the parameter `rules`
     *             (i.e. the rule whose left-hand side is 4 will be at index 4 in the parameter
     *              `rules` and can be accessed with `rules.get(4)`).
     * @param startingSites The list of site identifiers to begin your infiltration from.
     * @return The number of sites which cannot be infiltrated.
     */
    public static int susDomination(List<Integer> sites, List<List<List<Integer>>> rules,
                                     List<Integer> startingSites) {
        return -1;
    }
}
