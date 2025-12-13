package com.example.demo.controller;

import com.example.demo.model.City;
import com.example.demo.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Важливо: URL починається з /api/admin, який ми захистили
@RestController
@RequestMapping("/api/admin/cities")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminCityController {

    private final CityRepository cityRepository;

    // 1. Створити нове місто (POST)
    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody City city) {
        City savedCity = cityRepository.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCity);
    }

    // 2. Отримати всі міста (GET)
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityRepository.findAll());
    }

    // 3. Оновити місто (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City cityDetails) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Місто не знайдено"));

        city.setName(cityDetails.getName());
        City updatedCity = cityRepository.save(city);
        return ResponseEntity.ok(updatedCity);
    }

    // 4. Видалити місто (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCity(@PathVariable Long id) {
        cityRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}