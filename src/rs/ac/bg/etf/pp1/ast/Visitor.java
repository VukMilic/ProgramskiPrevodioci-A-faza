// generated with ast extension for cup
// version 0.8
// 7/5/2023 20:14:46


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(WhileTerm WhileTerm);
    public void visit(MethodDecl MethodDecl);
    public void visit(Mulop Mulop);
    public void visit(ConstTypeNumList ConstTypeNumList);
    public void visit(Relop Relop);
    public void visit(PostDesignator PostDesignator);
    public void visit(StatementList StatementList);
    public void visit(MulopFactList MulopFactList);
    public void visit(Addop Addop);
    public void visit(GVarIdent GVarIdent);
    public void visit(Factor Factor);
    public void visit(CondTerm CondTerm);
    public void visit(ForeachTerm ForeachTerm);
    public void visit(DeclList DeclList);
    public void visit(GVarDecl GVarDecl);
    public void visit(Designator Designator);
    public void visit(Term Term);
    public void visit(Condition Condition);
    public void visit(DesignatorOrNot DesignatorOrNot);
    public void visit(ElseStatement ElseStatement);
    public void visit(VarTypeList VarTypeList);
    public void visit(ExprList ExprList);
    public void visit(ExprOrNot ExprOrNot);
    public void visit(ExtendsType ExtendsType);
    public void visit(VarDeclList VarDeclList);
    public void visit(FormalParamList FormalParamList);
    public void visit(MinusOrNot MinusOrNot);
    public void visit(Expr Expr);
    public void visit(ActPars ActPars);
    public void visit(CommaNumConst CommaNumConst);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(AssignError AssignError);
    public void visit(ConstTypeBoolList ConstTypeBoolList);
    public void visit(Statement Statement);
    public void visit(GVarTypeList GVarTypeList);
    public void visit(ExprOrActPars ExprOrActPars);
    public void visit(ConstDecl ConstDecl);
    public void visit(CondFact CondFact);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(FormPars FormPars);
    public void visit(DeclType DeclType);
    public void visit(ClassMethodDecl ClassMethodDecl);
    public void visit(AddopTermList AddopTermList);
    public void visit(ConstTypeCharList ConstTypeCharList);
    public void visit(Mod Mod);
    public void visit(Div Div);
    public void visit(Mul Mul);
    public void visit(Minus Minus);
    public void visit(Plus Plus);
    public void visit(LessEqual LessEqual);
    public void visit(Less Less);
    public void visit(MoreEqual MoreEqual);
    public void visit(More More);
    public void visit(UnEqual UnEqual);
    public void visit(EqualEqual EqualEqual);
    public void visit(AssignOp AssignOp);
    public void visit(Label Label);
    public void visit(DesignatorIdent DesignatorIdent);
    public void visit(DesignatorBrackets DesignatorBrackets);
    public void visit(DesignatorDot DesignatorDot);
    public void visit(FactorExpr FactorExpr);
    public void visit(FactorNewActPars FactorNewActPars);
    public void visit(FactorNewExprExpr FactorNewExprExpr);
    public void visit(FactorNewExpr FactorNewExpr);
    public void visit(FactorBoolConst FactorBoolConst);
    public void visit(FactorCharConst FactorCharConst);
    public void visit(FactorNumConst FactorNumConst);
    public void visit(FactorMethod FactorMethod);
    public void visit(FactorDesignator FactorDesignator);
    public void visit(NoMulopFactList NoMulopFactList);
    public void visit(MulopFactorList MulopFactorList);
    public void visit(Terminal Terminal);
    public void visit(MinusNot MinusNot);
    public void visit(MinusOr MinusOr);
    public void visit(NoAddopTermList NoAddopTermList);
    public void visit(AddopTerminalList AddopTerminalList);
    public void visit(Expression Expression);
    public void visit(ExpressionNot ExpressionNot);
    public void visit(ExpressionYes ExpressionYes);
    public void visit(CondExpr CondExpr);
    public void visit(CondExprRelopExpr CondExprRelopExpr);
    public void visit(CondFactOne CondFactOne);
    public void visit(CondFactList CondFactList);
    public void visit(CondTermOne CondTermOne);
    public void visit(CondTermList CondTermList);
    public void visit(NoExprList NoExprList);
    public void visit(ExpressionList ExpressionList);
    public void visit(ActParams ActParams);
    public void visit(NoActPars NoActPars);
    public void visit(ActParameters ActParameters);
    public void visit(AssignOpError AssignOpError);
    public void visit(DesignatorNot DesignatorNot);
    public void visit(DesignatorYes DesignatorYes);
    public void visit(DesignatorMinusMinus DesignatorMinusMinus);
    public void visit(DesignatorPlusPlus DesignatorPlusPlus);
    public void visit(DesignatorActPars DesignatorActPars);
    public void visit(DesignatorAssignErrorStmt DesignatorAssignErrorStmt);
    public void visit(DesignatorAssignOp DesignatorAssignOp);
    public void visit(NoCommaNumConst NoCommaNumConst);
    public void visit(CommaNumConstant CommaNumConstant);
    public void visit(NoElseStmt NoElseStmt);
    public void visit(ElseStmt ElseStmt);
    public void visit(ForeachTerminal ForeachTerminal);
    public void visit(WhileTerminal WhileTerminal);
    public void visit(DesignatorForEach DesignatorForEach);
    public void visit(WhileStmt WhileStmt);
    public void visit(IfStatement IfStatement);
    public void visit(MoreStmts MoreStmts);
    public void visit(PrintStmt PrintStmt);
    public void visit(ReadDesignator ReadDesignator);
    public void visit(ReturnExpr ReturnExpr);
    public void visit(ContinueStmt ContinueStmt);
    public void visit(BreakStmt BreakStmt);
    public void visit(Assignment Assignment);
    public void visit(NoStmt NoStmt);
    public void visit(Statements Statements);
    public void visit(FormalParamDecl FormalParamDecl);
    public void visit(SingleFormalParamDecl SingleFormalParamDecl);
    public void visit(FormalParamDecls FormalParamDecls);
    public void visit(NoFormParam NoFormParam);
    public void visit(FormParams FormParams);
    public void visit(Type Type);
    public void visit(NoExtendsType NoExtendsType);
    public void visit(ExtendType ExtendType);
    public void visit(MethVoidName MethVoidName);
    public void visit(MethTypeName MethTypeName);
    public void visit(MethodDeclaration MethodDeclaration);
    public void visit(NoMethodDecl NoMethodDecl);
    public void visit(MethodDeclarations MethodDeclarations);
    public void visit(NoClassMethodDecl NoClassMethodDecl);
    public void visit(ClassMethodDeclaration ClassMethodDeclaration);
    public void visit(ClassDecl ClassDecl);
    public void visit(NoSquareBrackets NoSquareBrackets);
    public void visit(SquareBrackets SquareBrackets);
    public void visit(SquareBracketsDouble SquareBracketsDouble);
    public void visit(OneVarType OneVarType);
    public void visit(VariableTypeList VariableTypeList);
    public void visit(VarDecl VarDecl);
    public void visit(OneConstTypeBool OneConstTypeBool);
    public void visit(ConstantTypeBoolList ConstantTypeBoolList);
    public void visit(OneConstTypeChar OneConstTypeChar);
    public void visit(ConstantTypeCharList ConstantTypeCharList);
    public void visit(OneConstTypeNum OneConstTypeNum);
    public void visit(ConstantTypeNumList ConstantTypeNumList);
    public void visit(BoolConstDecl BoolConstDecl);
    public void visit(CharConstDecl CharConstDecl);
    public void visit(NumConstDecl NumConstDecl);
    public void visit(NoVarDecl NoVarDecl);
    public void visit(VarDeclarations VarDeclarations);
    public void visit(GlobalVarDeclCommaError GlobalVarDeclCommaError);
    public void visit(GlobalVarIdent GlobalVarIdent);
    public void visit(OneGlobalVarType OneGlobalVarType);
    public void visit(GlobalVariableTypeList GlobalVariableTypeList);
    public void visit(GlobalVarDeclErrorSemi GlobalVarDeclErrorSemi);
    public void visit(GlobalVarDecl GlobalVarDecl);
    public void visit(DeclClass DeclClass);
    public void visit(DeclGVar DeclGVar);
    public void visit(DeclConst DeclConst);
    public void visit(NoDeclList NoDeclList);
    public void visit(DeclarationsList DeclarationsList);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}
