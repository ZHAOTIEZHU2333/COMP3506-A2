/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2026.
 */

import uq.comp3506.a2.structures.Heap;
import uq.comp3506.a2.structures.Entry;

public class TestHeap {

    public static void main(String[] args) {
        System.out.println("Testing the Heap-based Priority Queue Class...");

        Heap<Integer, String> pq = new Heap<>();
        // Your code here

        assert pq.size() == 0 : "Initial size";
        assert pq.isEmpty() : "Initially empty";
        assert pq.peekMin() == null : "Peek empty heap";
        assert pq.removeMin() == null : "Remove from empty heap";

        pq.insert(5, "five");
        assert pq.size() == 1 : "Size after insertion";
        assert !pq.isEmpty() : "Heap should not be empty";
        assert pq.peekMin().getKey() == 5 : "Single element key";
        assert "five".equals(pq.peekMin().getValue()) : "Single element value";

        Entry<Integer, String> copy = pq.peekMin();
        copy.setValue("changed");
        assert "five".equals(pq.peekMin().getValue()) : "Peek must return a copy";
        assert pq.size() == 1 : "Peek must not remove";

        pq.insert(2, "two");
        pq.insert(new Entry<>(7, "seven"));
        pq.insert(-1, "negative");
        pq.insert(2, "another two");

        int[] expected = {-1, 2, 2, 5, 7};

        for (int i = 0; i < expected.length; i++) {
            Entry<Integer, String> removed = pq.removeMin();

            assert removed != null : "Unexpected null";
            assert removed.getKey() == expected[i] : "Incorrect removal order";
            assert pq.size() == expected.length - i - 1 : "Size after removal";
        }

        assert pq.isEmpty() : "Empty after removing all entries";
        assert pq.removeMin() == null : "Remove after draining";

        // Descending insertions exercise repeated upward movement and resizing.
        for (int i = 1000; i >= 0; i--) {
            pq.insert(i, "value-" + i);
        }

        for (int i = 0; i <= 1000; i++) {
            Entry<Integer, String> removed = pq.removeMin();

            assert removed.getKey() == i : "Bulk removal order";
            assert ("value-" + i).equals(removed.getValue())
                    : "Key and value must remain paired";
        }

        // Interleave insertions and removals.
        pq.insert(4, "four");
        pq.insert(8, "eight");
        assert pq.removeMin().getKey() == 4 : "Interleaved removal";

        pq.insert(1, "one");
        assert pq.removeMin().getKey() == 1 : "Insertion after removal";
        assert pq.removeMin().getKey() == 8 : "Remaining entry";

        pq.insert(10, "ten");
        pq.insert(20, "twenty");
        pq.clear();

        assert pq.size() == 0 : "Size after clear";
        assert pq.isEmpty() : "Empty after clear";
        assert pq.peekMin() == null : "Peek after clear";

        pq.insert(3, "three");
        assert pq.removeMin().getKey() == 3 : "Reuse after clear";
        assert pq.isEmpty() : "Empty after reuse";

        System.out.println("Heap: all basic tests passed");
    }
}