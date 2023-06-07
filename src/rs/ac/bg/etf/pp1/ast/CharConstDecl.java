// generated with ast extension for cup
// version 0.8
// 7/5/2023 20:14:46


package rs.ac.bg.etf.pp1.ast;

public class CharConstDecl extends ConstDecl {

    private Type Type;
    private ConstTypeCharList ConstTypeCharList;

    public CharConstDecl (Type Type, ConstTypeCharList ConstTypeCharList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ConstTypeCharList=ConstTypeCharList;
        if(ConstTypeCharList!=null) ConstTypeCharList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ConstTypeCharList getConstTypeCharList() {
        return ConstTypeCharList;
    }

    public void setConstTypeCharList(ConstTypeCharList ConstTypeCharList) {
        this.ConstTypeCharList=ConstTypeCharList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ConstTypeCharList!=null) ConstTypeCharList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ConstTypeCharList!=null) ConstTypeCharList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ConstTypeCharList!=null) ConstTypeCharList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CharConstDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstTypeCharList!=null)
            buffer.append(ConstTypeCharList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CharConstDecl]");
        return buffer.toString();
    }
}
