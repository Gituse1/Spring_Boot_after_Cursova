package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "Admin_user")
public class Admin {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(nullable = false)
    private String name; // Ім'я (наприклад "Ivan")

    @Column(unique = true, nullable = false)
    private String email; // 3. Це буде наш логін (унікальний)

    @Column(name = "password",nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;



}
