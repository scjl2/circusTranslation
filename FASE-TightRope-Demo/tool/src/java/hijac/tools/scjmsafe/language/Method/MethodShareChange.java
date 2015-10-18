package hijac.tools.scjmsafe.language.Method;

import java.util.HashSet;
import java.util.Iterator;

import hijac.tools.scjmsafe.language.Expressions.*;
import hijac.tools.scjmsafe.checker.environment.Share;

public class MethodShareChange {

    private HashSet<Share> shareRel;

    public MethodShareChange() {
        shareRel = new HashSet<Share>(0);
    }

    public MethodShareChange(HashSet<Share> rel) {
        shareRel = rel;
    }

    public void addShare(Share share) {
        shareRel.add(share);
    }

    public HashSet<Share> getShareRel() {
        return shareRel;
    }

    public void removeExprFromShares(Expr expr) {
        Iterator<Share> sharesIt = shareRel.iterator();
        while (sharesIt.hasNext()) {
            Share s = sharesIt.next();
            if (s.containsExpr(expr)) {
                sharesIt.remove();
            }
        }
    }

    public Share getShare(Expr e) {
        Share result = null;
        for (Share s : shareRel) {
            if (s.getLExpr().getExpressionString().equals(e.getExpressionString())) {
                result = s;
            }
        }
        return result;
    }

    public MethodShareChange clone() {
        MethodShareChange result = new MethodShareChange();
        for (Share s : shareRel) {
            result.addShare(s.clone());
        }
        return result;
    }

}
