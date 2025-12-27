package com.example.demo.controller;

import com.example.demo.model.City;
import com.example.demo.repository.CityRepository;
import com.example.demo.service.AdminCityService;
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
        try{
            City newCity = adminCityService.createCity(city.getName());

            return ResponseEntity.status(HttpStatus.CREATED).body(newCity);
        }
        catch (IllegalArgumentException e){
            throw new RuntimeException("Таке місто вже існує");
        }

    }

    //  Отримати всі міста (GET)
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityRepository.findAll());
    }

    //  Видалити місто (DELETE)
    @DeleteMapping
    public ResponseEntity<HttpStatus>deleteCity(@PathVariable Long id){

       try {
           adminCityService.deleteCity(id);
           return ResponseEntity.ok().build();
       }
       catch (IllegalArgumentException e){
           throw  new RuntimeException("Такого місця не існує");
       }

    }

    //  Оновити місто (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City cityDetails) {
        try {
            City updateCity = adminCityService.updateCity(id, cityDetails);
            return ResponseEntity.ok(updateCity);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<?> updateCityName(@PathVariable Long id,@RequestBody Map<String, String> updates) {
        try {
            City updateCity = adminCityService.updateCityName(id, updates);
            return ResponseEntity.ok(updateCity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NullPointerException e) {
            return ResponseEntity.notFound().build();
        }
    }


}