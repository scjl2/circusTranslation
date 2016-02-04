package hijac.tools.scjmsafe.language.RefCon;

import hijac.tools.scjmsafe.language.MSafeHandler;

public class PRMem extends RefCon {

    private MSafeHandler handler;

    public PRMem(MSafeHandler h) {
        handler = h;
    }

    public MSafeHandler getHandler() {
        return handler;
    }

    public void printRefCon() {
        System.out.print("PRMem(" + handler.getName() + ")");
    }


    public String getRefConString() {
        return ("PRMem(" + handler.getName() + ")");
    }

    public boolean equalTo(RefCon arg) {
        if (arg instanceof PRMem) {
            PRMem temp = (PRMem) arg;
            if (temp.getHandler().getName().equals(handler.getName())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public PRMem clone() {
        return new PRMem(handler);
    }

}
