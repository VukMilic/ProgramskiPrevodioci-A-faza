package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class Compiler {

	static {
		File errorFile = new File("test/izlaz.err");
		if( errorFile.exists() ) 
			errorFile.delete();
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void tsdump() {
		TabBool.dump();
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(Compiler.class);
		
		Reader br = null;
		try {
			String fileName;
			
			File sourceCode = new File(args[0]);
			fileName = args[0];
//			File sourceCode = new File("test/program.mj");
//			fileName = "test/program.mj";
//			File sourceCode = new File("test/TestSintaksaOporavak.mj");
//			fileName = "test/TestSintaksaOporavak.mj";
//			File sourceCode = new File("test/TestSintaksaNizMat.mj");
//			fileName = "test/TestSintaksaNizMat.mj";
//			File sourceCode = new File("test/TestSemUgradjeniObjekti.mj");
//			fileName = "test/TestSemUgradjeniObjekti.mj";
//			File sourceCode = new File("test/TestSemStatementa.mj");
//			fileName = "test/TestSemStatementa.mj";
//			File sourceCode = new File("test/TestSemGreskeStatementa.mj");
//			fileName = "test/TestSemGreskeStatementa.mj";
//			File sourceCode = new File("test/TestSemGreskeDeklaracije.mj");
//			fileName = "test/TestSemGreskeDeklaracije.mj";
//			File sourceCode = new File("test/TestSemDeklaracija.mj");
//			fileName = "test/TestSemDeklaracija.mj";
			
			if(sourceCode.length() > 8*1024){
				log.error("Greska: Izvorni kod programa ne sme biti veci od 8 KB!");
				return;
			}
			
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        
	        Program prog = (Program)(s.value); 
	        TabBool.init();
			// ispis sintaksnog stabla
			log.info(prog.toString(""));
			log.info("===================================");

			// ispis prepoznatih programskih konstrukcija
			SemanticAnalyzer v = new SemanticAnalyzer();
			prog.traverseBottomUp(v); 
			
			log.info("===================================");
			tsdump();
			
//			log.info(v.nVars);
			
			if(!p.errorDetected && v.passed()){
				// ako ulazni fajl treba da generise samo Semantiku i Sintaksu
				// onda ti ne treba izlazni fajl 
				if( !fileName.contains("TestSem") && !fileName.contains("TestSintaksa")){
					File objFile = new File(args[1]);
//					File objFile = new File("test/program.obj");
					
					if( objFile.exists() ) objFile.delete();
					
					CodeGenerator codeGenerator = new CodeGenerator();
					prog.traverseBottomUp(codeGenerator);
					
					Code.dataSize = v.nVars;
					Code.mainPc = codeGenerator.getMainPc();
					Code.write(new FileOutputStream(objFile));	
				}

				log.info("Parsiranje uspesno zavrseno!");
			}else{
				log.error("Parsiranje NIJE uspesno zavrseno!");
			}
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
	
	
}
