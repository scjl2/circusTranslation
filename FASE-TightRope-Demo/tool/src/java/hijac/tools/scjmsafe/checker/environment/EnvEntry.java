package hijac.tools.scjmsafe.checker.environment;

import java.util.ArrayList;

public class EnvEntry {

    private ShareRelation shareRel;
    private RefSet refSet;

    public EnvEntry() {
        shareRel = new ShareRelation();
        refSet = new RefSet();
    }

    public EnvEntry(ShareRelation shareRel, RefSet refSet) {
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


    public EnvEntry clone() {
        EnvEntry result = new EnvEntry(shareRel.clone(), refSet.clone());
        return result;
    }

    public boolean equalTo(EnvEntry e) {
        boolean result = false;
        if (shareRel.equalTo(e.getShareRelation()) && refSet.equalTo(e.getRefSet())) {
            result = true;
        }
        return result;
    }

}
