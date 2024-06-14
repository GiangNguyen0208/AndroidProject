package com.example.myandroidproject.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Integer id, idVehicle, rental_day, userid;
    private String nameItem, imageLink;
    private double price;
    private Date rentalDate, returnDate;

    private String address;

    private String email;

    private String phone;
}
