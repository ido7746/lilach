package il.server;
import il.entities.*;
import il.server.ocsf.ConnectionToClient;
import il.server.ocsf.AbstractServer;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SimpleServer extends AbstractServer {

    public SimpleServer(int port, boolean initServer) {
        super(port);
        System.out.println("Server listen on port:" + port);
        if(initServer)
            testDB.initMySQL();
        //TestControl.testUserControl();
        UserControl.logOutAllusers();
    }

    public void closeServer() throws IOException {
        testDB.closeSession();
        this.close();
    }


    //get item by order
    public static <T, S> LinkedList<T> getAllItemsByKeyandOrderby(Class<T> object, String colum,S key, String orderby){
        testDB.openSession();
        CriteriaBuilder builder = testDB.session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(object);
        Root<T> root = query.from(object);
        query.select(root);
        query.where(builder.equal(root.get(colum),key));
        query.orderBy(builder.desc(root.get(orderby)));
        List<T> data = testDB.session.createQuery(query).getResultList();
        LinkedList<T> listItems = new LinkedList<>(data);
        testDB.closeSession();
        return listItems;
    }

    public static <T, S> LinkedList<T> getAllItemsByKey(Class<T> object, String colum,S key){
        testDB.openSession();
        CriteriaBuilder builder = testDB.session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(object);
        Root<T> root = query.from(object);
        query.select(root);
        query.where(builder.equal(root.get(colum),key));
        List<T> data = testDB.session.createQuery(query).getResultList();
        LinkedList<T> listItems = new LinkedList<>(data);
        testDB.closeSession();
        return listItems;
    }

    public static <T> LinkedList<T> getAllItemsInorder(Class<T> object, String orderby){
        testDB.openSession();
        CriteriaBuilder builder = testDB.session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(object);
        Root<T> root = query.from(object);
        query.orderBy(builder.desc(root.get(orderby)));
        List<T> data = testDB.session.createQuery(query).getResultList();
        LinkedList<T> listItems = new LinkedList<>(data);
        testDB.closeSession();
        return listItems;
    }


    public static <T> LinkedList<T> getAllItems(Class<T> object){
        testDB.openSession();
        CriteriaBuilder builder = testDB.session.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(object);
        Root<T> root = query.from(object);
        List<T> data = testDB.session.createQuery(query).getResultList();
        LinkedList<T> listItems = new LinkedList<>(data);
        testDB.closeSession();
        return listItems;
    }



    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
            try {
            Message message = (Message) msg;
            Message sendMessage = new Message("");
            if (message.getMessage().equals("login")) {
                String username = message.getUsername();
                String pass = message.getPass();
                boolean isWorker = message.isWorker();

                sendMessage = LoginControl.checkLogin(username, pass, isWorker);
                System.out.println(sendMessage.getLoginResult());
                client.sendToClient(sendMessage);
                }

            if (message.getMessage().equals("getCatalogItems")) {
                sendMessage.setMessage("item catalog list");
                sendMessage.setListItem(getAllItems(Product.class));
                sendMessage.setListStors(getAllItems(Store.class));
                client.sendToClient(sendMessage);
                System.out.println("send init data to: " + client.getInetAddress());
            }


            if (message.getMessage().equals("logout")) {
                int id = message.getIddatabase();
                boolean isworker = message.isWorker();
                if (isworker)
                    LoginControl.setToDiactiveEmp(id);
                else
                    LoginControl.setToDiactiveU(id);
            }

            if (message.getMessage().equals("newItem")) {
                CatalogControl.newItem(message.getProduct());
            }

            if (message.getMessage().equals("setImageItem")) {
                CatalogControl.setImage(message.getIdItem(), message.getbFile());
            }

            if (message.getMessage().equals("register")) {
                String username = message.getUsername();
                String name = message.getName();
                String pass = message.getPass();
                String id = message.getId();
                String credit_card = message.getCredit_card();
                int plan = message.getPlan();
                List<Store> stores = message.getListStors();

                User newUser = new User(username, pass, credit_card, plan, name, id);
                System.out.println("get register request:" + username);

                String result = RegisterControl.checknewUser(newUser);


                if (result.equals("")) {
                    RegisterControl.register(newUser, stores);
                } else {
                    sendMessage.setRegisterStatus(false);
                    sendMessage.setRegisterResult(result);
                }

                sendMessage.setMessage("result register");
                client.sendToClient(sendMessage);
            }

            if (message.getMessage().equals("setNameItem")) {
                CatalogControl.setName(message.getIdItem(), message.getNameProduct());
            }
            if (message.getMessage().equals("setSaleItem")) {
                CatalogControl.setSale(message.getIdItem(), message.isSale(), message.getDiscountPer());
            }
            if (message.getMessage().equals("setPriceItem")) {
                CatalogControl.setPrice(message.getIdItem(), message.getNewPrice());
            }
            if (message.getMessage().equals("setTypeItem")) {
                CatalogControl.setType(message.getIdItem(), message.getType());
            }
            if (message.getMessage().equals("setColorItem")) {
                CatalogControl.setColor(message.getIdItem(), message.getColor());
            }
            if (message.getMessage().equals("deleteItem")) {
                CatalogControl.deleteItem(message.getIdItem());
            }
            if (message.getMessage().equals("cancelOrder")) {
                OrderControl.cancelOrder(message.getOrderID(),message.getTimeCancel(), message.getDateCancel());
            }
            if (message.getMessage().equals("newOrder")) {
                OrderControl.newOrder(message.getOrder(), message.getStoreID(), message.getUserID());
            }
            if (message.getMessage().equals("newComplain")) {
                ComplainConrtol.newComplain(message.getComplain(), message.getOrderID());
            }
            if (message.getMessage().equals("complainAnswer")) {
                ComplainConrtol.complainAnswer(message.getAnswer(), message.getRefund(), message.getComplainID());
            }
            if (message.getMessage().equals("setUserName")) {
                UserControl.setUserName(message.getUserID(), message.getUsername(), message.isWorker());
            }
            if (message.getMessage().equals("setName")) {
                UserControl.setName(message.getUserID(), message.getName(), message.isWorker());
            }
            if (message.getMessage().equals("setPassword")) {
                UserControl.setPassword(message.getUserID(), message.getPass(), message.isWorker());
            }
            if (message.getMessage().equals("setCreditCard")) {
                UserControl.setCreditCard(message.getUserID(), message.getCredit_card(), message.isWorker());
            }
            if (message.getMessage().equals("setPhone")) {
                UserControl.setPhone(message.getUserID(), message.getPhone(), message.isWorker());
            }
            if (message.getMessage().equals("setMail")) {
                UserControl.setName(message.getUserID(), message.getMail(), message.isWorker());
            }

            } catch(IOException e){
            System.out.println(e.getMessage());
            System.out.println("handleMessageFromClient Error!" + client.getInetAddress());
        }
    }
}