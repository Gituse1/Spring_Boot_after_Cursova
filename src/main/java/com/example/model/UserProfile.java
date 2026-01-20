package com.example.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user_data")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String address;

    private String city;

//    // Посилання на аватарку, якщо зберігаєш фото
//    @Column(name = "avatar_url")
//    private String avatarUrl;

    @Column(name = "bio", length = 1000,columnDefinition = "TEXT") // 1. Інструкція для БД: створити VARCHAR(1000)
    @Size(max = 1000, message = "Опис не може перевищувати 1000 символів")
    private String bio;
}