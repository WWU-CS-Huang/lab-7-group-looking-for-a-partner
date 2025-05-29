package lab7;

/*
 * Author: Rowen Stiles
 * Date: May 2025
 * Purpose: Implementing a Heap and HashTable
 */

/** A hash table modeled after java.util.Map. It uses chaining for collision
 * resolution and grows its underlying storage by a factor of 2 when the load
 * factor exceeds 0.8. */
public class HashTable<K,V> {

    protected Pair[] buckets; // array of list nodes that store K,V pairs
    protected int size; // how many items currently in the map


    /** class Pair stores a key-value pair and a next pointer for chaining
     * multiple values together in the same bucket, linked-list style*/
    public class Pair {
        protected K key;
        protected V value;
        protected Pair next;

        /** constructor: sets key and value */
        public Pair(K k, V v) {
            key = k;
            value = v;
            next = null;
        }

        /** constructor: sets key, value, and next */
        public Pair(K k, V v, Pair nxt) {
            key = k;
            value = v;
            next = nxt;
        }

        /** returns (k, v) String representation of the pair */
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    /** constructor: initialize with default capacity 17 */
    public HashTable() {
        this(17);
    }

    /** constructor: initialize the given capacity */
    public HashTable(int capacity) {
        buckets = createBucketArray(capacity);
    }

    /** Return the size of the map (the number of key-value mappings in the
     * table) */
    public int getSize() {
        return size;
    }

    /** Return the current capacity of the table (the size of the buckets
     * array) */
    public int getCapacity() {
        return buckets.length;
    }

    /** Return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * Runtime: average case O(1); worst case O(size) */
    public V get(K key) {
        int hashedKey = key.hashCode();
        int module = getCapacity();
        //if hashedKey is negative, turn that frown upside down
        if(hashedKey < 0){
            hashedKey *= -1;
        }
        //use the key to only search the bucket that the node should be in
        int position = hashedKey % module;
        Pair node = buckets[position];
        while (node != null) {
            if(node.key == key){
                return node.value;
            }
            node = node.next;
        }
        //if not found, return null
        return null;
    }

    /** Associate the specified value with the specified key in this map. If
     * the map previously contained a mapping for the key, the old value is
     * replaced. Return the previous value associated with key, or null if
     * there was no mapping for key. If the load factor exceeds 0.8 after this
     * insertion, grow the array by a factor of two and rehash.
     * Precondition: val is not null.
     * Runtime: average case O(1); worst case O(size + a.length)*/
    public V put(K key, V val) {
        int hashedKey = key.hashCode();
        int module = getCapacity();
        //if hashedKey is negative, turn that frown upside down
        if(hashedKey < 0){
            hashedKey *= -1;
        }
        int position = hashedKey % module;

        Pair node = buckets[position];
        //case 1: if the first node is null, replace with new node and increase size
        if(node == null){
            buckets[position] = new Pair(key, val);
            size++;
            growIfNeeded();
            return null;
        }
        //case 2: if the first node is a duplicate, replace, pointing to next node
        else if(node.key == key){
            V temp = node.value;
            buckets[position] = new Pair(key, val, node.next);
            return temp;
        }
        else{
            //case 3: checks all nodes in the bucket to see if they're a duplicate;
            //if so, replace with new node, pointing to the next node that would follow
            while(node.next != null){
                if(node.next.key == key){
                    V temp = node.next.value;
                    node.next = new Pair(key, val, node.next.next);
                    return temp;
                }
                node = node.next;
            }
            //case 4: after final node has been investigated with no
            //duplicates, add new node to end and increase size
            if(node.next == null){
                node.next = new Pair(key, val);
                size++;
                growIfNeeded();
                return null;
            }
        }
        //case 0: if all else fails for some reason, return null
        return null;
    }

    /** Return true if this map contains a mapping for the specified key.
     *  Runtime: average case O(1); worst case O(size) */
    public boolean containsKey(K key) {
        int hashedKey = key.hashCode();
        int module = getCapacity();
        //if hashedKey is negative, turn that frown upside down
        if(hashedKey < 0){
            hashedKey *= -1;
        }
        //use the key to only search the bucket that the node should be in
        int position = hashedKey % module;
        Pair node = buckets[position];
        while (node != null) {
            if(node.key == key){
                return true;
            }
            node = node.next;
        }
        //if not found, return false
        return false;
    }

    /** Remove the mapping for the specified key from this map if present.
     *  Return the previous value associated with key, or null if there was no
     *  mapping for key.
     *  Runtime: average case O(1); worst case O(size)*/
    public V remove(K key) {
        int hashedKey = key.hashCode();
        int module = getCapacity();
        //if hashedKey is negative, turn that frown upside down
        if(hashedKey < 0){
            hashedKey *= -1;
        }
        //use the key to only search the bucket that the node should be in, and if found, 
        //remove, reassigning pointers and positions as needed, and return former value
        int position = hashedKey % module;
        Pair node = buckets[position];

        //if the first node is null, skip all of this
        if(node != null){
            //case 1: single node without a child
            if(node.next == null && node.key == key){
                V temp = node.value;
                buckets[position] = null;
                size--;
                return temp;
            }
            //case 2: first node is parent
            else if(node.next != null && node.key == key){
                V temp = node.value;
                buckets[position] = new Pair(node.next.key, node.next.value, node.next.next);
                size--;
                return temp;
            }
            else{
                //case 3: checks all nodes in the bucket; if found, replace with following node,
                //acting like the targeted node was never there to begin with
                while(node.next != null){
                    if(node.next.key == key){
                        V temp = node.next.value;
                        node.next = node.next.next;
                        size--;
                        return temp;
                    }
                    node = node.next;
                }
            }
        }
        //case 0: if it's not found anywhere, then return null
        return null;
    }


    // suggested helper method:
    /* check the load factor; if it exceeds 0.8, double the capacity 
     * and rehash values from the old array to the new array */
    private void growIfNeeded() {
        double loadFactor = (double) getSize() / getCapacity();
        if(loadFactor > 0.8){
            HashTable<K,V>.Pair[] tempBuckets = buckets;
            buckets = createBucketArray(getCapacity() * 2);
            size = 0;
            //use the tempBuckets to re-fill the newly emptied buckets
            for (int i = 0; i < tempBuckets.length; i++) {
                Pair node = tempBuckets[i];
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    /* useful method for debugging - prints a representation of the current
     * state of the hash table by traversing each bucket and printing the
     * key-value pairs in linked-list representation */
    protected void dump() {
        System.out.println("Table size: " + getSize() + " capacity: " +
                getCapacity());
        for (int i = 0; i < buckets.length; i++) {
            System.out.print(i + ": --");
            Pair node = buckets[i];
            while (node != null) {
                System.out.print(">" + node + "--");
                node = node.next;

            }
            System.out.println("|");
        }
    }

    /*  Create and return a bucket array with the specified size, initializing
     *  each element of the bucket array to be an empty LinkedList of Pairs.
     *  The casting and warning suppression is necessary because generics and
     *  arrays don't play well together.*/
    @SuppressWarnings("unchecked")
    protected Pair[] createBucketArray(int size) {
        return (Pair[]) new HashTable<?,?>.Pair[size];
    }
}
