// generated with ast extension for cup
// version 0.8
// 1/5/2023 21:39:14


package rs.ac.bg.etf.pp1.ast;

public class FactorNumConst extends Factor {

    private Integer constName;

    public FactorNumConst (Integer constName) {
        this.constName=constName;
    }

    public Integer getConstName() {
        return constName;
    }

    public void setConstName(Integer constName) {
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
        buffer.append("FactorNumConst(\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorNumConst]");
        return buffer.toString();
    }
}
