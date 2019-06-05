import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.net.URL;
import java.io.UnsupportedEncodingException; 
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.lang.Object;
import com.google.gson.*;


public class Bitly extends Application {
  TextField txtLong, txtShort;
  Button btnShorten;

  @Override
  public void start(Stage primaryStage) throws Exception {
    // Make the controls
    txtLong=new TextField("Enter Long URL");
    btnShorten=new Button("Get Short Link");
    txtShort=new TextField("Short Link");
    
    // Center text in label
    txtLong.setAlignment(Pos.CENTER);
    btnShorten.setAlignment(Pos.CENTER);
    txtShort.setAlignment(Pos.CENTER);
    
    // Make container for app
    VBox root = new VBox();
    // Put container in middle of scene
    root.setAlignment(Pos.CENTER);
    // Spacing between items
    root.setSpacing(15);
    // Add to VBox
    root.getChildren().add(txtLong);
    root.getChildren().add(btnShorten);
    root.getChildren().add(txtShort);
   
    // Set widths
    setWidths();
    //attach buttons to code in separate method
    attachCode();
    // Set the scene
    Scene scene = new Scene(root, 350, 250);
    primaryStage.setTitle("Iuliia Buniak");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

    public void setWidths(){
    // Set widths of all controls
    txtLong.setMaxWidth(250);
    btnShorten.setMaxWidth(250);
    txtShort.setMaxWidth(250);
 
  }
   
    public void attachCode() throws Exception{
    // Attach actions to buttons
    btnShorten.setOnAction(e -> btnShortenCode(e));
 
  }
  
  // Function to validate an URL
  public static boolean urlValidator (String url) {
    try {
       new URL(url).toURI();
       return true;
    }
    catch (URISyntaxException exeption) {
      return false;
    }
    catch (MalformedURLException exception) {
      return false;
    }
  }
  
  // Button creates a URL request for data from bitly.com to shorten the URL entered.  // coverts to postfix and displays in the Postfix Notation Label 
  // Displays appropriate error messages for invalid URLs.
  public void btnShortenCode(ActionEvent e)  {
    if ( txtLong.getText().equals("Enter Long URL") ) {
      txtShort.setText("ERROR!");
    }
    else {
    
      JsonElement jse = null;
      String longURL = txtLong.getText();
      
      // if URL is not valid, display corresponding message
      if (!urlValidator(longURL)){
        txtShort.setText("Invalid URL");
      } 
      // if URL is valid, convert it into short form
      else { 
       try{
        
		// Construct API URL
		URL link = new URL("https://api-ssl.bitly.com/v3/shorten?access_token=499d66fdd0881aeba62ad940b30b8d58daae3b20&longURL=" 
        + URLEncoder.encode(longURL, "UTF-8"));

		// Open the URL
		InputStream is = link.openStream(); // throws an IOException
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// Read the result into a JSON Element
		jse = new JsonParser().parse(br);
      
		// Close the connection
		is.close();
		br.close();
	  }
	  catch (java.io.UnsupportedEncodingException uee){
		uee.printStackTrace();
        txtShort.setText("Unsupported Encoding");
	  }
	  catch (java.net.MalformedURLException mue){
		mue.printStackTrace();
        txtShort.setText("Invalid URL");
	  }
	  catch (java.io.IOException ioe){
		ioe.printStackTrace();
        txtShort.setText("IOException");
	  }
      }
      
	  if (jse != null){
        
        // Set a shorten URL 
        JsonObject obj = jse.getAsJsonObject();
        obj = obj.getAsJsonObject("data");
        String shortURL = obj.get("url").getAsString();
        txtShort.setText(shortURL);
        
      }   
    }  
  }
  
  
  public static void main(String[] args) {
    launch(args);
  }

}