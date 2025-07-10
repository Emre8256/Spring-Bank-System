package com.bank.BankSystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name",nullable = false)
    private String name;

    @Column(name="surname",nullable = false)
    private String surname;

    @Column(name="identity_number",unique = true,length = 11, nullable = false)
    private String identityNumber;

    @Column(name="phone",unique = true,length = 10, nullable = false)
    private String phone;

    @Column(name="address",nullable = false)
    private String address;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Account> accounts = new ArrayList<>();
}
