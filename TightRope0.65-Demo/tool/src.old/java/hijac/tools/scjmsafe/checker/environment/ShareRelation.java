package hijac.tools.scjmsafe.checker.environment;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;

import hijac.tools.scjmsafe.language.Expressions.*;

public class ShareRelation {

    private HashSet<Share> shares;

    public ShareRelation() {
        shares = new HashSet<Share>(0);
    }

    public ShareRelation(HashSet<Share> shares) {
        this.shares = shares;
    }

    public HashSet<Share> getShares() {
        return shares;
    }

    public void removeExprFromShares(Expr expr) {
        Iterator<Share> sharesIt = shares.iterator();
        HashSet<Share> temp = new HashSet<Share>(0);
        for (Share s : shares) {
            if (s.containsExpr(expr)) {
                temp.add(s);
            } else if (EnvUtil.exprPrefixOfFieldAccess(expr, s.getLExpr()) || EnvUtil.exprPrefixOfFieldAccess(expr, s.getRExpr())) {
                temp.add(s);
            }
        }
        shares.removeAll(temp);
/*
        while (sharesIt.hasNext()) {
            Share s = sharesIt.next();
            if (s.containsExpr(expr)) {
                sharesIt.remove();
//                System.out.println("Removing share " + s.getLExpr().getExpressionString() + " -> " + s.getRExpr().getExpressionString() + " from properties (share1)");
            } else if (EnvUtil.exprPrefixOfFieldAccess(expr, s.getLExpr()) || EnvUtil.exprPrefixOfFieldAccess(expr, s.getRExpr())) {
                sharesIt.remove();
//                System.out.println("Removing share " + s.getLExpr().getExpressionString() + " -> " + s.getRExpr().getExpressionString() +  " from properties (share2)");
            }
        }
*/
    }

    public HashSet<Share> getShares(Expr e) {
        HashSet<Share> result = new HashSet<Share>(0);
        for (Share s : shares) {
            if (s.getLExpr().equalTo(e)) {
                result.add(s);
            }
        }
        return result;
    }

    public void getAllEqualExprs(Expr e, HashSet<Expr> exprs, int indent) {
        for (Share s : shares) {
            if (s.getLExpr().equalTo(e) && !s.getRExpr().equalTo(s.getLExpr())) {
                boolean found = false;
                for (Expr e1 : exprs) {
                    if (e1.equalTo(s.getRExpr())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
//                    for (int i=0; i < indent; i++) {
//                        System.out.print(" ");
//                    }
//                    System.out.println("Expr : " + e.getExpressionString() + " is equal to " + s.getRExpr().getExpressionString());
                    exprs.add(s.getRExpr());
                    found = false;
                    getAllEqualExprs(s.getRExpr(), exprs, indent + 2);
//                    for (Expr e2 : getAllEqualExprs(s.getRExpr(), exprs)) {
//                        for (Expr e1 : result) {
//                            if (e1.equalTo(e2)) {
//                                found = true;
//                            }
//                        }
//                        if (!found) {
//                            result.add(e2);
//                        }
//                    }
                }
            } else if (s.getRExpr().equalTo(e) && !s.getRExpr().equalTo(s.getLExpr())) {
                boolean found = false;
                for (Expr e1 : exprs) {
                    if (e1.equalTo(s.getLExpr())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
//                    for (int i=0; i < indent; i++) {
//                        System.out.print(" ");
//                    }
//                    System.out.println("Expr : " + e.getExpressionString() + " is equal to " + s.getLExpr().getExpressionString());
                    exprs.add(s.getLExpr());
                    found = false;
                    getAllEqualExprs(s.getLExpr(), exprs, indent + 2);
//                    for (Expr e2 : getAllEqualExprs(s.getLExpr(), exprs)) {
//                        for (Expr e1 : result) {
//                            if (e1.equalTo(e2)) {
//                                found = true;
//                            }
//                        }
//                        if (!found) {
//                            result.add(e2);
//                        }
//                    }
                }
            }            
        }
//        return result;
    }

    public void removeShares(HashSet<Share> shares1) {
        for (Share s : shares1) {
            shares.remove(s);
        }
    }


    public HashSet<Expr> matchingExprsInShareRelation(Expr e) {
        HashSet<Expr> result = new HashSet<Expr>(0);
        for (Share s : shares) {
            Expr shareExpr = s.getLExpr();
            if (e.equalTo(shareExpr)) {
                result.add(shareExpr);
            } else if (EnvUtil.exprPrefixOfFieldAccess(e, shareExpr)) {
                result.add(shareExpr);
            }
        }
        return result;
    }

    public void addShare(Share share) {
//        System.out.println("Adding share for " + share.getLExpr().getExpressionString() + " to " + share.getRExpr().getExpressionString() + " to environment/properties");
        boolean flag = true;
        for (Share s : shares) {
            if (s.equalTo(share)) {
                flag = false;
                break;
            }
        }
        if (flag) {
            shares.add(share);
        }
    }

    public void addShares(HashSet<Share> s) {
        for (Share s1 : s) {
            addShare(s1);
        }
    }

    public HashSet<Expr> getExprs() {
        HashSet<Expr> result = new HashSet<Expr>(0);
        for (Share s : shares) {
            result.add(s.getLExpr());
//            result.add(s.getRExpr());   // TODO - Added this
        }
        return result;
    }

    public boolean equalTo(ShareRelation shareRel) {
        boolean result = true;
        if (shares.size() == shareRel.getShares().size()) {
            for (Share s1 : shares) {
                boolean found = false;
                for (Share s2 : shareRel.getShares()) {
                    if (s1.equalTo(s2)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    result = false;
                    break;
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    public boolean canMergeWith(ShareRelation shareRel) {
        boolean result = true;
        for (Share s1 : shares) {
            boolean inShare = false;
            for (Expr e : shareRel.getExprs()) {
                if (s1.getLExpr().equalTo(e)) {
                    inShare = true;
                    break;
                }
            }
            if (inShare) {
                boolean match = false;
                for (Share s2 : shareRel.getShares()) {
                    if (s1.equalTo(s2)) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    result = false;
                }
            }
        }
        return result;
    }

    public void mergeWith(ShareRelation shareRel) {
        for (Share s1 : shareRel.getShares()) {
            boolean found = false;
            for (Share s2 : shares) {
                if (s2.equalTo(s1)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                shares.add(s1);
            }
        }
    }

    public boolean isEmpty() {
        if (shares.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void printShareRel() {
        System.out.println("    {");
        for (Share s : shares) {
            s.printShare();
        }
        System.out.println("    }");
    }

    public ShareRelation clone() {
        ShareRelation result = new ShareRelation();
        for (Share share : shares) {
            result.addShare(share.clone());
        }
        return result;
    }

}
