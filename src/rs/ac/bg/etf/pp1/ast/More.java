// generated with ast extension for cup
// version 0.8
// 9/5/2023 21:12:36


package rs.ac.bg.etf.pp1.ast;

public class More extends Relop {

    public More () {
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
        buffer.append("More(\n");

        buffer.append(tab);
        buffer.append(") [More]");
        return buffer.toString();
    }
}
