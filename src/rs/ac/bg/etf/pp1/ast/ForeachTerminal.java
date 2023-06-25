// generated with ast extension for cup
// version 0.8
// 24/5/2023 22:14:57


package rs.ac.bg.etf.pp1.ast;

public class ForeachTerminal extends ForeachTerm {

    public ForeachTerminal () {
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
        buffer.append("ForeachTerminal(\n");

        buffer.append(tab);
        buffer.append(") [ForeachTerminal]");
        return buffer.toString();
    }
}
