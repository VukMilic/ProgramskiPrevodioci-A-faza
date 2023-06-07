// generated with ast extension for cup
// version 0.8
// 7/5/2023 20:14:46


package rs.ac.bg.etf.pp1.ast;

public class DesignatorForEach extends Statement {

    private Designator Designator;
    private ForeachTerm ForeachTerm;
    private String I3;
    private Statement Statement;

    public DesignatorForEach (Designator Designator, ForeachTerm ForeachTerm, String I3, Statement Statement) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.ForeachTerm=ForeachTerm;
        if(ForeachTerm!=null) ForeachTerm.setParent(this);
        this.I3=I3;
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public ForeachTerm getForeachTerm() {
        return ForeachTerm;
    }

    public void setForeachTerm(ForeachTerm ForeachTerm) {
        this.ForeachTerm=ForeachTerm;
    }

    public String getI3() {
        return I3;
    }

    public void setI3(String I3) {
        this.I3=I3;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(ForeachTerm!=null) ForeachTerm.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(ForeachTerm!=null) ForeachTerm.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(ForeachTerm!=null) ForeachTerm.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorForEach(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForeachTerm!=null)
            buffer.append(ForeachTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I3);
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorForEach]");
        return buffer.toString();
    }
}
