import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;

/**
 * Created by Jacob on 20-03-14.
 */
public class FirstRun extends HOMEBaseVisitor<Type> {

    SymbolTable scope = Main.scope;

    //TODO: Determine if newline shall be removed or not
    @Override
    public Type visitNewline(@NotNull HOMEParser.NewlineContext ctx)
    {

        return new Type(Type.TypeEnum.Nothing);
    }

    @Override
    public Type visitBlock(@NotNull HOMEParser.BlockContext ctx)
    {
        Type returnType = new Type(Type.TypeEnum.Nothing);
        Type blockType;

        //Checks if function already exist
        if(ctx.function() != null)
        {
            returnType = visitFunction(ctx.function());
            if(returnType.equals(Type.TypeEnum.Error))
                return returnType;
        }

        //Calls visitBlock recursively if it hasn't failed already.
        if(ctx.moreFunctions().block() != null && ctx.moreFunctions().block().getChildCount() > 0)
        {
            blockType = visitBlock(ctx.moreFunctions().block());
            if(!blockType.equals((Type.TypeEnum.Nothing)))
                returnType = blockType;
        }

        //TODO: Check if returning false positives
        //Checks if "setup" function has been found
        if(!scope.symbolExistsFunction("Setup"))
        {
            returnType = new Type(Type.TypeEnum.Error, "No \"Setup\" function found, please provide one.");
        }

        return returnType;
    }

    @Override
    public Type visitFunction(@NotNull HOMEParser.FunctionContext ctx)
    {
        Type returnType;
        //If the function returns some identifier
        Type returns;
        if(ctx.type() != null)
            returns = visitType(ctx.type());
        //or the function returns nothing
        else if(ctx.getChild(4).getText().equals("Nothing"))
            returns = new Type(Type.TypeEnum.Nothing);
        else //otherwise return error if other unexpected type
            returns = new Type(Type.TypeEnum.Error);

        String funcName = ctx.getChild(1).getText();

        //Gets the types of the parameters
        ArrayList<Type> paramTypes = (ArrayList<Type>)visitDeclarationParameters(ctx.declarationParameters()).value;
        Type symbol = new Type(Type.TypeEnum.Function, paramTypes, returns);

        if(funcName.equals("Setup"))
            if(paramTypes.size() > 0)
                return new Type(Type.TypeEnum.Error, "Function \"Setup\" doesn't accept any parameters, please remove these.");
            else if(!returns.equals(Type.TypeEnum.Nothing))
                return new Type(Type.TypeEnum.Error, "Function \"Setup\" can't return anything but nothing");

        //adds function + parameter types to symbol table.
        if(!scope.addSymbolFunction(funcName, symbol))
            returnType = new Type(Type.TypeEnum.Error, String.format("Function %s is duplicated at line %d",
                                                                       funcName, ctx.getStart().getLine()));
        else
            returnType = new Type(Type.TypeEnum.Nothing, String.format("Function %s inserted", funcName));

        return returnType;
    }
    
    @Override
    public Type visitDeclarationParameters(@NotNull HOMEParser.DeclarationParametersContext ctx)
    {
        Type returnType = new Type(Type.TypeEnum.Nothing, new ArrayList<Type>());
        ArrayList<Type> genericList = new ArrayList<>();

        for(HOMEParser.DeclarationContext currCtx : ctx.declaration())
        {
            genericList.add(new Type(visitType(currCtx.type()).typeEnum));
        }

        returnType.value = genericList;
        return returnType;
    }


    @Override
    public Type visitType(@NotNull HOMEParser.TypeContext ctx)
    {
        if (ctx.collectionType() != null)
            return visitCollectionType(ctx.collectionType());
        return visitPrimitiveType(ctx.primitiveType());
    }
    @Override
    public Type visitCollectionType(@NotNull HOMEParser.CollectionTypeContext ctx)
    {

        Type.TypeEnum outerType = Type.TypeEnum.valueOf(ctx.getText().split("<")[0]);
        Type innerType = visitType(ctx.type());
        return new Type(outerType, innerType.toList());
    }

    @Override
    public Type visitPrimitiveType(@NotNull HOMEParser.PrimitiveTypeContext ctx)
    {
        Type returnType;

        switch (Type.TypeEnum.valueOf(ctx.getText())){
            case Integer:
                returnType = new Type(Type.TypeEnum.Integer);
                break;
            case Decimal:
                returnType = new Type(Type.TypeEnum.Decimal);
                break;
            case Boolean:
                returnType = new Type(Type.TypeEnum.Boolean);
                break;
            case String:
                returnType = new Type(Type.TypeEnum.String);
                break;
            case Nothing:
                returnType = new Type(Type.TypeEnum.Nothing);
                break;
            default:
                returnType = new Type(Type.TypeEnum.Error, "Unknown primitive type");
                break;
        }
        return returnType;
    }
}
