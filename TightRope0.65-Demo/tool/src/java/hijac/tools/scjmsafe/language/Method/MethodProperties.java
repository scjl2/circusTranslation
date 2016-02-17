package hijac.tools.scjmsafe.language.Method;

import hijac.tools.scjmsafe.checker.environment.ShareRelation;

import java.util.ArrayList;

public class MethodProperties {

    private ShareRelation shareChange;
    private MethodRefChange refChange;


    public MethodProperties() {
        shareChange = new ShareRelation();
        refChange = new MethodRefChange();
    }

    public MethodProperties(ShareRelation share, MethodRefChange ref) {
        shareChange = share;
        refChange = ref;
    }

    public void setMethodShareChange(ShareRelation shareChange) {
        this.shareChange = shareChange;
    }

    public ShareRelation getMethodShareChange() {
        return shareChange;
    }

    public void setMethodRefChange(MethodRefChange refChange) {
        this.refChange = refChange;
    }

    public MethodRefChange getMethodRefChange() {
        return refChange;
    }


    public void setProperties(ShareRelation shareRel, MethodRefChange refSet) {
        this.shareChange = shareRel;
        this.refChange = refSet;
    }

    public void setProperties(MethodProperties p) {
        this.shareChange = p.getMethodShareChange();
        this.refChange = p.getMethodRefChange();
    }

    public MethodProperties clone() {
        MethodProperties result = new MethodProperties();
        result.setMethodShareChange(shareChange.clone());
        result.setMethodRefChange(refChange.clone());
        return result;
    }

    public boolean equalTo(MethodProperties p) {
        boolean result = false;
        if (shareChange.equalTo(p.getMethodShareChange()) && refChange.equalTo(p.getMethodRefChange())) {
            result = true;
        }
        return result;
    }


    public boolean isEmpty() {
        if (shareChange.isEmpty() && refChange.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

}
