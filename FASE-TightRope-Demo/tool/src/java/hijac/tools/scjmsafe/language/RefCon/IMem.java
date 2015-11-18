package hijac.tools.scjmsafe.language.RefCon;

public class IMem extends RefCon {

    public IMem() {};

    public void printRefCon() {
        System.out.print("IMem");
    }

    public String getRefConString() {
        return "IMem";
    }

    public boolean equalTo(RefCon arg) {
        if (arg instanceof IMem) {
            return true;
        } else {
            return false;
        }
    }

    public IMem clone() {
        return new IMem();
    }

}
