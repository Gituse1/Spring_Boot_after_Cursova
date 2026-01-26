package com.example.controller;

import com.example.model.City;
import com.example.repository.CityRepository;
import com.example.service.admin.AdminCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/cities")
@RequiredArgsConstructor
public class AdminCityController {

    private final CityRepository cityRepository;
    private final AdminCityService adminCityService;

    //  Створити нове місто (POST)
    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody City city) {

        City newCity = adminCityService.createCity(city.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(newCity);

    }

    //  Отримати всі міста (GET)
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityRepository.findAll());
    }

    //  Видалити місто (DELETE)
    @DeleteMapping
    public ResponseEntity<HttpStatus>deleteCity(@PathVariable Long id){

        adminCityService.deleteCity(id);
        return ResponseEntity.ok().build();

    }

    //  Оновити місто (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City cityDetails) {

        City updateCity = adminCityService.updateCity(id, cityDetails);
        return ResponseEntity.ok(updateCity);

    }

    @PatchMapping ("/{id}")
    public ResponseEntity<City> updateCityName(@PathVariable Long id,@RequestBody Map<String, String> updates) {

        City updateCity = adminCityService.updateCityName(id, updates);
        return ResponseEntity.ok(updateCity);

    }


}