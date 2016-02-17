package hijac.tools.scjmsafe.language.Method;

import java.util.HashSet;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.language.RefCon.*;

public class MethodRef {

    private LExpr le;
    private HashSet<MetaRefCon> metaRefCons;

    public void MethodRef(LExpr lexpr, HashSet<MetaRefCon> mrcs) {
        le = lexpr;
        metaRefCons = mrcs;
    }

}
