package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RoutePoint {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_point")
    private int idPoint;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "order_index")
    private int orderIndex;

    // Виправив 'prise' на 'price' :)
    @Column(name = "cumulative_price")
    private double price;

    // --- НОВІ ПОЛЯ ---

    @Column(name = "arrival_time")
    private LocalTime arrivalTime;   // Коли автобус приїжджає сюди

    @Column(name = "departure_time")
    private LocalTime departureTime; // Коли автобус їде звідси
}

