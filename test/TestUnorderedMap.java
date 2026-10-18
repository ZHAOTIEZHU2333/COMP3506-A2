/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2026.
 */

import uq.comp3506.a2.structures.UnorderedMap;

public class TestUnorderedMap {

    public static void main(String[] args) {
        System.out.println("Testing the UnorderedMap Class...");

        UnorderedMap<Integer, String> map = new UnorderedMap<>();
        // Your code here

        assert map.size() == 0 : "Initial size";
        assert map.isEmpty() : "Initially empty";
        assert map.get(99) == null : "Get missing key";
        assert map.remove(99) == null : "Remove missing key";

        assert map.put(1, "one") == null : "New key returns null";
        assert map.size() == 1 : "Size after insertion";
        assert "one".equals(map.get(1)) : "Get existing key";

        assert "one".equals(map.put(1, "updated")) : "Update returns old value";
        assert map.size() == 1 : "Update must not increase size";
        assert "updated".equals(map.get(1)) : "Updated value";

        map.put(-10, "negative");
        map.put(Integer.MIN_VALUE, "minimum");
        assert "negative".equals(map.get(-10)) : "Negative key";
        assert "minimum".equals(map.get(Integer.MIN_VALUE)) : "Minimum int key";

        assert "updated".equals(map.remove(1)) : "Remove returns old value";
        assert map.get(1) == null : "Removed key should be absent";
        assert map.remove(1) == null : "Repeated removal";
        assert map.size() == 2 : "Size after removal";

        map.clear();
        assert map.isEmpty() : "Empty after clear";
        assert map.size() == 0 : "Size after clear";
        assert map.get(-10) == null : "Old entries removed";

        // Enough entries to trigger several resizes.
        for (int i = 0; i < 5000; i++) {
            assert map.put(i, "value-" + i) == null : "Bulk insertion";
        }

        assert map.size() == 5000 : "Bulk size";

        for (int i = 0; i < 5000; i++) {
            assert ("value-" + i).equals(map.get(i)) : "Lookup after resizing";
        }

        for (int i = 0; i < 5000; i += 2) {
            assert ("value-" + i).equals(map.remove(i)) : "Bulk removal";
        }

        assert map.size() == 2500 : "Size after removing even keys";

        for (int i = 0; i < 5000; i++) {
            if (i % 2 == 0) {
                assert map.get(i) == null : "Even key should be absent";
            } else {
                assert ("value-" + i).equals(map.get(i))
                        : "Odd key should remain";
            }
        }

        // All these distinct keys intentionally have the same hash code.
        UnorderedMap<CollisionKey, String> collisions = new UnorderedMap<>();

        for (int i = 0; i < 100; i++) {
            collisions.put(new CollisionKey(i), "item-" + i);
        }

        for (int i = 0; i < 100; i++) {
            assert ("item-" + i).equals(collisions.get(new CollisionKey(i)))
                    : "Lookup with collisions and equal but distinct objects";
        }

        assert "item-50".equals(collisions.put(new CollisionKey(50), "updated"))
                : "Update in collision chain";
        assert collisions.size() == 100 : "Collision update size";

        assert "item-0".equals(collisions.remove(new CollisionKey(0)))
                : "Remove colliding key 0";
        assert "updated".equals(collisions.remove(new CollisionKey(50)))
                : "Remove colliding key 50";
        assert "item-99".equals(collisions.remove(new CollisionKey(99)))
                : "Remove colliding key 99";

        assert collisions.size() == 97 : "Collision removal size";

        for (int i = 0; i < 100; i++) {
            if (i == 0 || i == 50 || i == 99) {
                assert collisions.get(new CollisionKey(i)) == null
                        : "Deleted colliding key";
            } else {
                assert ("item-" + i).equals(collisions.get(new CollisionKey(i)))
                        : "Other colliding entries must remain";
            }
        }

        collisions.clear();
        collisions.put(new CollisionKey(7), "reused");
        assert "reused".equals(collisions.get(new CollisionKey(7)))
                : "Reuse after clear";

        System.out.println("UnorderedMap: all basic tests passed");
    }

    /** A test key that deliberately causes hash collisions. */
    private static class CollisionKey {

        private final int id;

        private CollisionKey(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return Integer.MIN_VALUE;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof CollisionKey)) {
                return false;
            }

            return id == ((CollisionKey) other).id;
        }
    }
}