// generated with ast extension for cup
// version 0.8
// 1/1/2023 23:18:41


package rs.ac.bg.etf.pp1.ast;

public class OneDesigList extends DesignatorList {

    private DesignatorOrNot DesignatorOrNot;

    public OneDesigList (DesignatorOrNot DesignatorOrNot) {
        this.DesignatorOrNot=DesignatorOrNot;
        if(DesignatorOrNot!=null) DesignatorOrNot.setParent(this);
    }

    public DesignatorOrNot getDesignatorOrNot() {
        return DesignatorOrNot;
    }

    public void setDesignatorOrNot(DesignatorOrNot DesignatorOrNot) {
        this.DesignatorOrNot=DesignatorOrNot;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorOrNot!=null) DesignatorOrNot.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorOrNot!=null) DesignatorOrNot.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorOrNot!=null) DesignatorOrNot.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneDesigList(\n");

        if(DesignatorOrNot!=null)
            buffer.append(DesignatorOrNot.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneDesigList]");
        return buffer.toString();
    }
}
