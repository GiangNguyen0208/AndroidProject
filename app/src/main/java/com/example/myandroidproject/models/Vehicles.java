package com.example.myandroidproject.models;

import org.json.JSONException;
import org.json.JSONObject;


public class Vehicles {
    private String name;
    private String about;
    private String type;
    private double price;
    private double discount;
    private String color, brand, model, imgURL;

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscount() {
        return discount;
    }

    public String getColor() {
        return color;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void importData(JSONObject data) {
        try {
            this.name = data.getString("name");
            this.about = data.getString("about");
            this.type = data.getString("type");
            this.price = data.getDouble("price");
            this.discount = data.getDouble("discount");
            this.color = data.getString("colorName");
            this.brand = data.getString("brandName");
            this.model = data.getString("model");
            this.imgURL = data.getString("imageUrl");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
