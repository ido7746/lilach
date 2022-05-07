package il.client; /**
 * Sample Skeleton for 'ProductView.fxml' Controller Class
 */


import il.entities.Flower;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;

public class ProductView {

    @FXML // fx:id="atc_product_button"
    private Button atc_product_button; // Value injected by FXMLLoader

    @FXML // fx:id="product_image"
    private ImageView product_image; // Value injected by FXMLLoader

    @FXML
    private Label product_name;

    @FXML
    private Label product_price;

    @FXML // fx:id="update_product_button"
    private Button update_product_button; // Value injected by FXMLLoader

    @FXML
    private ImageView discount_logo;

    private boolean on_discount;

    private double discound_precentage;

    private int id_of_flower;


    public void setData(Flower a) throws IOException {
        product_price.setText(String.valueOf(a.getPrice()));
        product_name.setText(a.getName());

        Image image = new Image(new ByteArrayInputStream(a.getImage()));
        product_image.setImage(image);

        this.on_discount = a.isOn_discount();
        this.discound_precentage = a.getDiscount_perc();
        if(this.on_discount){
            this.discount_logo.setVisible(true);
            this.product_price.setText(String.valueOf((int)(a.getPrice() - (a.getPrice()*this.discound_precentage)/100)));
        }
        this.id_of_flower = a.getId();
    }

    @FXML
    void initialize(String string){
    }


//    @FXML   ---------> not in use <---------
//    void init(String name, String price, int id){
//        product_name.setText(name);
//        product_price.setText(price);
//        id_of_flower = id;
//    }

    @FXML
    void ClickedImage(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("PopWindow.fxml"));
        Parent root = fxmlLoader.load();    // need to load before using controller.
        PopWindow controller = fxmlLoader.getController();
        controller.FullSetter(this.getId(), this.product_name.getText(),this.product_price.getText(), this.on_discount, this.product_image.getImage());
        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("Flower Inner");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void UpdateProduct(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Update.fxml"));
        Parent root = fxmlLoader.load();    // need to load before using controller.
        UpdateController controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 250, 130);
        stage.setTitle("Update Price Section");
        stage.setScene(scene);
        stage.show();
        controller.getProductView(this,stage);
    }

    void setNewPrice(String price){
        this.product_price.setText(price);
    }
    @FXML
    void AddProductToCart(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AddToCart.fxml"));
        Parent root = fxmlLoader.load();    // need to load before using controller.
        AddToCartController controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 330, 31);
        stage.setTitle("Add To Cart Section");
        stage.setScene(scene);
        stage.show();
        controller.setStage(stage);
    }

    public void setProduct_price(String product_price) {
        this.product_price.setText(product_price);
    }

    @FXML
    public String getProduct_image() {
        return product_image.getImage().getUrl();
    }

    int getId(){
        return this.id_of_flower;
    }


    public void setProduct_image(ImageView product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name.getText();
    }

    public void setProduct_name(Label product_name) {
        this.product_name = product_name;
    }

    public int getProduct_price() {
        return Integer.parseInt(product_price.getText());
    }

    public void setProduct_price(Label product_price) {
        this.product_price = product_price;
    }

    public boolean isOn_discount() {
        return on_discount;
    }

    public void setOn_discount(boolean on_discount) {
        this.on_discount = on_discount;
    }

    public double getDiscound_precentage() {
        return discound_precentage;
    }

    public void setDiscound_precentage(double discound_precentage) {
        this.discound_precentage = discound_precentage;
    }

    public int getId_of_flower() {
        return id_of_flower;
    }

    public void setId_of_flower(int id_of_flower) {
        this.id_of_flower = id_of_flower;
    }

}
