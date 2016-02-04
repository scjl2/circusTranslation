package hijac.tools.scjmsafe.language.RefCon;

public class TPMMem extends RefCon {

    private int depth;

    public TPMMem(int n) {
        depth = n;
    }

    public int getDepth() {
        return depth;
    }

    public void printRefCon() {
        System.out.print("TPMMem(" + depth + ")");
    }

    public String getRefConString() {
        return ("TPMMem(" + depth + ")");
    }

    public boolean equalTo(RefCon arg) {
        if (arg instanceof TPMMem) {
            TPMMem temp = (TPMMem) arg;
            if (temp.getDepth() == depth) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public TPMMem clone() {
        return new TPMMem(depth);
    }

}
