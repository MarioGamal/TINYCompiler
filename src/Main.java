import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) throws Exception {

        //File file = new File("C:\\Users\\Mario\\IdeaProjects\\TinyScanner\\src\\tiny_sample_code.txt");
        //ParserOptimized parser = new ParserOptimized(file);
        //JSONObject result = parser.prog();
        //System.out.println(result.toString());

        //generating siblings
        //String root = result.toJSONString();
        //JsonElement jelement = new JsonParser().parse(root);
        //JsonObject jobject = jelement.getAsJsonObject();

        //JsonArray jsonArray = parser.generateSiblings(jobject);
        //System.out.println(jsonArray.toString());

        /*for(Token x : parser.ScannedTokens)
        {
            System.out.println(x.stringVal);
        }
        System.out.println(parser.ScannedTokens.size());*/

        //LAUNCH GUI
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
