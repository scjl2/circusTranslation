package cdx;

/**
 * This Runnable enters the StateTable in order to allocate the callsign in
 * the PersistentScope
 */
class TransientDetectorScopeEntry_R implements Runnable {
    CallSign c;
    byte[] cs;

    public void run() {
        c = new CallSign(cs);
    }
}
