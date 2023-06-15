// generated with ast extension for cup
// version 0.8
// 15/5/2023 20:22:39


package rs.ac.bg.etf.pp1.ast;

public class SquareBracketsDouble extends SquareBrackets {

    public SquareBracketsDouble () {
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
        buffer.append("SquareBracketsDouble(\n");

        buffer.append(tab);
        buffer.append(") [SquareBracketsDouble]");
        return buffer.toString();
    }
}
