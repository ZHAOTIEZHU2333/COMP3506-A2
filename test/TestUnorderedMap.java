/**
 * Supplied by the COMP3506/7505 teaching team, Semester 2, 2025.
 */

import uq.comp3506.a2.structures.UnorderedMap;

public class TestUnorderedMap {

    static final class BadKey {
        final int id;
        BadKey(int id) { this.id = id; }
        @Override public int hashCode() { return 1; }
        @Override public boolean equals(Object o) {
            return (o instanceof BadKey) && ((BadKey) o).id == this.id;
        }
        @Override public String toString() { return "K(" + id + ")"; }
    }

    public static void main(String[] args) {
        System.out.println("Testing the UnorderedMap Class...");
     
        
        {
            UnorderedMap<Integer, String> map = new UnorderedMap<>();
        
            // 初始状态
            assert map.isEmpty() : "new map should be empty";
            assert map.size() == 0 : "size should be 0";
        
            // put 新键
            String old = map.put(1, "a");
            assert old == null : "put new key should return null (no old value)";
            assert map.size() == 1 : "size should be 1 after one put";
            assert "a".equals(map.get(1)) : "get(1) should be 'a'";
        
            // 覆盖已有键
            old = map.put(1, "A");
            assert "a".equals(old) : "put existing key should return old value 'a'";
            assert "A".equals(map.get(1)) : "value should be updated to 'A'";
            assert map.size() == 1 : "size unchanged when overriding same key";
        
            // 删除存在的键
            String removed = map.remove(1);
            assert "A".equals(removed) : "remove should return last value 'A'";
            assert map.size() == 0 && map.isEmpty() : "map should be empty after remove";
        
            // 删除不存在的键
            assert map.remove(2) == null : "remove non-existent key returns null";
        
            // get 不存在的键
            assert map.get(999) == null : "get of missing key returns null";
        
            // null key 行为：应抛 IllegalArgumentException（和你的实现保持一致）
            boolean threw = false;
            try { map.put(null, "x"); } catch (IllegalArgumentException e) { threw = true; }
            assert threw : "put(null, ...) should throw IllegalArgumentException";
        }
        
        // —— 扩容/rehash 压力测试（不会依赖具体容量，随便多放一些即可） ——
        {
            UnorderedMap<Integer, Integer> map = new UnorderedMap<>();
            final int N = 1000; // 放大到 1000，足以触发多次扩容
            for (int i = 0; i < N; i++) {
                Integer prev = map.put(i, i * 10);
                assert prev == null : "first insertion should return null";
            }
            assert map.size() == N : "size should be " + N;
        
            // 抽查若干点的正确性
            assert map.get(0) == 0 : "get(0) should be 0";
            assert map.get(1) == 10 : "get(1) should be 10";
            assert map.get(123) == 1230 : "get(123) should be 1230";
            assert map.get(N - 1) == (N - 1) * 10 : "tail check";
        
            // 删除一部分，再检查 size 与缺失
            int removedCount = 0;
            for (int i = 0; i < N; i += 3) {
                Integer val = map.remove(i);
                assert val != null : "removing existing key should return its value";
                removedCount++;
            }
            assert map.size() == (N - removedCount) : "size after removals should match";
        
            // 被删的键应当拿不到
            assert map.get(0) == null : "removed key should be absent";
            assert map.get(3) == null : "removed key should be absent";
            // 未删的键仍可取
            assert map.get(1) == 10 : "key 1 should still exist";
            assert map.get(2) == 20 : "key 2 should still exist";
        }
        
        // —— 碰撞处理测试：所有键同一个哈希桶（分离链表正确性） ——
        {
            UnorderedMap<BadKey, String> map = new UnorderedMap<>();
            BadKey k1 = new BadKey(1);
            BadKey k2 = new BadKey(2);
            BadKey k3 = new BadKey(3);
        
            assert map.put(k1, "v1") == null;
            assert map.put(k2, "v2") == null;
            assert map.put(k3, "v3") == null;
            assert map.size() == 3;
        
            assert "v1".equals(map.get(new BadKey(1))) : "equals 应仅按 id 判断";
            assert "v2".equals(map.get(new BadKey(2)));
            assert "v3".equals(map.get(new BadKey(3)));
        
            // 覆盖碰撞桶中的中间节点
            String old = map.put(new BadKey(2), "V2");
            assert "v2".equals(old);
            assert "V2".equals(map.get(k2));
        
            // 按不同顺序删除，确保链表链接正确
            assert "V2".equals(map.remove(new BadKey(2)));
            assert map.get(new BadKey(2)) == null;
            assert "v1".equals(map.remove(k1));
            assert "v3".equals(map.remove(k3));
            assert map.isEmpty();
        }
        
        System.out.println("All UnorderedMap tests passed!");
        

    }
}
