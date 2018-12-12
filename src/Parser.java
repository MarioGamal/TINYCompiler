import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    public static File file = new File("tiny_sample_code.txt");
    public static ArrayList<Token> ScannedTokens = new ArrayList<>(Scanner.scanInput(Scanner.ReadFile(file)));
    public static int TokensIterator = 0;
    private static JSONObject root = new JSONObject();

    public static void isMatchingVal (String t) throws IOException{
        if(ScannedTokens.get(TokensIterator).stringVal == t)
        {
            TokensIterator++;
        }
        else
        {
            throw (new IOException("THERE IS AN ERROR"));
        }
    }

    public static void isMatchingType(Token.TokenType t) throws IOException{
        if(ScannedTokens.get(TokensIterator).type==t)
            TokensIterator++;
        else
            throw (new IOException("Error"));
    }

    public static void factor() throws IOException{
        if(ScannedTokens.get(TokensIterator).stringVal == "(") {
            isMatchingVal("(");
            exp();
            isMatchingVal(")");
        }
        else if(ScannedTokens.get(TokensIterator).stringVal.matches("[0-9]")){
            isMatchingType(Token.TokenType.Number);
        }
        else{
            isMatchingType(Token.TokenType.Identifier);
        }
    }

    public static void term() throws IOException{
        factor();
        while(ScannedTokens.get(TokensIterator).stringVal=="*" || ScannedTokens.get(TokensIterator).stringVal=="/"){
            mulop();
            factor();
        }
    }

    public static void mulop() throws IOException{
        switch (ScannedTokens.get(TokensIterator).stringVal){
            case "*" :
                isMatchingVal("*");
                break;
            case "/":
                isMatchingVal("/");
                break;
        }
    }

    public static void addop() throws IOException{
        switch (ScannedTokens.get(TokensIterator).stringVal){
            case "+" :
                isMatchingVal("+");
                break;
            case "-":
                isMatchingVal("-");
                break;
        }

    }

    public static void comp_exp() throws IOException{
        switch (ScannedTokens.get(TokensIterator).stringVal){
            case "<" :
                isMatchingVal("<");
                break;
            case "=":
                isMatchingVal("=");
                break;
        }
    }

    public static void simple_exp() throws IOException {
        term();
        while(ScannedTokens.get(TokensIterator).stringVal=="+" || ScannedTokens.get(TokensIterator).stringVal=="-"){
            addop();
            term();
        }
    }


    public static void exp() throws IOException {
        simple_exp();
        if(ScannedTokens.get(TokensIterator).stringVal=="<" || ScannedTokens.get(TokensIterator).stringVal=="="){
            isMatchingType(Token.TokenType.Symbol);
            simple_exp();
        }
    }

    public static void write_stmt() throws IOException{
        isMatchingVal("write");
        exp();
    }

    public static void read_stmt() throws IOException{
        isMatchingVal("read");
        exp();
    }

    public static void assign_stmt() throws IOException{
        isMatchingType(Token.TokenType.Identifier);
        isMatchingVal(":=");
        exp();
    }


    public static void repeat_stmt() throws IOException{
        isMatchingVal("repeat");
        stmt_seq();
        isMatchingVal("until");
        exp();
    }

    public static void if_stmt() throws IOException{
        isMatchingVal("if");
        exp();
        isMatchingVal("then");
        stmt_seq();
        if(ScannedTokens.get(TokensIterator).stringVal=="else"){
            isMatchingVal("else");
            stmt_seq();
        }
        isMatchingVal("end");
    }

    public static void stmt() throws IOException{
        switch (ScannedTokens.get(TokensIterator).stringVal){
            case "if" :
                if_stmt();
                break;
            case "write":
                write_stmt();
                break;
            case "read":
                read_stmt();
                break;
            case "repeat":
                repeat_stmt();
                break;
            default:
                assign_stmt();
                break;
        }

    }

    public static void stmt_seq() throws IOException {
        stmt();
        while(ScannedTokens.get(TokensIterator).type == Token.TokenType.Reserved || ScannedTokens.get(TokensIterator).type== Token.TokenType.Identifier){
            stmt();
        }
    }

    public static void prog() throws IOException {
        stmt_seq();
    }




    public static void main(String[] args) throws IOException {

    }
}


