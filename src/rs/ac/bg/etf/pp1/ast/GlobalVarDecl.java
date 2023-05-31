// generated with ast extension for cup
// version 0.8
// 31/4/2023 22:10:31


package rs.ac.bg.etf.pp1.ast;

public class GlobalVarDecl extends GVarDecl {

    private Type Type;
    private GVarTypeList GVarTypeList;

    public GlobalVarDecl (Type Type, GVarTypeList GVarTypeList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.GVarTypeList=GVarTypeList;
        if(GVarTypeList!=null) GVarTypeList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public GVarTypeList getGVarTypeList() {
        return GVarTypeList;
    }

    public void setGVarTypeList(GVarTypeList GVarTypeList) {
        this.GVarTypeList=GVarTypeList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(GVarTypeList!=null) GVarTypeList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(GVarTypeList!=null) GVarTypeList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(GVarTypeList!=null) GVarTypeList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVarDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GVarTypeList!=null)
            buffer.append(GVarTypeList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVarDecl]");
        return buffer.toString();
    }
}
