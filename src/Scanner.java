import java.io.*;
import java.util.ArrayList;

public class Scanner {
    enum STATE{
        START,INCOMMENT,INID,INNUM,INASSIGN,DONE
    }


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

    public static ArrayList<Token> scanInput(String input){
        ArrayList<Token> CapturedTokens = new ArrayList<>();
        STATE currentState = STATE.START;
        char[] charsInput = input.toCharArray();
        String longTokenVal = "";
        for(int i=0;i<charsInput.length;i++) {
            String charstr = Character.toString(charsInput[i]);
            switch (currentState) {
                case START:
                    if(charstr.matches("\\s"))
                    {
                        currentState = STATE.START;
                    }
                    else if (charstr.matches("\\{"))
                    {
                        currentState=STATE.INCOMMENT;
                    }
                    else if (charstr.matches("[0-9]"))
                    {
                        currentState = STATE.INNUM;
                        longTokenVal+=charstr;
                    }
                    else if(charstr.matches("[a-zA-Z_]"))
                    {
                        currentState = STATE.INID;
                        longTokenVal+=charstr;
                    }
                    else if(charstr.matches(":"))
                    {
                        currentState = STATE.INASSIGN;
                        longTokenVal+=charstr;
                    }
                    else{
                        longTokenVal+=charstr;
                        currentState = STATE.DONE;
                    }
                    break;
                case INCOMMENT:
                    if (charstr.matches("\\}"))
                    {
                        currentState=STATE.START;
                    }
                    break;
                case INNUM:
                    if (charstr.matches("[0-9]"))
                    {
                        longTokenVal+=charstr;
                        currentState = STATE.INNUM;
                    }
                    else {
                        currentState = STATE.DONE;
                        i--;
                    }
                    break;
                case INID:
                    if(charstr.matches("[a-zA-Z_]") || charstr.matches("[0-9]"))
                    {
                        longTokenVal+=charstr;
                        currentState = STATE.INID;
                    }
                    else
                    {
                        currentState = STATE.DONE;
                        i--;
                    }
                    break;
                case INASSIGN:
                    if (charstr.matches("="))
                    {
                        longTokenVal+=charstr;
                        currentState = STATE.DONE;
                    }
                    else
                    {
                        currentState = STATE.DONE;
                    }
                    break;
                case DONE:
                    i--;

                    if (!longTokenVal.replace("\\s","").equals(""))
                    {
                        CapturedTokens.add(new Token(longTokenVal.toLowerCase()));
                        currentState = STATE.START;
                        longTokenVal="";
                    }
                    break;
            }
        }
        CapturedTokens.add(new Token(longTokenVal.toLowerCase()));
        return CapturedTokens;
    }



    public static void main(String[] args) throws IOException {
        File inputFile = new File(args[0]);
        String code = ReadFile(inputFile);
        //File outputFile = new File(args[0] + "/../output_file.txt");
        ArrayList<Token> Tokens = scanInput(code);

        //PrintStream out = new PrintStream(new FileOutputStream(args[0].replace(getExtension(args[0]),"out.txt")));
        //System.setOut(out);

        for(Token x : Tokens)
        {
            System.out.println(x.stringVal + " => "+x.type);
        }

    }

    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int extensionPos = filename.lastIndexOf('.');
        int lastUnixPos = filename.lastIndexOf('/');
        int lastWindowsPos = filename.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);

        int index = lastSeparator > extensionPos ? -1 : extensionPos;
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }
}
