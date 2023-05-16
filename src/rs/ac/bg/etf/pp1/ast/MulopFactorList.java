// generated with ast extension for cup
// version 0.8
// 1/1/2023 23:18:41


package rs.ac.bg.etf.pp1.ast;

public class MulopFactorList extends MulopFactList {

    private MulopFactList MulopFactList;
    private Mulop Mulop;
    private Factor Factor;

    public MulopFactorList (MulopFactList MulopFactList, Mulop Mulop, Factor Factor) {
        this.MulopFactList=MulopFactList;
        if(MulopFactList!=null) MulopFactList.setParent(this);
        this.Mulop=Mulop;
        if(Mulop!=null) Mulop.setParent(this);
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
    }

    public MulopFactList getMulopFactList() {
        return MulopFactList;
    }

    public void setMulopFactList(MulopFactList MulopFactList) {
        this.MulopFactList=MulopFactList;
    }

    public Mulop getMulop() {
        return Mulop;
    }

    public void setMulop(Mulop Mulop) {
        this.Mulop=Mulop;
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MulopFactList!=null) MulopFactList.accept(visitor);
        if(Mulop!=null) Mulop.accept(visitor);
        if(Factor!=null) Factor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MulopFactList!=null) MulopFactList.traverseTopDown(visitor);
        if(Mulop!=null) Mulop.traverseTopDown(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MulopFactList!=null) MulopFactList.traverseBottomUp(visitor);
        if(Mulop!=null) Mulop.traverseBottomUp(visitor);
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MulopFactorList(\n");

        if(MulopFactList!=null)
            buffer.append(MulopFactList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Mulop!=null)
            buffer.append(Mulop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MulopFactorList]");
        return buffer.toString();
    }
}
