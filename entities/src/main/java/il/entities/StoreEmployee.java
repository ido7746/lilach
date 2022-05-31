package il.entities;


import java.io.Serializable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;



@Entity
public class StoreEmployee extends Employee implements Serializable {

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public StoreEmployee() {
        super();
    }

    public StoreEmployee(String name, String username, String pass, int permission) {
        super(name, username, pass, permission);
    }

}