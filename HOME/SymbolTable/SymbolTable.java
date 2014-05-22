package HOME.SymbolTable; /**
 * Created by Frederik on 09-04-2014.
 */

/**
 * Created by Frederik on 13-03-14.
 */
public class SymbolTable
{
    public FunctionTable functions = new FunctionTable();
    public TypeTable types = new TypeTable();
    public VariableTable variables = new VariableTable();
    private Integer lineNumber = 1;
    private Integer labelNumber = 0;

    public String newLabel() {
        labelNumber++;
        return "Label" + labelNumber;
    }

    public String getLabel() {
        return "Label" + labelNumber + ":";
    }

    public void newLine() {
        lineNumber++;
    }

    public String getLineNumberText() {
        return ".line " + lineNumber;
    }

    public void resetVariableTable(){
        variables= new VariableTable();
    }

    public void openScope()
    {
        variables.openScope();
    }
    public void closeScope()
    {
        variables.closeScope();
    }
}