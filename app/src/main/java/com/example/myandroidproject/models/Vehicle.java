package com.example.myandroidproject.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehicle {
    private Integer id;
    private Integer vehicleid;
    private String nameVehicle;
    private String brandVehicle;
    private Date rentalDate;
    private Date returnDate;
    private Integer rental_day;
    private double price;

    private String imageLink;
    public Vehicle(String name, String brand, double price, String imageLink) {
    }
}
