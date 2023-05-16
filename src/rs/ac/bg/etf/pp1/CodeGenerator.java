package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	
	public int getMainPc(){
		return mainPc;
	}

	// -----------------------------------------------------------
	// (VMAssignOp)
	public void visit(DesignatorAssignOp designatorAssignOp){
		// Code.load(designatorAssignOp.getExpr().obj);
		if( designatorAssignOp.getExpr().obj.getKind() == Obj.Elem ){
			Code.load(designatorAssignOp.getExpr().obj);
		}
		Code.store(designatorAssignOp.getDesignator().obj);
	}
	
	// -----------------------------------------------------------
	// (VMPrint)

	public void visit(PrintStmt printStmt){
		if( printStmt.getExpr().obj.getType().getKind() == Struct.Int ){
			
			// radi se o int tipu
			// potrebno nam je 4B za operand (int=32bit)
			// i 1B za width() (sirinu operanda)
			Code.loadConst(5);
			Code.put(Code.print);
			
		} else if( printStmt.getExpr().obj.getType().getKind() == Struct.Char ){
			
			// radi se o char tipu
			// ovde load-ujemo 1B zbog char-a,
			//  zato sto postoji bprint naredba kojoj je dovoljan jedan bajt
			Code.loadConst(1);
			Code.put(Code.bprint);
			
		} else {
			// radi se o Bool tipu
			Code.loadConst(5);
			Code.put(Code.print);
		}
	}

	// ---------------------------------------------------------
	// (VMConst)
	
    public void visit(FactorNumConst factorNumConst) {
    	// stavimo da je globalno vidljiva
    	factorNumConst.obj.setLevel(0);
    	Code.load(factorNumConst.obj);
    }
    
    public void visit(FactorCharConst factorCharConst){
    	// stavimo da je globalno vidljiva
    	factorCharConst.obj.setLevel(0);
    	Code.load(factorCharConst.obj);
    }
    
    public void visit(FactorBoolConst factorBoolConst){
    	// stavimo da je globalno vidljiva
    	factorBoolConst.obj.setLevel(0);
    	Code.load(factorBoolConst.obj);
    }
    
    // ---------------------------------------------------------
    // (VMDesignator)
    
    public void visit(DesignatorIdent designatorIdent){
    	Code.load(designatorIdent.obj);
    }
    
    public void visit(DesignatorBrackets designatorBrackets){
    	//Code.load(designatorBrackets.obj);
    }
    
    // --------------------------------------------------------
    // (VMMeth)
 
    public void visit(MethodDeclaration methodDeclaration){
    	// pandan za enter sto smo zvali na pocetku metode
    	Code.put(Code.exit);
    	Code.put(Code.return_);
    }
    
    public void visit(MethTypeName methTypeName){
    	
    	if("main".equalsIgnoreCase(methTypeName.getMethName())){
    		mainPc = Code.pc;
    	}
    	methTypeName.obj.setAdr(Code.pc);
    	
    	// dohvatanje argumenata i lokalnih promenljivih
    	SyntaxNode methNode = methTypeName.getParent();
    	
    	VarCounter varCnt = new VarCounter();
    	methNode.traverseTopDown(varCnt);
    	
    	FormParamCounter formCnt = new FormParamCounter();
    	methNode.traverseTopDown(formCnt);
    	
    	// generisemo entry (enter na pocetku funkcije)
    	Code.put(Code.enter);
    	Code.put(formCnt.getCount());    	
    	Code.put(formCnt.getCount() + varCnt.getCount());
    }
    
    public void visit(MethVoidName methVoidName){
    	if("main".equalsIgnoreCase(methVoidName.getMethName())){
    		mainPc = Code.pc;
    	}
    	methVoidName.obj.setAdr(Code.pc);
    	
    	// dohvatanje argumenata i lokalnih promenljivih
    	SyntaxNode methNode = methVoidName.getParent();
    	
    	VarCounter varCnt = new VarCounter();
    	methNode.traverseTopDown(varCnt);
    	
    	FormParamCounter formCnt = new FormParamCounter();
    	methNode.traverseTopDown(formCnt);
    	
    	// generisemo entry (enter na pocetku funkcije)
    	Code.put(Code.enter);
    	Code.put(formCnt.getCount());    	
    	Code.put(formCnt.getCount() + varCnt.getCount());    	
    }
}
