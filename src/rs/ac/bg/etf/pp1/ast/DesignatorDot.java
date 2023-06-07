// generated with ast extension for cup
// version 0.8
// 7/5/2023 20:14:46


package rs.ac.bg.etf.pp1.ast;

public class DesignatorDot extends Designator {

    private Designator Designator;
    private String dotName;

    public DesignatorDot (Designator Designator, String dotName) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.dotName=dotName;
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public String getDotName() {
        return dotName;
    }

    public void setDotName(String dotName) {
        this.dotName=dotName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorDot(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+dotName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorDot]");
        return buffer.toString();
    }
}
