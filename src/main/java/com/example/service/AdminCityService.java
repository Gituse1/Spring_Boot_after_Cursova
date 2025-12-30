package com.example.service;

import com.example.model.*;
import com.example.repository.*;
import com.example.model.City;
import com.example.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class AdminCityService {

    private final CityRepository cityRepository;

    public City createCity(String name) {
        // 1. Перевірка: чи є вже таке місто?
        if (cityRepository.existsByName(name)) {
            throw new IllegalArgumentException("Місто з назвою '" + name + "' вже існує!");
        }
        // 2. Якщо немає - створюємо нове
        City city = new City();
        city.setName(name);

        return cityRepository.save(city);
    }

    public void deleteCity(Long id){
        if(!cityRepository.existsById(id)){
            cityRepository.deleteById(id);
            throw  new IllegalArgumentException("Такого місця не існує");
        }
        cityRepository.deleteById(id);
    }

    public City updateCity(Long id,  City cityDetails){

        City city = cityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Місто не знайдено"));
        city.setName(cityDetails.getName());

        return cityRepository.save(city);
    }

    public City updateCityName( Long id,  Map<String, String> updates){
        String newName= updates.get("name");

        if(newName==null || newName.isEmpty()){
            throw new NullPointerException();
        }
        City city = cityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Місто не знайдено"));

        city.setName(newName);
        return cityRepository.save(city);
    }



//    @Transactional
//    public Route createRoute(RouteRequest request) {
//        // 1. Створюємо "шапку" маршруту
//        Route route = new Route();
//        route.setNameRoute(request.getRouteName());
//        route = routeRepository.save(route);
//
//        // 2. Створюємо точки
//        int orderIndex = 0;
//        for (RoutePointRequest pointDto : request.getPoints()) {
//            orderIndex++;
//
//            City city = cityRepository.findById(pointDto.getCityId())
//                    .orElseThrow(() -> new RuntimeException("City not found: " + pointDto.getCityId()));
//
//            RoutePoint point = new RoutePoint();
//            point.setRoute(route);
//            point.setCity(city);
//            point.setOrderIndex(orderIndex);
//            point.setPrice( pointDto.getPriceFromStart());
//
//            routePointRepository.save(point);
//        }
//        return route;
//    }
//
}