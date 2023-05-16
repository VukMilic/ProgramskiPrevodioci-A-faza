package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import java.util.ArrayList;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	int nVars = 0;
	
	boolean errorDetected = false;
	boolean errorType = false;
	boolean retDetected = false;
	
	Obj currentMethod = null;
	Struct currentType = null;
	int currWhileLevel = 0;
	int currForeachLevel = 0;
	
	int isArray = 0;
	int isMinus = 0;
	
	ArrayList<Obj> designatorListObjects = new ArrayList<>();
	Struct desigListObjType = null;
	
	Logger log = Logger.getLogger(getClass());
	
	// ----------------------------------------------------------------------------
	// funkcije za prijavljivanje gresaka i informacija
	
	public String getTypeName(int typeNum) {
		switch(typeNum) {
		case Struct.None:
			return "Void";
		case Struct.Int:
			return "Int";
		case Struct.Char:
			return "Char";
		case Struct.Bool:
			return "Bool";
		case Struct.Array:
			return "Array";
		case Struct.Class:
			return "Class";
		case Struct.Interface:
			return "Interface";
		case Struct.Enum:
			return "Enum";
		default:
			return "";
		}
	}
	
	public String getKindName(int kindNum) {
		switch(kindNum) {
		case Obj.Con:
			return "Con";
		case Obj.Var:
			return "Var";
		case Obj.Type:
			return "Type";
		case Obj.Meth:
			return "Meth";
		case Obj.Elem:
			return "Elem";
		case Obj.Prog:
			return "Prog";
		case Obj.Fld:
			return "Fld";
		default:
			return "";
		}
	}
	
	public boolean passed() {
		return !errorDetected;
	}
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}
	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}

	// --------------------------------------------------------------------------------------------
	// vezano za semantiku pocetka programa (VMProgram)
		
    public void visit(ProgName progName){
    	progName.obj  = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
    	Tab.openScope();
    }

    public void visit(Program program){
    	Tab.chainLocalSymbols(program.getProgName().obj);
    	Tab.closeScope();
    }

	// --------------------------------------------------
	// vezano za Tip (VMType)
	
	public void visit(Type type){		
		// prvo proveravamo da li postoji type u Tabeli Simbola
		Obj typeNode = Tab.find(type.getTypeName());
		if( typeNode == Tab.noObj){
			// u slucaju da ne postoji treba proveriti da li je on mozda Boolean tip
			if( type.getTypeName().equals("Bool")) {
				type.struct = new Struct(Struct.Bool);
			} else {
				// ako nije ni Boolean, izbaci gresku
				errorType = true;
				report_error("Greska: Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", type);
				type.struct = Tab.noType;
			}
		}else{
			// ako ga ima u tabeli simbola, onda proveri da li se radi o Tipu ili o necemu drugom iz tabele simbola
			if( typeNode.getKind() == Obj.Type){
				// ako je tip, to je to 
				type.struct = typeNode.getType();
			}else{
				// ako je u pitanju nesto drugo izbaci gresku
				errorType = true;
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip! ", type);
				type.struct = Tab.noType;
			}
		}
		currentType = type.struct;
	}    

	// ----------------------------------------------------------------------------
	// da li je u pitanju niz (VMArray)
    
    public void visit(SquareBrackets squareBrackets) {
    	isArray = 1;
    }
	
    // ----------------------------------------------------------------------------
    // da li je u pitanju minus (VMMinus)
    
    public void visit(MinusOr minusOr) {
    	isMinus = 1;
    }
    
	// ----------------------------------------------------------------------------
	// sve vezano za semantiku lokalnih promenljivih (VMVardecl)
	
	public void visit(VarDecl varDecl){
    	currentType = null;
	}
    
	public void visit(VariableTypeList variableTypeList) {
    	if( errorType ) {
    		errorType = false;
    	} else {
    		Obj obj = Tab.find(variableTypeList.getVarName());
        	if (obj == Tab.noObj) {
        		if(isArray == 1) {
        			// ako je u pitanju deklaracija niza
            		Obj objPrint = Tab.insert(Obj.Var, variableTypeList.getVarName(), new Struct(Struct.Array, currentType));
            		isArray = 0;
            		report_info("Deklarisana lokalni niz " + variableTypeList.getVarName() + " na liniji " + variableTypeList.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + getTypeName(objPrint.getType().getKind()) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
        		}else {
        			// ako je u pitanju obicna lokalna promenljiva
            		Obj objPrint = Tab.insert(Obj.Var, variableTypeList.getVarName(), currentType);
            		report_info("Deklarisana lokalna promenljiva " + variableTypeList.getVarName() + " na liniji " + variableTypeList.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + getTypeName(objPrint.getType().getKind()) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
            		nVars++;
        		}
        	}else {
        		report_error("Greska: promenljiva " + variableTypeList.getVarName() + " je vec deklarisana! ", variableTypeList);
        	}
    	}
	}
	
	public void visit(OneVarType oneVarTypeList) {
		if( errorType ) {
			errorType = false;
		} else {
			Obj obj = Tab.find(oneVarTypeList.getVarName());
	    	if (obj == Tab.noObj) {
	    		if(isArray == 1) {
	    			// ako je u pitanju deklaracija niza
	        		Obj objPrint = Tab.insert(Obj.Var, oneVarTypeList.getVarName(), new Struct(Struct.Array, currentType));
	        		isArray = 0;
	        		report_info("Deklarisan lokalni niz " + oneVarTypeList.getVarName() + " na liniji " + oneVarTypeList.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + getTypeName(objPrint.getType().getKind()) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
	    		}else {
	    			// ako je u pitanju obicna globalna promenljiva
	        		Obj objPrint = Tab.insert(Obj.Var, oneVarTypeList.getVarName(), currentType);
	        		report_info("Deklarisana lokalna promenljiva " + oneVarTypeList.getVarName() + " na liniji " + oneVarTypeList.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + getTypeName(objPrint.getType().getKind()) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
	        		nVars++;
	    		}
	    	}else {
	    		report_error("Greska: promenljiva " + oneVarTypeList.getVarName() + " je vec deklarisana! ", oneVarTypeList);
	    	}
		}
	}
	
    // --------------------------------------------------------------------------------------------------------
    // sve vezano za semantiku globalnih promenljivih (VMGlobalVar)
    
    public void visit(GlobalVarDecl globalVarDecl) {
    	currentType = null;
    }
    
    public void visit(GlobalVarIdent globalVarIdent) {
    	if( errorType ) {
    		errorType = false;
    	} else {
        	Obj obj = Tab.find(globalVarIdent.getGlobVarName());
        	if (obj == Tab.noObj) {
        		if(isArray == 1) {
        			// ako je u pitanju deklaracija niza
            		Obj objPrint = Tab.insert(Obj.Var, globalVarIdent.getGlobVarName(), new Struct(Struct.Array, currentType));
            		objPrint.setLevel(0);
            		report_info("Deklarisan globalni niz " + globalVarIdent.getGlobVarName() + " na liniji " + globalVarIdent.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + getTypeName(objPrint.getType().getKind()) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
            		isArray = 0;
        		}else {
        			// ako je u pitanju obicna globalna promenljiva
            		Obj objPrint = Tab.insert(Obj.Var, globalVarIdent.getGlobVarName(), currentType);
            		objPrint.setLevel(0);
            		report_info("Deklarisana globalna promenljiva " + globalVarIdent.getGlobVarName() + " na liniji " + globalVarIdent.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + getTypeName(objPrint.getType().getKind()) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
            		nVars++;
        		}
        	}else {
        		report_error("Greska: promenljiva " + globalVarIdent.getGlobVarName() + " je vec deklarisana! ", globalVarIdent);
        	}
    	}
    }
	
    // ---------------------------------------------------------------------------------------------------------
    // vezano za semantiku metoda (VMMethod)

    public void visit(MethodDeclaration methodDeclaration){
    	if( retDetected ){
    		Tab.chainLocalSymbols(currentMethod);
        	Tab.closeScope();
        	
        	currentMethod = null;  
        	retDetected = false;
    	} else {
    		report_error("Greska: Ne postoji return naredba u funkciji " + methodDeclaration.getMethodTypeName().obj.getName(), methodDeclaration);
    	}
    }

    public void visit(MethTypeName methTypeName){
    	
    	methTypeName.obj = Tab.insert(Obj.Meth, methTypeName.getMethName(), methTypeName.getType().struct);
    	currentMethod = methTypeName.obj;
    	
    	Tab.openScope();
    	//report_info("Obradjuje se funkcija " + methTypeName.getMethName(), methTypeName);
    }
    
    public void visit(MethVoidName methVoidName) {
    	
    	methVoidName.obj = Tab.insert(Obj.Meth, methVoidName.getMethName(), Tab.noType);
    	currentMethod = methVoidName.obj;
    	
    	Tab.openScope();
    	//report_info("Obradjuje se funkcija " + methVoidName.getMethName(), methVoidName);    	
    }
    
    public void visit(ReturnExpr returnExpr) {
    	retDetected = true;
    	if( currentMethod == null ) {
    		report_error("Greska: naredba RETURN mora biti unutar metode", returnExpr);
    	} else {
    		if( (returnExpr.getExprOrNot().obj == Tab.noObj && currentMethod.getType() == Tab.noType ) 
    				|| ( returnExpr.getExprOrNot().obj != Tab.noObj && returnExpr.getExprOrNot().obj.getType().getKind() == currentMethod.getType().getKind() ) ) {
    			//report_info("Tip RETURN naredbe se poklapa sa tipom Metode", returnExpr);
    		} else {
    			report_error("Greska: Tip RETURN naredbe se ne poklapa sa tipom Metode", returnExpr);
    		}
    	}
    }
    
    // -------------------------------------------------------------------------------------------------------
    // sve vezano za semantiku konstanti (VMConst)
    
    public void visit(NumConstDecl numConstDecl) {
    	currentType = null;
    }
    
    public void visit(CharConstDecl charConstDecl) {
    	currentType = null;
    }
    
    public void visit(BoolConstDecl boolConstDecl) {
    	currentType = null;
    }
    
    // Int Const
    public void visit(ConstantTypeNumList constantTypeNumList) {
    	if( currentType.getKind() == Struct.Int ) {
    		Obj obj = Tab.find(constantTypeNumList.getConstName());
    		if( obj == Tab.noObj ) {
        		Obj refobj = Tab.insert(Obj.Con, constantTypeNumList.getConstName(), currentType);
        		refobj.setAdr(constantTypeNumList.getConstVal());
        		report_info("Deklarisana konstanta " + constantTypeNumList.getConstName() + " na liniji " + constantTypeNumList.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType().getKind()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
    		} else {
        		report_error("Greska: konstanta " + constantTypeNumList.getConstName() + " je vec deklarisana! ", constantTypeNumList);
    		}
    	} else {
    		report_error("Greska: ocekivan tip konstante " + constantTypeNumList.getConstName() + " je Int! ", constantTypeNumList);
    	}
    }
    
    public void visit(OneConstTypeNum oneConstTypeNum) {
    	if( currentType.getKind() == Struct.Int ) {
    		Obj obj = Tab.find(oneConstTypeNum.getConstName());
    		if( obj == Tab.noObj ) {
    			Obj refobj = Tab.insert(Obj.Con, oneConstTypeNum.getConstName(), currentType);
    			refobj.setAdr(oneConstTypeNum.getConstVal());
        		report_info("Deklarisana konstanta " + oneConstTypeNum.getConstName() + " na liniji " + oneConstTypeNum.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType().getKind()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
    		} else {
        		report_error("Greska: konstanta " + oneConstTypeNum.getConstName() + " je vec deklarisana! ", oneConstTypeNum);
    		}
    	} else {
    		report_error("Greska: ocekivan tip konstante " + oneConstTypeNum.getConstName() + " je Int! ", oneConstTypeNum);
    	}
    }
    
    // Char const
    public void visit(ConstantTypeCharList constantTypeCharList) {
    	if( currentType.getKind() == Struct.Char ) {
    		Obj obj = Tab.find(constantTypeCharList.getConstName());
    		if( obj == Tab.noObj ) {
        		Obj refobj = Tab.insert(Obj.Con, constantTypeCharList.getConstName(), currentType);
        		refobj.setAdr(constantTypeCharList.getConstVal().charAt(1));
        		report_info("Deklarisana konstanta " + constantTypeCharList.getConstName() + " na liniji " + constantTypeCharList.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType().getKind()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
    		} else {
        		report_error("Greska: konstanta " + constantTypeCharList.getConstName() + " je vec deklarisana! ", constantTypeCharList);
    		}
    	} else {
    		report_error("Greska: ocekivan tip konstante " + constantTypeCharList.getConstName() + " je Char! ", constantTypeCharList);
    	}
    }
    
    public void visit(OneConstTypeChar oneConstTypeChar) {
    	if( currentType.getKind() == Struct.Char ) {
    		Obj obj = Tab.find(oneConstTypeChar.getConstName());
    		if( obj == Tab.noObj ) {
        		Obj refobj = Tab.insert(Obj.Con, oneConstTypeChar.getConstName(), currentType);
    			refobj.setAdr(oneConstTypeChar.getConstVal().charAt(1));
    			report_info("Deklarisana konstanta " + oneConstTypeChar.getConstName() + " na liniji " + oneConstTypeChar.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType().getKind()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
    		} else {
        		report_error("Greska: konstanta " + oneConstTypeChar.getConstName() + " je vec deklarisana! ", oneConstTypeChar);
    		}
    	} else {
    		report_error("Greska: ocekivan tip konstante " + oneConstTypeChar.getConstName() + " je Char! ", oneConstTypeChar);
    	}
    }

    // Bool const
    public void visit(ConstantTypeBoolList constantTypeBoolList) {
    	if( currentType.getKind() == Struct.Bool ) {
    		Obj obj = Tab.find(constantTypeBoolList.getConstName());
    		if( obj == Tab.noObj ) {
        		Obj refobj = Tab.insert(Obj.Con, constantTypeBoolList.getConstName(), currentType);
        		if( ("true").equals(constantTypeBoolList.getConstVal().replaceAll("\"", "")) )
            		refobj.setAdr(1);
            	else
            		refobj.setAdr(0);
        		report_info("Deklarisana konstanta " + constantTypeBoolList.getConstName() + " na liniji " + constantTypeBoolList.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType().getKind()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
    		} else {
        		report_error("Greska: konstanta " + constantTypeBoolList.getConstName() + " je vec deklarisana! ", constantTypeBoolList);
    		}
    	} else {
    		report_error("Greska: ocekivan tip konstante " + constantTypeBoolList.getConstName() + " je Bool! ", constantTypeBoolList);
    	}    	
    }

    public void visit(OneConstTypeBool oneConstTypeBool) {
    	if( currentType.getKind() == Struct.Bool ) {
    		Obj obj = Tab.find(oneConstTypeBool.getConstName());
    		if( obj == Tab.noObj ) {
    			Obj refobj = Tab.insert(Obj.Con, oneConstTypeBool.getConstName(), currentType);
    			if( ("true").equals(oneConstTypeBool.getConstVal().replaceAll("\"", "")) )
            		refobj.setAdr(1);
            	else
            		refobj.setAdr(0);
    			report_info("Deklarisana konstanta " + oneConstTypeBool.getConstName() + " na liniji " + oneConstTypeBool.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType().getKind()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
    		} else {
        		report_error("Greska: konstanta " + oneConstTypeBool.getConstName() + " je vec deklarisana! ", oneConstTypeBool);
    		}
    	} else {
    		report_error("Greska: ocekivan tip konstante " + oneConstTypeBool.getConstName() + " je Bool! ", oneConstTypeBool);
    	}    	
    }
    
    // --------------------------------------------------------------------------------------------------------------------------------
    // sve vezano za semantiku designatora (VMDesignator)
    
    public void visit(DesignatorIdent designatorIdent) {
    	Obj obj = Tab.find(designatorIdent.getDesigName());
    	if( obj == Tab.noObj ) {
    		report_error("Greska: Simbol " + designatorIdent.getDesigName() + " koji se koristi nije deklarisan! ", designatorIdent);
    		designatorIdent.obj = Tab.noObj;
    	} else {    		
    		report_info("Simbol " + designatorIdent.getDesigName() + " se koristi na liniji " + designatorIdent.getLine() + ", objektni cvor: " + getKindName(obj.getKind()) + " " + obj.getName() + ": " + getTypeName(obj.getType().getKind()) + ", " + obj.getAdr() + ", " + obj.getLevel(), null);
    		designatorIdent.obj = obj;
    	}
    }
    
    public void visit(DesignatorDot designatorDot) {
    	// u slucaju da radis i klase, ovde bi designatorDot morao da bude Field
    }
    
    public void visit(DesignatorBrackets designatorBrackets) {
    	if( designatorBrackets.getDesignator().obj.getType().getKind() == Struct.Array ) {
    		if( designatorBrackets.getExpr().obj.getType().getKind() == Struct.Int ) {
    			// TODO: ako je sve okej, ovde radis sa nizom tako da ces morati nesto da promenis
    			
    			designatorBrackets.obj = new Obj(Obj.Elem, "elem", designatorBrackets.getDesignator().obj.getType().getElemType());
    		} else {
        		report_error("Greska: prilikom dohvatanja elementa niza vrednost u uglastim zagradama mora biti tipa int", designatorBrackets);
        		designatorBrackets.obj = Tab.noObj;
    		}
    	} else {
    		report_error("Greska: ne smeju se koristiti uglaste zagrade nakon simbola koji nije tipa Array", designatorBrackets);
    		designatorBrackets.obj = Tab.noObj;
    	}
    }
    
    public void visit(DesignatorAssignOp designatorAssignOp) {
    	// TODO: dodela vrednosti
    	
    	if ( designatorAssignOp.getDesignator().obj.getKind() == Obj.Var 
    			|| designatorAssignOp.getDesignator().obj.getKind() == Obj.Elem ) {
    		
        	if( designatorAssignOp.getDesignator().obj.getType().getKind() == designatorAssignOp.getExpr().obj.getType().getKind() ){
        		// ovde bi sad isla dodela vrednosti Expr u Designator
        		//report_info("Tipovi se poklapaju prilikom dodele vrednosti", designatorAssignOp);
        		
        	}else {
        		if( designatorAssignOp.getDesignator().obj != Tab.noObj )
        			report_error("Greska: tipovi se ne poklapaju prilikom dodele vrednosti! ", designatorAssignOp);
        	}
    	} else {
    		report_error("Greska: ne moze da se uradi dodela vrednosti tipu objekta " + designatorAssignOp.getDesignator().obj.getName(), designatorAssignOp);
    	}
    }
    
    public void visit(DesignatorActPars designatorActPars) {
    	// TODO: ovde ces videti sta treba da se doda da bi se funkcija uspesno izvrsila
    	
    	if( designatorActPars.getDesignator().obj.getKind() == Obj.Meth ) {
    		//report_info("Funkcija " + designatorActPars.getDesignator().obj.getName() + "() moze uspesno da se izvrsi", designatorActPars);
    	} else {
    		report_error("Greska: objekat " + designatorActPars.getDesignator().obj.getName() + " ne moze da se koristi kao funkcija" , designatorActPars);
    	}
    }
    
    public void visit(DesignatorPlusPlus designatorPlusPlus) {
    	// TODO: potrebno izmeniti za Array i Class
    	
    	if( designatorPlusPlus.getDesignator().obj.getKind() == Obj.Var && designatorPlusPlus.getDesignator().obj != Tab.noObj) {
    		if( designatorPlusPlus.getDesignator().obj.getType().getKind() == Struct.Int ) {
        		//report_info("Operacija ++ moze uspesno da se izvrsi", designatorPlusPlus);
        	}else {
        		report_error("Greska: Za operaciju ++ tip objekta " + designatorPlusPlus.getDesignator().obj.getName() + " mora biti int", designatorPlusPlus);
        	}
    	} else {
    		report_error("Greska: ne moze da se izvrsi operacija ++ nad objektom " + designatorPlusPlus.getDesignator().obj.getName(), designatorPlusPlus);
    	}
    }
    
    public void visit(DesignatorMinusMinus designatorMinusMinus) {
    	// TODO: potrebno izmeniti za Array i Class
    	
    	if( designatorMinusMinus.getDesignator().obj.getKind() == Obj.Var && designatorMinusMinus.getDesignator().obj != Tab.noObj ) {
    		if( designatorMinusMinus.getDesignator().obj.getType().getKind() == Struct.Int ) {
        		//report_info("Operacija -- moze uspesno da se izvrsi", designatorMinusMinus);
        	}else {
        		report_error("Greska: Za operaciju -- tip objekta " + designatorMinusMinus.getDesignator().obj.getName() + " mora biti int", designatorMinusMinus);
        	}
    	} else {
    		report_error("Greska: ne moze da se izvrsi operacija -- nad objektom " + designatorMinusMinus.getDesignator().obj.getName(), designatorMinusMinus);
    	}
    }
    
    public void visit(DesignatorYes designatorYes) {
    	designatorYes.obj = designatorYes.getDesignator().obj;
    }
    
    public void visit(DesignatorNot designatorNot) {
    	designatorNot.obj = Tab.noObj;
    }
    
    public void visit(DesigList desigList) {
    	// TODO: moras da dodas i da ovo radi za element niza (prvi if)
    	if( desigList.getDesignatorOrNot().obj.getKind() == Obj.Var /* element niza */ ) {
    		// prvo proveri da li se poklapaju elementi odvojeni zarezom
        	if( desigListObjType == null ) {
        		if( desigList.getDesignatorOrNot().obj != Tab.noObj ) {
        			desigListObjType = desigList.getDesignatorOrNot().obj.getType();
        		}
        	} else {
        		if( desigList.getDesignatorOrNot().obj != Tab.noObj ) {
        			if( desigListObjType != desigList.getDesignatorOrNot().obj.getType() ) {
        				report_error("Greska: Tip elementa " + desigList.getDesignatorOrNot().obj.getAdr() + " se ne poklapa sa tipom niza", desigList);
        				return;
        			}
        		}
        	}
        	
        	// ubacujem u listu objekata za operaciju [ ... , , ] = Array
        	designatorListObjects.add(desigList.getDesignatorOrNot().obj);
    	} else {
    		report_error("Greska: Prilikom dodele izmedju nizova, sa leve strane objekat "+ desigList.getDesignatorOrNot().obj.getName() + " mora biti promenljiva ili element niza", desigList);
    	}    	
    }
    
    public void visit(OneDesigList oneDesigList) {
    	// ubacujem u listu objekata za operaciju [ ... , , ] = Array
    	designatorListObjects.add(oneDesigList.getDesignatorOrNot().obj);
    	
    	// dohvatam tip koji treba da bude tip svih elemenata ListArray-a, ako ovaj element postoji
    	if( oneDesigList.getDesignatorOrNot().obj != Tab.noObj ) {
    		desigListObjType = oneDesigList.getDesignatorOrNot().obj.getType();
    	}
    }
    
    public void visit(DesignatorStmtTwo designatorStmtTwo) {
    	    	
    	if( designatorStmtTwo.getDesignator().obj.getType().getKind() == Struct.Array ) {
    		if( designatorStmtTwo.getDesignator().obj.getType().getElemType() == desigListObjType ) {
    			// TODO: ovde treba da se odradi dodela vrednosti
    			
    			//report_info("Tipovi se poklapaju prilikom dodele vrednosti nizu", designatorStmtTwo);
    		} else {
    			report_error("Greska: Tipovi se ne poklapaju prilikom dodele vrednosti izmedju dva niza", designatorStmtTwo);
    		}
    	} else {
    		report_error("Greska: Prilikom dodele vrednosti nizu, sa desne strane mora biti niz", designatorStmtTwo);
    	}
    	
    	desigListObjType = null;
    	designatorListObjects.clear();
    }
    
    // -------------------------------------------------------------------------------------------------------------------------------
    // sve vezano za Factor (VMFactor)
    
    public void visit(FactorDesignator factorDesignator) {
    	factorDesignator.obj = factorDesignator.getDesignator().obj;
    }
    
    public void visit(FactorMethod factorMethod) {
    	if( factorMethod.getDesignator().obj.getKind() != Obj.Meth ) {
    		report_error("Greska: Simbol mora oznacavati globalnu funkciju", factorMethod);
    		factorMethod.obj = Tab.noObj;
    	} else {
    		report_info("Funkcija "+ factorMethod.getDesignator().obj.getName() +" se uspesno poziva", factorMethod);
    		factorMethod.obj = factorMethod.getDesignator().obj;
    	}
    }
    
    public void visit(FactorNumConst factorNumConst) {
    	// napravio sam novi objekat koji sam nazvao nasumicno,  i njega dodelio
    	// objektu Factora, a zatim sam postavio vrednost Factora na NUMCONST vrednost
    	factorNumConst.obj = new Obj(Obj.Con, "constInt", new Struct(Struct.Int));
    	factorNumConst.obj.setAdr(factorNumConst.getConstName());
    }
    
    public void visit(FactorCharConst factorCharConst) {
    	// TODO: ovde sam dodelio novi objekat Factor objektu , ali trenutno nisam siguran
    	// kako da mu dodelim vrednost char konstante (mozda kroz ime objekta... (hakovanje))
    	factorCharConst.obj = new Obj(Obj.Con, "constChar", new Struct(Struct.Char));
    	factorCharConst.obj.setAdr(factorCharConst.getConstName().charAt(1));
    }
    
    public void visit(FactorBoolConst factorBoolConst) {
    	// TODO: ovde sam dodelio novi objekat Factor objektu , ali trenutno nisam siguran
    	// kako da mu dodelim vrednost Bool konstante (mozda kroz ime objekta... (hakovanje))
    	factorBoolConst.obj = new Obj(Obj.Con, "constBool", new Struct(Struct.Bool));
    	if( ("true").equals(factorBoolConst.getConstName().replaceAll("\"", "")) )
    		factorBoolConst.obj.setAdr(1);
    	else
    		factorBoolConst.obj.setAdr(0);
    }
    
    public void visit(FactorNewExpr factorNewExpr) {
    	// TODO: ovde trenutno samo pravim objekat koji ce da bude tipa Type
    	// za dalje dodeljivanje vrednosti ovom objektu ili bilo sta sledece
    	// moram da ostavim za kasnije
    	
    	if( factorNewExpr.getExpr().obj.getType().getKind() == Struct.Int ) {
        	factorNewExpr.obj = new Obj(Obj.Var, "factorNew", factorNewExpr.getType().struct);    		
    	} else {
    		report_error("Greska: Tip izraza prilikom konstrukcije objekta, NEW metodom, mora biti int", factorNewExpr);
    		factorNewExpr.obj = Tab.noObj;
    	}
    }
    
    public void visit(FactorNewActPars factorNewActPars) {
    	factorNewActPars.obj = new Obj(Obj.Var, "factorNew", factorNewActPars.getType().struct);
    }
    
    public void visit(FactorExpr factorExpr) {
    	// TODO: 
    	// e sad ovde ne znam trenutno kako da izvucem tip iz Expr neterminala
    	// znaci ovde moram cak i to da uradim , pored kasnijeg dodeljivanja vrednosti
    }
    
    public void visit(MulopFactorList mulopFactorList) {
    	// TODO: opet todo... ovde ces imati zadatak verovatno da racunas (mnozis, delis ...)
    	// tako da ove dodele Factor objekta ovom objektu vervotano nece biti
    	
    	if ( mulopFactorList.getFactor().obj.getType().getKind() == Struct.Int ) {
    		mulopFactorList.obj = mulopFactorList.getFactor().obj;
    	} else {
    		report_error("Greska: tip za operacije mnozenja i deljenja mora biti int! ", mulopFactorList);
    		mulopFactorList.obj = Tab.noObj;
    	}
    }
    
    public void visit(NoMulopFactList noMulopFactList) {
    	noMulopFactList.obj = Tab.noObj;
    }
    
    public void visit(AddopTerminalList addopTerminalList) {
    	// TODO: isto
    	
    	if ( addopTerminalList.getTerm().obj.getType().getKind() == Struct.Int ) {
    		addopTerminalList.obj = addopTerminalList.getTerm().obj;
    	} else {
    		report_error("Greska: tip za operacije sabiranja i oduzimanja mora biti int! ", addopTerminalList);
    		addopTerminalList.obj = Tab.noObj;
    	}
    }
    
    public void visit(NoAddopTermList noAddopTermList) {
    	noAddopTermList.obj = Tab.noObj;
    }
    
    public void visit(Terminal terminal) {
    	// TODO: isto
    	
    	if( terminal.getMulopFactList().obj == Tab.noObj ) {
    		terminal.obj = terminal.getFactor().obj;
    	} else {
    		// u slucaju da postoji MulopFactList objekat znaci da Factor mora da bude tipa int
    		if( terminal.getFactor().obj.getType().getKind() == Struct.Int ) {
    			terminal.obj = terminal.getFactor().obj;
    		} else {
    			report_error("Greska: tip za operacije mnozenja i deljenja mora biti int! ", terminal);
    			terminal.obj = Tab.noObj;
    		}
    	}
    }

    // ------------------------------------------------------------------------------------------------
    // sve vezano za Expr (VMExpr)
    
    public void visit(Expression expression) {
    	// TODO: isto
    	
    	if( isMinus == 0 ) {
    		// u slucaju da nema minusa, moze da bude bilo koji tip
    		if( expression.getAddopTermList().obj == Tab.noObj ) {
    			// u slucaju da nema AddopTermListe Term moze biti bilo koji tip
    			expression.obj = expression.getTerm().obj;
        	} else {
        		// u slucaju da postoji AddopTermLista objekat znaci da Term mora da bude tipa int
        		if( expression.getTerm().obj.getType().getKind() == Struct.Int ) {
        			expression.obj = expression.getTerm().obj;
        		} else {
        			report_error("Greska: tip za operacije sabiranja i oduzimanja mora biti int! ", expression);
        			expression.obj = Tab.noObj;
        		}
        	}
    	}else {
    		// u slucaju da minus postoji, tip mora biti integer
    		if( expression.getTerm().obj.getType().getKind() == Struct.Int ) {
    			expression.obj = expression.getTerm().obj;
    		}else {
    			report_error("Greska: tip posle minusa mora biti int! ", expression);
    			expression.obj = Tab.noObj;
    		}
    		
    		isMinus = 0;
    	}
    } 
    
    public void visit(ExpressionYes expressionYes) {
    	expressionYes.obj = expressionYes.getExpr().obj;
    }
    
    public void visit(ExpressionNot expressionNot) {
    	expressionNot.obj = Tab.noObj;
    }
    
    // -----------------------------------------------------------------------------------------------
    // sve vezano za While Statement (VMWhile)
    
    public void visit(WhileTerminal whileTerminal) {
    	currWhileLevel++;
    }
    
    public void visit(WhileStmt whileStmt) {
    	currWhileLevel--;
    }
    
    public void visit(ForeachTerm foreachTerm) {
    	currForeachLevel++;
    }
    
    public void visit(DesignatorForEach designatorForEach) {
    	currForeachLevel--;
    }
    
    public void visit(BreakStmt breakStmt) {
    	if( currWhileLevel == 0 && currForeachLevel == 0) {
    		report_error("Greska: BREAK se poziva izvan While ili Foreach petlje" , null);
    	} else {
    		// TODO: e ovde bi onda isla neka racunica gde bismo izasli iz
    		// while petlje i smanjili currWhileLevel
    	}
    }
    
    public void visit(ContinueStmt continueStmt) {
    	if( currWhileLevel == 0 && currForeachLevel == 0) {
    		report_error("Greska: CONTINUE se poziva izvan While ili Foreach petlje" , null);
    	} else {
    		// TODO: e ovde bi onda isla neka racunica gde bismo izasli iz
    		// while petlje i smanjili currWhileLevel
    	}	
    }
    
    // ---------------------------------------------------------------------------------------------
    // sve vezano za Read i Print Statement (VMRead) (VMPrint)
    
    public void visit(ReadDesignator readDesignator) {
    	if( readDesignator.getDesignator().obj.getKind() == Obj.Var ||  readDesignator.getDesignator().obj.getKind() == Obj.Elem) {
    		if( readDesignator.getDesignator().obj.getType().getKind() == Struct.Int 
    				|| readDesignator.getDesignator().obj.getType().getKind() == Struct.Char 
    				|| readDesignator.getDesignator().obj.getType().getKind() == Struct.Bool) {
        		// TODO: ovde bi isla funkcionalnost read-a
    			
    		} else {
    			report_error("Greska: READ mora da se pozove nad tipom int, char ili Bool", readDesignator);
    		}
    		
    	} else {
    		report_error("Greska: READ mora da se pozove nad promenljivom", readDesignator);
    	}
    }
    
    public void visit(PrintStmt printStmt) {
    	if( printStmt.getExpr().obj.getType().getKind() == Struct.Int 
    			|| printStmt.getExpr().obj.getType().getKind() == Struct.Char
    			|| printStmt.getExpr().obj.getType().getKind() == Struct.Bool) {
    		// TODO: ovde bi isla funkcionalnost print-a
    		
    	} else {
    		report_error("Greska: PRINT mora da se pozove nad tipom int, char ili Bool", printStmt);
    	}
    }
    
    // -----------------------------------------------------------------------------------------------------------
    // sve vezano za semantiku Condition-a iz if-a kao i iz While-a (VMCondition)

    public void visit(CondExprRelopExpr condExprRelopExpr) {
    	if( condExprRelopExpr.getExpr().obj.getType().getKind() != condExprRelopExpr.getExpr1().obj.getType().getKind()) {
    		report_error("Greska: Tipovi oba izraza moraju biti kompatibilni u slucaju poredjenja", condExprRelopExpr);
    	}
    }
    
    public void visit(CondExpr condExpr) {
    	if( condExpr.getExpr().obj.getType().getKind() != Struct.Bool ) {
    		report_error("Greska: Uslov mora biti tipa Bool", condExpr);
    	}
    }

}
