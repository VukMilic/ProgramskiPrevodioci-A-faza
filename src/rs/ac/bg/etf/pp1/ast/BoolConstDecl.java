// generated with ast extension for cup
// version 0.8
// 7/5/2023 20:14:46


package rs.ac.bg.etf.pp1.ast;

public class BoolConstDecl extends ConstDecl {

    private Type Type;
    private ConstTypeBoolList ConstTypeBoolList;

    public BoolConstDecl (Type Type, ConstTypeBoolList ConstTypeBoolList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ConstTypeBoolList=ConstTypeBoolList;
        if(ConstTypeBoolList!=null) ConstTypeBoolList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ConstTypeBoolList getConstTypeBoolList() {
        return ConstTypeBoolList;
    }

    public void setConstTypeBoolList(ConstTypeBoolList ConstTypeBoolList) {
        this.ConstTypeBoolList=ConstTypeBoolList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ConstTypeBoolList!=null) ConstTypeBoolList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ConstTypeBoolList!=null) ConstTypeBoolList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ConstTypeBoolList!=null) ConstTypeBoolList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BoolConstDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstTypeBoolList!=null)
            buffer.append(ConstTypeBoolList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BoolConstDecl]");
        return buffer.toString();
    }
}
