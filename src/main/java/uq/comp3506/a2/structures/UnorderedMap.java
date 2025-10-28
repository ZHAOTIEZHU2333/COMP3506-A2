// @edu:student-assignment

package uq.comp3506.a2.structures;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2025.
 * <p>
 * NOTE: You should go and carefully read the documentation provided in the
 * MapInterface.java file - this explains some of the required functionality.
 */
public class UnorderedMap<K, V> implements MapInterface<K, V> {


    /**
     * you will need to put some member variables here to track your
     * data, size, capacity, etc...
     */
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private ArrayList<LinkedList<Node<K, V>>> table;
    private int size;


    private static final class Node<K, V> {
        final K key;
        V value;
        Node(K k, V v) { this.key = k; this.value = v; }
    }

    /**
     * Constructs an empty UnorderedMap
     */
    public UnorderedMap() {
        this.table = new ArrayList<>(DEFAULT_CAPACITY);
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            table.add(new LinkedList<>());
        }
        this.size = 0;
    }

    private int indexFor(Object key, int capacity) {
        int h = key.hashCode();
        h ^= (h >>> 16);
        return Math.floorMod(h, capacity);
    }
    
    
    private Node<K, V> findNode(LinkedList<Node<K, V>> bucket, K key) {
        for (Node<K, V> n : bucket) {
            if (n.key.equals(key)) return n;
        }
        return null;
    }
    
    
    private void resizeIfNeeded() {
        int cap = table.size();
        if (size < (int) (cap * LOAD_FACTOR)) return;
        rehash(cap * 2);
    }
    
    
    private void rehash(int newCap) {
        ArrayList<LinkedList<Node<K, V>>> newTable = new ArrayList<>(newCap);
        for (int i = 0; i < newCap; i++) newTable.add(new LinkedList<>());
        for (LinkedList<Node<K, V>> bucket : table) {
            for (Node<K, V> n : bucket) {
                int idx = indexFor(n.key, newCap);
                newTable.get(idx).add(n);
            }
        }
        this.table = newTable;
    }

    /**
     * returns the size of the structure in terms of pairs
     * @return the number of kv pairs stored
     */
    @Override
    public int size() { 
        return size; 
    }

    /**
     * helper to indicate if the structure is empty or not
     * @return true if the map contains no key-value pairs, false otherwise.
     */
    @Override
    public boolean isEmpty() { 
        return size == 0; 
    }

    /**
     * Clears all elements from the map. That means, after calling clear(),
     * the return of size() should be 0, and the data structure should appear
     * to be "empty".
     */
    @Override
    public void clear() {
        int cap = DEFAULT_CAPACITY;
        this.table = new ArrayList<>(cap);
        for (int i = 0; i < cap; i++) table.add(new LinkedList<>());
        this.size = 0;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value
     * is replaced by the specified value.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the payload data value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no such key
     */
    @Override
    public V put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        int idx = indexFor(key, table.size());
        LinkedList<Node<K, V>> bucket = table.get(idx);
        Node<K, V> n = findNode(bucket, key);
        if (n != null) {              
            V old = n.value;
            n.value = value;
            return old;
        }
        bucket.add(new Node<>(key, value)); 
        size++;
        resizeIfNeeded();
        return null;
    }

    /**
     * Looks up the specified key in this map, returning its associated value
     * if such key exists.
     *
     * @param key the key with which the specified value is to be associated
     * @return the value associated with key, or null if there was no such key
     */
    @Override
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        int idx = indexFor(key, table.size());
        Node<K, V> n = findNode(table.get(idx), key);
        return (n == null) ? null : n.value;
    }

    /**
     * Looks up the specified key in this map, and removes the key-value pair
     * if the key exists.
     *
     * @param key the key with which the specified value is to be associated
     * @return the value associated with key, or null if there was no such key
     */
    @Override
    public V remove(K key) {
        if (key == null) throw new IllegalArgumentException("key must not be null");
        int idx = indexFor(key, table.size());
        LinkedList<Node<K, V>> bucket = table.get(idx);
        Iterator<Node<K, V>> it = bucket.iterator();
        while (it.hasNext()) {
            Node<K, V> n = it.next();
            if (n.key.equals(key)) {
                it.remove();
                size--;
                return n.value;
            }
        }
        return null;
    } 

}
