
// @edu:student-assignment

package uq.comp3506.a2.structures;

/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2026.
 * <p>
 * NOTE: You should go and carefully read the documentation provided in the
 * MapInterface.java file - this explains some of the required functionality.
 */
public class UnorderedMap<K, V> implements MapInterface<K, V> {


    /**
     * you will need to put some member variables here to track your
     * data, size, capacity, etc...
     */
    private Node<K, V>[] buckets;

    /** Number of key-value pairs stored in the map. */
    private int size;

    /** Initial number of buckets. */
    private static final int INITIAL_CAPACITY = 16;

    /** Maximum load factor before growing the bucket array. */
    private static final double MAX_LOAD_FACTOR = 0.75;

    /**
     * Stores one key-value pair in a bucket chain.
     *
     * @param <K> the key type
     * @param <V> the value type
     */
    private static class Node<K, V> {

        /** The key identifying this entry. */
        private final K key;

        /** The value associated with this key. */
        private V value;

        /** The next entry in the same bucket. */
        private Node<K, V> next;

        /**
         * Constructs a node.
         *
         * @param key the entry key
         * @param value the entry value
         * @param next the next node in the chain
         */
        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    /**
     * Constructs an empty UnorderedMap
     */
    public UnorderedMap() {
        buckets = createBuckets(INITIAL_CAPACITY);
        size = 0;
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
        buckets = createBuckets(INITIAL_CAPACITY);
        size = 0;
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
        if (value == null) {
            throw new IllegalArgumentException("Null values are not supported");
        }

        int index = bucketIndex(key, buckets.length);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (keysEqual(current.key, key)) {
                V previousValue = current.value;
                current.value = value;
                return previousValue;
            }

            current = current.next;
        }

        if (size + 1 > buckets.length * MAX_LOAD_FACTOR) {
            resize();
            index = bucketIndex(key, buckets.length);
        }

        buckets[index] = new Node<>(key, value, buckets[index]);
        size++;
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
        int index = bucketIndex(key, buckets.length);
        Node<K, V> current = buckets[index];

        while (current != null) {
            if (keysEqual(current.key, key)) {
                return current.value;
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Looks up the specified key in this map, and removes the key-value pair
     * if the key exists.
     *
     * @param key   the key with which the specified value is to be associated
     * @return the value associated with key, or null if there was no such key
     */
    @Override
    public V remove(K key) {
        int index = bucketIndex(key, buckets.length);
        Node<K, V> current = buckets[index];
        Node<K, V> previous = null;

        while (current != null) {
            if (keysEqual(current.key, key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                size--;
                return current.value;
            }

            previous = current;
            current = current.next;
        }

        return null;
    }

    // More helpers here if you need

    /**
     * Creates an empty array of bucket heads.
     *
     * @param capacity the number of buckets
     * @return the new bucket array
     */
    @SuppressWarnings("unchecked")
    private Node<K, V>[] createBuckets(int capacity) {
        return (Node<K, V>[]) new Node<?, ?>[capacity];
    }

    /**
     * Calculates a valid bucket index for a key.
     *
     * @param key the key to hash
     * @param capacity the number of buckets
     * @return the bucket index
     */
    private int bucketIndex(K key, int capacity) {
        int hash = key == null ? 0 : key.hashCode();
        hash ^= hash >>> 16;
        return (hash & 0x7fffffff) % capacity;
    }

    /**
     * Checks whether two keys are equal.
     *
     * @param first the first key
     * @param second the second key
     * @return true if the keys are equal
     */
    private boolean keysEqual(K first, K second) {
        return first == second || (first != null && first.equals(second));
    }

    /**
     * Doubles the bucket array and redistributes existing entries.
     */
    private void resize() {
        Node<K, V>[] newBuckets = createBuckets(buckets.length * 2);

        for (int i = 0; i < buckets.length; i++) {
            Node<K, V> current = buckets[i];

            while (current != null) {
                Node<K, V> next = current.next;
                int index = bucketIndex(current.key, newBuckets.length);

                current.next = newBuckets[index];
                newBuckets[index] = current;
                current = next;
            }
        }

        buckets = newBuckets;
    }

}