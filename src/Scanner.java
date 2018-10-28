import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Scanner {
    enum STATE{
        START,INCOMMENT,INID,INNUM,INASSIGN,DONE
    }

    public static String Reserveds[] = {"if","then","else","until","end","repeat","read","write"};
    public static String Symbols[] = {"+","-","*","/","=","<","(",")",";",":="};

    public static ArrayList<String> ReservedWords = new ArrayList<String>(Arrays.asList(Reserveds));
    public static ArrayList<String> SpecialSymbols = new ArrayList<String>(Arrays.asList(Symbols));



    public static String ReadFile(File file){
        String input = null;
        try{
            java.util.Scanner fscanner = new java.util.Scanner(file);
            input = fscanner.nextLine();
            while(fscanner.hasNextLine()){
                input+="\n"+fscanner.nextLine();
            }
        }
        catch(FileNotFoundException ex){
            System.out.println(ex.getMessage());
        }
        return input;
    }

    public void scanInput(String input){
        STATE currentState = STATE.START;
        char[] charsInput = input.toCharArray();


    }



    public static void main(String[] args) {
        File inputFile = new File("src/tiny_sample_code.txt");
        STATE currentState = STATE.START;





    }



}
