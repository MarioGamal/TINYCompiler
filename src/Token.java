import java.util.ArrayList;
import java.util.Arrays;

public class Token {
    enum TokenType{
        Reserved,Identifier,Number,Symbol
    }

    public static String Reserveds[] = {"if","then","else","until","end","repeat","read","write"};
    public static String Symbols[] = {"+","-","*","/","=","<","(",")",";",":="};
    public static ArrayList<String> ReservedWords = new ArrayList<String>(Arrays.asList(Reserveds));
    public static ArrayList<String> SpecialSymbols = new ArrayList<String>(Arrays.asList(Symbols));

    public TokenType type ;
    public String stringVal;


    Token(String val){
        this.stringVal = val;
        if(ReservedWords.contains(val)){
            this.type = TokenType.Reserved;
        }else if(SpecialSymbols.contains(val)){
            this.type=TokenType.Symbol;
        }else if(val.matches("[0-9]*")){
            this.type = TokenType.Number;
        }else{
            this.type = TokenType.Identifier;
        }
    }

}
