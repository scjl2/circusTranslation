package hijac.tools.scjmsafe.language.Expressions;

import java.util.ArrayList;

public class FieldAccess extends LExpr {

    private ArrayList<Identifier> elements;

    public FieldAccess() {
        elements = new ArrayList<Identifier>(0);
    }

    public FieldAccess(ArrayList<Identifier> fa) {
//        if ( fa.length() >= 2 ) {
            elements = fa;
//        } else {
//            System.out.println("Error - Field access does not have minimum length of 2");
//        }
    }

    public void addElement(Identifier id) {
        elements.add(id);
    }
    
    public void addElements(ArrayList<Identifier> ids) {
        for (Identifier i : ids) {
            elements.add(i);
        }
    }

    public Identifier getElement(int index) {
        if (elements.size() >= index) {
            return elements.get(index);
        } else {
            return null;
        }
    }

    public ArrayList<Identifier> getElements() {
        return elements;
    }

    public void setElement(Identifier i, int index) {
        elements.set(index, i);
    }

    public ArrayList<Identifier> getFront() {
        ArrayList<Identifier> result = new ArrayList<Identifier>(0);
        for (Identifier id : elements) {
            result.add(id);
        }
        result.remove(elements.size()-1);
        return result;
    }

    public Identifier getFirst() {
        return elements.get(0);
    }

    public Identifier getLast() {
        return elements.get(elements.size() - 1);
    }

    public void printExpression() {
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).printExpression();
            if (i+1 < elements.size()) {
                System.out.print(".");
            }
        }
    }

    public String getExpressionString() {
        String result = "";
        for (int i = 0; i < elements.size(); i++) {
            result += elements.get(i).getExpressionString();
            if (i+1 < elements.size()) {
                result += ".";
            }
        }
        return result;
    }

    @Override
    public String getTypeName() {
        return getLast().getTypeName();
    }

    public int getLength() {
        return elements.size();
    }

}
