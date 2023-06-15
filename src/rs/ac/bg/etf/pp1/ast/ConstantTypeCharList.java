// generated with ast extension for cup
// version 0.8
// 15/5/2023 20:22:39


package rs.ac.bg.etf.pp1.ast;

public class ConstantTypeCharList extends ConstTypeCharList {

    private ConstTypeCharList ConstTypeCharList;
    private String constName;
    private String constVal;

    public ConstantTypeCharList (ConstTypeCharList ConstTypeCharList, String constName, String constVal) {
        this.ConstTypeCharList=ConstTypeCharList;
        if(ConstTypeCharList!=null) ConstTypeCharList.setParent(this);
        this.constName=constName;
        this.constVal=constVal;
    }

    public ConstTypeCharList getConstTypeCharList() {
        return ConstTypeCharList;
    }

    public void setConstTypeCharList(ConstTypeCharList ConstTypeCharList) {
        this.ConstTypeCharList=ConstTypeCharList;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public String getConstVal() {
        return constVal;
    }

    public void setConstVal(String constVal) {
        this.constVal=constVal;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstTypeCharList!=null) ConstTypeCharList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstTypeCharList!=null) ConstTypeCharList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstTypeCharList!=null) ConstTypeCharList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstantTypeCharList(\n");

        if(ConstTypeCharList!=null)
            buffer.append(ConstTypeCharList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        buffer.append(" "+tab+constVal);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstantTypeCharList]");
        return buffer.toString();
    }
}
