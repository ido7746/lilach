package il.server;

import il.entities.Store;
import il.entities.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class RegisterControl {




    public static String checknewUser(User newUser){
        List<User> lUsers = SimpleServer.getAllItems(User.class);
//        compare_name c = new compare_name();
//        int result = Collections.binarySearch(lUsers,newUser, c.comparename());
        for (User user: lUsers){
            if (user.getUserName().equals(newUser.getUserName()) || user.getIdentifyNumbers().equals(newUser.getIdentifyNumbers()) ||
                    user.getCreditCard().equals(newUser.getCreditCard()))
                if (user.getUserName().equals(newUser.getUserName()))
                    return "Error: User has been used";
            if (user.getIdentifyNumbers().equals(newUser.getIdentifyNumbers()))
                return "Error: ID has been used";
            if (user.getCreditCard().equals(newUser.getCreditCard())) {
                if (user.getAccountStatus() == 0)
                    return "Error: credit_card belong to frozen account";
            }
        }
        return "";
    }



    public static void register(User newUser, List<Store> stores){
        testDB.openSession();
        try {
            //add
            if (newUser.getPriority() == 2)
                newUser.setCredit(-100);
            if (newUser.getPriority() == 3){
                DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDateTime now = LocalDateTime.now();
                String date = dtf2.format(now);
                char a =date.charAt(3);
                int b = (int)a;
                a =(char)(b+1);
                char[] exp_date = date.toCharArray();
                exp_date[3] = a;
                date =  new String(exp_date);
                newUser.setExpiryDate(date);
            }
            //
            testDB.session.save(newUser);
            for(Store s:stores){
                Store store = testDB.session.get(Store.class, s.getId());
                store.addUser(newUser);
            }
            testDB.session.flush();
            testDB.session.getTransaction().commit();
            System.out.println("user add to mySQL");
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error: register");
        }
        testDB.closeSession();
    }

}