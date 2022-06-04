package il.client;

import il.client.controls.LogInControl;
import il.client.events.LoginEvent;
import il.entities.Complain;
import il.entities.Order;
import il.entities.Store;
import il.entities.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.LinkedList;

public class LoginController extends ParentClass{

    @FXML
    private MFXToggleButton workerChecker;

    @FXML
    private MFXButton loginBTN;

    @FXML
    private MFXPasswordField password_field;

    @FXML
    private MFXTextField username_field;

    private MainPageController main_page_holder;



    private static boolean correctLogin;

    private int loginTries = 1;

    public static LinkedList<Complain> complains;
    public static LinkedList<Order> orders;
    public static LinkedList<Store> stores;
    public static User user;
    public static int permission;
    public static int idConnected;
    public static String username;
    public static boolean isWorker;
    public int storeIDWork;



    @Subscribe
    public void compliteLogin(LoginEvent event){
        Platform.runLater(()->{
            correctLogin = event.isLoginStatus();
            if(correctLogin){
                //goto var which represent the login option on the Main Controller and change it to 1.
                //change Main Controller AnchorPane to Catalog -> "maybe return to the last page the client was inside"
                MainPageController.LoginName = event.getUsername();
                MainPageController.isLogin = true;
                isWorker = event.isWorker();
                if(event.isWorker()){
                    permission = event.getPermission();
                    idConnected = event.getId();
                    username = event.getUsername();
                    orders = event.getOrderList();
                    complains = event.getComplainList();
                    storeIDWork = event.getStoreId();
                }
                else{
                    user = event.getUser();
                    complains = event.getComplainList();
                    orders = event.getOrderList();
                    stores = event.getStoreList();
                    username = user.getUserName();
                    idConnected = user.getId();
                }

                try {
                    MainPageController.getInstance().CatalogRefresh(); //Catalog, MyAccount, Cart
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    this.main_page_holder.UpdateMainController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.loginTries = 1;
            }else {
                password_field.clear();
                username_field.clear();
                username_field.setPromptText(event.getResult());
                this.loginTries++;
                if(this.loginTries == 6){
                    System.out.println(this.loginTries);
                    TranslateTransition transition = new TranslateTransition();
                    transition.setDuration(Duration.millis(60000)); //1 minute
                    transition.setNode(loginBTN);
                    transition.setAutoReverse(false);
                    loginBTN.setDisable(true);
                    loginBTN.setText("Waiting 1 Minute");
                    transition.setOnFinished(evt -> {
                        loginBTN.setDisable(false);
                        loginBTN.setText("Submit");
                    }); //disable the submit button for 1 minute

                    transition.play();
                }
            }
        });
    }

    @FXML
    void initialize() throws IOException, ClassNotFoundException, InterruptedException {
        EventBus.getDefault().register(this);
    }


    @FXML
    void LoginSumbitted(ActionEvent event) throws IOException {
        String username = username_field.getText();
        String password = password_field.getText();
        boolean usernameFound,passwordFound;  //this vars will get '1' value if we found the correct values in the DB
        usernameFound = passwordFound = false;

        if(username == "" || password == ""){ // taking care of null strings
            password_field.clear();
            username_field.clear();
            username_field.setPromptText("Username Or Password Empty");
            username_field.setStyle("-border-color:-mfx-red");
        }else if(password.contains("-")){

        }
        if(isWorker){
            LogInControl.logIn(username, password, isWorker);
//            UserClient.getInstance().setUserByServer(UserClient.getInstance().getUserServer());
            //correctLogin = (send msg to server - to find (string = username-password)
            // for specific worker id in the worker table
            //  return 'true' if username found and the password matches the username's id found
            // don't return list of workers, just return true or false according to the result)
        } else {
            LogInControl.logIn(username, password, isWorker);
//            UserClient.getInstance().setUserByServer(UserClient.getInstance().getUserServer());
            }
            //correctLogin = (send msg to server - to find (string = username-password)
            // for specific client id in the client table
            //  return 'true' if username found and the password matches the username's id found
            // don't return list of clients, just return true or false according to the result)
        }

    public Button getLoginBTN() {
        return loginBTN;
    }

    @FXML
    void WorkerChecked(MouseEvent event) {
        this.isWorker = workerChecker.isSelected(); //if the GUI user checked for 'worker login' return 'true'
    }

    /* Getters and Setters */

    public void setMain_page_holder(MainPageController main_page_holder) {
        this.main_page_holder = main_page_holder;
    }
    /*END*/

}