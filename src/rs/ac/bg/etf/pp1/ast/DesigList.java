// generated with ast extension for cup
// version 0.8
// 1/1/2023 23:18:41


package rs.ac.bg.etf.pp1.ast;

public class DesigList extends DesignatorList {

    private DesignatorList DesignatorList;
    private DesignatorOrNot DesignatorOrNot;

    public DesigList (DesignatorList DesignatorList, DesignatorOrNot DesignatorOrNot) {
        this.DesignatorList=DesignatorList;
        if(DesignatorList!=null) DesignatorList.setParent(this);
        this.DesignatorOrNot=DesignatorOrNot;
        if(DesignatorOrNot!=null) DesignatorOrNot.setParent(this);
    }

    public DesignatorList getDesignatorList() {
        return DesignatorList;
    }

    public void setDesignatorList(DesignatorList DesignatorList) {
        this.DesignatorList=DesignatorList;
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
        if(DesignatorList!=null) DesignatorList.accept(visitor);
        if(DesignatorOrNot!=null) DesignatorOrNot.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorList!=null) DesignatorList.traverseTopDown(visitor);
        if(DesignatorOrNot!=null) DesignatorOrNot.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorList!=null) DesignatorList.traverseBottomUp(visitor);
        if(DesignatorOrNot!=null) DesignatorOrNot.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesigList(\n");

        if(DesignatorList!=null)
            buffer.append(DesignatorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorOrNot!=null)
            buffer.append(DesignatorOrNot.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesigList]");
        return buffer.toString();
    }
}
