package com.example.service.admin;

import com.example.model.*;
import com.example.model.City;
import com.example.repository.CityRepository;
import com.example.service.AuditService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class AdminCityService {

    private final CityRepository cityRepository;

    private final AuditService auditService;

    public City createCity(String name) {
        // 1. Перевірка: чи є вже таке місто?
        if (cityRepository.existsByName(name)) {

            auditService.log(ActionType.ADMIN_CITY_CREATE_NOT_FOUND,LevelLogin.ERROR);
            throw new IllegalArgumentException("Місто з назвою '" + name + "' вже існує!");
        }

        // 2. Якщо немає - створюємо нове
        City city = City.builder()
                .name(name)
                .build();

        auditService.log(ActionType.ADMIN_CITY_CREATE_CITY_CREATED,LevelLogin.INFO);
        return cityRepository.save(city);
    }

    public void deleteCity(Long id){
        if(!cityRepository.existsById(id)){

            auditService.log(ActionType.ADMIN_CITY_DELETE_NOT_FOUND,LevelLogin.ERROR);
            throw  new IllegalArgumentException("Такого місця не існує");
        }

        auditService.log(ActionType.ADMIN_CITY_DELETE_DELETED,LevelLogin.INFO);

        cityRepository.deleteById(id);
    }

    public City updateCity(Long id, Map<String, String> updates){

        try {
            City city = cityRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Місто не знайдено"));

            city.setName(updates.get("name"));

            auditService.log(ActionType.ADMIN_CITY_UPDATE_CITY_UPDATED,LevelLogin.INFO);
            return cityRepository.save(city);

        } catch (EntityNotFoundException e) {

            auditService.log(ActionType.ADMIN_CITY_UPDATE_CITY_NAME_NOT_FOUND,LevelLogin.ERROR);
            throw e;
        }
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