package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.FormalParamDecl;
import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {

	protected int count;
	
	public int getCount(){
		return count;
	}
	
	public static class FormParamCounter extends CounterVisitor{
		public void visit(FormalParamDecl formalParamDecl){
			count++;
		}
	}
	
	public static class VarCounter extends CounterVisitor{
		public void visit(OneVarType OneVarType){
			count++;
		}
		public void visit(VariableTypeList VariableTypeList){
			count++;
		}
	}
}
