package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {

	int nVars = 0;
	int localVars = 0;
	int globalVars = 0;

	
	boolean errorDetected = false;
	boolean errorType = false;
	boolean retDetected = false;
	
	Obj currentMethod = null;
	Struct currentType = null;
	int currScopeLevel = -1;
	int currWhileLevel = 0;
	int currForeachLevel = 0;
	
	int isArray = 0;
	int isMinus = 0;
	int isMatrix = 0;
	
	int mainExists = 0;
	
	HashMap<String, List<Struct>> methodMap = new HashMap<>();
	String methNameForActParams = "";
	int cntActParams = 0;
	
//	ArrayList<Obj> designatorListObjects = new ArrayList<>();
//	Struct desigListObjType = null;
	
	Logger log = Logger.getLogger(getClass());
	
	// ----------------------------------------------------------------------------
	// funkcije za prijavljivanje gresaka i informacija
	
	public String getTypeName(Struct structure) {
		switch(structure.getKind()) {
		case Struct.None:
			return "Void";
		case Struct.Int:
			return "Int";
		case Struct.Char:
			return "Char";
		case Struct.Bool:
			return "Bool";
		case Struct.Array:
			if(structure.getElemType().getKind() == Struct.Array){
				return "Matrix of " + getTypeName(structure.getElemType().getElemType());
			} else {
				return "Array of " + getTypeName(structure.getElemType()) ;				
			}
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
    	currScopeLevel++;
    }

    public void visit(Program program){
    	Tab.chainLocalSymbols(program.getProgName().obj);
    	Tab.closeScope();
    	currScopeLevel--;
    	
    	// na kraju programa proveravamo da li postoji main funkcija u programu
    	if( mainExists == 0 )
    	{
    		report_error("Greska: Ne postoji main funkcija u programu ", null);
    	}
    	// provera da li broj globalnih promenljivih prelazi 65536
    	if( globalVars > 65536 )
    		report_error("Greska: Broj globalnih promenljivih mora biti manji od 65536 ", null);	
    }

	// --------------------------------------------------
	// vezano za Tip (VMType)
	
	public void visit(Type type){		
		// prvo proveravamo da li postoji type u Tabeli Simbola
		Obj typeNode = Tab.find(type.getTypeName());
		if( typeNode == Tab.noObj){
			// izbaci gresku
			errorType = true;
			report_error("Greska: Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", type);
			type.struct = Tab.noType;
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
    
    public void visit(SquareBracketsDouble squareBracketsDouble) {
    	isMatrix = 1;
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
    		// ako je pronadjen istoimeni simbol, da li su u istom scope-u
    		// ako nisu, dozvoli insert
    		if( obj.getLevel() != currScopeLevel )
				obj = Tab.noObj;
    		
        	if (obj == Tab.noObj) {
        		if(isArray == 1) {
        			// ako je u pitanju deklaracija niza
            		Obj objPrint = Tab.insert(Obj.Var, variableTypeList.getVarName(), new Struct(Struct.Array, currentType));
            		nVars++;
            		localVars++;
            		isArray = 0;
            		report_info("Deklarisan lokalni niz " + variableTypeList.getVarName() + " na liniji " + variableTypeList.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + "Array of " + getTypeName(currentType) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
        		} 
        		else if(isMatrix == 1)
        		{
        			// ako je u pitanju deklaracija matrice
            		Obj objPrint = Tab.insert(Obj.Var, variableTypeList.getVarName(), new Struct(Struct.Array, new Struct(Struct.Array, currentType)));
            		nVars++;
            		localVars++;
            		isMatrix = 0;
            		report_info("Deklarisana lokalna matrica " + variableTypeList.getVarName() + " na liniji " + variableTypeList.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + "Matrix of " + getTypeName(currentType) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
        		}
        		else 
        		{
        			// ako je u pitanju obicna lokalna promenljiva
            		Obj objPrint = Tab.insert(Obj.Var, variableTypeList.getVarName(), currentType);
            		report_info("Deklarisana lokalna promenljiva " + variableTypeList.getVarName() + " na liniji " + variableTypeList.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + getTypeName(objPrint.getType()) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
            		nVars++;
            		localVars++;
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
			// ako je pronadjen istoimeni simbol, da li su u istom scope-u
			// ako nisu, dozvoli insert
			if( obj.getLevel() != currScopeLevel )
				obj = Tab.noObj;
			
	    	if (obj == Tab.noObj) {
	    		if(isArray == 1) {
	    			// ako je u pitanju deklaracija niza
	        		Obj objPrint = Tab.insert(Obj.Var, oneVarTypeList.getVarName(), new Struct(Struct.Array, currentType));
	        		nVars++;
            		localVars++;
	        		isArray = 0;
	        		report_info("Deklarisan lokalni niz " + oneVarTypeList.getVarName() + " na liniji " + oneVarTypeList.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + "Array of " + getTypeName(currentType) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
	    		}
	    		else if(isMatrix == 1)
	    		{
	    			// ako je u pitanju deklaracija matrice
	        		Obj objPrint = Tab.insert(Obj.Var, oneVarTypeList.getVarName(), new Struct(Struct.Array, new Struct(Struct.Array, currentType)));
	        		nVars++;
            		localVars++;
	        		isMatrix = 0;
	        		report_info("Deklarisana lokalna matrica " + oneVarTypeList.getVarName() + " na liniji " + oneVarTypeList.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + "Matrix of " + getTypeName(currentType) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
	    		}	    		
	    		else 
	    		{
	    			// ako je u pitanju obicna globalna promenljiva
	        		Obj objPrint = Tab.insert(Obj.Var, oneVarTypeList.getVarName(), currentType);
	        		report_info("Deklarisana lokalna promenljiva " + oneVarTypeList.getVarName() + " na liniji " + oneVarTypeList.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + getTypeName(objPrint.getType()) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
	        		nVars++;
            		localVars++;
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
            		report_info("Deklarisan globalni niz " + globalVarIdent.getGlobVarName() + " na liniji " + globalVarIdent.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + "Array of " + getTypeName(currentType) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
            		nVars++;
            		globalVars++;
            		isArray = 0;
        		}
        		else if(isMatrix == 1)
        		{
        			// ako je u pitanju deklaracija matrice
            		Obj objPrint = Tab.insert(Obj.Var, globalVarIdent.getGlobVarName(), new Struct(Struct.Array, new Struct(Struct.Array, currentType)));
            		objPrint.setLevel(0);
            		report_info("Deklarisana globalna matrica " + globalVarIdent.getGlobVarName() + " na liniji " + globalVarIdent.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + "Matrix of " + getTypeName(currentType) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
            		nVars++;
            		globalVars++;
            		isMatrix = 0;
        		}
        		else 
        		{
        			// ako je u pitanju obicna globalna promenljiva
            		Obj objPrint = Tab.insert(Obj.Var, globalVarIdent.getGlobVarName(), currentType);
            		objPrint.setLevel(0);
            		report_info("Deklarisana globalna promenljiva " + globalVarIdent.getGlobVarName() + " na liniji " + globalVarIdent.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + getTypeName(objPrint.getType()) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
            		nVars++;
            		globalVars++;
        		}
        	}else {
        		report_error("Greska: promenljiva " + globalVarIdent.getGlobVarName() + " je vec deklarisana! ", globalVarIdent);
        	}
    	}
    }
	
    // ---------------------------------------------------------------------------------------------------------
    // vezano za semantiku metoda (VMMethod)

    public void visit(MethodDeclaration methodDeclaration){
    	if( retDetected || currentMethod.getType() == Tab.noType ){
    		Tab.chainLocalSymbols(currentMethod);
        	Tab.closeScope();
        	currScopeLevel--;
        	
        	// proveravamo da li je u pitanju funkcija main
        	// mora biti VOID, zvati se MAIN, biti bez ARGUMENATA
        	if( currentMethod.getType() == Tab.noType 
        		&& currentMethod.getName().equals("main") 
        		&& (methodDeclaration.getFormPars() instanceof NoFormParam))
        		mainExists = 1;
        	else
        		report_info("Deklarisana funkcija " + currentMethod.getName() + " na liniji " + methodDeclaration.getLine() + ", objektni cvor: " + getKindName(currentMethod.getKind()) + " " + currentMethod.getName() + ": " + getTypeName(currentMethod.getType()) + ", " + currentMethod.getAdr() + ", " + currentMethod.getLevel(), null);
        	
        	// proveravamo da li je deklarisano vise lokalnih promenljivih
        	// nego sto sme (256)
        	if( localVars > 256)
        		report_error("Greska: Lokalnih promenljivih ne sme biti vise od 256 " + methodDeclaration.getMethodTypeName().obj.getName(), methodDeclaration);
        	
        	localVars = 0;
        	currentMethod = null;
        	retDetected = false;        	
    	} else {
    		report_error("Greska: Ne postoji return naredba u funkciji " + methodDeclaration.getMethodTypeName().obj.getName(), methodDeclaration);
    	}
    }

    public void visit(MethTypeName methTypeName){
    	
    	methTypeName.obj = Tab.insert(Obj.Meth, methTypeName.getMethName(), methTypeName.getType().struct);
    	currentMethod = methTypeName.obj;
    	
    	// zbog formalnih parametara
    	methodMap.put(currentMethod.getName(), new ArrayList<>());
    	
    	Tab.openScope();
    	currScopeLevel++;
    	//report_info("Obradjuje se funkcija " + methTypeName.getMethName(), methTypeName);
    }
    
    public void visit(MethVoidName methVoidName) {
    	
    	methVoidName.obj = Tab.insert(Obj.Meth, methVoidName.getMethName(), Tab.noType);
    	currentMethod = methVoidName.obj;
    	
    	Tab.openScope();
    	currScopeLevel++;
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
        		report_info("Definisana konstanta " + constantTypeNumList.getConstName() + " na liniji " + constantTypeNumList.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
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
        		report_info("Definisana konstanta " + oneConstTypeNum.getConstName() + " na liniji " + oneConstTypeNum.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
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
        		report_info("Definisana konstanta " + constantTypeCharList.getConstName() + " na liniji " + constantTypeCharList.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
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
    			report_info("Definisana konstanta " + oneConstTypeChar.getConstName() + " na liniji " + oneConstTypeChar.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
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
        		report_info("Definisana konstanta " + constantTypeBoolList.getConstName() + " na liniji " + constantTypeBoolList.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
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
    			report_info("Definisana konstanta " + oneConstTypeBool.getConstName() + " na liniji " + oneConstTypeBool.getLine() + ", objektni cvor: " + getKindName(refobj.getKind()) + " " + refobj.getName() + ": " + getTypeName(refobj.getType()) + ", " + refobj.getAdr() + ", " + refobj.getLevel(), null);
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
    		report_info("Simbol " + designatorIdent.getDesigName() + " se koristi na liniji " + designatorIdent.getLine() + ", objektni cvor: " + getKindName(obj.getKind()) + " " + obj.getName() + ": " + getTypeName(obj.getType()) + ", " + obj.getAdr() + ", " + obj.getLevel(), null);
    		designatorIdent.obj = obj;
    		if(designatorIdent.getParent().getClass() == DesignatorActPars.class ||
    				designatorIdent.getParent().getClass() == FactorMethod.class){
    			// postavljam ime funkcije za koju treba proveriti argumente
    			methNameForActParams = designatorIdent.getDesigName();
    		}
    	}
    }
    
    public void visit(DesignatorDot designatorDot) {
    	// u slucaju da radis i klase, ovde bi designatorDot morao da bude Field
    }
    
    public void visit(DesignatorBrackets designatorBrackets) {
    	if( designatorBrackets.getDesignator().obj.getType().getKind() == Struct.Array ) {
    		if( designatorBrackets.getExpr().obj.getType().getKind() == Struct.Int ) {
    			// TODO: ako je sve okej, ovde radis sa nizom tako da ces morati nesto da promenis
    			if( isMinus == 1 ){
    				report_error("Greska: prilikom dohvatanja elementa niza vrednost u uglastim zagradama ne sme biti negativna vrednost", designatorBrackets);
            		designatorBrackets.obj = Tab.noObj;
            		isMinus = 0;
    			} else {
    				designatorBrackets.obj = new Obj(Obj.Elem, "elem", designatorBrackets.getDesignator().obj.getType().getElemType());
    			}
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
        		if( designatorAssignOp.getDesignator().obj.getType().getKind() == Struct.Array 
        				&& designatorAssignOp.getDesignator().obj.getType().getElemType().getKind() != designatorAssignOp.getExpr().obj.getType().getElemType().getKind()){
        			report_error("Greska: tipovi nizova se ne poklapaju prilikom dodele vrednosti! ", designatorAssignOp);	
        		} 
        		else if( designatorAssignOp.getDesignator().obj.getType().getKind() == Struct.Array
        				&& designatorAssignOp.getDesignator().obj.getType().getElemType().getKind() == Struct.Array
        				&& designatorAssignOp.getDesignator().obj.getType().getElemType().getElemType().getKind() != designatorAssignOp.getExpr().obj.getType().getElemType().getElemType().getKind())
        		{
        			report_error("Greska: tipovi matrica se ne poklapaju prilikom dodele vrednosti! ", designatorAssignOp);
        		}
        		else
        		{
        			// ovde bi sad isla dodela vrednosti Expr u Designator
            		//report_info("Tipovi se poklapaju prilikom dodele vrednosti", designatorAssignOp);	
        		}
        	}else {
        		if((designatorAssignOp.getExpr().obj.getName().equals("null") && designatorAssignOp.getDesignator().obj.getType().getKind() == Struct.Array)){
        			// to je okej, ako je sa leve strane niz ili matrica,a sa desne null
        		} else if( designatorAssignOp.getDesignator().obj != Tab.noObj )
        			report_error("Greska: tipovi se ne poklapaju prilikom dodele vrednosti! ", designatorAssignOp);
        	}
    	} else {
    		report_error("Greska: ne moze da se uradi dodela vrednosti tipu objekta " + designatorAssignOp.getDesignator().obj.getName(), designatorAssignOp);
    	}
    }
    
    public void visit(DesignatorActPars designatorActPars) {
    	// TODO: ovde ces videti sta treba da se doda da bi se funkcija uspesno izvrsila
    	if( designatorActPars.getDesignator().obj.getKind() != Obj.Meth ) {
    		report_error("Greska: Simbol mora oznacavati globalnu funkciju", designatorActPars);
    		designatorActPars.obj = Tab.noObj;
    	} else {
    		// provera za tri ugradjene funkcije - chr, ord i len
    		if( (designatorActPars.getDesignator().obj.getName().equals("chr") && designatorActPars.getActPars().obj.getType().getKind() != Struct.Int) ){
    			report_error("Greska: Prilikom poziva funkcije \"chr\" parametar mora biti tipa Int", designatorActPars);
    			designatorActPars.obj = Tab.noObj;
    		} 
    		else if( (designatorActPars.getDesignator().obj.getName().equals("ord") && designatorActPars.getActPars().obj.getType().getKind() != Struct.Char) ){
    			report_error("Greska: Prilikom poziva funkcije \"ord\" parametar mora biti tipa Char", designatorActPars);
    			designatorActPars.obj = Tab.noObj;
    		} 
    		else if( (designatorActPars.getDesignator().obj.getName().equals("len") && designatorActPars.getActPars().obj.getType().getKind() != Struct.Array) ){
    			report_error("Greska: Prilikom poziva funkcije \"len\" parametar mora biti tipa Array", designatorActPars);
    			designatorActPars.obj = Tab.noObj;
    		}
    		// ako smo prosli sve te provere, sve je okej
    		else{
    			designatorActPars.obj = designatorActPars.getDesignator().obj;
    		}
    	}
    }
    
    public void visit(DesignatorPlusPlus designatorPlusPlus) {
    	// TODO: potrebno izmeniti za Array i Class
    	
    	if( designatorPlusPlus.getDesignator().obj.getKind() == Obj.Var || designatorPlusPlus.getDesignator().obj.getKind() == Obj.Elem) {
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
    	
    	if( designatorMinusMinus.getDesignator().obj.getKind() == Obj.Var || designatorMinusMinus.getDesignator().obj.getKind() == Obj.Elem ) {
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
    
//    public void visit(DesigList desigList) {
//    	// TODO: moras da dodas i da ovo radi za element niza (prvi if)
//    	if( desigList.getDesignatorOrNot().obj.getKind() == Obj.Var /* element niza */ ) {
//    		// prvo proveri da li se poklapaju elementi odvojeni zarezom
//        	if( desigListObjType == null ) {
//        		if( desigList.getDesignatorOrNot().obj != Tab.noObj ) {
//        			desigListObjType = desigList.getDesignatorOrNot().obj.getType();
//        		}
//        	} else {
//        		if( desigList.getDesignatorOrNot().obj != Tab.noObj ) {
//        			if( desigListObjType != desigList.getDesignatorOrNot().obj.getType() ) {
//        				report_error("Greska: Tip elementa " + desigList.getDesignatorOrNot().obj.getAdr() + " se ne poklapa sa tipom niza", desigList);
//        				return;
//        			}
//        		}
//        	}
//        	
//        	// ubacujem u listu objekata za operaciju [ ... , , ] = Array
//        	designatorListObjects.add(desigList.getDesignatorOrNot().obj);
//    	} else {
//    		report_error("Greska: Prilikom dodele izmedju nizova, sa leve strane objekat "+ desigList.getDesignatorOrNot().obj.getName() + " mora biti promenljiva ili element niza", desigList);
//    	}    	
//    }
//    
//    public void visit(OneDesigList oneDesigList) {
//    	// ubacujem u listu objekata za operaciju [ ... , , ] = Array
//    	designatorListObjects.add(oneDesigList.getDesignatorOrNot().obj);
//    	
//    	// dohvatam tip koji treba da bude tip svih elemenata ListArray-a, ako ovaj element postoji
//    	if( oneDesigList.getDesignatorOrNot().obj != Tab.noObj ) {
//    		desigListObjType = oneDesigList.getDesignatorOrNot().obj.getType();
//    	}
//    }
//    
//    public void visit(DesignatorStmtTwo designatorStmtTwo) {
//    	    	
//    	if( designatorStmtTwo.getDesignator().obj.getType().getKind() == Struct.Array ) {
//    		if( designatorStmtTwo.getDesignator().obj.getType().getElemType() == desigListObjType ) {
//    			// TODO: ovde treba da se odradi dodela vrednosti
//    			
//    			//report_info("Tipovi se poklapaju prilikom dodele vrednosti nizu", designatorStmtTwo);
//    		} else {
//    			report_error("Greska: Tipovi se ne poklapaju prilikom dodele vrednosti izmedju dva niza", designatorStmtTwo);
//    		}
//    	} else {
//    		report_error("Greska: Prilikom dodele vrednosti nizu, sa desne strane mora biti niz", designatorStmtTwo);
//    	}
//    	
//    	desigListObjType = null;
//    	designatorListObjects.clear();
//    }
    
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
    		// provera za tri ugradjene funkcije - chr, ord i len
    		if( (factorMethod.getDesignator().obj.getName().equals("chr") && factorMethod.getActPars().obj.getType().getKind() != Struct.Int) ){
    			report_error("Greska: Prilikom poziva funkcije \"chr\" parametar mora biti tipa Int", factorMethod);
        		factorMethod.obj = Tab.noObj;
    		} 
    		else if( (factorMethod.getDesignator().obj.getName().equals("ord") && factorMethod.getActPars().obj.getType().getKind() != Struct.Char) ){
    			report_error("Greska: Prilikom poziva funkcije \"ord\" parametar mora biti tipa Char", factorMethod);
        		factorMethod.obj = Tab.noObj;
    		} 
    		else if( (factorMethod.getDesignator().obj.getName().equals("len") && factorMethod.getActPars().obj.getType().getKind() != Struct.Array) ){
    			report_error("Greska: Prilikom poziva funkcije \"len\" parametar mora biti tipa Array", factorMethod);
        		factorMethod.obj = Tab.noObj;
    		}
    		// ako smo prosli sve te provere, sve je okej
    		else{
        		factorMethod.obj = factorMethod.getDesignator().obj;		
    		}
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
    		if( currentType.getKind() == Struct.Int ||
    				currentType.getKind() == Struct.Char ||
    				currentType.getKind() == Struct.Bool )
    			factorNewExpr.obj = new Obj(Obj.Var, "factorNewArray", new Struct(Struct.Array, currentType));
    		else
    			report_error("Greska: Tip niza mora biti Int, Char ili Bool", factorNewExpr);
    	} else {
    		report_error("Greska: Tip izmedju zagrada mora biti Int", factorNewExpr);
    		factorNewExpr.obj = Tab.noObj;
    	}
    }
    
    public void visit(FactorNewExprExpr factorNewExprExpr) {
    	// TODO: ovde trenutno samo pravim objekat koji ce da bude tipa Type
    	// za dalje dodeljivanje vrednosti ovom objektu ili bilo sta sledece
    	// moram da ostavim za kasnije
    	
    	if( factorNewExprExpr.getExpr().obj.getType().getKind() == Struct.Int 
    			&& factorNewExprExpr.getExpr1().obj.getType().getKind() == Struct.Int ) {
    		if( currentType.getKind() == Struct.Int ||
    				currentType.getKind() == Struct.Char ||
    				currentType.getKind() == Struct.Bool )
    			factorNewExprExpr.obj = new Obj(Obj.Var, "factorNewMatrix", new Struct(Struct.Array, new Struct(Struct.Array, currentType)));
    		else
    			report_error("Greska: Tip matrice mora biti Int, Char ili Bool", factorNewExprExpr);
    	} else {
    		report_error("Greska: Tip izmedju zagrada mora biti Int", factorNewExprExpr);
    		factorNewExprExpr.obj = Tab.noObj;
    	}
    }
    
    public void visit(FactorNewActPars factorNewActPars) {
    	factorNewActPars.obj = new Obj(Obj.Var, "factorNew", factorNewActPars.getType().struct);
    }
    
    public void visit(FactorExpr factorExpr) {
    	// TODO: 
    	// e sad ovde ne znam trenutno kako da izvucem tip iz Expr neterminala
    	// znaci ovde moram cak i to da uradim , pored kasnijeg dodeljivanja vrednosti
    	
    	factorExpr.obj = factorExpr.getExpr().obj;
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
    	if(expression.getParent().getClass() == ActParams.class){
    		cntActParams = 1;
    	}
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
    		// ako je ovo slucaj za velicinu niza onda mora da ostane minus da bismo prijavili gresku
    		if(!(DesignatorBrackets.class == expression.getParent().getClass()))
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
    			|| printStmt.getExpr().obj.getType().getKind() == Struct.Bool
    			|| printStmt.getExpr().obj.getName().equals("eol")) {
    		// TODO: ovde bi isla funkcionalnost print-a
    		
    	}
    	else {
    		report_error("Greska: PRINT mora da se pozove nad tipom int, char ili Bool", printStmt);
    	}
    }
    
    // -----------------------------------------------------------------------------------------------------------
    // sve vezano za semantiku Condition-a iz if-a kao i iz While-a (VMCondition)

    public void visit(CondExprRelopExpr condExprRelopExpr) {
    	if( condExprRelopExpr.getExpr().obj.getType().getKind() != condExprRelopExpr.getExpr1().obj.getType().getKind()) {
    		report_error("Greska: Tipovi oba izraza moraju biti kompatibilni u slucaju poredjenja", condExprRelopExpr);
    	}
    	else
    	{
    		if( condExprRelopExpr.getExpr().obj.getType().getKind() == Struct.Array
    			&& !(condExprRelopExpr.getRelop() instanceof EqualEqual)
    			&& !(condExprRelopExpr.getRelop() instanceof UnEqual)){
    			report_error("Greska: Prilikom poredjenja nizova, relacioni operatori moraju biti != ili ==", condExprRelopExpr);		
    		}
    	}
    }
    
    public void visit(CondExpr condExpr) {
    	if( condExpr.getExpr().obj.getType().getKind() != Struct.Bool ) {
    		report_error("Greska: Uslov mora biti tipa Bool", condExpr);
    	}
    }
    
    // -----------------------------------------------------------------------------------------------------------
    // (VMFormParam)
    
    public void visit(FormalParamDecl formalParamDecl){
    	if( errorType ) {
			errorType = false;
		} else {
			Obj obj = Tab.find(formalParamDecl.getVarName());
			// ako je pronadjen istoimeni simbol, da li su u istom scope-u
    		// ako nisu, dozvoli insert
    		if( obj.getLevel() != currScopeLevel )
				obj = Tab.noObj;
    		
	    	if (obj == Tab.noObj) {
	    		if(isArray == 1) {
	    			// ako je u pitanju deklaracija niza
	        		Obj objPrint = Tab.insert(Obj.Var, formalParamDecl.getVarName(), new Struct(Struct.Array, currentType));
	        		nVars++;
	        		isArray = 0;
	        		report_info("Deklarisan lokalni niz " + formalParamDecl.getVarName() + " na liniji " + formalParamDecl.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + "Array of " + getTypeName(currentType) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
	        		// za trenutnu funkciju dodajem u listu argumenata strukturu Niza sa odredjenim tipom
	        		methodMap.get(currentMethod.getName()).add(new Struct(Struct.Array, currentType));
	    		}
	    		else if(isMatrix == 1)
	    		{
	    			// ako je u pitanju deklaracija matrice
	        		Obj objPrint = Tab.insert(Obj.Var, formalParamDecl.getVarName(), new Struct(Struct.Array, new Struct(Struct.Array, currentType)));
	        		nVars++;
	        		isMatrix = 0;
	        		report_info("Deklarisana lokalna matrica " + formalParamDecl.getVarName() + " na liniji " + formalParamDecl.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + "Matrix of " + getTypeName(currentType) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
	        		// za trenutnu funkciju dodajem u listu argumenata strukturu Matrice sa odredjenim tipom
	        		methodMap.get(currentMethod.getName()).add(new Struct(Struct.Array, new Struct(Struct.Array, currentType)));
	    		}	    		
	    		else 
	    		{
	    			// ako je u pitanju obicna promenljiva
	        		Obj objPrint = Tab.insert(Obj.Var, formalParamDecl.getVarName(), currentType);
	        		report_info("Deklarisana lokalna promenljiva " + formalParamDecl.getVarName() + " na liniji " + formalParamDecl.getLine() + ", objektni cvor: " + getKindName(objPrint.getKind()) + " " + objPrint.getName() + ": " + getTypeName(objPrint.getType()) + ", " + objPrint.getAdr() + ", " + objPrint.getLevel(), null);
	        		nVars++;
	        		// za trenutnu funkciju dodajem u listu argumenata strukturu promenljive sa odredjenim tipom
	        		methodMap.get(currentMethod.getName()).add(currentType);
	    		}
	    	}else {
	    		report_error("Greska: promenljiva " + formalParamDecl.getVarName() + " je vec deklarisana! ", formalParamDecl);
	    	}
		}
    }
    
    // ---------------------------------------------------------------------------------------------
    // (VMActParams)
    
    public void visit(ActParameters actParameters){
    	actParameters.obj = actParameters.getActParams().obj;
    }
    
    public void visit(ActParams actParams){
    	// trenutno mi je ovako samo zato sto hocu da uradim
    	// proveru zafunkcije chr, ord i len koje imaju samo jedan parametar
    	actParams.obj = actParams.getExpr().obj;
    	
    	if(!methNameForActParams.equals("chr") && !methNameForActParams.equals("ord") && !methNameForActParams.equals("len"))
    	{
        	if(actParams.getExpr().obj != Tab.noObj){
        		// moramo da proverimo ovde da li je isti broj argumenata
        		if( methodMap.get(methNameForActParams).size() == cntActParams){
            		// znaci ako postoji prvi argument
            		// onda bi lista morala da ima taj prvi argument
            		if( methodMap.get(methNameForActParams).size() > 0){
                		if( methodMap.get(methNameForActParams).get(0).getKind() != (actParams.getExpr().obj.getType()).getKind()){
                    		report_error("Greska: Argumenti se ne poklapaju prilikom poziva funkcije " + methNameForActParams , actParams);
                    	}
            		}else{
                		report_error("Greska: Broj argumenata prevazilazi stvaran broj parametara funkcije " + methNameForActParams , actParams);
            		}
        			
        		}else{
            		report_error("Greska: Broj argumenata nije jednak stvarnom broju parametara funkcije " + methNameForActParams , actParams);

        		}
        	}
	
    	}    	
    }
    
    public void visit(ExpressionList expressionList){
    	// uzimam prvo velicinu liste koja odgovara pozvanoj funkciji
    	// u slucaju da smo naisli na argument kojim se prevazilazi stvaran broj argumenata - imamo gresku
    	if(!methNameForActParams.equals("chr") && !methNameForActParams.equals("ord") && !methNameForActParams.equals("len"))
    	{
        	if( methodMap.get(methNameForActParams).size() > cntActParams){
            	// u slucaju da postoji element cntActParams u listi, proverava se poklapanje tipa argumenata
        		if( methodMap.get(methNameForActParams).get(cntActParams++).getKind() != (expressionList.getExpr().obj.getType()).getKind() ){
            		report_error("Greska: Argumenti se ne poklapaju prilikom poziva funkcije " + methNameForActParams , expressionList);
            	}  		
        	}else{
        		report_error("Greska: Broj argumenata prevazilazi stvaran broj parametara funkcije " + methNameForActParams , expressionList);
        	}
    		
    	}
    }
    
    public void visit(NoActPars noActPars){
    	// ako ne postoji prvi argument, onda broj elemenata u listi mora biti 0
    	if(!methNameForActParams.equals("chr") && !methNameForActParams.equals("ord") && !methNameForActParams.equals("len"))
    	{
        	if( methodMap.get(methNameForActParams).size() != 0){
        		report_error("Greska: Broj argumenata je manji od stvarnog broja parametara funkcije " + methNameForActParams , noActPars);
    		}    		
    	}
    }
}
