// generated with ast extension for cup
// version 0.8
// 8/5/2023 11:23:52


package rs.ac.bg.etf.pp1.ast;

public class ConstantTypeBoolList extends ConstTypeBoolList {

    private ConstTypeBoolList ConstTypeBoolList;
    private String constName;
    private String constVal;

    public ConstantTypeBoolList (ConstTypeBoolList ConstTypeBoolList, String constName, String constVal) {
        this.ConstTypeBoolList=ConstTypeBoolList;
        if(ConstTypeBoolList!=null) ConstTypeBoolList.setParent(this);
        this.constName=constName;
        this.constVal=constVal;
    }

    public ConstTypeBoolList getConstTypeBoolList() {
        return ConstTypeBoolList;
    }

    public void setConstTypeBoolList(ConstTypeBoolList ConstTypeBoolList) {
        this.ConstTypeBoolList=ConstTypeBoolList;
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
        if(ConstTypeBoolList!=null) ConstTypeBoolList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstTypeBoolList!=null) ConstTypeBoolList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstTypeBoolList!=null) ConstTypeBoolList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstantTypeBoolList(\n");

        if(ConstTypeBoolList!=null)
            buffer.append(ConstTypeBoolList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        buffer.append(" "+tab+constVal);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstantTypeBoolList]");
        return buffer.toString();
    }
}
