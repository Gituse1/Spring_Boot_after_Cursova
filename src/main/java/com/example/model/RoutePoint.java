package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutePoint {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idPoint;

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
    private Double price;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;   // Коли автобус приїжджає сюди

    @Column(name = "departure_time")
    private LocalDateTime departureTime; // Коли автобус їде звідси


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}

