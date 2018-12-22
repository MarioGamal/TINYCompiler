import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class Controller {

   @FXML public JFXButton inputBtn ;
   @FXML public Label filepathLabel ;
   @FXML public TextArea ScannerOpText;
   @FXML public WebView parserOp;
   @FXML public TextArea JSONText ;
    public File f ;

    public String scannerip;


    public void filechooserBtn() throws Exception{
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));
        f = fc.showOpenDialog(null);
        ScannerOpText.clear();
        JSONText.clear();

        if(f!=null)
        {
            filepathLabel.setText("Selected File:  " + f.getAbsolutePath());
            scannerip = Scanner.ReadFile(f);
            ArrayList<Token> Tokens = Scanner.scanInput(scannerip);
            for(Token x : Tokens)
            {
                ScannerOpText.appendText(x.stringVal + " => "+x.type +"\n");
            }
            ParserOptimized parser = new ParserOptimized(f);
            JSONObject jsonop = parser.prog();

            //PRETTY PRINT UGLYJSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(jsonop.toJSONString());
            String indentedJsonString = gson.toJson(je);

            JSONText.setText(indentedJsonString);

        // DRAWING OUTPUT OF PARSER
            //WebEngine engine = parserOp.getEngine();
            //File htmlFile = new File("C:\\Users\\Mario\\IdeaProjects\\TinyScanner\\src\\index.html");
            //engine.load(htmlFile.toURI().toString());
            //engine.executeScript("var root = "+jsonop.toJSONString());
            //JsonObject jobject = je.getAsJsonObject();

            //engine.executeScript("var siblings = "+parser.generateSiblings(jobject).toString());
            //engine.executeScript("DrawFamilyTree()");
            //engine.loadContent("index.html","HTML");

        }
    }

}
