package com.example.myandroidproject.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    private Integer id;
    private Integer vehicleid;
    private String type;
    private double discount;
    private String nameVehicle;
    private String brandVehicle;
    private Date rentalDate;
    private Date returnDate;
    private Integer rental_day;
    private double priceDiscount;
    private double price;
    private String imageLink;
    private String description;
    private String color;
    public Vehicle(String name, String brand, double price, String imageLink) {
    }
}
