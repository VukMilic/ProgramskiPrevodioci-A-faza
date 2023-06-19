// generated with ast extension for cup
// version 0.8
// 19/5/2023 21:13:30


package rs.ac.bg.etf.pp1.ast;

public class OneGlobalVarType extends GVarTypeList {

    private GVarIdent GVarIdent;

    public OneGlobalVarType (GVarIdent GVarIdent) {
        this.GVarIdent=GVarIdent;
        if(GVarIdent!=null) GVarIdent.setParent(this);
    }

    public GVarIdent getGVarIdent() {
        return GVarIdent;
    }

    public void setGVarIdent(GVarIdent GVarIdent) {
        this.GVarIdent=GVarIdent;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GVarIdent!=null) GVarIdent.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GVarIdent!=null) GVarIdent.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GVarIdent!=null) GVarIdent.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneGlobalVarType(\n");

        if(GVarIdent!=null)
            buffer.append(GVarIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneGlobalVarType]");
        return buffer.toString();
    }
}
