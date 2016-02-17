package hijac.tools.scjmsafe.language.RefCon;

import hijac.tools.scjmsafe.language.MSafeHandler;

public class TPMem extends RefCon {

    private MSafeHandler handler;
    private int depth;

    public TPMem(MSafeHandler h, int n) {
        handler = h;
        depth = n;
    }

    public MSafeHandler getHandler() {
        return handler;
    }

    public int getDepth() {
        return depth;
    }

    public void printRefCon() {
        System.out.print("TPMem(" + handler.getName() + ", " + depth + ")");
    }

    public String getRefConString() {
        return ("TPMem(" + handler.getName() + ", " + depth + ")");
    }

    public boolean equalTo(RefCon arg) {
        if (arg instanceof TPMem) {
            TPMem temp = (TPMem) arg;
            if (temp.getHandler().getName().equals(handler.getName()) && temp.getDepth() == depth) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public TPMem clone() {
        return new TPMem(handler, depth);
    }


}
