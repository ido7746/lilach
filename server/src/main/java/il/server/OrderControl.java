package il.server;

import il.entities.*;

import java.io.IOException;

public class OrderControl {

    public static void cancelOrder(int id){
        testDB.openSssion();
        Order a = testDB.session.get(Order.class, id);
        testDB.session.delete(a);
        testDB.session.flush();
        testDB.session.getTransaction().commit(); // Save everything.
        testDB.closeSession();
    }
    /** added 3 func for refunds*/
//    public static void deleteOrder(Order a){
//        testDB.openSssion();
//        testDB.session.delete(a);
//        testDB.session.flush();
//        testDB.session.getTransaction().commit(); // Save everything.
//        testDB.closeSession();
//    }
//
//    public static void cancelOrder(int id){
//        testDB.openSssion();
//        Order a = testDB.session.get(Order.class, id);
//        testDB.closeSession();
//        String current_date = java.time.LocalDate.now().toString();
//        int current_time = timeToInt(java.time.LocalTime.now().toString());
//        int order_time = timeToInt(a.getTimeReceive());
//        if (current_date.equals(a.getDateReceive()) && order_time - current_time < 300) {
//            if (order_time - current_time > 60)
//                //a.get 50% refunds
//            deleteOrder(a);
//            return;
//        }
//        //a.get 100% refunds
//        deleteOrder(a);
//        return;
//    }
//
//
//    public static int timeToInt(String time){
//        time = time.replace(":", "");
//        time = time.substring(0,3);
//        return Integer.parseInt(time);
//    }

    public static void newOrder(Order order, int storeID, int userID) throws IOException {
        testDB.openSssion();
        Store store = testDB.session.get(Store.class, storeID);
        User user = testDB.session.get(User.class, userID);

        if(!user.getListstore().contains(store)){
            System.out.println(user.getUserName() + " try to made order is store that he never register!");
        }
        else{
            for(CartProduct p : order.getProducts())
                testDB.session.save(p);

            testDB.session.save(order);
            user.addOrder(order);
            store.addOrder(order);
        }
        testDB.session.flush();
        testDB.session.getTransaction().commit(); // Save everything.
        testDB.closeSession();
    }

}
