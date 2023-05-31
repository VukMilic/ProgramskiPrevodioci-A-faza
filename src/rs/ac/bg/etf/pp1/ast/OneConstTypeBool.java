// generated with ast extension for cup
// version 0.8
// 31/4/2023 22:10:31


package rs.ac.bg.etf.pp1.ast;

public class OneConstTypeBool extends ConstTypeBoolList {

    private String constName;
    private String constVal;

    public OneConstTypeBool (String constName, String constVal) {
        this.constName=constName;
        this.constVal=constVal;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public String getConstVal() {
        return constVal;
    }

    public void setConstVal(String constVal) {
        this.constVal=constVal;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneConstTypeBool(\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        buffer.append(" "+tab+constVal);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneConstTypeBool]");
        return buffer.toString();
    }
}
