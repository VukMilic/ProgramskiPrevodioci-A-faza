// generated with ast extension for cup
// version 0.8
// 9/5/2023 21:12:35


package rs.ac.bg.etf.pp1.ast;

public class GlobalVariableTypeList extends GVarTypeList {

    private GVarTypeList GVarTypeList;
    private GVarIdent GVarIdent;

    public GlobalVariableTypeList (GVarTypeList GVarTypeList, GVarIdent GVarIdent) {
        this.GVarTypeList=GVarTypeList;
        if(GVarTypeList!=null) GVarTypeList.setParent(this);
        this.GVarIdent=GVarIdent;
        if(GVarIdent!=null) GVarIdent.setParent(this);
    }

    public GVarTypeList getGVarTypeList() {
        return GVarTypeList;
    }

    public void setGVarTypeList(GVarTypeList GVarTypeList) {
        this.GVarTypeList=GVarTypeList;
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
        if(GVarTypeList!=null) GVarTypeList.accept(visitor);
        if(GVarIdent!=null) GVarIdent.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GVarTypeList!=null) GVarTypeList.traverseTopDown(visitor);
        if(GVarIdent!=null) GVarIdent.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GVarTypeList!=null) GVarTypeList.traverseBottomUp(visitor);
        if(GVarIdent!=null) GVarIdent.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVariableTypeList(\n");

        if(GVarTypeList!=null)
            buffer.append(GVarTypeList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GVarIdent!=null)
            buffer.append(GVarIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVariableTypeList]");
        return buffer.toString();
    }
}
