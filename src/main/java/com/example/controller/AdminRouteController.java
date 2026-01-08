package com.example.controller;


import com.example.model.Route;
import com.example.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "http://localhost:3000") // Дозволяємо запити з React
public class AdminRouteController {

    @Autowired
    private RouteRepository routeRepository;

    // Цей метод потрібен для AdminPage.jsx
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeRepository.findAll());
    }

    // (Опціонально) Створення маршруту, якщо немає в базі
    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        return ResponseEntity.ok(routeRepository.save(route));
    }
}