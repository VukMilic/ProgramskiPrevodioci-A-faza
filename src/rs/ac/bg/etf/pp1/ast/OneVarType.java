// generated with ast extension for cup
// version 0.8
// 19/5/2023 21:13:30


package rs.ac.bg.etf.pp1.ast;

public class OneVarType extends VarTypeList {

    private String varName;
    private SquareBrackets SquareBrackets;

    public OneVarType (String varName, SquareBrackets SquareBrackets) {
        this.varName=varName;
        this.SquareBrackets=SquareBrackets;
        if(SquareBrackets!=null) SquareBrackets.setParent(this);
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
        if(SquareBrackets!=null) SquareBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SquareBrackets!=null) SquareBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SquareBrackets!=null) SquareBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneVarType(\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        if(SquareBrackets!=null)
            buffer.append(SquareBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneVarType]");
        return buffer.toString();
    }
}
