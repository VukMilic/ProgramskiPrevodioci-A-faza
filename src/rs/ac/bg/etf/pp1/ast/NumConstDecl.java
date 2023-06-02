// generated with ast extension for cup
// version 0.8
// 1/5/2023 21:39:14


package rs.ac.bg.etf.pp1.ast;

public class NumConstDecl extends ConstDecl {

    private Type Type;
    private ConstTypeNumList ConstTypeNumList;

    public NumConstDecl (Type Type, ConstTypeNumList ConstTypeNumList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ConstTypeNumList=ConstTypeNumList;
        if(ConstTypeNumList!=null) ConstTypeNumList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ConstTypeNumList getConstTypeNumList() {
        return ConstTypeNumList;
    }

    public void setConstTypeNumList(ConstTypeNumList ConstTypeNumList) {
        this.ConstTypeNumList=ConstTypeNumList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ConstTypeNumList!=null) ConstTypeNumList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ConstTypeNumList!=null) ConstTypeNumList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ConstTypeNumList!=null) ConstTypeNumList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NumConstDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstTypeNumList!=null)
            buffer.append(ConstTypeNumList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NumConstDecl]");
        return buffer.toString();
    }
}
