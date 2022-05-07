package il.client;


import il.entities.Flower;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class UpdateController {

    @FXML
    private Button submit_button;

    @FXML
    private TextField update_text;

    private String new_price;

    private ProductView product;

    private Stage stage;

    private void updateImage(String url, int id) throws IOException {

        File file;
        file = new File(url);
        byte[] bFile = new byte[(int) file.length()];
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            String base64String = Base64.getEncoder().encodeToString(bFile);
            JSONObject cmd = new JSONObject();
            cmd.put("command", "setImages");
            cmd.put("id", product.getId());
            SimpleClient.getClient().sendToServer(cmd.toString());
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error: load image in updateImage!");
        }
    }


    @FXML
    void Submit_Button_Clicked(MouseEvent event) throws IOException, ClassNotFoundException, InterruptedException, JSONException {
        this.new_price = update_text.getText();
        product.setProduct_price(this.new_price);
        stage.close();
        JSONObject cmd = new JSONObject();
        cmd.put("command", "setPrice");
        cmd.put("id", product.getId());
        cmd.put("newPrice", product.getProduct_price());
        updateServerNewPrice(cmd.toString());
    }



    public void getProductView(ProductView to_change, Stage stage){
        this.product = to_change;
        this.stage = stage;
    }

    private void updateServerNewPrice(String command) throws IOException, ClassNotFoundException, InterruptedException {
        SimpleClient.getClient().sendToServer(command);
        TimeUnit.SECONDS.sleep(3);//need to wait to the server, need to use lock
    }

}
