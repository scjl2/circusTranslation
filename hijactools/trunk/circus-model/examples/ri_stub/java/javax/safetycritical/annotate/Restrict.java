package javax.safetycritical.annotate;

public enum Restrict {
   MAY_ALLOCATE,
   MAY_BLOCK,
   BLOCK_FREE,
   INITIALIZATION,
   CLEANUP,
   ANY_TIME
}
