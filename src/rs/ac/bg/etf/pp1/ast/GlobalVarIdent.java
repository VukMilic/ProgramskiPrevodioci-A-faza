// generated with ast extension for cup
// version 0.8
// 3/5/2023 12:17:6


package rs.ac.bg.etf.pp1.ast;

public class GlobalVarIdent extends GVarIdent {

    private String globVarName;
    private SquareBrackets SquareBrackets;

    public GlobalVarIdent (String globVarName, SquareBrackets SquareBrackets) {
        this.globVarName=globVarName;
        this.SquareBrackets=SquareBrackets;
        if(SquareBrackets!=null) SquareBrackets.setParent(this);
    }

    public String getGlobVarName() {
        return globVarName;
    }

    public void setGlobVarName(String globVarName) {
        this.globVarName=globVarName;
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
        buffer.append("GlobalVarIdent(\n");

        buffer.append(" "+tab+globVarName);
        buffer.append("\n");

        if(SquareBrackets!=null)
            buffer.append(SquareBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVarIdent]");
        return buffer.toString();
    }
}
