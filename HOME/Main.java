package HOME;
import HOME.Grammar.*;
import HOME.Type.*;

import SymbolTableNew.SymbolTableNew;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;

/**
 * Created by Jacob on 12-03-14.
 */
public class Main
{
    public static SymbolTableOLD scope = new SymbolTableOLD();
    public static SymbolTableNew symbolTable = new SymbolTableNew();

    public static Type nothing = new Type("Nothing");

    public static Type integer;
    public static Type decimal;
    public static Type bool;
    public static Type string;

    public static void main(String[] args) throws IOException
    {
        System.out.println("\n-----------------------------Loader-----------------------\n");
        //       try{
        HOMELexer lexer = new HOMELexer(new ANTLRFileStream("NotInput"));
        HOMEParser parser = new HOMEParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.program();

/*
            CustomErrorListener errorListener = new CustomErrorListener(false);
            parser.addErrorListener(errorListener);

            ParseTree tree = parser.program();

            System.out.println("Error Messages: ");
            for (String s : errorListener.ErrorMessages()) {
                System.out.println(s);
            }
 */
        FileReader fileR = new FileReader();
//        fileR.loadMethods();
        fileR.loadClassPrototypes();
        fileR.loadStandardClasses();

        integer = symbolTable.types.getSymbol("Integer");
        decimal = symbolTable.types.getSymbol("Decimal");
        bool = symbolTable.types.getSymbol("Boolean");
        string = symbolTable.types.getSymbol("String");


        System.out.println("\n-----------------------------FirstRun-----------------------\n");
        FirstRun firstVisit = new FirstRun();
        Type returnType = firstVisit.visitBlock((HOMEParser.BlockContext) tree.getChild(1));
        if (returnType.equals("Error"))
        {
            //System.out.println(String.format("Error: %s", returnType.value));
        }
        else
        {
            System.out.println("\n-----------------------------Typechecker-----------------------\n");
            TypeChecker visitor = new TypeChecker();
            visitor.visit(tree);
        }
        /*
        }catch(Exception e)
        {
            e.printStackTrace();
            //System.err.println(e.initCause(e.getCause()));
        }
        */
    }
}
