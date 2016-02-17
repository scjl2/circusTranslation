package hijac.tools.scjmsafe.language.RefCon;

public class MMem extends RefCon {

    public MMem() {};

    public void printRefCon() {
        System.out.print("MMem");
    }

    public String getRefConString() {
        return "MMem";
    }

    public boolean equalTo(RefCon arg) {
        if (arg instanceof MMem) {
            return true;
        } else {
            return false;
        }
    }

    public MMem clone() {
        return new MMem();
    }

}
