package hijac.tools.scjmsafe.language.RefCon;

public class CurrentPrivate extends MetaRefCon {

    private int depth;

    public CurrentPrivate(int i) {
        depth = i;
    }

    public void setDepth(int i) {
        depth = i;
    }

    public int getDepth() {
        return depth;
    }

    public void printMetaRefCon() {
        System.out.print("CurrentPriv(" + depth + ")");
    }

    public String getMetaRefConString() {
        return "CurrentPriv(" + depth + ")";
    }

    public boolean equalTo(MetaRefCon arg) {
        if (arg instanceof CurrentPrivate) {
            CurrentPrivate temp = (CurrentPrivate) arg;
            if (temp.getDepth() == depth) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public CurrentPrivate clone() {
        return new CurrentPrivate(depth);
    }

}
