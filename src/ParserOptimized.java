import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ParserOptimized {

    public static ArrayList<Token> ScannedTokens = new ArrayList<>();
    public static int TokensIterator = 0;
    public static JSONObject root;
    private int NAssignment=0;
    private boolean skip = false;
    public static int counter = 0;
    public static boolean no_parent = true;
    public static JsonArray siblings = new JsonArray();

    //CONSTRUCTOR
    public ParserOptimized(File file){
        //File file = new File("C:\\Users\\Mario\\IdeaProjects\\TinyScanner\\src\\tiny_sample_code.txt");
        ScannedTokens = Scanner.scanInput(Scanner.ReadFile(file)) ;
        TokensIterator = 0;
        root = new JSONObject();
    }

    public  void isMatchingVal (String t) throws IOException {
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

    public  JSONObject factor() throws IOException{
        JSONObject result = new JSONObject();
        if(ScannedTokens.get(TokensIterator).stringVal == "(") {
            isMatchingVal("(");
            result = exp();
            isMatchingVal(")");
        }
        else if(ScannedTokens.get(TokensIterator).stringVal.matches("[0-9]")){
            result.put("id",String.valueOf(counter));
            counter++;
            result.put("name","const ("+ScannedTokens.get(TokensIterator).stringVal+")");
            result.put("no_parent","false");
            result.put("type","number");
            result.put("children",null);
            isMatchingType(Token.TokenType.Number);
        }
        else{
            result.put("id",String.valueOf(counter));
            counter++;
            result.put("name","identifier ("+ScannedTokens.get(TokensIterator).stringVal+")");
            result.put("no_parent","false");
            result.put("type","identifier");
            result.put("children",null);
            isMatchingType(Token.TokenType.Identifier);
        }
        return result;
    }

    public  JSONObject term() throws IOException{
        JSONObject result = factor();
        if(TokensIterator == ScannedTokens.size())
            return result;
        while(ScannedTokens.get(TokensIterator).stringVal.matches("\\*") || ScannedTokens.get(TokensIterator).stringVal.matches("/")){
            JSONObject temp = result;
            result = new JSONObject();
            result.put("id",String.valueOf(counter));
            counter++;
            result.put("name","op ("+ScannedTokens.get(TokensIterator).stringVal+")");
            result.put("no_parent","false");
            result.put("type","operation");

            isMatchingType(Token.TokenType.Symbol);
            JSONArray children = new JSONArray();
            children.add(temp);
            children.add(factor());
            result.put("children",children);

        }
        return result;
    }


    public  JSONObject simple_exp() throws IOException {
        JSONObject result = term();
        if(TokensIterator == ScannedTokens.size())
            return result;
        while(ScannedTokens.get(TokensIterator).stringVal.matches("\\+") || ScannedTokens.get(TokensIterator).stringVal.matches("-")){
            JSONObject temp = result;
            result=new JSONObject();
            result.put("id",String.valueOf(counter));
            counter++;
            result.put("name","op ("+ScannedTokens.get(TokensIterator).stringVal+")");
            result.put("no_parent","false");
            result.put("type","operation");
            //addop();
            isMatchingType(Token.TokenType.Symbol);
            JSONArray children = new JSONArray();
            children.add(temp);
            children.add(term());
            result.put("children",children);

            if(TokensIterator == ScannedTokens.size())
            {
                //term();
                return result;
            }

        }
        return result;
    }

    public  JSONObject exp() throws IOException {

        JSONObject result = simple_exp();
        if(TokensIterator == ScannedTokens.size())
            return result;
        if(ScannedTokens.get(TokensIterator).stringVal.matches("<") || ScannedTokens.get(TokensIterator).stringVal.matches("=")){

            JSONObject temp = result;
            result = new JSONObject();
            result.put("id",String.valueOf(counter));
            counter++;
            result.put("name","op ("+ScannedTokens.get(TokensIterator).stringVal+")");
            result.put("no_parent","false");
            result.put("type","operation");
            //comp_exp();
            isMatchingType(Token.TokenType.Symbol);
            JSONArray children = new JSONArray();
            children.add(temp);
            children.add(simple_exp());
            //simple_exp();
            result.put("children",children);
        }
        return result;
    }

    public  JSONObject write_stmt() throws IOException{
        isMatchingVal("write");

        JSONObject result = new JSONObject();
        result.put("id",String.valueOf(counter));
        result.put("name","write");
        result.put("no_parent",String.valueOf(no_parent));
        result.put("type","exp");
        // creation of write-body object
        result.put("children",exp());
        //exp();

        return result;
    }

    public  JSONObject read_stmt() throws IOException{
        isMatchingVal("read");

        JSONObject result = new JSONObject();
        result.put("id",String.valueOf(counter));
        counter++;
        result.put("name","read ("+ScannedTokens.get(TokensIterator).stringVal+")");
        result.put("type","read");
        result.put("no_parent",String.valueOf(no_parent));
        result.put("children",null);
        isMatchingType(Token.TokenType.Identifier);

        return result;
    }

    public  JSONObject assign_stmt() throws IOException{

        JSONObject result = new JSONObject();
        result.put("id",String.valueOf(counter));
        result.put("name","assign ("+ScannedTokens.get(TokensIterator).stringVal+")");
        result.put("no_parent",String.valueOf(no_parent));
        isMatchingType(Token.TokenType.Identifier);
        isMatchingVal(":=");

        //creation of children array of assign-statement
     // creation of assign-body object
        result.put("children",exp());

        return result;
    }

    public  JSONObject repeat_stmt() throws IOException{
        isMatchingVal("repeat");

        JSONObject result = new JSONObject();
        result.put("id",String.valueOf(counter));
        counter++;
        result.put("name","repeat");
        result.put("type","repeat");
        result.put("no_parent",String.valueOf(no_parent));

        //creation of children array of repeat-statement
        JSONArray repeatChildren = new JSONArray();

        //creation of repeat-body object
        if(no_parent == false)
        {
            repeatChildren.add(stmt_seq());
        }
        else {
            no_parent = false;
            repeatChildren.add(stmt_seq());
            no_parent=true;
        }


        isMatchingVal("until");
        //creation of repeat-test object
        repeatChildren.add(exp());
        //exp();
        result.put("children",repeatChildren);
        return result;
    }

    public  JSONObject if_stmt() throws IOException{

        isMatchingVal("if");

        JSONObject result = new JSONObject();
        result.put("name","if");
        result.put("type","if_stmt");
        result.put("no_parent",String.valueOf(no_parent));
        result.put("id",String.valueOf(counter));
        counter++;

        //creation of children array of if-statement
        JSONArray ifChildren = new JSONArray();
        ifChildren.add(exp());

        isMatchingVal("then");

        //creation of if-thenpart object

        if(no_parent == false)
        {
            ifChildren.add(stmt_seq());
        }
        else {
            no_parent = false;
            ifChildren.add(stmt_seq());
            no_parent=true;
        }

        if(ScannedTokens.get(TokensIterator).stringVal.matches("else")){
            isMatchingVal("else");

            //creation of if-elsepart object

            if(no_parent == false)
            {
                ifChildren.add(stmt_seq());
            }
            else {
                no_parent = false;
                ifChildren.add(stmt_seq());
                no_parent=true;
            }

           }
        result.put("children",ifChildren);
        isMatchingVal("end");
        return result;
    }

    public  JSONObject stmt() throws IOException{
        switch (ScannedTokens.get(TokensIterator).stringVal){
            case "if" :
                return if_stmt();
            case "write":
                return write_stmt();
            case "read":
                return read_stmt();
            case "repeat":
                return repeat_stmt();
            default:
                return assign_stmt();
        }

    }

    public  JSONObject stmt_seq() throws IOException {
        JSONObject result = new JSONObject();
        if(counter==0)
            result.put("hidden","true");
        result.put("type","stmt_sequence");
        result.put("name","");
        result.put("id",String.valueOf(counter));
        counter++;

        JSONArray childrenArr = new JSONArray();

        childrenArr.add(stmt());
        //isMatchingVal(";");

        if(TokensIterator == ScannedTokens.size()) {
            result.put("children",childrenArr);
            return result;
        }

        while(ScannedTokens.get(TokensIterator).stringVal.equals(";"))
        {
            isMatchingVal(";");
            result.put("children",childrenArr);
            if(TokensIterator == ScannedTokens.size()) {
                break;
            }
            return result;
        }

        result.put("children",childrenArr);
        return result;

    }

    public  JSONObject prog() throws IOException {
        no_parent=true;
        return(stmt_seq());
    }

    public JsonArray generateSiblings(JsonObject x)
    {

        JsonElement jsonElement = new JsonParser().parse(x.toString());
        JsonObject jobject = jsonElement.getAsJsonObject();
        if(jobject.get("type").getAsString().equals("stmt_sequence")) {
            JsonArray jarray = jobject.getAsJsonArray("children");
            for (int i = 1; i < jarray.size(); i++) {
                JsonObject temp = new JsonObject();
                JsonObject source = new JsonObject();
                JsonObject target = new JsonObject();
                source.add("id", jarray.get(i - 1).getAsJsonObject().get("id"));
                target.add("id", jarray.get(i).getAsJsonObject().get("id"));
                temp.add("source", source);
                temp.add("target", target);
                siblings.add(temp);

                if (jarray.get(i-1).getAsJsonObject().has("children")) {
                    JsonObject t = jarray.get(i).getAsJsonObject();
                    generateSiblings(t);
                }
            }
        }


        return siblings;
    }


}


