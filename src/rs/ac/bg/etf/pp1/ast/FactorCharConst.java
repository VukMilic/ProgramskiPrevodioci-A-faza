// generated with ast extension for cup
// version 0.8
// 24/5/2023 22:14:57


package rs.ac.bg.etf.pp1.ast;

public class FactorCharConst extends Factor {

    private String constName;

    public FactorCharConst (String constName) {
        this.constName=constName;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
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
        buffer.append("FactorCharConst(\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorCharConst]");
        return buffer.toString();
    }
}
