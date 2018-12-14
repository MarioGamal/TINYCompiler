import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Parser {

    public static ArrayList<Token> ScannedTokens = new ArrayList<>();
    public static int TokensIterator = 0;
    public static JSONObject root;
    private static Stack<JSONObject> jsonObjectStack;
    private int NAssignment=0;
    private boolean skip = false;


    public Parser(){
        File file = new File("C:\\Users\\Mario\\IdeaProjects\\TinyScanner\\src\\tiny_sample_code.txt");
        ScannedTokens = Scanner.scanInput(Scanner.ReadFile(file)) ;
        TokensIterator = 0;
        root = new JSONObject();
        jsonObjectStack = new Stack<>();
        jsonObjectStack.push(root);
    }


    public  void isMatchingVal (String t) throws IOException{
        if(ScannedTokens.get(TokensIterator).stringVal.matches(t))
        {
            TokensIterator++;
        }
        else
        {
            throw (new IOException("THERE IS AN ERROR"));
        }
    }

    public  void isMatchingType(Token.TokenType t) throws IOException{
        if(ScannedTokens.get(TokensIterator).type == t)
            TokensIterator ++;
        else{
            throw (new IOException("Error"));
        }
    }

    public  void factor() throws IOException{
        if(ScannedTokens.get(TokensIterator).stringVal == "(") {
            isMatchingVal("(");
            exp();
            isMatchingVal(")");
        }
        else if(ScannedTokens.get(TokensIterator).stringVal.matches("[0-9]")){
            JSONObject last = jsonObjectStack.peek();
            last.put("constant",ScannedTokens.get(TokensIterator).stringVal);
            isMatchingType(Token.TokenType.Number);
        }
        else{
            JSONObject last = jsonObjectStack.peek();
            last.put("id",ScannedTokens.get(TokensIterator).stringVal);
            isMatchingType(Token.TokenType.Identifier);
        }
    }

    public  void term() throws IOException{
        factor();
        while(ScannedTokens.get(TokensIterator).stringVal.matches("\\*") || ScannedTokens.get(TokensIterator).stringVal.matches("/")){
            mulop();
            factor();
            jsonObjectStack.pop();
        }
    }

    public  void mulop() throws IOException{
        JSONObject last = jsonObjectStack.peek();
        JSONObject temp = new JSONObject();
        String leftop;
        if(last.containsKey("constant")){
            leftop = String.valueOf(last.get("constant"));
            last.remove("constant");
        }
        else{
            leftop = String.valueOf(last.get("id"));
            last.remove("id");
        }

        switch (ScannedTokens.get(TokensIterator).stringVal){
            case "*" :
                last.put("Operation",temp);
                jsonObjectStack.push(temp);
                temp.put("operator","*");
                temp.put("leftOperand",leftop);
                isMatchingVal("\\*");
                //temp.put("rightOperand",ScannedTokens.get(TokensIterator).stringVal);
                break;
            case "/":
                last.put("Operation",temp);
                jsonObjectStack.push(temp);
                temp.put("operator","/");
                temp.put("leftOperand",leftop);
                isMatchingVal("/");
                //temp.put("rightOperand",ScannedTokens.get(TokensIterator).stringVal);
                break;
        }
    }

    public  void addop() throws IOException{
        JSONObject last = jsonObjectStack.peek();
        JSONObject temp = new JSONObject();
        String leftop;
        if(last.containsKey("constant")){
            leftop = String.valueOf(last.get("constant"));
            last.remove("constant");
        }
        else{
            leftop = String.valueOf(last.get("id"));
            last.remove("id");
        }
        switch (ScannedTokens.get(TokensIterator).stringVal){
            case "+" :
                last.put("Operation",temp);
                jsonObjectStack.push(temp);
                temp.put("operator","+");
                temp.put("leftOperand",leftop);
                isMatchingVal("\\+");
                //temp.put("rightOperand",ScannedTokens.get(TokensIterator).stringVal);

                break;
            case "-":
                last.put("Operation",temp);
                jsonObjectStack.push(temp);
                temp.put("operator","-");
                temp.put("leftOperand",leftop);
                isMatchingVal("-");
                //temp.put("rightOperand",ScannedTokens.get(TokensIterator).stringVal);
                break;
        }

    }

    public  void comp_exp() throws IOException{
        JSONObject last = jsonObjectStack.peek();
        JSONObject temp = new JSONObject();
        String leftop;
        if(last.containsKey("constant")){
            leftop = String.valueOf(last.get("constant"));
            last.remove("constant");
        }
        else{
            leftop = String.valueOf(last.get("id"));
            last.remove("id");
        }
        switch (ScannedTokens.get(TokensIterator).stringVal){
            case "<" :
                last.put("Operation",temp);
                jsonObjectStack.push(temp);
                temp.put("operator","<");
                temp.put("leftOperand",leftop);
                isMatchingVal("<");
                //temp.put("rightOperand",ScannedTokens.get(TokensIterator).stringVal);

                break;
            case "=":
                last.put("Operation",temp);
                jsonObjectStack.push(temp);
                temp.put("operator","=");
                temp.put("leftOperand",leftop);
                isMatchingVal("=");
                //temp.put("rightOperand",ScannedTokens.get(TokensIterator).stringVal);

                break;
        }
    }

    public  void simple_exp() throws IOException {
        term();
        while(ScannedTokens.get(TokensIterator).stringVal.matches("\\+") || ScannedTokens.get(TokensIterator).stringVal.matches("-")){
            addop();
            term();
            jsonObjectStack.pop();
        }
    }

    public  void exp() throws IOException {
        simple_exp();
        if(ScannedTokens.get(TokensIterator).stringVal.matches("<") || ScannedTokens.get(TokensIterator).stringVal.matches("=")){
            comp_exp();
            simple_exp();
            jsonObjectStack.pop();
        }
    }

    public  void write_stmt() throws IOException{
        JSONObject last ;
        last = jsonObjectStack.peek();
        JSONObject temp = new JSONObject() ;
        isMatchingVal("write");
        last.put("WriteStatement",temp);
        jsonObjectStack.push(temp);
        exp();
        jsonObjectStack.pop();
    }

    public  void read_stmt() throws IOException{
        JSONObject last ;
        last = jsonObjectStack.peek();
        isMatchingVal("read");
        last.put("ReadStatement",ScannedTokens.get(TokensIterator).stringVal);
        isMatchingType(Token.TokenType.Identifier);
    }

    public  void assign_stmt() throws IOException{
        JSONObject last = jsonObjectStack.peek();
        JSONObject temp = new JSONObject();
        last.put("assign"+ String.valueOf(NAssignment++),temp);
        temp.put("LeftHandSide",ScannedTokens.get(TokensIterator).stringVal);
        isMatchingType(Token.TokenType.Identifier);
        //temp.put("operator",":=");
        isMatchingVal(":=");
        jsonObjectStack.push(temp);
        exp();
        jsonObjectStack.pop();
    }

    public  void repeat_stmt() throws IOException{
        isMatchingVal("repeat");
        JSONObject last = jsonObjectStack.peek();
        JSONObject temp = new JSONObject();
        last.put("RepeatStatement",temp);
        jsonObjectStack.push(temp);

        last = jsonObjectStack.peek();
        temp = new JSONObject();
        last.put("body",temp);
        jsonObjectStack.push(temp); //1
        skip=true;
        stmt_seq();
        skip=false;
        jsonObjectStack.pop();

        isMatchingVal("until");

        last = jsonObjectStack.peek();
        temp = new JSONObject();
        last.put("test",temp);
        jsonObjectStack.push(temp); //2
        exp();
        jsonObjectStack.pop(); //2
        jsonObjectStack.pop(); //1
    }

    public  void if_stmt() throws IOException{
        isMatchingVal("if");
        JSONObject last = jsonObjectStack.peek();
        JSONObject temp = new JSONObject();
        last.put("ifStatement",temp);
        jsonObjectStack.push(temp);
        last = jsonObjectStack.peek();
        temp = new JSONObject();
        last.put("testPart", temp);
        jsonObjectStack.push(temp);
        exp();
        jsonObjectStack.pop();
        isMatchingVal("then");
        last = jsonObjectStack.peek();
        temp = new JSONObject();
        last.put("thenPart",temp);
        jsonObjectStack.push(temp);
        skip=true;
        stmt_seq();
        jsonObjectStack.pop();
        if(ScannedTokens.get(TokensIterator).stringVal.matches("else")){
            isMatchingVal("else");
            last = jsonObjectStack.peek();
            temp = new JSONObject();
            last.put("elsePart",temp);
            jsonObjectStack.push(temp);
            stmt_seq();
            skip=false;
            jsonObjectStack.pop();
        }
        isMatchingVal("end");
    }

    public  void stmt() throws IOException{
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

    public  void stmt_seq() throws IOException {
        stmt();
        isMatchingVal(";");
        if(TokensIterator == ScannedTokens.size()) return ;
        while(TokensIterator < ScannedTokens.size() && !ScannedTokens.get(TokensIterator).stringVal.matches("until")){
            if (TokensIterator == (ScannedTokens.size())) {
                return;

            }
            if((ScannedTokens.get(TokensIterator).stringVal.matches("end"))) return;
            stmt();
            isMatchingVal(";");
        }
    }

    public  void prog() throws IOException {
        stmt_seq();
    }




    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        parser.prog();
        System.out.println(parser.root.toJSONString());
        for(Token x : parser.ScannedTokens)
        {
            System.out.println(x.stringVal);
        }
        System.out.println(parser.ScannedTokens.size());
    }
}


