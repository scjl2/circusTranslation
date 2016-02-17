package hijac.tools.scjmsafe.language.RefCon;

import hijac.tools.scjmsafe.checker.CheckingUtil;

import java.util.HashSet;

public class Rcs extends MetaRefCon {

    private HashSet<RefCon> rcs;

    public Rcs(HashSet<RefCon> setRefCon) {
        rcs = setRefCon;
    }

    public void addRefCon(RefCon rc) {
        rcs.add(rc);
    }

    public HashSet<RefCon> getRefCons() {
        return rcs;
    }

    public void printMetaRefCon() {
        System.out.print("Rcs {");
        int count = 0;
        for (RefCon rc : rcs) {
            rc.printRefCon();
            if (count+1 < rcs.size()) {
                System.out.print(", ");
            }
            count++;
        }
        System.out.print("}");
    }

    public String getMetaRefConString() {
        String result = "Rcs {";
        int count = 0;
        for (RefCon rc : rcs) {
            result += rc.getRefConString();
            if (count+1 < rcs.size()) {
                result += ", ";
            }
            count++;
        }
        result += "}";
        return result;
    }

    public boolean equalTo(MetaRefCon arg) {
        if (arg instanceof Rcs) {
            Rcs temp = (Rcs) arg;
            if (rcs.size() == temp.getRefCons().size()) {
                if (CheckingUtil.refConSetsEqual(rcs, temp.getRefCons())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Rcs clone() {
        return new Rcs(rcs);
    }

}
