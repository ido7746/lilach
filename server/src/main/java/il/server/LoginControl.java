package il.server;

import il.entities.*;
import il.entities.Order;

import java.util.LinkedList;
import java.util.List;



public class LoginControl {

    public static Message checkLogin(String userName, String pass, boolean isWorker) {
        Message message = new Message("result login");
        message.setWorker(isWorker);
        if (isWorker) {
            List<Employee> lEmp = SimpleServer.getAllItems(Employee.class);
            for (Employee employee : lEmp) {
                if (employee.getUsername().equals(userName)) {
                    if (employee.getPassword().equals(pass)) {
                        if (employee.isLogin()) {
                            message.setLoginResult("this user already sighing from another device!");
                            message.setLoginStatus(false);
                            return message;
                        }

                        //employee
                        setToActiveEmp(employee.getId());
                        message.setLoginStatus(true);
                        message.setWorker(true);
                        message.setUsername(employee.getUsername());
                        message.setName(employee.getName());
                        message.setIddatabase(employee.getId());
                        message.setPermision(employee.getPermission());
                        message.setPass(employee.getPassword());
                        switch (employee.getPermission()){
                            case 5://system admin send all information
                                SystemAdmin admin = (SystemAdmin) employee;
                                message.setEmployee(admin);
                                message.setListOrder(SimpleServer.getAllItemsInorder(Order.class, "dateOrder"));
                                message.setListComplains(SimpleServer.getAllItemsInorder(Complain.class,"date_complain"));
                                message.setListStors(SimpleServer.getAllItems(Store.class));
                                message.setListUsers(SimpleServer.getAllItems(User.class));
                                message.setListEmploeeys(SimpleServer.getAllItems(Employee.class));
                                //users
                                //employees
                                //stores
                                //report
                                break;
                            case 4://networkmaneger
                                NetworkManger net = (NetworkManger) employee;
                                message.setEmployee(net);
                                message.setListOrder(SimpleServer.getAllItemsInorder(Order.class, "dateOrder"));
                                message.setListComplains(SimpleServer.getAllItemsInorder(Complain.class,"date_complain"));
                                message.setListStors(SimpleServer.getAllItems(Store.class));
                                break;
                            case 3://branchManager
                                //report
                                BranchManager e = (BranchManager) employee;
                                message.setEmployee(employee);
                                message.setListOrder(SimpleServer.getAllItemsByKeyandOrderby(Order.class, "store", e.getStore().getId(), "dateOrder"));
                                message.setListComplains(SimpleServer.getAllItemsByKeyandOrderby(Complain.class, "store",e.getStore().getId(),"date_complain"));
                                break;
                            case 2://CustomerService
                                CustomerService c = (CustomerService)employee;
                                message.setEmployee(c);
                                message.setListComplains(SimpleServer.getAllItemsInorder(Complain.class,"date_complain"));
                                message.setListOrder(SimpleServer.getAllItems(Order.class));
                                break;
                            case 1://StoreEmployee
                                StoreEmployee s = (StoreEmployee)employee;
                                message.setEmployee(s);
                                message.setStoreID(s.getStore().getId());
                                message.setListComplains(SimpleServer.getAllItemsByKeyandOrderby(Complain.class, "store",s.getStore().getId(),"date_complain"));
                                break;
                        }
                        return message;
                    } else {
                        message.setLoginResult("incorrect password!");
                        message.setLoginStatus(false);
                        return message;
                    }
                }
            }
        }
        else {
            List<User> lUsers = SimpleServer.getAllItems(User.class);
            for (User user : lUsers) {
                if (user.getUserName().equals(userName)) {
                    if (user.getPassword().equals(pass)) {
                        if (user.isLogin()) {
                            message.setLoginResult("this user already sighing from another device!");
                            message.setLoginStatus(false);
                            return message;
                        }

                        //user
                        setToActiveUser(user.getId());
                        message.setLoginStatus(true);
                        message.setWorker(false);
                        message.setUser(user);
                        message.setListOrder(SimpleServer.getAllItemsByKey(Order.class, "user",user.getId()));
                        message.setListComplains(SimpleServer.getAllItemsByKey(Complain.class, "user",user.getId()));
                        testDB.openSession();
                        user = testDB.session.get(User.class, user.getId());
                        List<Store> s = user.getListstore();
                        LinkedList<Store> storelist = new LinkedList<>(s);
                        testDB.closeSession();
                        message.setListStors(storelist);
                        return message;

                    } else {
                        message.setLoginResult("incorrect password!");
                        message.setLoginStatus(false);
                        return message;
                    }
                }
            }
        }
        message.setLoginResult("username does not exist!");
        message.setLoginStatus(false);
        testDB.closeSession();
        return message;
    }




    private static void setToActiveUser(int idUser){
        testDB.openSession();
        User user = testDB.session.get(User.class, idUser);
        user.setLogin(true);
        testDB.session.flush();
        testDB.session.getTransaction().commit(); // Save everything.
        testDB.closeSession();
    }

    private static void setToActiveEmp(int idUser){
        testDB.openSession();
        Employee e = testDB.session.get(Employee.class, idUser);
        e.setLogin(true);
        testDB.session.flush();
        testDB.session.getTransaction().commit(); // Save everything.
        testDB.closeSession();
    }

    public static void setToDiactiveU(int id){
        testDB.openSession();
        User a = testDB.session.get(User.class, id);
        a.setLogin(false);
        testDB.session.flush();
        testDB.session.getTransaction().commit(); // Save everything.
        testDB.closeSession();
    }

    public static void setToDiactiveEmp(int id){
        testDB.openSession();
        Employee a = testDB.session.get(Employee.class, id);
        a.setLogin(false);
        testDB.session.flush();
        testDB.session.getTransaction().commit(); // Save everything.
        testDB.closeSession();
    }




}