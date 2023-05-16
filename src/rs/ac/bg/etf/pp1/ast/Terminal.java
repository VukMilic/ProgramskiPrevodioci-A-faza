// generated with ast extension for cup
// version 0.8
// 1/1/2023 23:18:41


package rs.ac.bg.etf.pp1.ast;

public class Terminal extends Term {

    private Factor Factor;
    private MulopFactList MulopFactList;

    public Terminal (Factor Factor, MulopFactList MulopFactList) {
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
        this.MulopFactList=MulopFactList;
        if(MulopFactList!=null) MulopFactList.setParent(this);
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public MulopFactList getMulopFactList() {
        return MulopFactList;
    }

    public void setMulopFactList(MulopFactList MulopFactList) {
        this.MulopFactList=MulopFactList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Factor!=null) Factor.accept(visitor);
        if(MulopFactList!=null) MulopFactList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
        if(MulopFactList!=null) MulopFactList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        if(MulopFactList!=null) MulopFactList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Terminal(\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MulopFactList!=null)
            buffer.append(MulopFactList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Terminal]");
        return buffer.toString();
    }
}
