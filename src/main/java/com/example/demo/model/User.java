package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_users") // 1. Змінили назву, щоб не конфліктувати з SQL
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser; // 2. Краще використовувати Long для ID (int може переповнитися)

    @Column(nullable = false)
    private String name; // Ім'я (наприклад "Ivan")

    @Column(unique = true, nullable = false)
    private String email; // 3. Це буде наш логін (унікальний)

    @Column(nullable = false)
    private String password; // Пароль (буде зашифрований)

    private String role = "ROLE_USER"; // 4. Роль за замовчуванням
}