//Rowen Stiles, May 2025, creating a dynamic array class like ArrayList
package lab7;

import java.util.NoSuchElementException;

/** An ArrayList-like dynamic array class that allocates
* new memory when needed */
public class AList<T> {

  protected int size; // number of elements in the AList
  protected T[] a; // the backing array storage

  public int size() {
    return size;
  }

  protected int getCap() {
    return a.length;
  }

  /** Creates an AList with a default capacity of 8 */
  public AList() {
    a = createArray(8);
    size = 0;
  }

  /** Creates an AList with the given capacity */
  public AList(int capacity) {
    a = createArray(capacity);
    size = 0;
  }

  /* Grows a to double its current capacity if newSize exceeds a's capacity. Does
  * nothing if newSize <= a.length.  Grow the array by allocating a new array
  * and copying the old array's contents into the new one. This does *not*
  * change the AList's size. */
  protected void growIfNeeded(int newSize) {
    if(newSize > getCap()){
      //calculate new cap
      int newCap = getCap();
      while(newSize > newCap){
        newCap *= 2;
      }

      //create new array with new cap
      T[] b = createArray(newCap);

      //copy old values over
      for(int i = 0; i < getCap(); i++){
        b[i] = a[i];
      }

      //assign a to the new array
      a = b;
    }
  }

  /** Resizes the AList.
  *  this *does* modify the size, and may modify the capacity if newsize
  *  exceeds capacity. */
  public void resize(int newsize) {
    growIfNeeded(newsize);
    size = newsize;
  }

  /** Gets element i from AList.
  * @throws ArrayIndexOutOfBoundsException if 0 <= i < size does not hold */
  public T get(int i) {
    if(i < 0 || i >= size){
      throw new ArrayIndexOutOfBoundsException();
    }
    return a[i];
  }

  /** Sets the ith element of the list to value.
  * @throws ArrayIndexOutOfBoundsException if 0 <= i < size does not hold */
  public void put(int i, T value) {
    if(i < 0 || i >= size){
      throw new ArrayIndexOutOfBoundsException();
    }
    a[i] = value;
  }

  /** Appends value at the end of the AList, increasing size by 1.
  * Grows the array if needed to fit the appended value */
  public void append(T value) {
    size += 1;
    resize(size);
    a[size - 1] = value;
  }

  /** Removes and returns the ENTRY (not value) at the end of the AList.  <- (This originally said value,)
  *  this *does* modify size and cannot modify capacity.                  (which is wrong, since it returns T)
  *  @throws NoSuchElementException if size == 0*/
  public T pop() {
    if(size == 0){
      throw new NoSuchElementException();
    }
    size -= 1;
    T temp = a[size];
    a[size] = null;
    return temp;
  }

  /*  Create and return a T[] of size n.
  *  This is necessary because generics and arrays don't play well together.*/
  @SuppressWarnings("unchecked")
  protected T[] createArray(int size) {
    return (T[]) new Object[size];
  }

}