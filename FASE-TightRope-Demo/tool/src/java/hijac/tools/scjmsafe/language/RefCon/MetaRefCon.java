package hijac.tools.scjmsafe.language.RefCon;

public abstract class MetaRefCon {

    public abstract void printMetaRefCon();

    public abstract String getMetaRefConString();

    public abstract boolean equalTo(MetaRefCon arg);

    public abstract MetaRefCon clone();

}
