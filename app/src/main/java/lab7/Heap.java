package lab7;

/*
 * Author: Rowen Stiles
 * Date: May 2025
 * Purpose: Implementing a Heap and HashTable
 * 
 * Edit: Added a method to return the priority of
 * a value to then be altered.
 */
import java.util.NoSuchElementException;

/** An instance is a min-heap of distinct values of type V with
 *  priorities of type P. Since it's a min-heap, the value
 *  with the smallest priority is at the root of the heap. */
public final class Heap<V, P extends Comparable<P>> {

    /**
     * The contents of c represent a complete binary tree. We use square-bracket
     * shorthand to denote indexing into the AList (which is actually
     * accomplished using its get method. In the complete tree,
     * c[0] is the root; c[2i+1] is the left child of c[i] and c[2i+2] is the
     * right child of i.  If c[i] is not the root, then c[(i-1)/2] (using
     * integer division) is the parent of c[i].
     *
     * Class Invariants:
     *
     *   The tree is complete:
     *     1. `c[0..c.size()-1]` are non-null
     *
     *   The tree satisfies the heap property:
     *     2. if `c[i]` has a parent, then `c[i]`'s parent's priority
     *        is smaller than `c[i]`'s priority
     *
     *   In Phase 3, the following class invariant also must be maintained:
     *     3. The tree cannot contain duplicate *values*; note that dupliate
     *        *priorities* are still allowed.
     *     4. map contains one entry for each element of the heap, so
     *        map.size() == c.size()
     *     5. For each value v in the heap, its map entry contains in the
     *        the index of v in c. Thus: map.get(c[i]) = i.
     */
    protected AList<Entry> c;
    protected HashTable<V, Integer> map;

    /** Constructor: an empty heap with capacity 10. */
    public Heap() {
        c = new AList<Entry>(10);
        map = new HashTable<V, Integer>();
    }

    /** An Entry contains a value and a priority. */
    class Entry {
        public V value;
        public P priority;

        /** An Entry with value v and priority p*/
        Entry(V v, P p) {
            value = v;
            priority = p;
        }

        public String toString() {
            return value.toString();
        }
    }

    /** Add v with priority p to the heap.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap. Precondition: p is not null.
     *  In Phase 3 only:
     *  @throws IllegalArgumentException if v is already in the heap.*/
    public void add(V v, P p) throws IllegalArgumentException {
        if(map.get(v) != null){
            throw new IllegalArgumentException();
        }
        Entry entry = new Entry(v, p);
        c.append(entry);
        int indexOfEntry = size() - 1;
        map.put(v, indexOfEntry);
        //if child's index is less than parent's, bubble
        if(c.get(indexOfEntry).priority.compareTo(c.get((indexOfEntry - 1)/2).priority) < 0){
            bubbleUp(indexOfEntry);
        }
    }

    /** Return the number of values in this heap.
     *  This operation takes constant time. */
    public int size() {
        return c.size();
    }

    /** Swap c[h] and c[k].
     *  precondition: h and k are >= 0 and < c.size() */
    protected void swap(int h, int k) {
        //remove each from map first
        map.remove(c.get(h).value);
        map.remove(c.get(k).value);
        //then replace onto map after replacing into heap
        Entry temp = c.get(h);
        c.put(h, c.get(k));
        map.put(c.get(k).value, h);
        c.put(k, temp);
        map.put(temp.value, k);
    }

    /** Bubble c[k] up in heap to its right place.
     *  Precondition: Priority of every c[i] >= its parent's priority
     *                except perhaps for c[k] */
    protected void bubbleUp(int k) {
        swap((k - 1)/2, k);
        k = (k - 1)/2;
        //if no parent (k is at top)
        if(k == 0){
            return;
        }
        //if child is smaller than parent
        else if(c.get(k).priority.compareTo(c.get((k - 1)/2).priority) < 0){
            bubbleUp(k);
        }
    }

    /** Return the value of this heap with lowest priority. Do not
     *  change the heap. This operation takes constant time.
     *  @throws NoSuchElementException if the heap is empty. */
    public V peek() throws NoSuchElementException {
        if(c.size() == 0){
            throw new NoSuchElementException();
        }
        return c.get(0).value;
    }

