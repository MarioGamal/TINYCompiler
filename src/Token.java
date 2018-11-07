import java.util.ArrayList;
import java.util.Arrays;

public class Token {
    enum TokenType{
        Reserved,Identifier,Number,Symbol,Assign
    }

    public static String Reserveds[] = {"if","then","else","until","end","repeat","read","write"};
    public static String Symbols[] = {"+","-","*","/","=","<","(",")",";"};
    public static String Assign[] ={":="};
    public static ArrayList<String> ReservedWords = new ArrayList<String>(Arrays.asList(Reserveds));
    public static ArrayList<String> SpecialSymbols = new ArrayList<String>(Arrays.asList(Symbols));
    public static ArrayList<String> Assignment = new ArrayList<>(Arrays.asList(Assign));

    public TokenType type ;
    public String stringVal;


    Token(String val){
        this.stringVal = val;
        if(ReservedWords.contains(val)){
            this.type = TokenType.Reserved;
        }else if (Assignment.contains(val)){
            this.type = TokenType.Assign;
        }else if(SpecialSymbols.contains(val)){
            this.type=TokenType.Symbol;
        }else if(val.matches("[0-9]*")){
            this.type = TokenType.Number;
        }else{
            this.type = TokenType.Identifier;
        }
    }

}
