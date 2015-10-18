package javax.safetycritical.annotate;

public enum Restrict {
  MAY_ALLOCATE,
  MAY_BLOCK,
  BLOCK_FREE,
  ALLOCATE_FREE,
  INITIALIZE,
  CLEANUP,
  INTERRUPT_SAFE,
  ANY_TIME
}
