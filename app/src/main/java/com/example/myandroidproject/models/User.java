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
public class User {
    private Integer id;

    private String username;

    private String firstname;

    private String lastname;

    private Boolean gender;

    private String phone;

    private String email;

    private Date birthDay;

    private Boolean status;

    private String password;

    private String address;

    private String roleName;

}
