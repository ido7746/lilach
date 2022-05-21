package il.entities;

import java.io.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "items")
public class Flower implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    @Lob
    @Column(name="Folwer_IMAGE", nullable=false, columnDefinition="mediumblob")
    private byte[] image;
    private double price;
    private boolean sale;
    private double discount_perc;

    public Flower(String name, double price, boolean sale, double discount_perc){
        this.name = name;
        this.price = price;
        this.sale = sale;
        this.discount_perc = discount_perc;
        this.image = null;
    }

    public Flower(String name, double price){
        this.name = name;
        this.price = price;
        this.sale=false;
        this.discount_perc = 0;
        this.image = null;
    }

    public Flower() {

    }


    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getDiscount_perc() {
        return discount_perc;
    }

    public void setDiscount_perc(double discount_perc) {
        this.discount_perc = discount_perc;
    }

    public void setSale(boolean sale) {
        this.sale = sale;
        if(sale==false)
            this.discount_perc=0;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public boolean isOn_discount() {
        return sale;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}