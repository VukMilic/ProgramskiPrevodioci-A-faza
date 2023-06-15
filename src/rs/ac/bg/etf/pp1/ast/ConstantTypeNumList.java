// generated with ast extension for cup
// version 0.8
// 15/5/2023 20:22:39


package rs.ac.bg.etf.pp1.ast;

public class ConstantTypeNumList extends ConstTypeNumList {

    private ConstTypeNumList ConstTypeNumList;
    private String constName;
    private Integer constVal;

    public ConstantTypeNumList (ConstTypeNumList ConstTypeNumList, String constName, Integer constVal) {
        this.ConstTypeNumList=ConstTypeNumList;
        if(ConstTypeNumList!=null) ConstTypeNumList.setParent(this);
        this.constName=constName;
        this.constVal=constVal;
    }

    public ConstTypeNumList getConstTypeNumList() {
        return ConstTypeNumList;
    }

    public void setConstTypeNumList(ConstTypeNumList ConstTypeNumList) {
        this.ConstTypeNumList=ConstTypeNumList;
    }

    public String getConstName() {
        return constName;
    }

    public void setConstName(String constName) {
        this.constName=constName;
    }

    public Integer getConstVal() {
        return constVal;
    }

    public void setConstVal(Integer constVal) {
        this.constVal=constVal;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstTypeNumList!=null) ConstTypeNumList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstTypeNumList!=null) ConstTypeNumList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstTypeNumList!=null) ConstTypeNumList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstantTypeNumList(\n");

        if(ConstTypeNumList!=null)
            buffer.append(ConstTypeNumList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+constName);
        buffer.append("\n");

        buffer.append(" "+tab+constVal);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstantTypeNumList]");
        return buffer.toString();
    }
}
