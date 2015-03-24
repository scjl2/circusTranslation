package hijac.tools.scjmsafe.language.RefCon;

public class Prim extends RefCon {

    public Prim() {};

    public void printRefCon() {
        System.out.print("Prim");
    }

    public String getRefConString() {
        return "Prim";
    }

    public boolean equalTo(RefCon arg) {
        if (arg instanceof Prim) {
            return true;
        } else {
            return false;
        }
    }

    public Prim clone() {
        return new Prim();
    }

}
