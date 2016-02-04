package hijac.tools.scjmsafe.checker.environment;

import java.util.ArrayList;

public class Environment {

    private ShareRelation shareRel;
    private RefSet refSet;

    public Environment() {
        shareRel = new ShareRelation();
        refSet = new RefSet();
    }

    public Environment(ShareRelation shareRel, RefSet refSet) {
        this.shareRel = shareRel;
        this.refSet = refSet;
    }

    public void setShareRelation(ShareRelation s) {
        shareRel = s;
    }

    public ShareRelation getShareRelation() {
        return shareRel;
    }

    public void setRefSet(RefSet rs) {
        refSet = rs;
    }

    public RefSet getRefSet() {
        return refSet;
    }

    public void setEnvironment(ShareRelation shareRel, RefSet refSet) {
        this.shareRel = shareRel;
        this.refSet = refSet;
    }

    public void setEnvironment(Environment env) {
        this.shareRel = env.getShareRelation();
        this.refSet = env.getRefSet();
    }

    public Environment clone() {
        Environment result = new Environment(shareRel.clone(), refSet.clone());
        return result;
    }

    public boolean equalTo(Environment e) {
        boolean result = false;
        if (shareRel.equalTo(e.getShareRelation()) && refSet.equalTo(e.getRefSet())) {
            result = true;
        }
        return result;
    }

}
