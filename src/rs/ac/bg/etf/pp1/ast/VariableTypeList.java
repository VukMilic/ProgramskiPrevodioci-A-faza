// generated with ast extension for cup
// version 0.8
// 19/5/2023 21:13:30


package rs.ac.bg.etf.pp1.ast;

public class VariableTypeList extends VarTypeList {

    private VarTypeList VarTypeList;
    private String varName;
    private SquareBrackets SquareBrackets;

    public VariableTypeList (VarTypeList VarTypeList, String varName, SquareBrackets SquareBrackets) {
        this.VarTypeList=VarTypeList;
        if(VarTypeList!=null) VarTypeList.setParent(this);
        this.varName=varName;
        this.SquareBrackets=SquareBrackets;
        if(SquareBrackets!=null) SquareBrackets.setParent(this);
    }

    public VarTypeList getVarTypeList() {
        return VarTypeList;
    }

    public void setVarTypeList(VarTypeList VarTypeList) {
        this.VarTypeList=VarTypeList;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName=varName;
    }

    public SquareBrackets getSquareBrackets() {
        return SquareBrackets;
    }

    public void setSquareBrackets(SquareBrackets SquareBrackets) {
        this.SquareBrackets=SquareBrackets;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarTypeList!=null) VarTypeList.accept(visitor);
        if(SquareBrackets!=null) SquareBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarTypeList!=null) VarTypeList.traverseTopDown(visitor);
        if(SquareBrackets!=null) SquareBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarTypeList!=null) VarTypeList.traverseBottomUp(visitor);
        if(SquareBrackets!=null) SquareBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VariableTypeList(\n");

        if(VarTypeList!=null)
            buffer.append(VarTypeList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        if(SquareBrackets!=null)
            buffer.append(SquareBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VariableTypeList]");
        return buffer.toString();
    }
}
