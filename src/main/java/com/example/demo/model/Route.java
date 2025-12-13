package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_route")
    private Long idRoute;

    @Column(name = "name_route")
    private String nameRoute;

    // --- НОВІ ПОЛЯ ---

    @Column(name = "start_time")
    private LocalTime startTime; // Час відправлення з першої точки

    @Column(name = "end_time")
    private LocalTime endTime;   // Час прибуття в останню точку

    // Цей список дозволить тобі дістати всі зупинки маршруту однією командою
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<RoutePoint> routePoints;
}
