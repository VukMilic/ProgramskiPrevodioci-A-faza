// generated with ast extension for cup
// version 0.8
// 19/5/2023 21:13:30


package rs.ac.bg.etf.pp1.ast;

public class DeclGVar extends DeclType {

    private GVarDecl GVarDecl;

    public DeclGVar (GVarDecl GVarDecl) {
        this.GVarDecl=GVarDecl;
        if(GVarDecl!=null) GVarDecl.setParent(this);
    }

    public GVarDecl getGVarDecl() {
        return GVarDecl;
    }

    public void setGVarDecl(GVarDecl GVarDecl) {
        this.GVarDecl=GVarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GVarDecl!=null) GVarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GVarDecl!=null) GVarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GVarDecl!=null) GVarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DeclGVar(\n");

        if(GVarDecl!=null)
            buffer.append(GVarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DeclGVar]");
        return buffer.toString();
    }
}