    /** Remove and return the element of this heap with lowest priority.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws NoSuchElementException if the heap is empty. */
    public V poll() throws NoSuchElementException {
        //if no elements, do nothing
        if(c.size() == 0){
            throw new NoSuchElementException();
        }
        //set temp for later return value
        Entry temp = c.get(0);
        if(c.size() > 1){
            map.remove(c.get(0).value);
            c.put(0, c.pop());
            map.put(c.get(0).value, 0);
            
            //if left child isn't null (within size range) and parent is greater than left child
            //OR right child isn't null (within size range) and parent is greater than right child
            if(((1 <= c.size() - 1) && c.get(0).priority.compareTo(c.get(1).priority) > 0)
            || ((2 <= c.size() - 1) && c.get(0).priority.compareTo(c.get(2).priority) > 0)){
                bubbleDown(0);
            }
        }
        //if there's only one value, then just remove it, no need to do extra work
        else {
            map.remove(c.pop().value);
        }
        return temp.value;
    }

    /** Bubble c[k] down in heap until it finds the right place.
     *  If there is a choice to bubble down to both the left and
     *  right children (because their priorities are equal), choose
     *  the right child.
     *  Precondition: Each c[i]'s priority <= its childrens' priorities
     *                except perhaps for c[k] */
    protected void bubbleDown(int k) {
        int indexOfSmaller = smallerChild(k);
        swap(indexOfSmaller, k);
        k = indexOfSmaller;
        //if left child exists (within size range)
        if(((2 * k) + 1) <= c.size() - 1){
            //if parent is greater than left child
            //OR right child exists (within size range) and parent is greater than right child, bubble
            if((c.get(k).priority.compareTo(c.get((2 * k) + 1).priority) > 0)
            || (((2 * k) + 2) <= (c.size() - 1) && c.get(k).priority.compareTo(c.get((2 * k) + 2).priority) > 0)){
                bubbleDown(k);
            }
        }
    }

    /** Return true if the value v is in the heap, false otherwise.
     *  The average case runtime is O(1).  */
    public boolean contains(V v) {
        if(map.containsKey(v)){
            return true;
        }
        else {
            return false;
        }
    }

    /** Change the priority of value v to p.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws IllegalArgumentException if v is not in the heap. */
    public void changePriority(V v, P p) throws IllegalArgumentException {
        Integer indexToUpdate = map.get(v);
        if(indexToUpdate != null){
            Entry needsUpdate = c.get(indexToUpdate);
            needsUpdate.priority = p;
            //if parent exists and the node is less than its parent's priority, bubble up
            if(c.get((indexToUpdate - 1)/2) != null && c.get(indexToUpdate).priority.compareTo(c.get((indexToUpdate - 1)/2).priority) < 0){
                bubbleUp(indexToUpdate);
            }
            //if left child isn't null (within size range) and node is greater than left child
            //OR right child isn't null (within size range) and node is greater than right child
            else if((((2 * indexToUpdate) + 1) <= c.size() - 1 && c.get(indexToUpdate).priority.compareTo(c.get((2 * indexToUpdate) + 1).priority) > 0)
            || (((2 * indexToUpdate) + 2) <= c.size() && c.get(indexToUpdate).priority.compareTo(c.get((2 * indexToUpdate) + 2).priority) > 0)){
                bubbleDown(indexToUpdate);
            }
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    // Recommended helper method spec:
    /* Return the index of the child of k with smaller priority.
     * if only one child exists, return that child's index
     * Precondition: at least one child exists.*/
    private int smallerChild(int k) {
        //if right child doesn't exist (out of size range), return left's index  
        if(((2 * k) + 2) > (c.size() - 1)){
            return (2 * k) + 1;
        }
        //if left child's priority is less than right child's, return left's index
        else if(c.get((2 * k) + 1).priority.compareTo(c.get((2 * k) + 2).priority) < 0){
            return (2 * k) + 1;
        //else, return right's index
        } else {
            return (2 * k) + 2;
        }
    }

    //NEW: Return priority of a key.
    //Precon: v exists/isn't null
    public P getPriority(V v){
        return c.get(map.get(v)).priority;
    }
}