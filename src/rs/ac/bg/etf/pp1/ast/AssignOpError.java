// generated with ast extension for cup
// version 0.8
// 8/5/2023 11:23:52


package rs.ac.bg.etf.pp1.ast;

public class AssignOpError extends AssignError {

    public AssignOpError () {
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
        buffer.append("AssignOpError(\n");

        buffer.append(tab);
        buffer.append(") [AssignOpError]");
        return buffer.toString();
    }
}
