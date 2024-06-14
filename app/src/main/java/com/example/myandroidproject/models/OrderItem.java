package com.example.myandroidproject.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Integer id;

    private Integer vehicleid;

    private String nameVehicle;

    private String brandVehicle;

    private String imageLink;

    private double price;

    private Date rentalDate;

    private Date returnDate;

    private String status;

    private int rental_day;

    private String address;

    private String email;

    private String phone;
}
