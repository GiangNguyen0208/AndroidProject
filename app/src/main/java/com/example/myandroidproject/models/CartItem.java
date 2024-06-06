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
    private Integer id, idVehicle;
    private String nameItem, imageLink;
    private double price;
    private Date rentalDate, returnDate;
}
