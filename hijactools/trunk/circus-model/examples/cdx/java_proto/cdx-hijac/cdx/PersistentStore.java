package cdx;

/* Generics do not seem to be supported in our RI implementation. Hence we may
 * need a version of this class for each class type whose objects have to be
 * shared, replacing T by the respective type in the definition of the class. */

class PersistentStore {
  /* Pool of pre-allocated objects. */
  private T[] pool;

  /* Index of the next object to be returned by T get(). */
  private int index;

  /* We assume the constructor is invoked in the memory area where the objects
   * are meant to reside. Its behaviour is to create all objects in the pool. */

  public PersistentStore(int size) {
    pool = new T[size];
    for (int i = 0; i < size; i++) {
      pool[i] = new T();
    }
  }  

  /* Get another preallocated object from the pool. */

  public T get() {
    if (!(index < pool.length)) {
      /* This should never happen in a verified program. */
      throw new AssertionError("PersistentStore running out of objects");
    }
    return pool[index++];
  }

  /* Throw all objects away and start allocating from scratch. */

  public void dispose() {
    index = 0;
  }
}

class T { } /* For testing only, remove. */
