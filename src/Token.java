public class Token {
    enum TokenType{
        Reserved,Identifier,Number,Symbol
    }
    public TokenType type ;
    public int numValue ;
    public String stringVal;

    Token(TokenType type1 , int val){
        this.type = type1;
        this.numValue = val;
    }

    Token(TokenType type , String val){
        this.type = type;
        this.stringVal = val;
    }

}
