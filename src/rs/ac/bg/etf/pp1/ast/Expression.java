// generated with ast extension for cup
// version 0.8
// 3/5/2023 12:17:6


package rs.ac.bg.etf.pp1.ast;

public class Expression extends Expr {

    private MinusOrNot MinusOrNot;
    private Term Term;
    private AddopTermList AddopTermList;

    public Expression (MinusOrNot MinusOrNot, Term Term, AddopTermList AddopTermList) {
        this.MinusOrNot=MinusOrNot;
        if(MinusOrNot!=null) MinusOrNot.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
        this.AddopTermList=AddopTermList;
        if(AddopTermList!=null) AddopTermList.setParent(this);
    }

    public MinusOrNot getMinusOrNot() {
        return MinusOrNot;
    }

    public void setMinusOrNot(MinusOrNot MinusOrNot) {
        this.MinusOrNot=MinusOrNot;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public AddopTermList getAddopTermList() {
        return AddopTermList;
    }

    public void setAddopTermList(AddopTermList AddopTermList) {
        this.AddopTermList=AddopTermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MinusOrNot!=null) MinusOrNot.accept(visitor);
        if(Term!=null) Term.accept(visitor);
        if(AddopTermList!=null) AddopTermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MinusOrNot!=null) MinusOrNot.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
        if(AddopTermList!=null) AddopTermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MinusOrNot!=null) MinusOrNot.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        if(AddopTermList!=null) AddopTermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expression(\n");

        if(MinusOrNot!=null)
            buffer.append(MinusOrNot.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AddopTermList!=null)
            buffer.append(AddopTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expression]");
        return buffer.toString();
    }
}
