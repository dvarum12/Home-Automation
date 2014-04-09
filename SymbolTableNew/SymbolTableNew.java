package SymbolTableNew; /**
 * Created by Frederik on 09-04-2014.
 */

/**
 * Created by Frederik on 13-03-14.
 */
public class SymbolTableNew
{
    public SymbolSubTable functions = new SymbolSubTable();
    public SymbolSubTable types = new SymbolSubTable();
    public SymbolSubTable variables = new SymbolSubTable();

    public void openScope()
    {
        variables.openScope();
    }
    public void closeScope()
    {
        variables.closeScope();
    }
}