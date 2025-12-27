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
    private final AdminCityService adminCityService

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
        if(!cityRepository.existsById(id)){
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
    @PatchMapping ("/{id}")
    public ResponseEntity<City> updateCityName(@PathVariable Long id,@RequestBody Map<String, String> updates){
       String newName= updates.get("name");

       if(newName==null || newName.isEmpty()){
           return ResponseEntity.badRequest().build();
       }
       City city = cityRepository.findById(id).orElseThrow(()-> new RuntimeException("Місто не знайдено"));

       city.setName(newName);
       City updateCity = cityRepository.save(city);
       return ResponseEntity.ok((City) updateCity);
    }


}