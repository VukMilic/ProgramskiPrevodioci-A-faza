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
	
	private String muloperation = "";
	private String addoperation = "";

	private int isMinus = 0;
	
	public int getMainPc() {
		return mainPc;
	}

	// -----------------------------------------------------------
	// (VMDesignatorStatement)

	public void visit(DesignatorAssignOp designatorAssignOp) {
		// Code.load(designatorAssignOp.getExpr().obj);
		// ovaj load smatramo da smo vec uradili
		Code.store(designatorAssignOp.getDesignator().obj);
	}
	
	public void visit(DesignatorPlusPlus designatorPlusPlus){
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(designatorPlusPlus.getDesignator().obj);
	}
	
	public void visit(DesignatorMinusMinus designatorMinusMinus){
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(designatorMinusMinus.getDesignator().obj);
	}

	// -----------------------------------------------------------
	// (VMPrint)

	public void visit(PrintStmt printStmt) {
		// Expr smo vec ubacili na stek u prolasku kroz stablo
		if (printStmt.getExpr().obj.getType().getKind() == Struct.Int
				|| printStmt.getExpr().obj.getType().getKind() == Struct.Bool) {

			// radi se o int tipu
			// potrebno nam je 4B za operand (int=32bit)
			// i 1B za width() (sirinu operanda)
			Code.loadConst(5);
			Code.put(Code.print);

		} else if (printStmt.getExpr().obj.getType().getKind() == Struct.Char) {

			// radi se o char tipu
			// ovde load-ujemo 1B zbog char-a,
			// zato sto postoji bprint naredba kojoj je dovoljan jedan bajt
			Code.loadConst(1);
			Code.put(Code.bprint);

		}
	}
	
	// ---------------------------------------------------------
	//(VMRead)
	
	public void visit(ReadDesignator readDesignator) {
		if( readDesignator.getDesignator().obj.getType().getKind() == Struct.Int 
				|| readDesignator.getDesignator().obj.getType().getKind() == Struct.Bool ) {
			Code.put(Code.read);
		} else if (readDesignator.getDesignator().obj.getType().getKind() == Struct.Char) {
			Code.put(Code.bread);
		}
		Code.store(readDesignator.getDesignator().obj);
	}

	// ---------------------------------------------------------
	// (VMConst)

	public void visit(FactorNumConst factorNumConst) {
		// stavimo da je globalno vidljiva
		factorNumConst.obj.setLevel(0);
		Code.load(factorNumConst.obj);
	}

	public void visit(FactorCharConst factorCharConst) {
		// stavimo da je globalno vidljiva
		factorCharConst.obj.setLevel(0);
		Code.load(factorCharConst.obj);
	}

	public void visit(FactorBoolConst factorBoolConst) {
		// stavimo da je globalno vidljiva
		factorBoolConst.obj.setLevel(0);
		Code.load(factorBoolConst.obj);
	}

	// ---------------------------------------------------------
	// (VMDesignator)

	public void visit(DesignatorIdent designatorIdent) {
		// znaci ovde moram da proverim, ako mu roditelj nije Assignment ili FuncCall
		// jer ako mu je roditelj Assignment, nece biti potreban load, vec ce se
		// vrednost
		// store-ovati u Designator (u DesignatorAssignOp visitu)

		SyntaxNode parent = designatorIdent.getParent();

		if (DesignatorAssignOp.class != parent.getClass() && ReadDesignator.class != parent.getClass())
			Code.load(designatorIdent.obj);
	}

	public void visit(DesignatorBrackets designatorBrackets) {
		// load-ovao si Array na stek u designatorIdent smeni
		// load-ovao si Expr takodje pre ove smene
		
		SyntaxNode parent = designatorBrackets.getParent();

		if (DesignatorAssignOp.class != parent.getClass() && ReadDesignator.class != parent.getClass())
			Code.load(designatorBrackets.obj);
	}

	// --------------------------------------------------------
	// (VMFactor)

	public void visit(FactorNewExpr factorNewExpr) {

		Code.put(Code.newarray);
		// sada treba namestiti byte b iz instrukcije newarray b
		if (factorNewExpr.obj.getType().getElemType().getKind() == Struct.Char) {
			Code.put(0);
		} else {
			Code.put(1);
		}
	}

	// --------------------------------------------------------
	// (VMMeth)

	public void visit(MethodDeclaration methodDeclaration) {
		// pandan za enter sto smo zvali na pocetku metode
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(MethTypeName methTypeName) {

		if ("main".equalsIgnoreCase(methTypeName.getMethName())) {
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

	public void visit(MethVoidName methVoidName) {
		if ("main".equalsIgnoreCase(methVoidName.getMethName())) {
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
	
	// ---------------------------------------------------------------
	//(VMMulop)
	
	public void visit(MulopFactorList mulopFactorList) {
		if( muloperation.equals("mnozenje")) {
			Code.put(Code.mul);
			muloperation = "";
		} else if( muloperation.equals("deljenje")) {
			Code.put(Code.div);
			muloperation = "";
		} else if( muloperation.equals("ostatak")) {
			Code.put(Code.rem);
			muloperation = "";
		}
	}
	
	public void visit(Mul mul) {
		muloperation = "mnozenje";
	}
	
	public void visit(Div div) {
		muloperation = "deljenje";
	}
	
	public void visit(Mod mod) {
		muloperation = "ostatak";
	}
	
	// -------------------------------------------------------
	// (VMAddop)
	
	public void visit(AddopTerminalList addopTerminalList) {
		if( addoperation.equals("sabiranje")) {
			Code.put(Code.add);
			addoperation = "";
		} else if( addoperation.equals("oduzimanje")) {
			Code.put(Code.sub);
			addoperation = "";
		}
	}
	
	public void visit(Plus plus) {
		addoperation = "sabiranje";
	}
	
	public void visit(Minus minus) {
		addoperation = "oduzimanje";
	}
	
	// ------------------------------------------------------------
	// (VMExpr)
	
	public void visit(Expression expression){
		if( isMinus == 1 ){
			Code.put(Code.neg);
			isMinus = 0;
		}
	}
	
	public void visit(MinusOr minusOr){
		isMinus = 1;
	}
	
}
