package hijac.tools.scjmsafe.language.Method;

import hijac.tools.scjmsafe.checker.environment.ShareRelation;

import java.util.ArrayList;

public class MethodProperty {

    private ShareRelation shareChange;
    private MethodRefChange refChange;
    private boolean complete;

    public MethodProperty() {
        shareChange = new ShareRelation();
        refChange = new MethodRefChange();
        complete = false;
    }

    public MethodProperty(ShareRelation share, MethodRefChange ref) {
        shareChange = share;
        refChange = ref;
        complete = false;
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

    public void complete() {
        complete = true;
    }

    public boolean isComplete() {
        return complete;
    }

    public MethodProperty clone() {
        MethodProperty result = new MethodProperty();
        result.setMethodShareChange(shareChange.clone());
        result.setMethodRefChange(refChange.clone());
        if (isComplete()) {
            result.complete();
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

    public boolean canMergeWith(MethodProperty p) {
        boolean result = false;
        if (shareChange.canMergeWith(p.getMethodShareChange())) {
            result = true;
        }
        return result;
    }

    public boolean equalTo(MethodProperty p) {
        boolean result = false;
        if (shareChange.equalTo(p.getMethodShareChange()) && refChange.equalTo(p.getMethodRefChange())) {
            result = true;
        }
        return result;
    }

}
