package hijac.tools.scjmsafe.language.Expressions;

public class Variable extends Identifier {

    private String name;
    private VarType varType;

    public Variable(String n, String t) {
        name = n;
        varType = new VarType(t);
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public void setType(String t) {
        varType.setTypeName(t);
    }

    public VarType getVarType() {
        return varType;
    }

    public void setVarType(VarType varType) {
        this.varType = varType;
    }

    public boolean compareTypeAgainst(Variable v) {
//        System.out.println("Comparing var '" + name + "' with type '" + varType.getTypeName() + "' against '" + name + "' with type '" + v.getVarType().getTypeName() + "'");
        if (v.getVarType().getTypeName().equals(varType.getTypeName())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void printExpression() {
        System.out.print(name);
    }

    @Override
    public String getExpressionString() {
        return name;
    }

    @Override
    public String getTypeName() {
        return varType.getTypeName();
    }

    public int getLength() {
        return 1;
    }

}
