// generated with ast extension for cup
// version 0.8
// 3/5/2023 12:17:6


package rs.ac.bg.etf.pp1.ast;

public class DeclarationsList extends DeclList {

    private DeclList DeclList;
    private DeclType DeclType;

    public DeclarationsList (DeclList DeclList, DeclType DeclType) {
        this.DeclList=DeclList;
        if(DeclList!=null) DeclList.setParent(this);
        this.DeclType=DeclType;
        if(DeclType!=null) DeclType.setParent(this);
    }

    public DeclList getDeclList() {
        return DeclList;
    }

    public void setDeclList(DeclList DeclList) {
        this.DeclList=DeclList;
    }

    public DeclType getDeclType() {
        return DeclType;
    }

    public void setDeclType(DeclType DeclType) {
        this.DeclType=DeclType;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DeclList!=null) DeclList.accept(visitor);
        if(DeclType!=null) DeclType.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DeclList!=null) DeclList.traverseTopDown(visitor);
        if(DeclType!=null) DeclType.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DeclList!=null) DeclList.traverseBottomUp(visitor);
        if(DeclType!=null) DeclType.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DeclarationsList(\n");

        if(DeclList!=null)
            buffer.append(DeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DeclType!=null)
            buffer.append(DeclType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DeclarationsList]");
        return buffer.toString();
    }
}
