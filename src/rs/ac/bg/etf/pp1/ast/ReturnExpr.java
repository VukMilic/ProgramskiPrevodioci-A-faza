// generated with ast extension for cup
// version 0.8
// 1/1/2023 23:18:41


package rs.ac.bg.etf.pp1.ast;

public class ReturnExpr extends Statement {

    private ExprOrNot ExprOrNot;

    public ReturnExpr (ExprOrNot ExprOrNot) {
        this.ExprOrNot=ExprOrNot;
        if(ExprOrNot!=null) ExprOrNot.setParent(this);
    }

    public ExprOrNot getExprOrNot() {
        return ExprOrNot;
    }

    public void setExprOrNot(ExprOrNot ExprOrNot) {
        this.ExprOrNot=ExprOrNot;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprOrNot!=null) ExprOrNot.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprOrNot!=null) ExprOrNot.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprOrNot!=null) ExprOrNot.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ReturnExpr(\n");

        if(ExprOrNot!=null)
            buffer.append(ExprOrNot.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ReturnExpr]");
        return buffer.toString();
    }
}
