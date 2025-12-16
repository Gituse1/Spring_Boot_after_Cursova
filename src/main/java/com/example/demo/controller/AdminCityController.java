package com.example.demo.controller;

import com.example.demo.model.City;
import com.example.demo.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cities")
@RequiredArgsConstructor
public class AdminCityController {

    private final CityRepository cityRepository;

    //  Створити нове місто (POST)
    @PostMapping
    public ResponseEntity<City> createCity(@RequestBody City city) {
        City savedCity = cityRepository.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCity);
    }

    //  Отримати всі міста (GET)
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityRepository.findAll());
    }

    //  Видалити місто (DELETE)
    @DeleteMapping
    public ResponseEntity<HttpStatus>deleteCity(@PathVariable Long id){
        if(cityRepository.existsById(id)){
            cityRepository.deleteById(id);
        }
        else{
            throw  new RuntimeException("Такого місця не існує");
        }
        return ResponseEntity.noContent().build();
    }

    //  Оновити місто (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City cityDetails) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Місто не знайдено"));

        city.setName(cityDetails.getName());
        City updatedCity = cityRepository.save(city);
        return ResponseEntity.ok(updatedCity);
    }

    // 4. Видалити місто (DELETE)

}