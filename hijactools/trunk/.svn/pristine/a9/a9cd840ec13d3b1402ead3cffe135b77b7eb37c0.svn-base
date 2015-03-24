package hijac.tools.scjmsafe.language.RefCon;

public class CurrentPlus extends MetaRefCon {

    private int depth;

    public CurrentPlus(int i) {
        depth = i;
    }

    public void setDepth(int i) {
        depth = i;
    }

    public int getDepth() {
        return depth;
    }

    public void printMetaRefCon() {
        System.out.print("CurrentPlus(" + depth + ")");
    }

    public String getMetaRefConString() {
        return "CurrentPlus(" + depth + ")";
    }

    public boolean equalTo(MetaRefCon arg) {
        if (arg instanceof CurrentPlus) {
            CurrentPlus temp = (CurrentPlus) arg;
            if (temp.getDepth() == depth) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public CurrentPlus clone() {
        return new CurrentPlus(depth);
    }

}
